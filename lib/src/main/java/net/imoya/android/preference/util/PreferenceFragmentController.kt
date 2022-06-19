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

package net.imoya.android.preference.util

import androidx.fragment.app.Fragment
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.view.PreferenceView

/**
 * [Fragment] へ [PreferenceView] を配置するための追加機能を実装および提供します。
 *
 * @author IceImo-P
 */
open class PreferenceFragmentController : PreferenceScreenController() {
    fun <T> onCreateFragment(fragment: T) where T : Fragment, T : DialogListener {
        super.onCreate()
        parent = PreferenceScreenParentFragment(fragment)
    }
}