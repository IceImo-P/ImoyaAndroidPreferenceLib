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

package net.imoya.android.preference.view

import android.content.SharedPreferences
import android.view.View

/**
 * 設定値の変更を通知します。
 *
 * [PreferenceView] は、あくまで表示を行う [View] です。
 * ユーザが [PreferenceView] を操作して状態を変更した場合、変更後の値は
 * [PreferenceView] を使用するプログラムが、
 * [SharedPreferences] へ保存する必要があります。
 */
interface OnPreferenceChangeListener {
    /**
     * 設定値が変更された直後に呼び出されます。
     *
     * @param view 設定値が変更された [PreferenceView]
     */
    fun onPreferenceChange(view: PreferenceView)
}