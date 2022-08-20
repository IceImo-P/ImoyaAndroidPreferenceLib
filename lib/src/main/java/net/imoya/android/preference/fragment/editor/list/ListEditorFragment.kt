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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.fragment.editor.EditorFragment
import net.imoya.android.preference.model.state.list.ListEditorState

/**
 * 一覧より選択するUIを持つ [Fragment]
 */
abstract class ListEditorFragment : EditorFragment(),
    AdapterView.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.imoya_preference_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferenceLog.v(TAG, "onViewCreated: start")

        // Setup ListView
        val listView: ListView = this.listView
        listView.adapter = listAdapter
        listView.onItemClickListener = this

        // Restore ListView scroll position
        if (savedInstanceState != null) {
            listView.scrollY = savedInstanceState.getInt(KEY_SCROLL_POSITION)
        }

        PreferenceLog.v(TAG, "onViewCreated: end")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save ListView scroll position
        outState.putInt(KEY_SCROLL_POSITION, listView.scrollY)
    }

    /**
     * Returns [ListEditorState]
     */
    abstract var editorState: ListEditorState

    /**
     * Returns labels to show in [ListView]
     */
    open val labels: Array<String>
        get() = editorState.entries

    /**
     * Returns [ListView]
     */
    open val listView: ListView
        get() = requireView().findViewById(R.id.list)

    /**
     * Returns adapter for [ListView]
     */
    abstract val listAdapter: ListPreferenceListAdapter

    override fun setupActionBar(actionBar: ActionBar) {
        // Set title
        PreferenceLog.v(TAG) { "onViewCreated: title = ${editorState.title ?: editorState.key}" }
        requireActivity().title = editorState.title ?: editorState.key
    }

    override fun setupFakeActionBar(fakeActionBar: View) {
        // Set title
        titleViewOnFakeActionBar?.text = editorState.title ?: editorState.key
    }

    companion object {
        /**
         * InstanceState key: ScrollView position
         */
        private const val KEY_SCROLL_POSITION = "scrollPosition"

        /**
         * Tag for log
         */
        private const val TAG = "ListEditorFragment"
    }
}