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
import net.imoya.android.dialog.MultiChoiceDialog
import net.imoya.android.log.LogUtil
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.MultiSelectionListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.MultiSelectionListPreferenceView

/**
 * 一覧より複数個を選択するダイアログの設定画面を使用する、設定コントローラの abstract
 *
 * * 選択画面は [MultiChoiceDialog] を使用します。
 */
abstract class MultiSelectionListDialogEditor(
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
        return MultiSelectionListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is MultiSelectionListPreferenceView) {
            throw IllegalArgumentException("View must be MultiSelectionListPreferenceView")
        }

        val state = this.state as MultiSelectionListEditorState
        state.checkedList = view.getCheckedList(checkPreferences())
            ?: BooleanArray(state.entries.size)
    }

    override fun showDialog(view: PreferenceView) {
        val currentParent = checkParent()
        val key = checkKey()
        if (view !is MultiSelectionListPreferenceView) {
            throw IllegalArgumentException("View must be MultiSelectionListPreferenceView")
        }
        checkAndWarnRequestCode()
        val state = this.state as MultiSelectionListEditorState
        val entries: Array<String> = state.entries
        val checkedList = state.checkedList

        PreferenceLog.v(TAG) {
            "showDialog: title = ${view.title}, entries = ${LogUtil.logString(entries)}" +
                    ", selectedIndex = $checkedList, key = $key"
        }

        MultiChoiceDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setItems(entries)
            .setCheckedList(checkedList)
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