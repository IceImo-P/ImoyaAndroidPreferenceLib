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

import android.content.Intent
import android.content.SharedPreferences
import android.text.InputType
import androidx.annotation.CallSuper
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.InputDialog
import net.imoya.android.dialog.NumberInputDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.state.NumberAndUnitEditorState
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.view.NumberAndUnitPreferenceView
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 単位表示付き整数設定値編集コントローラ
 *
 * * 編集画面はダイアログを使用します。
 * * [NumberAndUnitPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class NumberAndUnitDialogEditor(
    parent: DialogParent? = null,
    preferences: SharedPreferences? = null,
    requestCode: Int = Constants.DEFAULT_REQUEST_CODE
) : DialogEditor(parent, preferences, requestCode) {

    fun setHint(hint: String?) {
        (state as NumberAndUnitEditorState).hint = hint
    }

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is NumberAndUnitPreferenceView
    }

    override fun createState(): ScreenEditorState {
        return NumberAndUnitEditorState()
    }

    @CallSuper
    override fun setupState(view: PreferenceView) {
        super.setupState(view)

        if (view !is NumberAndUnitPreferenceView) {
            throw IllegalArgumentException("View must be NumberAndUnitPreferenceView")
        }
        val currentState = state as NumberAndUnitEditorState

        PreferenceLog.v(TAG) {
            "setupState: defaultValue = ${view.defaultValue}, minValue = ${
                view.minValue
            }, maxValue = ${view.maxValue}, unit = ${view.unit}"
        }
        currentState.defaultValue = view.defaultValue
        currentState.minValue = view.minValue
        currentState.maxValue = view.maxValue
        currentState.unit = view.unit
    }

    override fun showDialog(view: PreferenceView) {
        val currentPreferences = checkPreferences()
        val currentParent = checkParent()
        val key = checkKey()
        if (view !is SingleValuePreferenceView) {
            throw IllegalStateException("View must be SingleValuePreferenceView")
        }
        checkAndWarnRequestCode()
        val currentState = state as NumberAndUnitEditorState

        val inputType =
            if (currentState.minValue < 0) {
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            } else {
                InputType.TYPE_CLASS_NUMBER
            }
        NumberInputDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setInputType(inputType)
            .setHint(currentState.hint)
            .setNumber(currentPreferences.getInt(key, currentState.defaultValue))
            .setTag(key)
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val currentPreferences = checkPreferences()
        val key = checkKey()
        val value = data.getIntExtra(
            InputDialog.EXTRA_KEY_INPUT_VALUE, (state as NumberAndUnitEditorState).defaultValue
        )
        currentPreferences.edit().putInt(key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "NumberAndUnitPreferenceEditor"
    }
}