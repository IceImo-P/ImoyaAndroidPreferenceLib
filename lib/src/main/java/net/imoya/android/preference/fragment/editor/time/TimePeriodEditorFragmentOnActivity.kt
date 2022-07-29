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
import net.imoya.android.preference.Constants
import net.imoya.android.preference.activity.editor.time.TimePeriodEditorActivity
import net.imoya.android.preference.model.result.time.TimePeriodEditorFragmentResult

/**
 * [TimePeriodEditorActivity] へ表示する、 [androidx.fragment.app.Fragment] のデフォルト実装
 */
open class TimePeriodEditorFragmentOnActivity : TimePeriodEditorFragment() {
    override val containerId: Int
        get() = requireArguments().getInt(Constants.KEY_CONTAINER_ID)

    override val endTimeFragment: TimePeriodEditorFragment
        get() = TimePeriodEditorFragmentOnActivity()

    /**
     * 編集を完了し、呼び出し元画面へ遷移します。
     */
    override fun complete() {
        setResultAndFinish(AppCompatActivity.RESULT_OK)
    }

    /**
     * 編集をキャンセルし、呼び出し元画面へ遷移します。
     */
    override fun cancel() {
        setResultAndFinish(AppCompatActivity.RESULT_CANCELED)
    }

    /**
     * 呼び出し元画面へ結果を通知し、この画面を終了します。
     */
    protected open fun setResultAndFinish(resultCode: Int) {
        val data = Intent()
        data.putExtra(
            TimePeriodEditorFragmentResult.KEY_SELECTED_TIME_PERIOD,
            fragmentState.timePeriod
        )
        val activity = requireActivity()
        activity.setResult(resultCode, data)
        activity.finish()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "TimePeriodEditorFragmentOA"
//    }
}