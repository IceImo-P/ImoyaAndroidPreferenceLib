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

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.util.PreferenceViewUtil
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView
import net.imoya.android.preference.view.TimePreferenceView

class SampleFragment : Fragment(), DialogListener {
    private lateinit var preferences: SharedPreferences
    private lateinit var updater: PreferenceViewUpdater
    private lateinit var string1Editor: StringPreferenceEditor
    private lateinit var number1Editor: NumberAndUnitPreferenceEditor
    private lateinit var number2Editor: SliderNumberEditor
    private lateinit var intList1Editor: IntListPreferenceEditor
    private lateinit var stringList1Editor: StringListPreferenceEditor
    private lateinit var time1Editor: TimePreferenceEditor
    private lateinit var timePeriod1Editor: TimePeriodPreferenceEditor

    private lateinit var is24hourViewHandler: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        updater = PreferenceViewUpdater()
        preferences = PreferenceManager.getDefaultSharedPreferences(context)

        val parent: PreferenceScreenParent = PreferenceScreenParentFragment(this)
        string1Editor = StringPreferenceEditor(parent, preferences, REQUEST_STRING_1)
        number1Editor = NumberAndUnitPreferenceEditor(
            parent, preferences,
            REQUEST_NUMBER_1
        )
        number2Editor = SliderNumberEditor(parent, preferences, REQUEST_NUMBER_2)
        intList1Editor = IntListPreferenceEditor(
            parent, preferences,
            REQUEST_INT_LIST_1
        )
        stringList1Editor = StringListPreferenceEditor(
            parent, preferences,
            REQUEST_STRING_LIST_1
        )
        time1Editor = TimePreferenceEditor(parent, preferences, REQUEST_TIME_1)
        timePeriod1Editor = TimePeriodPreferenceEditor(
            parent, preferences,
            REQUEST_TIME_PERIOD_1
        )

        val is24hourViewKey = getString(R.string.pref_switch_1_key)
        is24hourViewHandler = SharedPreferences.OnSharedPreferenceChangeListener()
        { _, key ->
            if (key == is24hourViewKey) {
                update24hourView()
                updater.update(preferences)
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(is24hourViewHandler)

        if (savedInstanceState != null) {
            string1Editor.instanceState = savedInstanceState.getParcelable(STATE_STRING_1_EDITOR)
            number1Editor.instanceState = savedInstanceState.getParcelable(STATE_NUMBER_1_EDITOR)
            number2Editor.instanceState = savedInstanceState.getParcelable(STATE_NUMBER_2_EDITOR)
            intList1Editor.instanceState = savedInstanceState.getParcelable(STATE_INT_LIST_1_EDITOR)
            stringList1Editor.instanceState =
                savedInstanceState.getParcelable(STATE_STRING_LIST_1_EDITOR)
            time1Editor.instanceState = savedInstanceState.getParcelable(STATE_TIME_1_EDITOR)
            timePeriod1Editor.instanceState =
                savedInstanceState.getParcelable(STATE_TIME_PERIOD_1_EDITOR)
        }

        AppLog.v(TAG, "onCreate: end")
    }

    override fun onDestroy() {
        super.onDestroy()

        AppLog.v(TAG, "onDestroy: start")

        preferences.unregisterOnSharedPreferenceChangeListener(is24hourViewHandler)

        AppLog.v(TAG, "onDestroy: end")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        AppLog.v(TAG, "onSaveInstanceState: start")

        outState.putParcelable(
            STATE_STRING_1_EDITOR, string1Editor.instanceState
        )
        outState.putParcelable(
            STATE_NUMBER_1_EDITOR, number1Editor.instanceState
        )
        outState.putParcelable(
            STATE_NUMBER_2_EDITOR, number2Editor.instanceState
        )
        outState.putParcelable(
            STATE_INT_LIST_1_EDITOR, intList1Editor.instanceState
        )
        outState.putParcelable(
            STATE_STRING_LIST_1_EDITOR, stringList1Editor.instanceState
        )
        outState.putParcelable(
            STATE_TIME_1_EDITOR, time1Editor.instanceState
        )
        outState.putParcelable(
            STATE_TIME_PERIOD_1_EDITOR, timePeriod1Editor.instanceState
        )

        AppLog.v(TAG, "onSaveInstanceState: end")
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

        val switchController = SwitchPreferenceViewController(preferences)

        val views = ArrayList<PreferenceView>()

        PreferenceViewUtil.setupView(view.findViewById(R.id.pref_string_1), string1Editor, views)
        PreferenceViewUtil.setupView(view.findViewById(R.id.pref_number_1), number1Editor, views)
        PreferenceViewUtil.setupView(view.findViewById(R.id.pref_number_2), number2Editor, views)
        PreferenceViewUtil.setupView(view.findViewById(R.id.pref_switch_1), switchController, views)
        PreferenceViewUtil.setupView(
            view.findViewById(R.id.pref_string_list_1),
            stringList1Editor,
            views
        )
        PreferenceViewUtil.setupView(view.findViewById(R.id.pref_int_list_1), intList1Editor, views)
        PreferenceViewUtil.setupView(view.findViewById(R.id.pref_time_1), time1Editor, views)
        PreferenceViewUtil.setupView(
            view.findViewById(R.id.pref_time_period_1),
            timePeriod1Editor,
            views
        )

        // 「戻る」項目押下時、前の Fragment へ遷移
        view.findViewById<PreferenceView>(R.id.back).onPreferenceViewClickListener =
            OnPreferenceViewClickListener { parentFragmentManager.popBackStack() }

        updater.views = views.toTypedArray()
        updater.update(preferences)
        updater.start(preferences)

        AppLog.v(TAG, "onViewCreated: end")
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?) {
        string1Editor.onDialogResult(requestCode, resultCode, data)
        number1Editor.onDialogResult(requestCode, resultCode, data)
        number2Editor.onDialogResult(requestCode, resultCode, data)
        stringList1Editor.onDialogResult(requestCode, resultCode, data)
        intList1Editor.onDialogResult(requestCode, resultCode, data)
        time1Editor.onDialogResult(requestCode, resultCode, data)
    }

    override fun onAttach(context: Context) {
        AppLog.v(TAG, "onAttach(Context): start")

        super.onAttach(context)

        AppLog.v(TAG, "onAttach(Context): end")
    }

    private fun update24hourView() {
        val is24hourView = preferences.getBoolean(
            getString(R.string.pref_switch_1_key), false
        )
        view?.findViewById<TimePreferenceView>(R.id.pref_time_1)?.is24hourView = is24hourView
        view?.findViewById<TimePeriodPreferenceView>(R.id.pref_time_period_1)?.is24hourView =
            is24hourView
        updater.update(preferences)
    }

    companion object {
        private const val STATE_STRING_1_EDITOR = "string1Editor"
        private const val STATE_NUMBER_1_EDITOR = "number1Editor"
        private const val STATE_NUMBER_2_EDITOR = "number2Editor"
        private const val STATE_STRING_LIST_1_EDITOR = "stringList1Editor"
        private const val STATE_INT_LIST_1_EDITOR = "intList1Editor"
        private const val STATE_TIME_1_EDITOR = "time1Editor"
        private const val STATE_TIME_PERIOD_1_EDITOR = "timePeriod1Editor"

        private const val REQUEST_STRING_1 = 10
        private const val REQUEST_NUMBER_1 = 20
        private const val REQUEST_NUMBER_2 = 30
        private const val REQUEST_INT_LIST_1 = 40
        private const val REQUEST_STRING_LIST_1 = 50
        private const val REQUEST_TIME_1 = 60
        private const val REQUEST_TIME_PERIOD_1 = 70

        const val TAG = "SampleFragment"
    }
}