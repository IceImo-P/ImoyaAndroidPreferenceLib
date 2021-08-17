package net.imoya.android.preference.view

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.View
import net.imoya.android.preference.R
import net.imoya.android.util.Log
import net.imoya.android.util.LogUtil

/**
 * 設定値が [String] である、 [ListPreferenceView] の実装
 */
open class StringListPreferenceView : ListPreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : ListPreferenceView.SavedState {
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
        protected constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            entryValues = parcel.createStringArray()
                ?: throw RuntimeException("parcel.createStringArray returns null")
            currentValue = parcel.readString()
            defaultValue = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeStringArray(entryValues)
            out.writeString(currentValue)
            out.writeString(defaultValue)
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
    @Suppress("unused", "MemberVisibilityCanBePrivate")
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
    @Suppress("MemberVisibilityCanBePrivate")
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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        Log.d(
            TAG, "loadAttributes: preferenceKey = $preferenceKey"
                    + ", entries = ${LogUtil.logString(entries)}"
                    + ", entryValues = ${LogUtil.logString(entryValues)}"
                    + ", defaultValue = $mDefaultValue"
        )
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
            Log.w(TAG, "onSaveInstanceState(s): savedState is not SavedState")
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)

        if (savedState is SavedState) {
            entryValues = savedState.entryValues
            mCurrentValue = savedState.currentValue
            mDefaultValue = savedState.defaultValue
        } else {
            Log.w(TAG, "onSaveInstanceState(s): savedState is not SavedState")
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
        private const val TAG = "StringListPreferenceView"
    }
}