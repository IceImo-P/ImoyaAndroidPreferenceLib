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

package net.imoya.android.preference.fragment.editor.list

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.ActionBar
import net.imoya.android.preference.R
import net.imoya.android.preference.model.result.list.MultiSelectionListEditorFragmentResult
import net.imoya.android.preference.model.state.list.*
import net.imoya.android.preference.model.state.list.ListEditorState
import net.imoya.android.preference.model.state.list.MultiSelectionListEditorState

/**
 * 一覧より複数項目を選択する [androidx.fragment.app.Fragment]
 */
open class MultiSelectionListEditorFragment : ListEditorFragment(),
    AdapterView.OnItemClickListener {

    private lateinit var myEditorState: MultiSelectionListEditorState

    /**
     * Returns [SingleSelectionListEditorState]
     */
    override var editorState: ListEditorState
        get() = myEditorState
        set(value) {
            myEditorState =
                if (value is MultiSelectionListEditorState) value
                else throw IllegalArgumentException(
                    "EditorState must be MultiSelectionListPreferenceEditorState"
                )
        }

    /**
     * Returns adapter for [ListView]
     */
    override val listAdapter: MultiSelectionListPreferenceListAdapter
        get() {
            val adapter = MultiSelectionListPreferenceListAdapter()
            adapter.labels = labels
            adapter.selectedIndices =
                (editorState as MultiSelectionListEditorState).checkedList
            return adapter
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myEditorState = MultiSelectionListEditorState(getEditorStateBundle(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup bottom buttons
        view.findViewById<View>(R.id.buttons).visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveEditorStateToInstanceState(outState, myEditorState.toBundle())
    }

    override fun setupActionBar(actionBar: ActionBar) {
        super.setupActionBar(actionBar)

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(backButtonImage(actionBar.themedContext))
        setHasOptionsMenu(false)
        actionBar.setDisplayHomeAsUpEnabled(false)
    }

    override fun setupFakeActionBar(fakeActionBar: View) {
        super.setupFakeActionBar(fakeActionBar)

        fakeActionBar.findViewById<View>(R.id.back).visibility = View.GONE
    }

    override fun onClickOkButton() {
        notifyResultToHost((editorState as MultiSelectionListEditorState).checkedList)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // クリックされた項目の選択状態を変更する
        val state = editorState as MultiSelectionListEditorState
        state.checkedList[position] = !state.checkedList[position]
        (listView.adapter as MultiSelectionListPreferenceListAdapter).selectedIndices[position] =
            state.checkedList[position]
        (listView.adapter as MultiSelectionListPreferenceListAdapter).notifyDataSetChanged()
        listView.invalidateViews()
    }

    /**
     * 呼び出し元画面へ結果を通知します。
     *
     * @param checkedList 項目の選択状態リスト
     */
    protected open fun notifyResultToHost(checkedList: BooleanArray) {
        val result =
            MultiSelectionListEditorFragmentResult(Activity.RESULT_OK, checkedList)
        returnToHost(result.toBundle())
    }

    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "MSelListEditorFragment"
    }
}