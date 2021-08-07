package net.imoya.android.preference.view

import net.imoya.android.util.Log
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import android.widget.CompoundButton
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.annotation.LayoutRes
import net.imoya.android.preference.R
import android.content.res.TypedArray
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import java.lang.RuntimeException

/**
 * スイッチ付き設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される boolean 型の設定値を [Switch] で表示します。
 *
 * このビューが有効な状態で、ユーザが [SwitchPreferenceView]
 * をクリックした場合、スイッチの状態が切り替わり、 [OnPreferenceChangeListener]
 * の [OnPreferenceChangeListener.onPreferenceChange]
 * メソッドが呼び出されます。プログラムは同メソッドに於いて、引数 view の
 * [SwitchPreferenceView.getIsOn] メソッドが返す値を、 [SharedPreferences]
 * へ保存する等の処理を行ってください。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView] に以下を加えたものとなります:
 *  * android:defaultValue([android.R.attr.defaultValue])
 * - このビューに表示する設定値が未保存の場合に使用する、デフォルト値の boolean を指定します。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [SwitchPreferenceView] を配置する際、 android:layout
 * ([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、 [PreferenceView]
 * の説明に記載された規則に加え、最低限次の規則に従ってください:
 *  * 設定値をユーザへ表示するため、IDが &quot;@+id/check&quot;
 * ([R.id.check])である [CompoundButton] の派生オブジェクトを配置してください。
 *
 */
open class SwitchPreferenceView : SingleValuePreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected class SavedState : SingleValuePreferenceView.SavedState {
        /**
         * 現在のON/OFF状態
         */
        var currentValue = false

        /**
         * デフォルト値
         */
        var defaultValue = false

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
        private constructor(parcel: Parcel): this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        private constructor(parcel: Parcel, loader: ClassLoader? = null) : super(parcel, loader) {
            val booleans = parcel.createBooleanArray()
            if (booleans == null || booleans.size < 2) {
                throw RuntimeException("Invalid parcel content")
            }
            currentValue = booleans[0]
            defaultValue = booleans[1]
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeBooleanArray(booleanArrayOf(currentValue, defaultValue))
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
     * [OnPreferenceChangeListener]
     */
    var onPreferenceChangeListener: OnPreferenceChangeListener? = null

    /**
     * ビュー上のスイッチを表す [CompoundButton]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var compoundButton: CompoundButton

    /**
     * 現在のON/OFF状態
     */
    private var currentValue = false

    /**
     * デフォルト値
     */
    private var defaultValue = false

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
    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

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
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    @get:LayoutRes
    override val defaultLayout: Int
        get() = R.layout.preference_switch

    override fun onCreateChildViews() {
        super.onCreateChildViews()
        compoundButton = findViewById(R.id.check)
    }

    override fun loadAttributes(values: TypedArray) {
        Log.d(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        defaultValue = values.getBoolean(
            R.styleable.PreferenceView_android_defaultValue, false
        )
        Log.d(TAG, "loadAttributes: defaultValue = $defaultValue")
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.currentValue = currentValue
            savedState.defaultValue = defaultValue
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            currentValue = savedState.currentValue
            defaultValue = savedState.defaultValue
        }
    }

    /**
     * 現在のON/OFF状態を取得します。
     *
     * @return 現在のON/OFF状態
     */
    fun getIsOn(): Boolean {
        return currentValue
    }

    override fun updateViews(sharedPreferences: SharedPreferences?) {
        val preferenceKey = preferenceKey
        Log.d(TAG, "updateViews: preferenceKey = $preferenceKey")
        super.updateViews(sharedPreferences)
        if (sharedPreferences != null) {
            currentValue = sharedPreferences.getBoolean(preferenceKey, defaultValue)
            Log.d(TAG, "updateViews: value = $currentValue")
            compoundButton.isChecked = currentValue
        }
    }

    override fun onClickRootView() {
        super.onClickRootView()

        // ON/OFF状態を切り替える
        Log.d(TAG, "onClickRootView: before = $currentValue, after = ${!currentValue}")
        currentValue = !currentValue
        compoundButton.isChecked = currentValue

        // リスナへ変更を通知する
        if (onPreferenceChangeListener != null) {
            onPreferenceChangeListener!!.onPreferenceChange(this)
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SwitchPreferenceView"
    }
}