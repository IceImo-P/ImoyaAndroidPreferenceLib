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

package net.imoya.android.preference.fragment.editor.time

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import net.imoya.android.preference.activity.editor.time.TimeEditorActivity
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.result.time.TimeEditorFragmentResult

/**
 * [TimeEditorActivity] へ表示する、 [androidx.fragment.app.Fragment] のデフォルト実装
 */
open class TimeEditorFragmentOnActivity : TimeEditorFragment() {
    override fun onClickBackButton() {
        val activity = requireActivity()
        activity.setResult(AppCompatActivity.RESULT_CANCELED)
        activity.finish()
    }

    override fun notifyResultToHost(selectedTime: Time) {
        val data = Intent()
        data.putExtra(TimeEditorFragmentResult.KEY_SELECTED_TIME, selectedTime)
        val activity = requireActivity()
        activity.setResult(AppCompatActivity.RESULT_OK, data)
        activity.finish()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "TimePrefEditorFragmentOA"
//    }
}