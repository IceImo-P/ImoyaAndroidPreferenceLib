/*
 * Copyright (C) 2022-2023 IceImo-P
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.imoya.android.preference.view.list

import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import net.imoya.android.log.LogUtil
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 固定の選択肢より選択する設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される設定値に対応する文字列を [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView] に以下を加えたものとなります:
 *  * android:entries([android.R.attr.entries]) -
 * 各選択肢の表示用文字列が設定された、文字列の配列リソースを指定します。
 *  * app:defaultLabel([R.attr.defaultLabel]) -
 * このビューに表示する設定値が未選択状態の場合に表示する、デフォルトの文字列を指定します。
 * この属性が存在しない場合は、固定の文字列を使用します。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [ListPreferenceView] を配置する際、 android:layout([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは [PreferenceView] の説明に記載された規則に従ってください。
 */
abstract class ListPreferenceView : SingleValuePreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : SingleValuePreferenceView.SavedState {
        /**
         * 選択肢の表示用文字列リスト
         */
        var entries: Array<String>

        /**
         * 未選択時(未保存時)に表示する文字列
         */
        var defaultLabel: String

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState) {
            entries = arrayOf()
            defaultLabel = ""
        }

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        constructor(parcel: Parcel) : super(parcel) {
            val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
            entries = bundle.getStringArray(KEY_ENTRIES) ?: arrayOf()
            defaultLabel = bundle.getString(KEY_DEFAULT_LABEL, "")
        }

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, loader)
            entries = bundle.getStringArray(KEY_ENTRIES) ?: arrayOf()
            defaultLabel = bundle.getString(KEY_DEFAULT_LABEL, "")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            val bundle = Bundle()
            bundle.putStringArray(KEY_ENTRIES, entries)
            bundle.putString(KEY_DEFAULT_LABEL, defaultLabel)
            dest.writeBundle(bundle)
        }

        companion object {
            /**
             * Key at [Bundle] : [entries]
             */
            const val KEY_ENTRIES = "labels"

            /**
             * Key at [Bundle] : [defaultLabel]
             */
            const val KEY_DEFAULT_LABEL = "defLbl"

            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<SavedState> = object : Creator<SavedState> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    /**
     * 選択肢の表示用文字列リスト
     */
    lateinit var entries: Array<String>

    /**
     * 未選択時に表示する文字列
     */
    lateinit var defaultLabel: String

    /**
     * 選択中の項目を表示する [TextView]
     */
    protected lateinit var selectionView: TextView

    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : this(context, null)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    override val defaultLayout: Int
        get() = R.layout.imoya_preference_string

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    override fun onCreateChildViews() {
        super.onCreateChildViews()
        selectionView = findViewById(android.R.id.text1)
    }

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    override fun loadAttributes(values: TypedArray) {
        super.loadAttributes(values)
        val entriesId = values.getResourceId(R.styleable.PreferenceView_android_entries, 0)
        entries =
            if (entriesId != 0) values.resources.getStringArray(entriesId)
            else throw RuntimeException("entries is not defined at layout XML")
        PreferenceLog.v(TAG) {
            "loadAttributes: preferenceKey = $preferenceKey, entries = ${LogUtil.logString(entries)}"
        }

        defaultLabel = values.getString(R.styleable.PreferenceView_defaultLabel)
            ?: defaultLabelDefaultText
        PreferenceLog.v(TAG) { "loadAttributes: defaultLabel = $defaultLabel" }
    }

    /**
     * [defaultLabel] のデフォルト値
     */
    abstract val defaultLabelDefaultText: String

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.entries = entries
            savedState.defaultLabel = defaultLabel
        } else {
            PreferenceLog.w(TAG, "onSaveInstanceState(s): savedState is not SavedState")
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            entries = savedState.entries
            defaultLabel = savedState.defaultLabel
        }
    }

    /**
     * [SharedPreferences] 更新時の処理を行います。
     *
     * @param sharedPreferences 更新された [SharedPreferences]
     * @param key 更新された項目のキー
     * @return 処理を行った場合は true, そうでない場合は false
     */
    override fun onPreferenceChange(
        sharedPreferences: SharedPreferences, key: String
    ): Boolean {
        return if (!super.onPreferenceChange(sharedPreferences, key)
            && key == preferenceKey
        ) {
            updateViews(sharedPreferences)
            true
        } else {
            false
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "ListPrefView"
    }
}