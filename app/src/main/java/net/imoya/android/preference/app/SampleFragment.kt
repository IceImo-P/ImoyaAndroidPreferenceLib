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
import android.view.*
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import net.imoya.android.preference.fragment.PreferenceFragment
import net.imoya.android.preference.controller.PreferenceScreenController
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePeriodPreferenceView
import net.imoya.android.preference.view.time.TimePreferenceView

/**
 * [PreferenceFragment] implementation sample
 *
 * @author IceImo-P
 */
class SampleFragment : PreferenceFragment() {
    /** Custom preference change handler */
    private lateinit var myPreferenceChangeHandler: SharedPreferences.OnSharedPreferenceChangeListener

    override val rootView: ViewGroup
        get() = (requireView().parent as ViewGroup).findViewById(R.id.content_frame)

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
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppLog.v(TAG, "onViewCreated: start")

        // Setup menu
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        // Home ボタン押下時は前の画面へ戻る
                        parentFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        })

        // Set the combination of view and editor
        setupPreferenceView(view.findViewById(R.id.pref_string_1))
        setupPreferenceView(view.findViewById(R.id.pref_string_2))
        setupPreferenceView(view.findViewById(R.id.pref_number_1))
        setupPreferenceView(
            view.findViewById(R.id.pref_number_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_NUMBER_SLIDER
        )
        setupPreferenceView(view.findViewById(R.id.pref_string_list_single_dialog))
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_FRAGMENT
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_single_fragment_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_FRAGMENT
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_multi_dialog),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_ACTIVITY
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_string_list_multi_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_FRAGMENT
        )
        setupPreferenceView(view.findViewById(R.id.pref_int_list_single_dialog))
        setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_FRAGMENT
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_activity_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_int_list_single_fragment_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_FRAGMENT
        )
        setupPreferenceView(view.findViewById(R.id.pref_int_list_multi_dialog))
        setupPreferenceView(
            view.findViewById(R.id.pref_int_list_multi_activity),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_ACTIVITY
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_int_list_multi_fragment),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_FRAGMENT
        )
        setupPreferenceView(view.findViewById(R.id.pref_switch_1))
        setupPreferenceView(view.findViewById(R.id.pref_time_1))
        setupPreferenceView(
            view.findViewById(R.id.pref_time_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_FRAGMENT
        )
        setupPreferenceView(
            view.findViewById(R.id.pref_time_3),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_ACTIVITY
        )
        setupPreferenceView(view.findViewById(R.id.pref_time_period_1))
        setupPreferenceView(
            view.findViewById(R.id.pref_time_period_2),
            PreferenceScreenController.DEFAULT_EDITOR_TAG_TIME_PERIOD_FRAGMENT
        )

        // 「戻る」項目押下時、前の Fragment へ遷移
        view.findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { parentFragmentManager.popBackStack() }

        commitSetupPreferenceViews()

        requirePreferences().registerOnSharedPreferenceChangeListener(myPreferenceChangeHandler)
        update24hourView()

        // Restore ScrollView position
        if (savedInstanceState != null) {
            view.findViewById<ScrollView>(R.id.scrollView).scrollY =
                savedInstanceState.getInt(KEY_SCROLL_POSITION)
        }

        requireActivity().title = getString(R.string.start_fragment_sample)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(null)

        AppLog.v(TAG, "onViewCreated: end")
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            parentFragmentManager.popBackStack()
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

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
            view?.findViewById<ScrollView>(R.id.scrollView)?.scrollY ?: 0
        )
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
        view?.findViewById<TimePreferenceView>(R.id.pref_time_2)?.is24hourView = is24hourView
        view?.findViewById<TimePreferenceView>(R.id.pref_time_3)?.is24hourView = is24hourView
        view?.findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        view?.findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_2)?.is24hourView =
            is24hourView
        updatePreferenceViews()
    }

    companion object {
        /** InstanceState key: ScrollView position */
        private const val KEY_SCROLL_POSITION = "scrollPosition"

        /**
         * Tag for log
         */
        const val TAG = "SampleFragment"
    }
}