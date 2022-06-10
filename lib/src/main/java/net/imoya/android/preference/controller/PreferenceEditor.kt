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

package net.imoya.android.preference.controller

import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 設定値編集コントローラの共通実装
 *
 * 設定項目タップ時に設定ダイアログを表示し、ダイアログの結果を保存するタイプの
 * 設定値編集コントローラ共通部分を実装します。
 */
@Suppress("unused")
abstract class PreferenceEditor(
    /**
     * 設定値が保存される [SharedPreferences]
     */
    @JvmField protected val preferences: SharedPreferences
) : OnPreferenceViewClickListener {
    /**
     * 状態オブジェクト
     */
    protected open class State : Parcelable {
        /**
         * 設定キー
         */
        var key: String? = null

        constructor()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) {
            key = parcel.readString()
        }

        /**
         * 指定の [Parcel] へ、このオブジェクトの内容を保存します。
         *
         * @param dest [Parcel]
         * @param flags  フラグ
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(key)
        }

        override fun describeContents() = 0

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<State> = object : Creator<State> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): State {
                    return State(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<State?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    protected lateinit var state: State

    /**
     * 指定の設定項目ビューをタップした時に、このコントローラが呼び出されるよう設定します。
     *
     * @param view 設定項目ビュー
     */
    fun attach(view: SingleValuePreferenceView) {
        view.onPreferenceViewClickListener = this
    }

    /**
     * [Fragment] 再起動時に一時保存するデータ, データなしの場合は null
     */
    open var instanceState: Parcelable?
        get() = if (::state.isInitialized) state else null
        set(value) {
            if (value != null && isCompatibleState(value)) {
                state = value as State
            } else if (value != null) {
                throw RuntimeException("Incompatible parcelable")
            }
        }

    /**
     * 設定項目ビューがタップされた時の処理を行います。
     *
     * @param view タップされた項目設定ビュー
     */
    override fun onPreferenceViewClick(view: PreferenceView) {
        val prefView = view as SingleValuePreferenceView

        // SingleValuePreferenceView より、設定情報を取得する
        setupState(prefView)

        // 入力画面を開始する
        startEditorUI(prefView)
    }

    /**
     * [SingleValuePreferenceView] より、設定情報を取得します。
     *
     * @param view [SingleValuePreferenceView]
     */
    @CallSuper
    protected open fun setupState(view: SingleValuePreferenceView) {
        state = createState()
        state.key = view.preferenceKey
    }

    /**
     * 状態オブジェクトの新しいインスタンスを生成して返します。
     *
     * @return 状態オブジェクト
     */
    protected abstract fun createState(): State

    /**
     * 指定の [Parcelable] が利用可能な状態オブジェクトであるか否かを返します。
     *
     * @param parcelable [Parcelable]
     * @return 利用可能である場合は true, その他の場合は false
     */
    protected open fun isCompatibleState(parcelable: Parcelable): Boolean {
        return parcelable is State
    }

    /**
     * 設定画面を表示します。
     *
     * @param view タップされた項目設定ビュー
     */
    protected abstract fun startEditorUI(view: SingleValuePreferenceView)

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "PreferenceEditor"
//    }
}