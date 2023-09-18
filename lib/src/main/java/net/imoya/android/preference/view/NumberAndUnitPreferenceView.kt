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

package net.imoya.android.preference.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil
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
 *  * 単位をユーザへ表示するため、IDが &quot;@+id/unit&quot;
 * ([R.id.unit])である [TextView] を配置してください。
 *
 */
open class NumberAndUnitPreferenceView : SingleValuePreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : SingleValuePreferenceView.SavedState {
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
        var minValue = 0

        /**
         * 設定可能な最大値
         */
        var maxValue = 100

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
        protected constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            currentValue = PreferenceViewSavedStateUtil.readInt(parcel, TAG)
            defaultValue = PreferenceViewSavedStateUtil.readInt(parcel, TAG)
            minValue = PreferenceViewSavedStateUtil.readInt(parcel, TAG)
            maxValue = PreferenceViewSavedStateUtil.readInt(parcel, TAG, 100)
            unit = PreferenceViewSavedStateUtil.readStringOrNull(parcel, TAG)
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
    @JvmField
    protected var mCurrentValue = 0

    /**
     * 現在の設定値
     */
    @Suppress("unused")
    var currentValue: Int
        get() = mCurrentValue
        @Suppress("unused")
        set(value) {
            mCurrentValue = value

            // 表示へ反映する
            valueView.text = mCurrentValue.toString()
            invalidate()
            requestLayout()
        }

    /**
     * デフォルト値
     */
    @JvmField
    protected var mDefaultValue = 0

    /**
     * デフォルト値
     */
    var defaultValue: Int
        get() = mDefaultValue
        @Suppress("unused")
        set(value) {
            mDefaultValue = value
        }

    /**
     * 設定可能な最小値
     */
    @JvmField
    protected var mMinValue = 0

    /**
     * 設定可能な最小値
     */
    var minValue: Int
        get() {
            PreferenceLog.v(TAG) { "getMinValue: value = $mMinValue" }
            return mMinValue
        }
        @Suppress("unused")
        set(value) {
            PreferenceLog.v(TAG) { "setMinValue: value = $value" }
            mMinValue = value
        }

    /**
     * 設定可能な最大値
     */
    @JvmField
    protected var mMaxValue = 0

    /**
     * 設定可能な最大値
     */
    var maxValue: Int
        get() {
            PreferenceLog.v(TAG) { "getMaxValue: value = $mMaxValue" }
            return mMaxValue
        }
        @Suppress("unused")
        set(value) {
            PreferenceLog.v(TAG) { "setMaxValue: value = $value" }
            mMaxValue = value
        }

    /**
     * 単位
     */
    var unit: String? = null
        set(unit) {
            field = unit

            // 表示へ反映する
            val unitView = this.unitView
            unitView.text = unit
            ViewUtil.setVisibleOrGone(unitView, !unit.isNullOrEmpty())
            invalidate()
            requestLayout()
        }

    /**
     * 値を表示する [TextView]
     */
    protected open val valueView: TextView
        get() = findViewById(android.R.id.text1)

    /**
     * 単位を表示する [TextView]
     */
    protected open val unitView: TextView
        get() = findViewById(R.id.unit)

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
    @Suppress("unused")
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    @get:LayoutRes
    override val defaultLayout: Int
        get() = R.layout.imoya_preference_number_unit

    override fun loadAttributes(values: TypedArray) {
        super.loadAttributes(values)
        PreferenceLog.v(TAG) { "NumberAndUnitPreferenceView.loadAttributes: preferenceKey = $preferenceKey" }
        mDefaultValue = values.getInt(R.styleable.PreferenceView_android_defaultValue, 0)
        mMinValue = values.getInt(R.styleable.PreferenceView_minValue, 0)
        mMaxValue = values.getInt(R.styleable.PreferenceView_maxValue, 0)
        unit = values.getString(R.styleable.PreferenceView_unit)
        PreferenceLog.v(TAG) {
            "loadAttributes: defaultValue = $mDefaultValue, minValue = $mMinValue, maxValue = $mMaxValue, unit = $unit"
        }
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    @CallSuper
    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            PreferenceLog.v(TAG) {
                "onSaveInstanceState: currentValue = $mCurrentValue, defaultValue = $mDefaultValue, minValue = $mMinValue, maxValue = $mMaxValue, unit = $unit"
            }
            savedState.currentValue = mCurrentValue
            savedState.defaultValue = mDefaultValue
            savedState.minValue = mMinValue
            savedState.maxValue = mMaxValue
            savedState.unit = unit
        } else {
            PreferenceLog.w(TAG, "onSaveInstanceState: savedState is incompatible")
        }
    }

    @CallSuper
    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            PreferenceLog.v(TAG) {
                "onRestoreState: currentValue = $mCurrentValue, defaultValue = $mDefaultValue, minValue = $mMinValue, maxValue = $mMaxValue, unit = $unit"
            }
            mCurrentValue = savedState.currentValue
            mDefaultValue = savedState.defaultValue
            mMinValue = savedState.minValue
            mMaxValue = savedState.maxValue
            unit = savedState.unit
        } else {
            PreferenceLog.w(TAG, "onRestoreState: savedState is incompatible")
        }
    }

    override fun updateViews(sharedPreferences: SharedPreferences) {
        val preferenceKey = preferenceKey
        PreferenceLog.v(TAG) { "updateViews: preferenceKey = $preferenceKey" }
        super.updateViews(sharedPreferences)
        mCurrentValue = sharedPreferences.getInt(preferenceKey, mDefaultValue)
        PreferenceLog.v(TAG) { "updateViews: value = $mCurrentValue" }
        valueView.text = mCurrentValue.toString()
        invalidate()
        requestLayout()

        PreferenceLog.v(TAG) {
            "updateViews: currentValue = $mCurrentValue, defaultValue = $mDefaultValue, minValue = $mMinValue, maxValue = $mMaxValue, unit = $unit"
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "NumberAndUnitPreferenceView"
    }
}