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

import android.content.Intent
import android.content.SharedPreferences
import android.widget.SeekBar
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.SeekBarInputDialog
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.view.NumberAndUnitPreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * [SeekBar] 付き整数値設定コントローラ
 *
 * [NumberAndUnitPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class SliderNumberEditor(
    parent: DialogParent, preferences: SharedPreferences, requestCode: Int
) : NumberAndUnitPreferenceEditor(parent, preferences, requestCode) {
    override fun showDialog(view: SingleValuePreferenceView) {
        PreferenceLog.v(TAG) {
            "SliderNumberEditor.showDialog: title = ${view.title}, minValue = ${
                (state as State).minValue
            }, maxValue = ${(state as State).maxValue}, defaultValue = ${
                (state as State).defaultValue
            }"
        }
        SeekBarInputDialog.Builder(parent, requestCode)
            .setTitle(view.title ?: "")
            .setMin((state as State).minValue)
            .setMax((state as State).maxValue)
            .setValue(
                preferences.getInt(
                    state.key, (state as State).defaultValue
                )
            )
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val value = data.getIntExtra(
            SeekBarInputDialog.EXTRA_KEY_INPUT_VALUE, (state as State).defaultValue
        )
        preferences.edit().putInt(state.key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SliderNumberEditor"
    }
}