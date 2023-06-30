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

import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.InputDialog
import net.imoya.android.dialog.TextInputDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.StringEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.StringPreferenceView

/**
 * 文字列設定値編集コントローラ
 *
 * * 編集画面はダイアログを使用します。
 * * [StringPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class StringDialogEditor(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent? = null,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCode: Int = Constants.DEFAULT_REQUEST_CODE
) : DialogEditor(parent, preferences, requestCode) {

    fun setInputType(inputType: Int) {
        (state as StringEditorState).inputType = inputType
    }

    fun setHint(hint: String?) {
        (state as StringEditorState).hint = hint
    }

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is StringPreferenceView
    }

    override val instanceStateClass: Class<out ScreenEditorState>
        get() = StringEditorState::class.java

    override fun createState(): ScreenEditorState {
        return StringEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is StringPreferenceView) {
            throw IllegalArgumentException("View must be StringPreferenceView")
        }
        (state as StringEditorState).maxLength = view.maxLength
    }

    override fun showDialog(view: PreferenceView) {
        val currentPreferences = checkPreferences()
        val currentParent = checkParent()
        val key = checkKey()
        if (view !is StringPreferenceView) {
            throw IllegalArgumentException("View must be StringPreferenceView")
        }
        checkAndWarnRequestCode()
        val currentState = state as StringEditorState

        TextInputDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setInputType(currentState.inputType)
            .setMaxLength(currentState.maxLength)
            .setHint(currentState.hint)
            .setText(currentPreferences.getString(key, "") ?: "")
            .setTag(key)
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val currentPreferences = checkPreferences()
        val key = checkKey()
        val value = data.getStringExtra(InputDialog.EXTRA_KEY_INPUT_VALUE)
        currentPreferences.edit().putString(key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "StringPreferenceEditor"
    }
}