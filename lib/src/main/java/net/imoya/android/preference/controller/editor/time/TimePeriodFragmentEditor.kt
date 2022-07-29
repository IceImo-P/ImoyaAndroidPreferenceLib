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

package net.imoya.android.preference.controller.editor.time

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import net.imoya.android.fragment.ArgumentsUtil
import net.imoya.android.fragment.roundtrip.RoundTripManager
import net.imoya.android.preference.Constants
import net.imoya.android.preference.controller.editor.FragmentEditor
import net.imoya.android.preference.fragment.editor.time.TimePeriodEditorFragment
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.model.result.time.TimePeriodEditorFragmentResult
import net.imoya.android.preference.model.state.time.TimePeriodEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePeriodPreferenceView

/**
 * [TimePeriod] 設定値編集コントローラ with [Fragment]
 *
 * * 設定画面は [TimePeriodEditorFragment] を使用します。
 * * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
class TimePeriodFragmentEditor(
    /**
     * [RoundTripManager]
     */
    roundTripManager: RoundTripManager? = null,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null,
    /**
     * 編集画面の識別に使用するリクエストキー
     */
    requestKey: String? = null
) : FragmentEditor(roundTripManager, preferences, requestKey) {
    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is TimePeriodPreferenceView
    }

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
                if (timeString != null && timeString.isNotEmpty()) TimePeriod.parse(timeString)
                else null
        } catch (e: Exception) {
            editorState.timePeriod = null
        }
        editorState.timePeriodForNull =
            TimePeriod(timePeriodPreferenceView.timeForNull, timePeriodPreferenceView.timeForNull)
    }

    override fun startEditorUI(view: PreferenceView) {
        val fragment = TimePeriodEditorFragment()
        ArgumentsUtil.setArgument(fragment) {
            it.putBundle(
                Constants.KEY_EDITOR_STATE,
                (state as TimePeriodEditorState).toBundle()
            )
        }
        checkRoundTripManager().start(
            checkRequestKey(),
            fragment,
            TimePeriodEditorFragment.TAG_START_TIME
        )
    }

    override fun onEditorResult(result: Bundle) {
        val currentPreferences = checkPreferences()
        val key = checkKey()

        val fragmentResult = TimePeriodEditorFragmentResult(result)

        if (fragmentResult.resultCode == Activity.RESULT_OK) {
            if (fragmentResult.selectedTimePeriod == null) {
                currentPreferences.edit().remove(key).apply()
            } else {
                currentPreferences.edit()
                    .putString(key, fragmentResult.selectedTimePeriod.toString())
                    .apply()
            }
        }
    }

}