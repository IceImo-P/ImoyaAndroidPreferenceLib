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

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import net.imoya.android.preference.R
import net.imoya.android.preference.controller.PreferenceScreenParent
import net.imoya.android.preference.view.PreferenceView

/**
 * [Activity] を表示する設定値編集コントローラの共通実装
 *
 * [PreferenceView] タップ時に [Activity] へ遷移し、ユーザが画面へ入力した結果を
 * [SharedPreferences] へ保存するタイプの設定値編集コントローラ共通部分を実装します。
 */
abstract class ActivityEditor(
    /**
     * 設定画面の親画面
     */
    protected val parent: PreferenceScreenParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null
) : ScreenEditor(preferences) {
    /**
     * 編集画面の [Activity] より結果を受け取る目的で使用する [ActivityResultLauncher]
     */
    protected open val editorLauncher: ActivityResultLauncher<Intent> =
        parent.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult -> onEditorResult(result.resultCode, result.data) }

    /**
     * Returns [Intent] for start editor [Activity]
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    protected abstract fun createEditorIntent(view: PreferenceView): Intent

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

    override fun startEditorUI(view: PreferenceView) {
        // Start editor Activity
        editorLauncher.launch(createEditorIntent(view))

        // Call AfterLaunchListener
        afterStartEditorUI(view)
    }

    /**
     * Process after launch editor UI
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    protected open fun afterStartEditorUI(view: PreferenceView) {
        parent.activity.overridePendingTransition(
            R.anim.imoya_preference_activity_start_enter,
            R.anim.imoya_preference_activity_start_exit
        )
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "ActivityPreferenceEditor"
//    }
}