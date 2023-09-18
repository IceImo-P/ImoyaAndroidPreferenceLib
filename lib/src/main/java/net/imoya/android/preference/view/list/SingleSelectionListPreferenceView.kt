/*
 * Copyright (C) 2022 IceImo-P
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
import net.imoya.android.preference.type.SingleSelectionType
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 固定の選択肢より1個を選択する設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される1個の設定値に対応する文字列を [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView], [ListPreferenceView] に以下を加えたものとなります:
 *  * app:singleSelectionType([R.attr.singleSelectionType]) - 選択UIの種別を指定します。
 *      * OK_CANCEL - OK, キャンセルボタンが表示され、いずれかのボタンをクリック(タップ)するとUIを終了します。
 *      * ITEM_CLICK - 戻るボタンが表示され、一覧の項目か戻るボタンをクリック(タップ)するとUIを終了します。
 *  * app:defaultLabel([R.attr.defaultLabel]) -
 * このビューに表示する設定値が未保存の場合に表示する、デフォルトの文字列を指定します。
 * この属性が存在しない場合は、固定の文字列("(Not set)", "(未設定)")を使用します。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [SingleSelectionListPreferenceView] を配置する際、 android:layout([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは [PreferenceView] の説明に記載された規則に従ってください。
 */
abstract class SingleSelectionListPreferenceView : ListPreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : ListPreferenceView.SavedState {
        /**
         * 選択時の操作方法
         */
        var singleSelectionType: SingleSelectionType

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState) {
            singleSelectionType = SingleSelectionType.ITEM_CLICK
        }

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            singleSelectionType =
                SingleSelectionType.from(PreferenceViewSavedStateUtil.readInt(parcel, TAG, 1))
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(singleSelectionType.id)
        }

        companion object {
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
     * 単一項目選択時の操作方法
     */
    lateinit var singleSelectionType: SingleSelectionType

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
        singleSelectionType =
            SingleSelectionType.from(
                values.getInt(
                    R.styleable.PreferenceView_singleSelectionType,
                    0
                )
            )
        PreferenceLog.v(TAG) {
            "loadAttributes: singleSelectionType = $singleSelectionType"
        }
    }

    override val defaultLabelDefaultText: String
        get() = context.getString(R.string.imoya_preference_not_set)

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.singleSelectionType = singleSelectionType
        } else {
            PreferenceLog.w(TAG, "onSaveInstanceState(s): savedState is not SavedState")
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            singleSelectionType = savedState.singleSelectionType
            defaultLabel = savedState.defaultLabel
        }
    }

    /**
     * 表示を更新します。
     *
     * @param sharedPreferences [SharedPreferences]
     */
    override fun updateViews(sharedPreferences: SharedPreferences) {
        PreferenceLog.v(TAG) { "updateViews: key = $preferenceKey" }
        super.updateViews(sharedPreferences)
        val index = getSelectedIndex(sharedPreferences)
        PreferenceLog.v(TAG) { "updateViews: entries = ${LogUtil.logString(entries)}, index = $index" }
        selectionView.text =
            if (index >= 0 && index < entries.size) entries[index] else defaultLabel
        invalidate()
        requestLayout()
    }

    /**
     * 選択肢リストに於いて、現在選択されている位置を返します。
     *
     * @param sharedPreferences [SharedPreferences]
     * @return 選択位置を表すインデックス, 未選択時は -1
     */
    abstract fun getSelectedIndex(sharedPreferences: SharedPreferences): Int

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
        private const val TAG = "ListPreferenceView"
    }
}