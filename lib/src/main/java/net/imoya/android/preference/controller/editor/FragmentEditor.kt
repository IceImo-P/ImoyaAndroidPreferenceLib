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

package net.imoya.android.preference.controller.editor

import android.content.SharedPreferences
import android.os.Bundle
import net.imoya.android.fragment.roundtrip.Constants
import net.imoya.android.fragment.roundtrip.RoundTripClientFragment
import net.imoya.android.fragment.roundtrip.RoundTripManager
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.view.PreferenceView

/**
 * [androidx.fragment.app.Fragment] を表示する設定値編集コントローラの共通実装
 *
 * [PreferenceView] タップ時に設定画面の [androidx.fragment.app.Fragment]
 * へ遷移し、ユーザが設定画面で入力・選択した結果を
 * [SharedPreferences] へ保存するタイプの、設定値編集コントローラ共通部分を実装します。
 */
abstract class FragmentEditor(
    /**
     * [RoundTripManager]
     */
    var roundTripManager: RoundTripManager? = null,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null,
    /**
     * 編集画面の識別に使用するリクエストキー
     */
    var requestKey: String? = null
) : ScreenEditor(preferences) {
    /**
     * [androidx.fragment.app.Fragment] より結果を受け取る機能を初期化します。
     *
     * * 結果を受け取る画面が [androidx.fragment.app.Fragment] の場合、
     *   [androidx.fragment.app.Fragment.onViewCreated] で呼び出してください。
     * * 結果を受け取る画面が [androidx.appcompat.app.AppCompatActivity] の場合、
     *   [androidx.appcompat.app.AppCompatActivity.onCreate] で呼び出してください。
     */
    open fun registerFragmentCallback() {
        PreferenceLog.v(TAG, "registerFragmentCallback: start")

        val myRequestKey = checkRequestKey()

        // コールバックを受け取る処理を FragmentManager へ登録する
        checkRoundTripManager().setResultListener(myRequestKey) { requestKey: String, result: Bundle ->
            if (myRequestKey.contentEquals(requestKey)) {
                onEditorResult(result)
            }
        }

        PreferenceLog.v(TAG, "registerFragmentCallback: end")
    }

    /**
     * 編集画面の入力結果を処理します。
     *
     * @param result 編集画面が返した結果情報
     */
    protected abstract fun onEditorResult(result: Bundle)

    /**
     * Start preference fragment
     *
     * @param fragment 開始する最初の "client" となる [RoundTripClientFragment]
     * @param tag      最初の "client" への画面遷移に設定するタグ。
     *                 Round-trip (往復)ナビゲーション中に発生する画面遷移の中で、重複しない文字列を指定します。
     */
    @Suppress("unused")
    protected open fun startPreferenceFragment(
        fragment: RoundTripClientFragment,
        tag: String = Constants.TAG_FIRST_CLIENT
    ) {
        val roundTripManager = checkRoundTripManager()
        val requestKey = checkRequestKey()
        roundTripManager.start(requestKey, fragment, tag)
    }

    /**
     * Check [roundTripManager] is set
     *
     * @return [RoundTripManager]
     * @throws IllegalStateException [roundTripManager] is not set
     */
    protected fun checkRoundTripManager(): RoundTripManager {
        return roundTripManager ?: throw IllegalStateException("roundTripManager is not set")
    }

    /**
     * Check [requestKey] is set
     *
     * @return Request key string
     * @throws IllegalStateException [requestKey] is not set
     */
    protected fun checkRequestKey(): String {
        return requestKey ?: throw IllegalStateException("requestKey is not set")
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "DialogPreferenceEditor"
    }
}