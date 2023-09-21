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
import net.imoya.android.preference.activity.editor.list.MultiSelectionListEditorActivity
import net.imoya.android.preference.controller.PreferenceScreenParent
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.MultiSelectionStringListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.MultiSelectionStringListPreferenceView

/**
 * 文字列値複数選択設定コントローラ
 *
 * * 選択画面は [MultiSelectionListEditorActivity] を使用します。
 * * [MultiSelectionStringListPreferenceView] と組み合わせて使用することを想定しています。
 */
open class MultiSelectionStringListActivityEditor(
    /**
     * 設定画面の親画面
     */
    parent: PreferenceScreenParent,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null
) : MultiSelectionListActivityEditor(parent, preferences) {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is MultiSelectionStringListPreferenceView
    }

    override val instanceStateClass: Class<out ScreenEditorState>
        get() = MultiSelectionStringListEditorState::class.java

    override fun createState(): ScreenEditorState {
        return MultiSelectionStringListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is MultiSelectionStringListPreferenceView) {
            throw IllegalArgumentException("View must be StringListPreferenceView")
        }
        (state as MultiSelectionStringListEditorState).entryValues = view.entryValues
    }

    override fun createEditorIntent(view: PreferenceView): Intent {
        if (view !is MultiSelectionStringListPreferenceView) {
            throw IllegalArgumentException("View must be StringListPreferenceView")
        }

        return super.createEditorIntent(view)
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        val currentPreferences = checkPreferences()
        val key = checkKey()

        val checkedList =
            requireNotNull(requireNotNull(data).getBooleanArrayExtra(ListEditorUtil.KEY_SELECTION))
        val entryValues = (state as MultiSelectionStringListEditorState).entryValues

        ListEditorUtil.saveInput(
            currentPreferences,
            key,
            entryValues,
            checkedList
        ) { e, c -> createPreferenceValue(e, c) }
    }

    /**
     * [SharedPreferences] へ保存する値を生成して返します。
     *
     * @param entryValues 各項目の設定値リスト
     * @param checkedList 各項目の選択状態リスト
     * @return [SharedPreferences] へ保存する値
     */
    open fun createPreferenceValue(entryValues: Array<String>, checkedList: BooleanArray): String {
        return ListEditorUtil.createPreferenceValue(entryValues, checkedList)
    }

    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "MSelStrListAPrefEditor"
    }
}