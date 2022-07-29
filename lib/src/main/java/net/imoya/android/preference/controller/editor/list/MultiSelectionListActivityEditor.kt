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
import androidx.appcompat.app.AppCompatActivity
import net.imoya.android.preference.Constants
import net.imoya.android.preference.activity.editor.list.MultiSelectionListEditorActivity
import net.imoya.android.preference.controller.PreferenceScreenParent
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.MultiSelectionListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.MultiSelectionListPreferenceView

/**
 * 一覧より複数個を選択する [AppCompatActivity] の設定画面を使用する、設定コントローラの abstract
 *
 * * 選択画面は [MultiSelectionListEditorActivity] を使用します。
 */
abstract class MultiSelectionListActivityEditor(
    /**
     * 設定画面の親画面
     */
    parent: PreferenceScreenParent,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null
) : ListActivityEditor(parent, preferences) {

    /**
     * 設定画面を起動する [Intent] (extra 設定前のもの)
     */
    protected open val editorIntent: Intent
        get() = Intent(parent.context, MultiSelectionListEditorActivity::class.java)

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

    override fun createEditorIntent(view: PreferenceView): Intent {
        if (view !is MultiSelectionListPreferenceView) {
            throw IllegalArgumentException("View must be MultiSelectionListPreferenceView")
        }

        val intent = editorIntent
        intent.putExtra(
            Constants.KEY_EDITOR_STATE,
            (state as MultiSelectionListEditorState).toBundle()
        )
        return intent
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "MSelListActivityEditor"
//    }
}