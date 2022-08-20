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

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.TimePicker
import androidx.appcompat.app.ActionBar
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.result.time.TimeEditorFragmentResult
import net.imoya.android.preference.model.state.time.TimeEditorState
import net.imoya.android.util.TimePickerHelper

/**
 * [Time] を編集する [androidx.fragment.app.Fragment] のデフォルト実装
 */
open class TimeEditorFragment : TimeEditorFragmentBase() {
    /**
     * 起動時のパラメータ、および編集中のデータ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var editorState: TimeEditorState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceLog.v(TAG) { "onCreate: arguments = { ${requireArguments()} } " }
        editorState = TimeEditorState(getEditorStateBundle(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subtitleView?.visibility = View.GONE
        setupTimePicker()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveEditorStateToInstanceState(outState, editorState.toBundle())
    }

    override fun setupActionBar(actionBar: ActionBar) {
        // Set title
        PreferenceLog.v(TAG) { "setupActionBar: editorState = { title: ${editorState.title}, key: ${editorState.key}, time: ${editorState.time}, is24: ${editorState.is24hourView} }" }
        val titleText = editorState.title ?: editorState.key
        PreferenceLog.v(TAG) { "setupActionBar: title = $titleText" }
        requireActivity().title = titleText
        actionBar.setDisplayHomeAsUpEnabled(false)
    }

    override fun setupFakeActionBar(fakeActionBar: View) {
        // Set title
        titleViewOnFakeActionBar?.text = editorState.title ?: editorState.key
        backButtonOnFakeActionBar?.visibility = View.GONE
    }

    /**
     * [TimePicker] の時刻を初期設定します。
     */
    protected open fun setupTimePicker() {
        val picker: TimePicker = timePicker ?: throw IllegalStateException("TimePicker not found")
        picker.setIs24HourView(editorState.is24hourView)

        // state.time が null でない場合はその値を設定し、
        // state.time が null である場合は state.timeForNull の値を設定する
        TimePickerHelper(picker).setHourAndMinute(
            editorState.time?.hour ?: editorState.timeForNull.hour,
            editorState.time?.minute ?: editorState.timeForNull.minute
        )

        picker.setOnTimeChangedListener { _, hourOfDay, minute ->
            editorState.time = Time(hourOfDay, minute, 0)
        }
    }

    /**
     * OKボタン押下時の処理
     */
    override fun onClickOkButton() {
        notifyResultToHost(editorState.time ?: editorState.timeForNull)
    }

    /**
     * 呼び出し元画面へ結果を通知します。
     *
     * @param selectedTime 選択された時刻
     */
    protected open fun notifyResultToHost(selectedTime: Time) {
        val result =
            TimeEditorFragmentResult(Activity.RESULT_OK, selectedTime)
        returnToHost(result.toBundle())
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePrefEditorFragment"
    }
}