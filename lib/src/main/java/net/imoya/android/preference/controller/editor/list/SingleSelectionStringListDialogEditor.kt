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
import net.imoya.android.dialog.DialogBase
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.SingleChoiceDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.SingleSelectionStringListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.SingleSelectionStringListPreferenceView

/**
 * 文字列値 1 件選択設定コントローラ
 *
 * * 選択画面は [SingleChoiceDialog] を使用します。
 * * [SingleSelectionStringListPreferenceView] と組み合わせて使用することを想定しています。
 */
open class SingleSelectionStringListDialogEditor(
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
) : SingleSelectionListDialogEditor(parent, preferences, requestCode) {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is SingleSelectionStringListPreferenceView
    }

    override val instanceStateClass: Class<out ScreenEditorState>
        get() = SingleSelectionStringListEditorState::class.java

    override fun createState(): ScreenEditorState {
        return SingleSelectionStringListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is SingleSelectionStringListPreferenceView) {
            throw IllegalArgumentException("View must be StringListPreferenceView")
        }
        (state as SingleSelectionStringListEditorState).entryValues = view.entryValues
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val currentPreferences = checkPreferences()
        val key = checkKey()

        val selection = data.getIntExtra(DialogBase.EXTRA_KEY_WHICH, -1)
        val entryValues = (state as SingleSelectionStringListEditorState).entryValues

        ListEditorUtil.saveInput(currentPreferences, key, entryValues, selection)
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "SSelStrListDPrefEditor"
//    }
}