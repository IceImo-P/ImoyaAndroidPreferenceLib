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

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.editor.SliderNumberDialogEditor
import net.imoya.android.preference.controller.PreferenceActivityController
import net.imoya.android.preference.controller.PreferenceScreenController
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePeriodPreferenceView
import net.imoya.android.preference.view.time.TimePreferenceView

/**
 * Sample implementation: preference views on plain [AppCompatActivity]
 *
 * @author IceImo-P
 */
class SamplePlainActivity : AppCompatActivity(), DialogListener {
    /** [PreferenceView] のコントローラー */
    private val pref = PreferenceActivityController()

    /** Custom preference change handler */
    private lateinit var myPreferenceChangeHandler: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        // Default process for PreferenceActivityController
        pref.onCreateActivity(this, this)

        // Set up preferences first
        pref.preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        // Registering default editors
        pref.setupDefaultEditors()

        // Registering non-default editors
        pref.registerEditor(KEY_SLIDER_NUMBER_EDITOR, SliderNumberDialogEditor())

        // Restore editors state (if savedInstanceState exists)
        pref.restoreEditorState(savedInstanceState)

        AppLog.v(TAG, "onCreate: inflating layout")

        setContentView(R.layout.sample)

        // Set the combination of view and editor
        pref.setupPreferenceView(findViewById(R.id.pref_string_1))
        pref.setupPreferenceView(findViewById(R.id.pref_string_2))
        pref.setupPreferenceView(findViewById(R.id.pref_number_1))
        pref.setupPreferenceView(findViewById(R.id.pref_number_2), KEY_SLIDER_NUMBER_EDITOR)
        pref.setupPreferenceView(findViewById(R.id.pref_string_list_single_dialog))
        pref.setupPreferenceView(
            findViewById(R.id.pref_string_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_string_list_single_fragment).visibility = View.GONE
        pref.setupPreferenceView(
            findViewById(R.id.pref_string_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_string_list_single_fragment_2).visibility = View.GONE
        pref.setupPreferenceView(findViewById(R.id.pref_string_list_multi_dialog))
        pref.setupPreferenceView(
            findViewById(R.id.pref_string_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_string_list_multi_fragment).visibility = View.GONE
        pref.setupPreferenceView(findViewById(R.id.pref_int_list_single_dialog))
        pref.setupPreferenceView(
            findViewById(R.id.pref_int_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_int_list_single_fragment).visibility = View.GONE
        pref.setupPreferenceView(
            findViewById(R.id.pref_int_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_int_list_single_fragment_2).visibility = View.GONE
        pref.setupPreferenceView(findViewById(R.id.pref_int_list_multi_dialog))
        pref.setupPreferenceView(
            findViewById(R.id.pref_int_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_ACTIVITY
        )
        findViewById<View>(R.id.pref_int_list_multi_fragment).visibility = View.GONE
        pref.setupPreferenceView(findViewById(R.id.pref_switch_1))
        pref.setupPreferenceView(findViewById(R.id.pref_time_1))
        findViewById<View>(R.id.pref_time_2).visibility = View.GONE
        pref.setupPreferenceView(
            findViewById(R.id.pref_time_3),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_ACTIVITY
        )
        pref.setupPreferenceView(findViewById(R.id.pref_time_period_1))
        findViewById<View>(R.id.pref_time_period_2).visibility = View.GONE

        // 「戻る」項目押下時、 Activity を終了し前の画面へ遷移
        findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { finish() }

        pref.commitSetupPreferenceViews()

        // Custom logic: Update views if SwitchPreference is changed
        val is24hourViewKey = getString(R.string.pref_switch_1_key)
        myPreferenceChangeHandler = SharedPreferences.OnSharedPreferenceChangeListener()
        { _, key ->
            if (key == is24hourViewKey) {
                update24hourView()
            }
        }
        pref.requirePreferences()
            .registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

        // Restore ScrollView position
        if (savedInstanceState != null) {
            findViewById<ScrollView>(R.id.scrollView).scrollY =
                savedInstanceState.getInt(KEY_SCROLL_POSITION)
        }

        AppLog.v(TAG, "onCreate: end")
    }

    override fun onResume() {
        super.onResume()

        pref.onResume()
    }

    override fun onPause() {
        super.onPause()

        pref.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        pref.onSaveInstanceState(outState)

        // Save ScrollView position
        outState.putInt(
            KEY_SCROLL_POSITION,
            findViewById<ScrollView>(R.id.scrollView)?.scrollY ?: 0
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        AppLog.v(TAG, "onDestroy: start")

        pref.requirePreferences()
            .unregisterOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        pref.onDestroyViews()
        pref.onDestroy()

        AppLog.v(TAG, "onDestroy: end")
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?) {
        pref.onDialogResult(requestCode, resultCode, data)
    }

    private fun update24hourView() {
        val is24hourView = pref.requirePreferences().getBoolean(
            getString(R.string.pref_switch_1_key), false
        )
        findViewById<TimePreferenceView>(R.id.pref_time_1)?.is24hourView = is24hourView
        findViewById<TimePreferenceView>(R.id.pref_time_3)?.is24hourView = is24hourView
        findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        pref.updatePreferenceViews()
    }

    companion object {
        /** InstanceState key and tag: SliderNumberEditor */
        private const val KEY_SLIDER_NUMBER_EDITOR = "sliderNumberEditor"

        /** InstanceState key: ScrollView position */
        private const val KEY_SCROLL_POSITION = "scrollPosition"

        /**
         * Tag for log
         */
        const val TAG = "SamplePlainActivity"
    }
}