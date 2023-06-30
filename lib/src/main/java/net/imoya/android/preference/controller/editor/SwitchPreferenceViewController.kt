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
import net.imoya.android.preference.view.OnPreferenceChangeListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SwitchPreferenceView

/**
 * スイッチ付き設定項目ビュー用、デフォルトの設定値編集コントローラ
 *
 * スイッチ付き設定項目ビュー ([SwitchPreferenceView]) の操作に対応する、デフォルトの処理を実装します。
 * * ユーザーがビューをタップしてスイッチを変更した場合、[SharedPreferences] へ、スイッチに対応する値を保存します。
 * * [SharedPreferences] へ保存する値は Boolean 型で、スイッチONへ変更した場合に true,
 *   OFFへ変更した場合に false を保存します。
 */
open class SwitchPreferenceViewController(preferences: SharedPreferences? = null) :
    PreferenceEditor(preferences), OnPreferenceChangeListener {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is SwitchPreferenceView
    }

    override fun attach(view: PreferenceView) {
        if (view !is SwitchPreferenceView) {
            throw IllegalStateException("View must be SwitchPreferenceView")
        }
        view.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(view: PreferenceView) {
        if (view !is SwitchPreferenceView) {
            throw IllegalStateException("View must be SwitchPreferenceView")
        }
        checkPreferences().edit()
            .putBoolean(view.preferenceKey, view.getIsOn())
            .apply()
    }
}