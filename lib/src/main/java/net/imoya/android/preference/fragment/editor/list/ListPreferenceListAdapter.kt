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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.imoya.android.preference.R
import net.imoya.android.preference.fragment.editor.list.ListEditorFragment

/**
 * [ListEditorFragment] に於いて、選択肢の一覧表示に使用する Adapter
 */
abstract class ListPreferenceListAdapter : BaseAdapter() {
    /**
     * Labels to show in list
     */
    var labels: Array<String> = arrayOf()

    override fun getCount(): Int {
        return labels.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItem(position: Int): Any? {
        val items = this.labels
        return if (position >= 0 && position < items.size) items[position]
        else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(
            R.layout.imoya_preference_list_fragment_item, parent, false
        )
        val label = labels[position]

        // ラベルを表示
        view.findViewById<TextView>(android.R.id.title)?.text = label

        return view
    }

    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "ListPreferenceListAdapter"
    }
}