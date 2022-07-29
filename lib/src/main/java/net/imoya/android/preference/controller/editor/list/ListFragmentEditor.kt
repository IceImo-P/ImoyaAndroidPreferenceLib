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
import net.imoya.android.preference.controller.editor.FragmentEditor
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.ListEditorState
import net.imoya.android.preference.view.list.ListPreferenceView
import net.imoya.android.preference.view.PreferenceView

/**
 * 一覧より選択する [Fragment] の設定画面を使用する、設定コントローラの abstract
 */
abstract class ListFragmentEditor(
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
) : FragmentEditor(roundTripManager, preferences, requestKey) {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is ListPreferenceView
    }

    override fun createState(): ScreenEditorState {
        return ListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is ListPreferenceView) {
            throw IllegalArgumentException("View must be ListPreferenceView")
        }

        val state = this.state as ListEditorState
        state.entries = view.entries
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "ListFragmentEditor"
//    }
}