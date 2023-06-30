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
import net.imoya.android.preference.activity.editor.time.TimeEditorActivity
import net.imoya.android.preference.controller.editor.ActivityEditor
import net.imoya.android.preference.controller.PreferenceScreenParent
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.result.time.TimeEditorFragmentResult
import net.imoya.android.preference.model.state.time.TimeEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePreferenceView
import net.imoya.android.util.IntentUtil

/**
 * [Time] 設定値編集コントローラ
 *
 * * 設定画面は [TimeEditorActivity] を使用します。
 * * [TimePreferenceView] と組み合わせて使用することを想定しています。
 */
open class TimeActivityEditor(
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
     * 入力完了リスナ
     */
    fun interface OnEditListener {
        /**
         * 入力完了時コールバック
         *
         * @param editor [TimeActivityEditor]
         * @param key    Key of [SharedPreferences]
         * @param value  User-input [Time] (or null)
         */
        @Suppress("unused")
        fun onEdit(editor: TimeActivityEditor, key: String, value: Time?)
    }

    /**
     * 入力完了リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var onEditListener: OnEditListener? = null

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is TimePreferenceView
    }

    override val instanceStateClass: Class<out ScreenEditorState>
        get() = TimeEditorState::class.java

    override fun createState(): ScreenEditorState {
        return TimeEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)

        val timePreferenceView = view as TimePreferenceView
        val editorState = (state as TimeEditorState)
        editorState.is24hourView = timePreferenceView.is24hourView
        try {
            val timeString = preferences?.getString(state.key, null)
            editorState.time =
                if (timeString?.isNotEmpty() == true) Time.parse(timeString)
                else null
        } catch (e: Exception) {
            editorState.time = null
        }
        editorState.timeForNull = timePreferenceView.timeForNull
    }

    override fun createEditorIntent(view: PreferenceView): Intent {
        if (view !is TimePreferenceView) {
            throw IllegalArgumentException("View must be TimePreferenceView")
        }

        val intent = Intent(parent.context, TimeEditorActivity::class.java)
        intent.putExtra(
            Constants.KEY_EDITOR_STATE,
            (state as TimeEditorState).toBundle()
        )
        return intent
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val key = checkKey()
        val period = IntentUtil.getParcelableExtra(
            data, TimeEditorFragmentResult.KEY_SELECTED_TIME, Time::class.java
        ) ?: throw IllegalArgumentException("time is null")

        checkPreferences().edit()
            .putString(key, period.toString())
            .apply()

        onEditListener?.onEdit(this, key, period)
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "TimePreferenceEditor"
//    }
}