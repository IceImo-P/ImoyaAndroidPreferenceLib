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

import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.MultiChoiceDialog
import net.imoya.android.dialog.MultiChoiceDialogBase
import net.imoya.android.preference.Constants
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.MultiSelectionIntListEditorState
import net.imoya.android.preference.view.list.MultiSelectionIntListPreferenceView
import net.imoya.android.preference.view.PreferenceView

/**
 * 数値複数選択設定コントローラ
 *
 * * 選択画面は [MultiChoiceDialog] を使用します。
 * *[MultiSelectionIntListPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class MultiSelectionIntListDialogEditor(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent? = null,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCode: Int = Constants.DEFAULT_REQUEST_CODE
) : MultiSelectionListDialogEditor(parent, preferences, requestCode) {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is MultiSelectionIntListPreferenceView
    }

    override fun createState(): ScreenEditorState {
        return MultiSelectionIntListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is MultiSelectionIntListPreferenceView) {
            throw IllegalArgumentException("View must be MultiSelectionIntListPreferenceView")
        }
        (state as MultiSelectionIntListEditorState).entryValues = view.entryValues
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val currentPreferences = checkPreferences()
        val key = checkKey()
        val state = this.state as MultiSelectionIntListEditorState

        val checkedList = data.getBooleanArrayExtra(MultiChoiceDialogBase.EXTRA_KEY_CHECKED_LIST)
            ?: BooleanArray(state.entries.size)
        val entryValues = state.entryValues

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
    open fun createPreferenceValue(entryValues: IntArray, checkedList: BooleanArray): String {
        return ListEditorUtil.createPreferenceValue(entryValues, checkedList)
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "MSelIntListDPrefEditor"
//    }
}