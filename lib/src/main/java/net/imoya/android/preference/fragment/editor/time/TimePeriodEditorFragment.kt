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
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.ActionBar
import net.imoya.android.fragment.ArgumentsUtil
import net.imoya.android.fragment.roundtrip.RoundTripManager
import net.imoya.android.fragment.roundtrip.RoundTripManagerForFragmentHost
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.model.result.time.TimePeriodEditorFragmentResult
import net.imoya.android.preference.model.state.time.TimePeriodEditorFragmentState
import net.imoya.android.preference.model.state.time.TimePeriodEditorFragmentState.Step
import net.imoya.android.preference.model.state.time.TimePeriodEditorState
import net.imoya.android.util.TimePickerHelper

/**
 * [TimePeriod] を編集する [androidx.fragment.app.Fragment] のデフォルト実装
 */
open class TimePeriodEditorFragment : TimeEditorFragmentBase() {
    /**
     * 起動時のパラメータ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var editorState: TimePeriodEditorState

    /**
     * 編集中のデータ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var fragmentState: TimePeriodEditorFragmentState

    /**
     * "Host" が指定した、 "client" を表示する領域となる [View] の ID
     */
    open val containerId: Int
        get() = roundTripContainerId

    /**
     * 終了時刻入力画面の [androidx.fragment.app.Fragment]
     */
    open val endTimeFragment: TimePeriodEditorFragment
        get() = TimePeriodEditorFragment()

    /**
     * 終了時刻を入力する [TimePeriodEditorFragment] 起動用の [RoundTripManager]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var roundTripManagerForEndTime: RoundTripManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editorState = TimePeriodEditorState(getEditorStateBundle(null))
        // PreferenceLog.d(TAG) { "onCreate: editorState.is24hourView = ${editorState.is24hourView}" }
        val fragmentStateBundle: Bundle? = savedInstanceState?.getBundle(KEY_FRAGMENT_STATE)
            ?: requireArguments().getBundle(KEY_FRAGMENT_STATE)
        fragmentState =
            if (fragmentStateBundle != null) {
                TimePeriodEditorFragmentState(fragmentStateBundle)
            } else {
                TimePeriodEditorFragmentState(
                    editorState.timePeriod ?: editorState.timePeriodForNull
                )
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roundTripManagerForEndTime = RoundTripManagerForFragmentHost(this)
        roundTripManagerForEndTime.containerId = containerId

        when (fragmentState.step) {
            Step.START_TIME -> {
                setupStartViews(view)
            }
            Step.END_TIME -> {
                setupEndViews(view)
            }
        }
    }

    override fun setupActionBar(actionBar: ActionBar) {
        // Set title
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
     * 開始時刻入力 UI を初期化します。
     */
    open fun setupStartViews(view: View) {
        subtitleView?.text =
            view.context.getString(R.string.imoya_preference_time_period_edit_start_title)
        setupTimePicker(view, fragmentState.timePeriod.start)
        val cancelButton = cancelButtonOnButtonsView
        cancelButton?.text =
            requireContext().getString(R.string.imoya_preference_time_period_edit_cancel)
        cancelButton?.setOnClickListener { cancel() }
        val okButton = okButtonOnButtonsView
        okButton?.text =
            requireContext().getString(R.string.imoya_preference_time_period_edit_to_end)
        okButton?.setOnClickListener { toEndTime() }

        // 終了時刻のコールバック処理
        roundTripManagerForEndTime.setResultListener(REQUEST_KEY_END_TIME) { requestKey: String, bundle: Bundle ->
            PreferenceLog.v(TAG) { "onFragmentResult: start. requestKey = $requestKey" }
            val result = TimePeriodEditorFragmentResult(bundle)
            fragmentState.timePeriod.end =
                result.selectedTimePeriod?.end ?: editorState.timePeriodForNull.end
            PreferenceLog.d(TAG) { "onFragmentResult: resultCode = ${result.resultCode}" }
            when (result.resultCode) {
                Activity.RESULT_OK -> returnToHost(bundle)
                // Activity.RESULT_CANCELED -> setupStartViews(requireView())
            }
        }
    }

    /**
     * 終了時刻入力 UI を初期化します。
     */
    open fun setupEndViews(view: View) {
        subtitleView?.text =
            view.context.getString(R.string.imoya_preference_time_period_edit_end_title)
        setupTimePicker(view, fragmentState.timePeriod.end)
        val cancelButton = cancelButtonOnButtonsView
        cancelButton?.text =
            requireContext().getString(R.string.imoya_preference_time_period_edit_to_start)
        cancelButton?.setOnClickListener { toStartTime() }
        val okButton = okButtonOnButtonsView
        okButton?.text =
            requireContext().getString(R.string.imoya_preference_time_period_edit_save)
    }

    /**
     * [TimePicker] を初期化しします。
     *
     * @param view この [androidx.fragment.app.Fragment] の [View]
     * @param time 初期表示する [Time]
     */
    open fun setupTimePicker(view: View, time: Time) {
        val picker: TimePicker = timePicker ?: throw IllegalStateException("TimePicker not found")
        picker.setIs24HourView(editorState.is24hourView)
        TimePickerHelper(picker).setHourAndMinute(time.hour, time.minute)
        picker.setOnTimeChangedListener { _, hourOfDay, minute ->
            when (fragmentState.step) {
                Step.START_TIME -> {
                    fragmentState.timePeriod.start.hour = hourOfDay
                    fragmentState.timePeriod.start.minute = minute
                }
                Step.END_TIME -> {
                    fragmentState.timePeriod.end.hour = hourOfDay
                    fragmentState.timePeriod.end.minute = minute
                }
            }
        }
    }

    override fun onClickOkButton() {
        when (fragmentState.step) {
            Step.START_TIME -> toEndTime()
            Step.END_TIME -> complete()
        }
    }

    override fun onClickBackButton() {
        when (fragmentState.step) {
            Step.START_TIME -> cancel()
            Step.END_TIME -> toEndTime()
        }
    }

    /**
     * 開始時刻入力画面へ遷移します。
     */
    open fun toStartTime() {
        val result = TimePeriodEditorFragmentResult(
            Activity.RESULT_CANCELED,
            fragmentState.timePeriod
        )
        returnToHost(result.toBundle())
    }

    /**
     * 終了時刻入力画面へ遷移します。
     */
    open fun toEndTime() {
        val fragment = endTimeFragment
        val nextFragmentState = TimePeriodEditorFragmentState(
            fragmentState.timePeriod,
            Step.END_TIME
        )
        ArgumentsUtil.setArgument(fragment) {
            it.putBundle(
                Constants.KEY_EDITOR_STATE,
                editorState.toBundle()
            )
            it.putBundle(KEY_FRAGMENT_STATE, nextFragmentState.toBundle())
        }
        roundTripManagerForEndTime.start(REQUEST_KEY_END_TIME, fragment, TAG_TO_END_TIME)
    }

    /**
     * 編集を完了し、呼び出し元画面へ遷移します。
     */
    open fun complete() {
        val result = TimePeriodEditorFragmentResult(
            Activity.RESULT_OK,
            fragmentState.timePeriod
        )
        returnToHost(result.toBundle())
    }

    /**
     * 編集をキャンセルし、呼び出し元画面へ遷移します。
     */
    open fun cancel() {
        val result = TimePeriodEditorFragmentResult(
            Activity.RESULT_CANCELED,
            fragmentState.timePeriod
        )
        returnToHost(result.toBundle())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveEditorStateToInstanceState(outState, fragmentState.toBundle(), KEY_FRAGMENT_STATE)
    }

    companion object {
        /**
         * Fragment tag: start
         */
        const val TAG_START_TIME = "TimePeriodPreferenceEditorFragment_start"

        /**
         * Fragment tag: start to end
         */
        const val TAG_TO_END_TIME = "TimePeriodPreferenceEditorFragment_toEnd"

        /**
         * Argument or instanceState key: TimePeriodPreferenceEditorFragmentState
         */
        const val KEY_FRAGMENT_STATE = "TimePeriodPreferenceEditorFragmentState"

        /**
         * Request key: end time
         */
        const val REQUEST_KEY_END_TIME = "TimePeriodPreferenceEditorFragmentEndTime"

        /**
         * Tag for log
         */
        private const val TAG = "PeriodPrefEditorFragment"
    }
}