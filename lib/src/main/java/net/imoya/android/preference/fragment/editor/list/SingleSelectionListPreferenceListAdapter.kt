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

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import net.imoya.android.preference.PreferenceLog
import java.lang.IllegalStateException

/**
 * [ListEditorFragment] に於いて、選択肢の一覧表示に使用する Adapter
 */
open class SingleSelectionListPreferenceListAdapter : ListPreferenceListAdapter() {
    /**
     * Initial selected index
     */
    var selectedIndex: Int = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        // 選択中アイコンを表示
        val selectedIconView: ImageView = getItemSelectedIconView(view)
            ?: throw IllegalStateException("Selected icon view not found")
        selectedIconView.visibility = if (position == selectedIndex) {
            PreferenceLog.v(TAG) { "selected: $position" }
            View.VISIBLE
        } else View.INVISIBLE

        return view
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SingleSelListPrefListAdapter"
    }
}