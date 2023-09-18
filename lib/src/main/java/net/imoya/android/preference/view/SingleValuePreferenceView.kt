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
import androidx.annotation.CallSuper
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [SharedPreferences] に於ける、1件の設定項目を表すビュー
 *
 * 設定項目風の [View] です。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView]
 * に以下を加えたものとなります:
 *  * android:preferenceKey([android.R.attr.key])
 * - このビューに表示する設定値が保存される、[SharedPreferences] のキーを指定します。
 */
abstract class SingleValuePreferenceView : PreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : PreferenceView.SavedState {
        /**
         * この項目の設定値を [SharedPreferences] へ保存する際のキー
         */
        var preferenceKey: String = ""

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
         */
        @TargetApi(Build.VERSION_CODES.N)
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            preferenceKey = PreferenceViewSavedStateUtil.readString(parcel, TAG)
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
     * この項目の設定値を [SharedPreferences] へ保存する際のキー
     */
    lateinit var preferenceKey: String

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
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    @CallSuper
    override fun loadAttributes(values: TypedArray) {
        super.loadAttributes(values)
        preferenceKey = values.getString(R.styleable.PreferenceView_android_key) ?: ""
        PreferenceLog.v(TAG) { "loadAttributes: preferenceKey = $preferenceKey" }
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    @CallSuper
    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.preferenceKey = preferenceKey
        }
    }

    @CallSuper
    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            preferenceKey = savedState.preferenceKey
        }
    }

    override fun onPreferenceChange(sharedPreferences: SharedPreferences, key: String): Boolean {
        var result = super.onPreferenceChange(sharedPreferences, key)
        if (key == preferenceKey) {
            updateViews(sharedPreferences)
            result = true
        }
        return result
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SingleValuePreferenceView"
    }
}