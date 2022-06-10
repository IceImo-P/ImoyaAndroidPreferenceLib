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
import net.imoya.android.preference.view.OnPreferenceChangeListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SwitchPreferenceView

/**
 * スイッチ付き設定項目ビュー用、デフォルトコントローラ
 *
 * スイッチ付き設定項目ビューの操作に対応する、デフォルトの処理を実装します。
 *  * [SharedPreferences] へ、スイッチの状態を保存します。
 */
@Suppress("unused")
open class SwitchPreferenceViewController(private val preferences: SharedPreferences) :
    OnPreferenceChangeListener {

    fun attach(view: SwitchPreferenceView) {
        view.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(view: PreferenceView) {
        if (view is SwitchPreferenceView) {
            preferences.edit()
                .putBoolean(view.preferenceKey, view.getIsOn())
                .apply()
        }
    }
}