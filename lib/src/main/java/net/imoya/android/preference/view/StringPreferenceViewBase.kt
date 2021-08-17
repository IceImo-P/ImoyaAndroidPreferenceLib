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
import android.widget.TextView
import androidx.annotation.LayoutRes
import net.imoya.android.preference.R
import net.imoya.android.util.Log

/**
 * 文字列値設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される [String] 型の設定値を、
 * [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView] に以下を加えたものとなります:
 *  * android:defaultValue([android.R.attr.defaultValue]) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値とする文字列を指定します。
 *  * app:valueForNull([R.attr.valueForNull]) -
 * 設定値が null の場合に表示する文字列を指定します。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [SwitchPreferenceView] を配置する際、 android:layout
 * ([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、 [PreferenceView]
 * の説明に記載された規則に加え、最低限次の規則に従ってください:
 *  * 設定値をユーザへ表示するため、IDが &quot;@android:id/text1&quot;
 * ([android.R.id.text1])である [TextView] を配置してください。
 */
abstract class StringPreferenceViewBase : SingleValuePreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : SingleValuePreferenceView.SavedState {
        /**
         * 現在の設定値
         */
        var currentValue: String? = null

        /**
         * デフォルト値
         */
        var defaultValue: String? = null

        /**
         * null時に表示する文言
         */
        var valueForNull: String? = null

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        internal constructor(superState: Parcelable?) : super(superState)

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
            currentValue = parcel.readString()
            defaultValue = parcel.readString()
            valueForNull = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)

            out.writeString(currentValue)
            out.writeString(defaultValue)
            out.writeString(valueForNull)
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
     * 現在の設定値
     */
    @JvmField
    protected var mCurrentValue: String? = null

    /**
     * 現在の設定値
     */
    protected var currentValue: String?
        get() = mCurrentValue
        set(value) {
            mCurrentValue = value
            invalidate()
            requestLayout()
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
    protected var defaultValue: String?
        get() = mDefaultValue
        set(value) {
            mDefaultValue = value
        }

    /**
     * null時に表示する文言
     */
    @JvmField
    protected var mValueForNull: String? = null

    /**
     * null時に表示する文言
     */
    protected var valueForNull: String?
        get() = mValueForNull
        set(value) {
            mValueForNull = value
        }

    /**
     * 値を表示する [TextView]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var valueView: TextView

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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    @get:LayoutRes
    override val defaultLayout: Int
        get() = R.layout.preference_string

    override fun onCreateChildViews() {
        super.onCreateChildViews()
        valueView = findViewById(android.R.id.text1)
    }

    override fun loadAttributes(values: TypedArray) {
        Log.d(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        Log.d(TAG, "loadAttributes: preferenceKey = $preferenceKey")
        mDefaultValue = values.getString(R.styleable.PreferenceView_android_defaultValue)
        mValueForNull = values.getString(R.styleable.PreferenceView_valueForNull)
        if (mValueForNull == null) {
            mValueForNull = ""
        }
        Log.d(
            TAG,
            "loadAttributes: defaultValue = $mDefaultValue, valueForNull = $mValueForNull"
        )
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.currentValue = mCurrentValue
            savedState.defaultValue = mDefaultValue
            savedState.valueForNull = mValueForNull
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            mCurrentValue = savedState.currentValue
            mDefaultValue = savedState.defaultValue
            mValueForNull = savedState.valueForNull
        }
    }

    override fun updateViews(sharedPreferences: SharedPreferences) {
        val key = preferenceKey
        Log.d(TAG, "updateViews: key = $key")
        super.updateViews(sharedPreferences)
        mCurrentValue = sharedPreferences.getString(key, mDefaultValue)
        Log.d(TAG, "updateViews: value = $mCurrentValue")
        valueView.text = valueViewText
        invalidate()
        requestLayout()
    }

    /**
     * ビューへ表示する値の文字列を取得します。
     *
     * @return 表示用の文字列
     */
    protected abstract val valueViewText: String

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "StringPreferenceView"
    }
}