/*
 * Copyright (C) 2022-2023 IceImo-P
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

package net.imoya.android.preference.controller.editor.time

import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.preference.Constants
import net.imoya.android.preference.activity.editor.time.TimePeriodEditorActivity
import net.imoya.android.preference.controller.editor.ActivityEditor
import net.imoya.android.preference.controller.PreferenceScreenParent
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.model.result.time.TimePeriodEditorFragmentResult
import net.imoya.android.preference.model.state.time.TimePeriodEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePeriodPreferenceView
import net.imoya.android.util.IntentUtil

/**
 * [TimePeriod] 設定値編集コントローラ
 *
 * * 設定画面は [TimePeriodEditorActivity] を使用します。
 * * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
open class TimePeriodActivityEditor(
    /**
     * 設定画面の親画面
     */
    parent: PreferenceScreenParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null
) : ActivityEditor(parent, preferences) {
    /**
     * リスナ
     */
    fun interface Listener {
        /**
         * 入力完了時コールバック
         *
         * @param editor [TimePeriodActivityEditor]
         * @param key    Key of [SharedPreferences]
         * @param value  User-input [TimePeriod] (or null)
         */
        @Suppress("unused")
        fun onEdit(editor: TimePeriodActivityEditor, key: String, value: TimePeriod?)
    }

    /**
     * リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var listener: Listener? = null

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is TimePeriodPreferenceView
    }

    override val instanceStateClass: Class<out ScreenEditorState>
        get() = TimePeriodEditorState::class.java

    override fun createState(): ScreenEditorState {
        return TimePeriodEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)

        val timePeriodPreferenceView = view as TimePeriodPreferenceView
        val editorState = (state as TimePeriodEditorState)
        editorState.is24hourView = timePeriodPreferenceView.is24hourView
        try {
            val timeString = preferences?.getString(state.key, null)
            editorState.timePeriod =
                if (!timeString.isNullOrEmpty()) TimePeriod.parse(timeString)
                else null
        } catch (e: Exception) {
            editorState.timePeriod = null
        }
        editorState.timePeriodForNull =
            TimePeriod(timePeriodPreferenceView.timeForNull, timePeriodPreferenceView.timeForNull)
    }

    override fun createEditorIntent(view: PreferenceView): Intent {
        if (view !is TimePeriodPreferenceView) {
            throw IllegalArgumentException("View must be TimePeriodPreferenceView")
        }

        val intent = Intent(parent.context, TimePeriodEditorActivity::class.java)
        intent.putExtra(
            Constants.KEY_EDITOR_STATE,
            (state as TimePeriodEditorState).toBundle()
        )
        return intent
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val key = checkKey()
        val period = IntentUtil.getParcelableExtra(
            data, TimePeriodEditorFragmentResult.KEY_SELECTED_TIME_PERIOD, TimePeriod::class.java
        ) ?: throw IllegalArgumentException("timePeriod is null")

        checkPreferences().edit()
            .putString(key, period.toString())
            .apply()

        listener?.onEdit(this, key, period)
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "TimePeriodPreferenceEditor"
//    }
}