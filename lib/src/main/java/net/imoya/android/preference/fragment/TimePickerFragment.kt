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

package net.imoya.android.preference.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import net.imoya.android.fragment.BaseFragment
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.model.Time
import net.imoya.android.util.TimePickerHelper

/**
 * [TimePicker] を表示する [Fragment]
 */
class TimePickerFragment : BaseFragment() {
    /**
     * 時刻
     */
    private val time = Time()

    /**
     * [TimePicker]
     */
    private var picker: TimePicker? = null

    /**
     * 24時間表示フラグを設定します。
     *
     * @param is24HourView 24時間表示フラグ
     */
    fun setIs24HourView(is24HourView: Boolean) {
        val original = this.arguments
        val arguments = original ?: Bundle()
        arguments.putBoolean(ARGUMENT_IS_24_HOUR_VIEW, is24HourView)
        if (original == null) {
            this.arguments = arguments
        }
    }

    /**
     * [TimePicker] の時刻を取得します。
     *
     * @return 時刻
     */
    fun getTime(): Time {
        loadTime()
        return Time(time.hour, time.minute, time.second)
    }

    /**
     * [TimePicker] の時刻を設定します。
     *
     * @param time 時刻
     */
    fun setTime(time: Time) {
        val original = this.arguments
        val arguments = original ?: Bundle()
        arguments.putParcelable(ARGUMENT_TIME, time)
        if (original == null) {
            this.arguments = arguments
        }
    }

    /**
     * 画面のタイトル文言を設定します。
     *
     * @param title 画面のタイトル文言
     */
    fun setScreenTitle(title: String) {
        val original = this.arguments
        val arguments = original ?: Bundle()
        arguments.putString(ARGUMENT_TITLE, title)
        if (original == null) {
            this.arguments = arguments
        }
    }

    private fun loadTime() {
        val timePicker = picker
        if (timePicker != null) {
            val pickerHelper = TimePickerHelper(timePicker)
            time.hour = pickerHelper.getHour()
            time.minute = pickerHelper.getMinute()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        loadTime()
        outState.putParcelable(ARGUMENT_TIME, time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val time: Time = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(ARGUMENT_TIME)
                ?: throw RuntimeException("savedInstanceState[$ARGUMENT_TIME] is not set")
        } else {
            requireArguments().getParcelable(ARGUMENT_TIME)
                ?: throw RuntimeException("arguments[$ARGUMENT_TIME] is not set")
        }
        PreferenceLog.v(TAG) { "onCreate: time = $time" }
        this.time.hour = time.hour
        this.time.minute = time.minute
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.imoya_preference_time_input_fragment, container, false
        )
        picker = view.findViewById(R.id.time)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timePicker = picker!!
        TimePickerHelper(timePicker).setHourAndMinute(time.hour, time.minute)
        timePicker.setIs24HourView(requireArguments().getBoolean(ARGUMENT_IS_24_HOUR_VIEW))
        val title = requireArguments().getString(ARGUMENT_TITLE)
        PreferenceLog.v(TAG) { "onViewCreated: title = $title" }
        this.setTitle(title)
    }

    companion object {
        /**
         * 再生成時保存データキー:時刻
         */
        private val ARGUMENT_TIME = TimePickerFragment::class.java.name + ".CurrentTime"

        /**
         * 再生成時保存データキー:24時間表示フラグ
         */
        private val ARGUMENT_IS_24_HOUR_VIEW = TimePickerFragment::class.java.name + ".Is24HourView"

        /**
         * 再生成時保存データキー:タイトル文言
         */
        private val ARGUMENT_TITLE = TimePickerFragment::class.java.name + ".Title"

        /**
         * Tag for log
         */
        private const val TAG = "TimePickerFragment"
    }
}