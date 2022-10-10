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
import android.view.View
import android.widget.ScrollView
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.activity.PreferenceActivity
import net.imoya.android.preference.controller.PreferenceScreenController
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePeriodPreferenceView
import net.imoya.android.preference.view.time.TimePreferenceView

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

        AppLog.v(TAG, "onCreate: inflating layout")

        setContentView(R.layout.sample)

        // Set the combination of view and editor
        setupPreferenceView(findViewById(R.id.pref_string_1))
        setupPreferenceView(findViewById(R.id.pref_string_2))
        setupPreferenceView(findViewById(R.id.pref_number_1))
        setupPreferenceView(
            findViewById(R.id.pref_number_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_NUMBER_SLIDER
        )
        setupPreferenceView(findViewById(R.id.pref_string_list_single_dialog))
        setupPreferenceView(
            findViewById(R.id.pref_string_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_string_list_single_fragment).visibility = View.GONE
        setupPreferenceView(
            findViewById(R.id.pref_string_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_string_list_single_fragment_2).visibility = View.GONE
        setupPreferenceView(findViewById(R.id.pref_string_list_multi_dialog))
        setupPreferenceView(
            findViewById(R.id.pref_string_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_string_list_multi_fragment).visibility = View.GONE
        setupPreferenceView(findViewById(R.id.pref_int_list_single_dialog))
        setupPreferenceView(
            findViewById(R.id.pref_int_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_int_list_single_fragment).visibility = View.GONE
        setupPreferenceView(
            findViewById(R.id.pref_int_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_int_list_single_fragment_2).visibility = View.GONE
        setupPreferenceView(findViewById(R.id.pref_int_list_multi_dialog))
        setupPreferenceView(
            findViewById(R.id.pref_int_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_int_list_multi_fragment).visibility = View.GONE
        setupPreferenceView(findViewById(R.id.pref_switch_1))
        setupPreferenceView(findViewById(R.id.pref_time_1))
        setupPreferenceView(
            findViewById(R.id.pref_time_3),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_ACTIVITY
        )
        findViewById<View>(R.id.pref_time_2).visibility = View.GONE
        setupPreferenceView(findViewById(R.id.pref_time_period_1))
        findViewById<View>(R.id.pref_time_period_2).visibility = View.GONE

        // 「戻る」項目押下時、 Activity を終了し前の画面へ遷移
        findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { finish() }

        commitSetupPreferenceViews()

        // Custom logic: Update views if SwitchPreference is changed
        val is24hourViewKey = getString(R.string.pref_switch_1_key)
        myPreferenceChangeHandler = SharedPreferences.OnSharedPreferenceChangeListener()
        { _, key ->
            if (key == is24hourViewKey) {
                update24hourView()
            }
        }
        requirePreferences().registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

        // Restore ScrollView position
        if (savedInstanceState != null) {
            findViewById<ScrollView>(R.id.scrollView).scrollY =
                savedInstanceState.getInt(KEY_SCROLL_POSITION)
        }

        AppLog.v(TAG, "onCreate: end")
    }

//    override fun onRegisterCustomEditors() {
//        super.onRegisterCustomEditors()
//
//        // Register non-default editors
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save ScrollView position
        outState.putInt(
            KEY_SCROLL_POSITION,
            findViewById<ScrollView>(R.id.scrollView)?.scrollY ?: 0
        )
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
        findViewById<TimePreferenceView>(R.id.pref_time_2)?.is24hourView = is24hourView
        findViewById<TimePreferenceView>(R.id.pref_time_3)?.is24hourView = is24hourView
        findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        updatePreferenceViews()
    }

    companion object {
        /** InstanceState key: ScrollView position */
        private const val KEY_SCROLL_POSITION = "scrollPosition"

        /**
         * Tag for log
         */
        const val TAG = "SampleActivity"
    }
}