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

package net.imoya.android.preference.activity.editor.list

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import net.imoya.android.preference.activity.editor.PreferenceEditorActivityWithFragment
import net.imoya.android.preference.fragment.editor.list.MultiSelectionListEditorFragmentOnActivity

/**
 * 一覧より複数項目を選択する [AppCompatActivity]
 * with [MultiSelectionListEditorFragmentOnActivity]
 */
class MultiSelectionListEditorActivity : PreferenceEditorActivityWithFragment() {

    override val editorFragment: Fragment
        get() = MultiSelectionListEditorFragmentOnActivity()

//    companion object {
//        /**
//         * Tag for log
//         */
//        const val TAG = "MSelListEditorActivity"
//    }
}