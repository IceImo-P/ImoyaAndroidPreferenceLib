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

package net.imoya.android.preference.controller.editor

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.dialog.DialogBase
import net.imoya.android.dialog.DialogParent
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.view.PreferenceView

/**
 * ダイアログを表示する設定値編集コントローラの共通実装
 *
 * [PreferenceView] タップ時に編集ダイアログを表示し、ユーザがダイアログへ入力した結果を
 * [SharedPreferences] へ保存するタイプの設定値編集コントローラ共通部分を実装します。
 */
abstract class DialogEditor(
    /**
     * 編集ダイアログの親画面
     */
    var parent: DialogParent? = null,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null,
    /**
     * 編集ダイアログの識別に使用するリクエストコード
     */
    var requestCode: Int = Constants.DEFAULT_REQUEST_CODE
) : ScreenEditor(preferences) {
    /**
     * ダイアログより結果を受け取る機能を初期化します。
     *
     * [androidx.fragment.app.Fragment.onViewCreated] で呼び出してください。
     */
    open fun registerDialogCallback() {
        PreferenceLog.v(TAG, "registerFragmentCallback: start")

        // リクエストコードがデフォルト値である場合は WARN ログを出力する
        checkAndWarnRequestCode()

        // コールバックを受け取る処理を FragmentManager へ登録する
        if (parent == null) {
            PreferenceLog.w(TAG, "Parent is null. Is it intended?")
        }
        parent?.registerListener(requestCode)

        PreferenceLog.v(TAG, "registerDialogCallback: end")
    }

    /**
     * Check [requestCode] is set to non-default value
     *
     * - If [requestCode] is the default value ([Constants.DEFAULT_REQUEST_CODE]),
     *   WARN log is output.
     */
    protected fun checkAndWarnRequestCode() {
        if (requestCode == Constants.DEFAULT_REQUEST_CODE) {
            PreferenceLog.w(
                TAG,
                "requestCode is default value(Integer.MIN_VALUE). Is it intended?"
            )
        }
    }

    override fun startEditorUI(view: PreferenceView) {
        showDialog(view)
    }

    /**
     * 編集ダイアログを表示します。
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    protected abstract fun showDialog(view: PreferenceView)

    /**
     * 編集ダイアログが閉じられた時の処理を行います。
     *
     * @param requestCode 編集ダイアログの識別に使用するリクエストコード
     * @param resultCode  ダイアログが返した結果コード
     * @param data        ダイアログが返した追加情報
     * @return このコントローラが処理した場合はtrue, そうでない場合はfalse
     */
    open fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return if (requestCode == this.requestCode) {
            PreferenceLog.v(TAG) {
                val tag = data?.getStringExtra(DialogBase.EXTRA_KEY_TAG) ?: "(null)"
                "onDialogResult: tag = $tag requestCode = $requestCode resultCode = $resultCode"
            }
            onEditorResult(resultCode, data)
            true
        } else false
    }

    /**
     * 編集画面の入力結果を保存します。
     *
     * @param resultCode 編集画面が返した結果コード
     * @param data       編集画面が返した追加情報
     */
    protected abstract fun saveInput(resultCode: Int, data: Intent?)

    /**
     * 編集画面の入力結果を処理します。
     *
     * @param resultCode 編集画面が返した結果コード
     * @param data       編集画面が返した追加情報
     */
    protected open fun onEditorResult(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_CANCELED) {
            // 入力値を保存する
            saveInput(resultCode, data)
        }
    }

    /**
     * Check [parent] is set
     *
     * @return [DialogParent]
     * @throws IllegalStateException [parent] is not set
     */
    protected fun checkParent(): DialogParent {
        return parent ?: throw IllegalStateException("preferences is not set")
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "DialogPreferenceEditor"
    }
}