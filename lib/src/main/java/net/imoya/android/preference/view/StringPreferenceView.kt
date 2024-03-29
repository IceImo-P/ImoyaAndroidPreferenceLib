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

package net.imoya.android.preference.view

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
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * 文字列値設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される [String] 型の設定値を [TextView] へ表示します。
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
 * ([android.R.attr.layout])属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、 [PreferenceView] の説明に記載された規則に加え、最低限次の規則に従ってください:
 *  * 設定値をユーザへ表示するため、IDが &quot;@android:id/text1&quot;
 * ([android.R.id.text1])である [TextView] を配置してください。
 */
open class StringPreferenceView : StringPreferenceViewBase {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : StringPreferenceViewBase.SavedState {
        /**
         * 最大入力可能文字数
         */
        var maxLength = Integer.MAX_VALUE

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
            maxLength = bundle.getInt(KEY_MAX_LENGTH, Integer.MAX_VALUE)
        }

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, loader)
            maxLength = bundle.getInt(KEY_MAX_LENGTH, Integer.MAX_VALUE)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            val bundle = Bundle()
            bundle.putInt(KEY_MAX_LENGTH, maxLength)
            dest.writeBundle(bundle)
        }

        companion object {
            /**
             * Key at Bundle : [maxLength]
             */
            const val KEY_MAX_LENGTH = "maxLen"

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
     * 最大入力可能文字数
     */
    @JvmField
    protected var mMaxLength = 0

    /**
     * 最大入力可能文字数
     */
    var maxLength: Int
        get() = mMaxLength
        set(value) {
            mMaxLength = value
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
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun loadAttributes(values: TypedArray) {
        PreferenceLog.v(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        PreferenceLog.d(TAG) { "loadAttributes: preferenceKey = $preferenceKey" }
        mMaxLength = values.getInt(
            R.styleable.PreferenceView_android_maxLength, Int.MAX_VALUE
        )
        PreferenceLog.d(TAG) { "loadAttributes: maxLength = $mMaxLength" }
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.maxLength = mMaxLength
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            mMaxLength = savedState.maxLength
        }
    }

    override val valueViewText: String
        get() {
            return currentValue ?: (valueForNull ?: "")
        }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "StringPrefView"
    }
}