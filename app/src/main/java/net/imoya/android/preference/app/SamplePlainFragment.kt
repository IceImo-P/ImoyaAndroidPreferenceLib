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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.PreferenceFragmentController
import net.imoya.android.preference.controller.PreferenceScreenController
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePeriodPreferenceView
import net.imoya.android.preference.view.time.TimePreferenceView

/**
 * [PreferenceFragmentController] implementation sample on [Fragment]
 *
 * @author IceImo-P
 */
open class SamplePlainFragment : Fragment(), DialogListener {
    /** [PreferenceView] のコントローラー */
    private val pref = PreferenceFragmentController()

    /** Custom preference change handler */
    private lateinit var myPreferenceChangeHandler: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        // Default process for PreferenceFragmentController
        pref.onCreateFragment(this, this)

        // Set up preferences first
        pref.preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // Registering default editors
        pref.setupDefaultEditors()

        // Registering non-default editors
        // - (none)

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

        // Set ID of root view
//        pref.roundTripManager.containerId = R.id.root
        pref.roundTripManager.containerId = R.id.content_frame

        // Set the combination of view and editor
        pref.setupPreferenceView(view.findViewById(R.id.pref_string_1))
        pref.setupPreferenceView(view.findViewById(R.id.pref_string_2))
        pref.setupPreferenceView(view.findViewById(R.id.pref_number_1))
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_number_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_NUMBER_SLIDER
        )
        pref.setupPreferenceView(view.findViewById(R.id.pref_string_list_single_dialog))
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_FRAGMENT
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_fragment_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_FRAGMENT
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_multi_dialog),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_ACTIVITY
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_string_list_multi_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_FRAGMENT
        )
        pref.setupPreferenceView(view.findViewById(R.id.pref_int_list_single_dialog))
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_FRAGMENT
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_fragment_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_FRAGMENT
        )
        pref.setupPreferenceView(view.findViewById(R.id.pref_int_list_multi_dialog))
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_int_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_ACTIVITY
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_int_list_multi_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_FRAGMENT
        )
        pref.setupPreferenceView(view.findViewById(R.id.pref_switch_1))
        pref.setupPreferenceView(view.findViewById(R.id.pref_time_1))
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_time_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_FRAGMENT
        )
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_time_3),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_ACTIVITY
        )
        pref.setupPreferenceView(view.findViewById(R.id.pref_time_period_1))
        pref.setupPreferenceView(
            view.findViewById(R.id.pref_time_period_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_PERIOD_FRAGMENT
        )

        // 「戻る」項目押下時、前の画面へ遷移
        view.findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { onClickBackButton() }

        pref.commitSetupPreferenceViews()

        // Custom logic: Update views if SwitchPreference is changed
        pref.requirePreferences()
            .registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

        // Restore ScrollView position
        if (savedInstanceState != null) {
            view.findViewById<ScrollView>(R.id.scrollView).scrollY =
                savedInstanceState.getInt(KEY_SCROLL_POSITION)
        }

        requireActivity().title = getString(R.string.start_plain_fragment_sample)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(null)

        AppLog.v(TAG, "onViewCreated: end")
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
            view?.findViewById<ScrollView>(R.id.scrollView)?.scrollY ?: 0
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        AppLog.v(TAG, "onDestroyView: start")

        pref.onDestroyViews()
        pref.requirePreferences()
            .unregisterOnSharedPreferenceChangeListener(myPreferenceChangeHandler)

        AppLog.v(TAG, "onDestroyView: end")
    }

    override fun onDestroy() {
        super.onDestroy()

        pref.onDestroy()
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?) {
        pref.onDialogResult(requestCode, resultCode, data)
    }

    /**
     * 前の画面へ遷移します。
     */
    protected open fun onClickBackButton() {
        parentFragmentManager.popBackStack()
    }

    private fun update24hourView() {
        val is24hourView = pref.requirePreferences().getBoolean(
            getString(R.string.pref_switch_1_key), false
        )
        view?.findViewById<TimePreferenceView>(R.id.pref_time_1)?.is24hourView = is24hourView
        view?.findViewById<TimePreferenceView>(R.id.pref_time_2)?.is24hourView = is24hourView
        view?.findViewById<TimePreferenceView>(R.id.pref_time_3)?.is24hourView = is24hourView
        view?.findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        view?.findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_2)?.is24hourView =
            is24hourView
        pref.updatePreferenceViews()
    }

    companion object {
        /** InstanceState key: ScrollView position */
        private const val KEY_SCROLL_POSITION = "scrollPosition"

        /**
         * Tag for log
         */
        const val TAG = "SamplePlainFragment"
    }
}