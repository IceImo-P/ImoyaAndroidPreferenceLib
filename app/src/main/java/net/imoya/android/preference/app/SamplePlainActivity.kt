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
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.util.PreferenceActivityController
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView
import net.imoya.android.preference.view.TimePreferenceView

/**
 * Sample implementation: preference views on plain [AppCompatActivity]
 *
 * @author IceImo-P
 */
class SamplePlainActivity : AppCompatActivity(), DialogListener {
    /** [PreferenceView] を配置する機能の実装 */
    private val pref = PreferenceActivityController<SamplePlainActivity>()

    /** Custom preference change handler */
    private lateinit var myPreferenceChangeHandler: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        // Default process for PreferenceActivityLogic
        pref.onCreateActivity(this)

        // Set up preferences first
        pref.preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        // Registering default editors
        pref.setupDefaultEditors()

        // Registering non-default editors
        pref.registerEditor(KEY_SLIDER_NUMBER_EDITOR, SliderNumberEditor())

        // Restore editors state (if savedInstanceState exists)
        pref.restoreEditorState(savedInstanceState)

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
        pref.setupPreferenceView(findViewById(R.id.pref_string_1))
        pref.setupPreferenceView(findViewById(R.id.pref_string_2))
        pref.setupPreferenceView(findViewById(R.id.pref_number_1))
        pref.setupPreferenceView(findViewById(R.id.pref_number_2), KEY_SLIDER_NUMBER_EDITOR)
        pref.setupPreferenceView(findViewById(R.id.pref_string_list_1))
        pref.setupPreferenceView(findViewById(R.id.pref_int_list_1))
        pref.setupPreferenceView(findViewById(R.id.pref_switch_1))
        pref.setupPreferenceView(findViewById(R.id.pref_time_1))
        pref.setupPreferenceView(findViewById(R.id.pref_time_period_1))

        // 「戻る」項目押下時、 Activity を終了し前の画面へ遷移
        findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { finish() }

        pref.commitSetupPreferenceViews()

        pref.requirePreferences()
            .registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

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
        findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        pref.updatePreferenceViews()
    }

    companion object {
        /** InstanceState key and tag: SliderNumberEditor */
        private const val KEY_SLIDER_NUMBER_EDITOR = "sliderNumberEditor"

        /**
         * Tag for log
         */
        const val TAG = "SamplePlainActivity"
    }
}