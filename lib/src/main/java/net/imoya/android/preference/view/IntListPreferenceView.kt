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
 * 設定値が整数値である、 [ListPreferenceView] の実装
 */
class IntListPreferenceView : ListPreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected class SavedState : ListPreferenceView.SavedState {
        /**
         * 選択肢の設定値整数リスト
         */
        var entryValues: IntArray = intArrayOf()

        /**
         * 現在の設定値
         */
        var currentValue = 0

        /**
         * デフォルト値
         */
        var defaultValue = 0

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
        private constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        private constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            entryValues = parcel.createIntArray() ?: intArrayOf()
            currentValue = parcel.readInt()
            defaultValue = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeIntArray(entryValues)
            out.writeInt(currentValue)
            out.writeInt(defaultValue)

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
    var entryValues: IntArray = intArrayOf()

    /**
     * デフォルト値
     */
    var defaultValue: Int = 0

    /**
     * 選択中の値
     */
    var currentValue: Int = 0

    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : super(context)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    override fun loadAttributes(values: TypedArray) {
        Log.d(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        Log.d(TAG, "loadAttributes: preferenceKey = $preferenceKey")
        val entryValuesId = values.getResourceId(
            R.styleable.PreferenceView_android_entryValues, 0
        )
        entryValues =
            if (entryValuesId != 0) values.resources.getIntArray(entryValuesId)
            else throw RuntimeException("entry_values is not defined at layout XML")
        defaultValue = values.getInt(R.styleable.PreferenceView_android_defaultValue, 0)
        Log.d(
            TAG,
            "loadAttributes: entryValues = ${LogUtil.logString(entryValues)}"
                    + ", defaultValue = $defaultValue"
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

    /**
     * 選択肢リストに於いて、現在選択されている位置を返します。
     *
     * @param sharedPreferences [SharedPreferences]
     * @return 選択位置を表すインデックス
     */
    override fun getSelectedIndex(sharedPreferences: SharedPreferences): Int {
        // 現在の設定値を取得する
        currentValue = sharedPreferences.getInt(preferenceKey, defaultValue)

        // 選択肢に於ける位置を検索する
        var selectedIndex = 0
        for (i in entryValues.indices) {
            if (entryValues[i] == currentValue) {
                selectedIndex = i
                break
            }
        }
        return selectedIndex
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "StringListPreferenceView"
    }
}