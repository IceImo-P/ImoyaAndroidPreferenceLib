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
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.activity.PreferenceActivity
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView
import net.imoya.android.preference.view.TimePreferenceView

/**
 * [PreferenceActivity] implementation sample
 *
 * @author IceImo-P
 */
class SampleActivity : PreferenceActivity(), DialogListener {
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

        AppLog.v(TAG, "onCreate: inflating layout")

        setContentView(R.layout.sample)

        // Set the combination of view and editor
        setupPreferenceView(findViewById(R.id.pref_string_1))
        setupPreferenceView(findViewById(R.id.pref_string_2))
        setupPreferenceView(findViewById(R.id.pref_number_1))
        setupPreferenceView(findViewById(R.id.pref_number_2), KEY_SLIDER_NUMBER_EDITOR)
        setupPreferenceView(findViewById(R.id.pref_string_list_1))
        setupPreferenceView(findViewById(R.id.pref_int_list_1))
        setupPreferenceView(findViewById(R.id.pref_switch_1))
        setupPreferenceView(findViewById(R.id.pref_time_1))
        setupPreferenceView(findViewById(R.id.pref_time_period_1))

        // ?????????????????????????????? Activity ?????????????????????????????????
        findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { finish() }

        commitSetupPreferenceViews()

        requirePreferences().registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

        AppLog.v(TAG, "onCreate: end")
    }

    override fun onRegisterCustomEditors() {
        super.onRegisterCustomEditors()

        // Register non-default editors
        registerEditor(KEY_SLIDER_NUMBER_EDITOR, SliderNumberEditor())
    }

    override fun onDestroy() {
        super.onDestroy()

        AppLog.v(TAG, "onDestroy: start")

        requirePreferences().unregisterOnSharedPreferenceChangeListener(myPreferenceChangeHandler)

        AppLog.v(TAG, "onDestroy: end")
    }

    private fun update24hourView() {
        val is24hourView = requirePreferences().getBoolean(
            getString(R.string.pref_switch_1_key), false
        )
        findViewById<TimePreferenceView>(R.id.pref_time_1)?.is24hourView = is24hourView
        findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        updatePreferenceViews()
    }

    companion object {
        /** InstanceState key and tag: SliderNumberEditor */
        private const val KEY_SLIDER_NUMBER_EDITOR = "sliderNumberEditor"

        /**
         * Tag for log
         */
        const val TAG = "SampleActivity"
    }
}