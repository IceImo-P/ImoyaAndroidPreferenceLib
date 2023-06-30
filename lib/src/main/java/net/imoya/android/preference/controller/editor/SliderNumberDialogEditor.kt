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
import android.widget.SeekBar
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.SeekBarInputDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.state.NumberAndUnitEditorState
import net.imoya.android.preference.view.NumberAndUnitPreferenceView
import net.imoya.android.preference.view.PreferenceView

/**
 * [SeekBar] 付き整数値設定コントローラ
 *
 * * 編集画面はダイアログを使用します。
 * * [NumberAndUnitPreferenceView] と組み合わせて使用することを想定しています。
 */
open class SliderNumberDialogEditor(
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
) : NumberAndUnitDialogEditor(parent, preferences, requestCode) {

    @Suppress("unused")
    override fun showDialog(view: PreferenceView) {
        val currentPreferences = checkPreferences()
        val currentParent = checkParent()
        val key = checkKey()
        val currentState = state as NumberAndUnitEditorState
        checkAndWarnRequestCode()

        PreferenceLog.v(TAG) {
            "SliderNumberEditor.showDialog: title = ${view.title}, minValue = ${
                currentState.minValue
            }, maxValue = ${currentState.maxValue}, defaultValue = ${
                currentState.defaultValue
            }"
        }
        SeekBarInputDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setMin(currentState.minValue)
            .setMax(currentState.maxValue)
            .setValue(currentPreferences.getInt(key, currentState.defaultValue))
            .show()
    }

    @Suppress("unused")
    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val currentPreferences = checkPreferences()
        val key = checkKey()
        val value = data.getIntExtra(
            SeekBarInputDialog.EXTRA_KEY_INPUT_VALUE,
            (state as NumberAndUnitEditorState).defaultValue
        )
        currentPreferences.edit().putInt(key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SliderNumberEditor"
    }
}