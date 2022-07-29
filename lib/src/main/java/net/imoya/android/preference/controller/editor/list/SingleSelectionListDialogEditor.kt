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
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.SingleChoiceDialog
import net.imoya.android.log.LogUtil
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.SingleSelectionListEditorState
import net.imoya.android.preference.view.list.ListPreferenceView
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.SingleSelectionListPreferenceView

/**
 * 一覧より 1 個を選択するダイアログの設定画面を使用する、設定コントローラの abstract
 */
abstract class SingleSelectionListDialogEditor(
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
) : ListDialogEditor(parent, preferences, requestCode) {

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

    override fun showDialog(view: PreferenceView) {
        val currentParent = checkParent()
        val key = checkKey()
        if (view !is ListPreferenceView) {
            throw IllegalArgumentException("View must be ListPreferenceView")
        }
        checkAndWarnRequestCode()
        val state = this.state as SingleSelectionListEditorState
        val entries: Array<String> = state.entries
        val selectedIndex = state.selectedIndex

        PreferenceLog.v(TAG) {
            "showDialog: title = ${view.title}, entries = ${LogUtil.logString(entries)}" +
                    ", selectedIndex = $selectedIndex, key = $key"
        }

        SingleChoiceDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setItems(entries)
            .setSelectedPosition(selectedIndex)
            .setTag(key)
            .show()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SingleSelListPrefEditor"
    }
}