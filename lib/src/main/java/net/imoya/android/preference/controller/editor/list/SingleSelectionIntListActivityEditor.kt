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

package net.imoya.android.preference.controller.editor.list

import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.preference.activity.editor.list.SingleSelectionListEditorActivity
import net.imoya.android.preference.controller.PreferenceScreenParent
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.SingleSelectionIntListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.SingleSelectionIntListPreferenceView

/**
 * 数値 1 件選択設定コントローラ
 *
 * * 選択画面は [SingleSelectionListEditorActivity] を使用します。
 * * [SingleSelectionIntListPreferenceView] と組み合わせて使用することを想定しています。
 */
open class SingleSelectionIntListActivityEditor(
    /**
     * 設定画面の親画面
     */
    parent: PreferenceScreenParent,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null,
) : SingleSelectionListActivityEditor(parent, preferences) {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is SingleSelectionIntListPreferenceView
    }

    override val instanceStateClass: Class<out ScreenEditorState>
        get() = SingleSelectionIntListEditorState::class.java

    override fun createState(): ScreenEditorState {
        return SingleSelectionIntListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is SingleSelectionIntListPreferenceView) {
            throw IllegalArgumentException("View must be IntListPreferenceView")
        }
        (state as SingleSelectionIntListEditorState).entryValues = view.entryValues
    }

    override fun createEditorIntent(view: PreferenceView): Intent {
        if (view !is SingleSelectionIntListPreferenceView) {
            throw IllegalArgumentException("View must be IntListPreferenceView")
        }

        return super.createEditorIntent(view)
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        val currentPreferences = checkPreferences()
        val key = checkKey()

        val selection =
            requireNotNull(data).getIntExtra(ListEditorUtil.KEY_SELECTION, -1)
        val entryValues = (state as SingleSelectionIntListEditorState).entryValues

        ListEditorUtil.saveInput(currentPreferences, key, entryValues, selection)
    }

    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "SSelIntListAPrefEditor"
    }
}