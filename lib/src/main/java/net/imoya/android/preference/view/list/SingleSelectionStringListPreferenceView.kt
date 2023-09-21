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
import net.imoya.android.log.LogUtil
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 設定値が [String] である、 [SingleSelectionListPreferenceView] の実装
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される1個の設定値に対応する文字列を [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView], [ListPreferenceView],
 * [SingleSelectionListPreferenceView] に以下を加えたものとなります:
 *  * android:entryValues([android.R.attr.entryValues]) -
 * 各選択肢の実際の設定値が設定された、配列のリソースを指定します。
 *  * android:defaultValue([android.R.attr.defaultValue]) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値を指定します。
 * この属性が存在しない場合は、デフォルトの文言("(Not set)", "(未設定)")デフォルト値とします。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [SingleSelectionIntListPreferenceView] を配置する際、
 * android:layout([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは [PreferenceView] の説明に記載された規則に従ってください。
 */
open class SingleSelectionStringListPreferenceView : SingleSelectionListPreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : SingleSelectionListPreferenceView.SavedState {
        /**
         * 選択肢の設定値文字列リスト
         */
        var entryValues: Array<String> = arrayOf()

        /**
         * 現在の設定値
         */
        var currentValue: String? = null

        /**
         * デフォルト値
         */
        var defaultValue: String? = null

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
            entryValues = bundle.getStringArray(KEY_ENTRY_VALUES) ?: arrayOf()
            currentValue = bundle.getString(KEY_CURRENT_VALUE, TAG)
            defaultValue = bundle.getString(KEY_DEFAULT_VALUE, TAG)
        }

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, loader)
            entryValues = bundle.getStringArray(KEY_ENTRY_VALUES) ?: arrayOf()
            currentValue = bundle.getString(KEY_CURRENT_VALUE, TAG)
            defaultValue = bundle.getString(KEY_DEFAULT_VALUE, TAG)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            val bundle = Bundle()
            bundle.putStringArray(KEY_ENTRY_VALUES, entryValues)
            bundle.putString(KEY_CURRENT_VALUE, currentValue)
            bundle.putString(KEY_DEFAULT_VALUE, defaultValue)
            dest.writeBundle(bundle)
        }

        companion object {
            /**
             * Key at [Bundle] : [entryValues]
             */
            const val KEY_ENTRY_VALUES = "values"

            /**
             * Key at [Bundle] : [currentValue]
             */
            const val KEY_CURRENT_VALUE = "cur"

            /**
             * Key at [Bundle] : [defaultValue]
             */
            const val KEY_DEFAULT_VALUE = "def"

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
     * 選択肢の設定値リスト
     */
    lateinit var entryValues: Array<String>

    /**
     * 現在の設定値
     */
    @JvmField
    protected var mCurrentValue: String? = null

    /**
     * 現在の設定値
     */
    var currentValue: String?
        get() = mCurrentValue
        set(value) {
            mCurrentValue = value
        }

    /**
     * デフォルト値
     */
    @JvmField
    protected var mDefaultValue: String? = null

    /**
     * デフォルト値
     */
    var defaultValue: String?
        get() = mDefaultValue
        set(value) {
            mDefaultValue = value
        }

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    @Suppress("unused")
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    override fun loadAttributes(values: TypedArray) {
        super.loadAttributes(values)
        val entryValuesId = values.getResourceId(
            R.styleable.PreferenceView_android_entryValues, 0
        )
        entryValues =
            if (entryValuesId != 0) values.resources.getStringArray(entryValuesId)
            else throw RuntimeException("entry_values is not defined at layout XML")
        mDefaultValue = values.getString(R.styleable.PreferenceView_android_defaultValue)
            ?: context.getString(R.string.imoya_preference_not_set)
        PreferenceLog.d(TAG) {
            "loadAttributes: preferenceKey = $preferenceKey, entries = ${
                LogUtil.logString(entries)
            }, entryValues = ${LogUtil.logString(entryValues)}, defaultValue = $mDefaultValue"
        }
        if (entries.size != entryValues.size) {
            throw RuntimeException("entries.length != entryValues.length")
        }
    }

    /**
     * 再起動時に一時保存する [SavedState] を生成して返します。
     *
     * @param superState 親クラスの保存情報
     * @return [SavedState]
     */
    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)

        if (savedState is SavedState) {
            savedState.entryValues = entryValues
            savedState.currentValue = mCurrentValue
            savedState.defaultValue = mDefaultValue
        } else {
            PreferenceLog.w(TAG, "onSaveInstanceState(s): savedState is not SavedState")
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)

        if (savedState is SavedState) {
            entryValues = savedState.entryValues
            mCurrentValue = savedState.currentValue
            mDefaultValue = savedState.defaultValue
        } else {
            PreferenceLog.w(TAG, "onSaveInstanceState(s): savedState is not SavedState")
        }
    }

    /**
     * 選択肢リストに於いて、現在選択されている位置を返します。
     *
     * @param sharedPreferences [SharedPreferences]
     * @return 選択位置を表すインデックス
     */
    override fun getSelectedIndex(sharedPreferences: SharedPreferences): Int {
        // 現在の設定値を取得する
        mCurrentValue = sharedPreferences.getString(preferenceKey, mDefaultValue)

        // 選択肢に於ける位置を検索する
        return entryValues.indexOf(mCurrentValue)
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SSelStringListPrefView"
    }
}