package net.imoya.android.preference.view

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.widget.TextView
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.annotation.LayoutRes
import net.imoya.android.preference.R
import android.content.res.TypedArray
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import net.imoya.android.util.Log
import net.imoya.android.util.ViewUtil

/**
 * 単位付き数値設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される int 型の設定値と、プログラムが指定する単位文字列を
 * [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView] に以下を加えたものとなります:
 *  * android:defaultValue([android.R.attr.defaultValue]) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値とする整数値を指定します。
 *  * app:unit([R.attr.unit]) - ビューへ表示する単位文字列を指定します。
 *
 * <h2>カスタムレイアウト</h2>
 *
 * レイアウトXMLへ [SwitchPreferenceView] を配置する際、 android:layout
 * ([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、 [PreferenceView]
 * の説明に記載された規則に加え、最低限次の規則に従ってください:
 *  * 設定値をユーザへ表示するため、IDが &quot;@android:id/text1&quot;
 * ([android.R.id.text1])である [TextView] を配置してください。
 *  * 単位をユーザへ表示するため、IDが &quot;@+id/unit&quot;
 * ([R.id.unit])である [TextView] を配置してください。
 *
 */
open class NumberAndUnitPreferenceView : SingleValuePreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected class SavedState : SingleValuePreferenceView.SavedState {
        /**
         * 現在の設定値
         */
        var currentValue = 0

        /**
         * デフォルト値
         */
        var defaultValue = 0

        /**
         * 設定可能な最小値
         */
        var minValue = Int.MIN_VALUE

        /**
         * 設定可能な最大値
         */
        var maxValue = Int.MAX_VALUE

        /**
         * 単位
         */
        var unit: String? = null

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
         * @param source [Parcel]
         * @param loader [ClassLoader]
         */
        private constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            currentValue = source.readInt()
            defaultValue = source.readInt()
            minValue = source.readInt()
            maxValue = source.readInt()
            unit = source.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(currentValue)
            out.writeInt(defaultValue)
            out.writeInt(minValue)
            out.writeInt(maxValue)
            out.writeString(unit)
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
    var currentValue = 0

    /**
     * デフォルト値
     */
    var defaultValue = 0

    /**
     * 設定可能な最小値
     */
    var minValue = Int.MIN_VALUE

    /**
     * 設定可能な最大値
     */
    var maxValue = Int.MAX_VALUE

    /**
     * 単位
     */
    var unit: String? = null
        set(unit) {
            field = unit

            // 表示へ反映する
            unitView.text = unit
            ViewUtil.setVisibleOrGone(unitView, unit != null && unit.isNotEmpty())
        }

    /**
     * 値を表示する [TextView]
     */
    private lateinit var valueView: TextView

    /**
     * 単位を表示する [TextView]
     */
    private lateinit var unitView: TextView

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
        get() = R.layout.preference_number_unit

    override fun onCreateChildViews() {
        super.onCreateChildViews()
        valueView = findViewById(android.R.id.text1)
        unitView = findViewById(R.id.unit)
    }

    override fun loadAttributes(values: TypedArray) {
        Log.d(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        Log.d(TAG, "loadAttributes: preferenceKey = $preferenceKey")
        defaultValue = values.getInt(R.styleable.PreferenceView_android_defaultValue, 0)
        minValue = values.getInt(R.styleable.PreferenceView_minValue, 0)
        maxValue = values.getInt(R.styleable.PreferenceView_maxValue, 0)
        unit = values.getString(R.styleable.PreferenceView_unit)
        Log.d(TAG, "loadAttributes: defaultValue = $defaultValue, unit = $unit")
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    @CallSuper
    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.currentValue = currentValue
            savedState.defaultValue = defaultValue
            savedState.maxValue = maxValue
            savedState.minValue = minValue
            savedState.unit = unit
        }
    }

    @CallSuper
    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            currentValue = savedState.currentValue
            defaultValue = savedState.defaultValue
            maxValue = savedState.maxValue
            minValue = savedState.minValue
            unit = savedState.unit
        }
    }

    override fun updateViews(sharedPreferences: SharedPreferences?) {
        val preferenceKey = preferenceKey
        Log.d(TAG, "updateViews: preferenceKey = $preferenceKey")
        super.updateViews(sharedPreferences)
        if (sharedPreferences != null) {
            currentValue = sharedPreferences.getInt(preferenceKey, defaultValue)
            Log.d(TAG, "updateViews: value = $currentValue")
            valueView.text = currentValue.toString()
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "NumberAndUnitPreferenceView"
    }
}