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
import android.content.Intent
import net.imoya.android.preference.activity.editor.list.MultiSelectionListEditorActivity
import net.imoya.android.preference.controller.editor.list.ListEditorUtil

/**
 * 一覧より複数項目を選択する [androidx.fragment.app.Fragment]
 * on [MultiSelectionListEditorActivity]
 */
open class MultiSelectionListEditorFragmentOnActivity :
    MultiSelectionListEditorFragment() {

    override fun notifyResultToHost(checkedList: BooleanArray) {
        val data = Intent()
        data.putExtra(
            ListEditorUtil.KEY_SELECTION,
            checkedList
        )
        val activity = requireActivity()
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    override fun onClickBackButton() {
        val activity = requireActivity()
        activity.setResult(Activity.RESULT_CANCELED, Intent())
        activity.finish()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "MSelListEditFragmentOA"
//    }
}