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

package net.imoya.android.preference.app

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.fragment.PreferenceFragment
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView
import net.imoya.android.preference.view.TimePreferenceView

/**
 * [PreferenceFragment] implementation sample
 *
 * @author IceImo-P
 */
class SampleFragment : PreferenceFragment() {
    /** Custom preference change handler */
    private lateinit var myPreferenceChangeHandler: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        // Custom logic: Update views if SwitchPreference is changed
        val is24hourViewKey = getString(R.string.pref_switch_1_key)
        myPreferenceChangeHandler = SharedPreferences.OnSharedPreferenceChangeListener()
        { _, key ->
            if (key == is24hourViewKey) {
                update24hourView()
            }
        }

        AppLog.v(TAG, "onCreate: end")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppLog.v(TAG, "onViewCreated: start")

        // Set the combination of view and editor
        setupPreferenceView(view.findViewById(R.id.pref_string_1))
        setupPreferenceView(view.findViewById(R.id.pref_string_2))
        setupPreferenceView(view.findViewById(R.id.pref_number_1))
        setupPreferenceView(view.findViewById(R.id.pref_number_2), KEY_SLIDER_NUMBER_EDITOR)
        setupPreferenceView(view.findViewById(R.id.pref_string_list_1))
        setupPreferenceView(view.findViewById(R.id.pref_int_list_1))
        setupPreferenceView(view.findViewById(R.id.pref_switch_1))
        setupPreferenceView(view.findViewById(R.id.pref_time_1))
        setupPreferenceView(view.findViewById(R.id.pref_time_period_1))

        // ???????????????????????????????????? Fragment ?????????
        view.findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { parentFragmentManager.popBackStack() }

        commitSetupPreferenceViews()

        requirePreferences().registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

        activity?.title = getString(R.string.start_fragment_sample)

        AppLog.v(TAG, "onViewCreated: end")
    }

    override fun onRegisterCustomEditors() {
        super.onRegisterCustomEditors()

        // Register non-default editors
        registerEditor(KEY_SLIDER_NUMBER_EDITOR, SliderNumberEditor())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        AppLog.v(TAG, "onDestroyView: start")

        requirePreferences().unregisterOnSharedPreferenceChangeListener(myPreferenceChangeHandler)

        AppLog.v(TAG, "onDestroyView: end")
    }

    private fun update24hourView() {
        val is24hourView = requirePreferences().getBoolean(
            getString(R.string.pref_switch_1_key), false
        )
        view?.findViewById<TimePreferenceView>(R.id.pref_time_1)?.is24hourView = is24hourView
        view?.findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        updatePreferenceViews()
    }

    companion object {
        /** InstanceState key and tag: SliderNumberEditor */
        private const val KEY_SLIDER_NUMBER_EDITOR = "sliderNumberEditor"

        /**
         * Tag for log
         */
        const val TAG = "SampleFragment"
    }
}