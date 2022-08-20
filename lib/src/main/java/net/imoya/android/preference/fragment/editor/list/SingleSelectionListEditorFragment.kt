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
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.ActionBar
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.state.list.ListEditorState
import net.imoya.android.preference.model.result.list.SingleSelectionListEditorFragmentResult
import net.imoya.android.preference.model.state.list.SingleSelectionListEditorState
import net.imoya.android.preference.type.SingleSelectionType

/**
 * 一覧より1項目を選択する [androidx.fragment.app.Fragment]
 */
open class SingleSelectionListEditorFragment : ListEditorFragment(),
    AdapterView.OnItemClickListener {

    private lateinit var myEditorState: ListEditorState

    /**
     * Returns [SingleSelectionListEditorState]
     */
    override var editorState: ListEditorState
        get() = myEditorState
        set(value) {
            myEditorState = value
        }

    /**
     * Returns adapter for [ListView]
     */
    override val listAdapter: SingleSelectionListPreferenceListAdapter
        get() {
            val adapter = SingleSelectionListPreferenceListAdapter()
            adapter.labels = labels
            adapter.selectedIndex =
                (editorState as SingleSelectionListEditorState).selectedIndex
            return adapter
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceLog.v(TAG) { "onCreate: start. savedInstanceState ${
            if (savedInstanceState != null) "exists" else "does not exist"
        }" }

        myEditorState = SingleSelectionListEditorState(getEditorStateBundle(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup bottom buttons
        val buttons: View? = buttonsView
        when ((editorState as SingleSelectionListEditorState).singleSelectionType) {
            SingleSelectionType.ITEM_CLICK -> buttons?.visibility = View.GONE
            else -> buttons?.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveEditorStateToInstanceState(outState, myEditorState.toBundle())
    }

    override fun setupActionBar(actionBar: ActionBar) {
        super.setupActionBar(actionBar)

        actionBar.setHomeAsUpIndicator(null)
        setHasOptionsMenu(false)
        when ((editorState as SingleSelectionListEditorState).singleSelectionType) {
            SingleSelectionType.ITEM_CLICK -> {
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
            else -> actionBar.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun setupFakeActionBar(fakeActionBar: View) {
        super.setupFakeActionBar(fakeActionBar)

        val backButton: ImageButton? = backButtonOnFakeActionBar
//        backButton?.setImageDrawable(backButtonImage(fakeActionBar.context))
        backButton?.setImageDrawable(backButtonImage(requireContext()))
        when ((editorState as SingleSelectionListEditorState).singleSelectionType) {
            SingleSelectionType.ITEM_CLICK -> backButton?.visibility = View.VISIBLE
            else -> backButton?.visibility = View.GONE
        }
    }

    override fun onClickOkButton() {
        notifyResultToHost((editorState as SingleSelectionListEditorState).selectedIndex)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when ((editorState as SingleSelectionListEditorState).singleSelectionType) {
            SingleSelectionType.ITEM_CLICK -> {
                // 選択結果を親画面へ通知する
                PreferenceLog.v(TAG) { "onItemClick: position = $position, id = $id" }
                notifyResultToHost(position)
            }
            else -> {
                // 選択中の位置を変更する
                (editorState as SingleSelectionListEditorState).selectedIndex = position
                (listView.adapter as SingleSelectionListPreferenceListAdapter).selectedIndex =
                    position
                (listView.adapter as SingleSelectionListPreferenceListAdapter).notifyDataSetChanged()
                listView.invalidateViews()
            }
        }
    }

    /**
     * 呼び出し元画面へ結果を通知します。
     *
     * @param position 選択された項目のインデックス位置
     */
    protected open fun notifyResultToHost(position: Int) {
        val result =
            SingleSelectionListEditorFragmentResult(Activity.RESULT_OK, position)
        returnToHost(result.toBundle())
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SSelListEditorFragment"
    }
}