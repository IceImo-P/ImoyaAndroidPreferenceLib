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

package net.imoya.android.preference.controller.editor.list

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import net.imoya.android.fragment.roundtrip.RoundTripManager
import net.imoya.android.preference.fragment.editor.list.SingleSelectionListEditorFragment
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.SingleSelectionListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.SingleSelectionListPreferenceView

/**
 * 一覧より 1 個を選択する [Fragment] の設定画面を使用する、設定コントローラの abstract
 *
 * * 選択画面は [SingleSelectionListEditorFragment] を使用します。
 */
abstract class SingleSelectionListFragmentEditor(
    /**
     * [RoundTripManager]
     */
    roundTripManager: RoundTripManager? = null,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null,
    /**
     * 編集画面の識別に使用するリクエストキー
     */
    requestKey: String? = null
) : ListFragmentEditor(roundTripManager, preferences, requestKey) {

    override fun createState(): ScreenEditorState {
        return SingleSelectionListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is SingleSelectionListPreferenceView) {
            throw IllegalArgumentException("View must be SingleSelectionListPreferenceView")
        }

        val state = this.state as SingleSelectionListEditorState
        state.entries = view.entries
        state.selectedIndex = view.getSelectedIndex(checkPreferences())
        state.singleSelectionType = view.singleSelectionType
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "SSelListFPrefEditor"
//    }
}