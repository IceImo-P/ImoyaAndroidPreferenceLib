/*
 * Copyright (C) 2022-2023 IceImo-P
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

package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.DialogListener
import net.imoya.android.dialog.DialogUtil
import net.imoya.android.fragment.roundtrip.RoundTripManager
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.controller.editor.*
import net.imoya.android.preference.controller.editor.list.*
import net.imoya.android.preference.controller.editor.time.*
import net.imoya.android.preference.view.PreferenceView

/**
 * [AppCompatActivity] または [Fragment] へ
 * [PreferenceView] を配置するための追加機能を実装および提供します。
 *
 * * 1個の [PreferenceScreenController] インスタンスは、1個の [SharedPreferences] を編集します。
 *   (同時に複数の [SharedPreferences] を編集することはできません)
 *
 * @author IceImo-P
 */
abstract class PreferenceScreenController : DialogListener {
    /** [PreferenceEditor]s for [PreferenceView]s */
    private val editors = LinkedHashMap<String, PreferenceEditor>()

    /** Views for register [PreferenceViewUpdater] */
    private val views = ArrayList<PreferenceView>()

    /** [PreferenceViewUpdater] */
    lateinit var updater: PreferenceViewUpdater

    /** [PreferenceScreenParent] */
    lateinit var parent: PreferenceScreenParent

    /** [RoundTripManager] */
    lateinit var roundTripManager: RoundTripManager

    /** [SharedPreferences] for edit through [PreferenceEditor]s and [PreferenceView]s */
    var preferences: SharedPreferences? = null

    /**
     * Return new instance of [PreferenceViewUpdater]
     *
     * デフォルトの実装は、 [PreferenceViewUpdater] の新しいインスタンスを返します。
     * [PreferenceViewUpdater] を独自に拡張した場合、このメソッドを override
     * し、拡張したクラスの新しいインスタンスを返してください。
     *
     * @return New instance of [PreferenceViewUpdater]
     */
    open fun createPreferenceViewUpdater(): PreferenceViewUpdater {
        return PreferenceViewUpdater()
    }

    /**
     * Return new instance of [PreferenceScreenParent]
     *
     * @return New instance of [PreferenceScreenParent]
     */
    abstract fun createPreferenceScreenParent(): PreferenceScreenParent

    /**
     * Return new instance of [RoundTripManager]
     *
     * @return New instance of [RoundTripManager]
     */
    abstract fun createRoundTripManager(): RoundTripManager

    /**
     * Retrieve [SharedPreferences]
     *
     * @return [SharedPreferences]
     * @throws IllegalStateException [preferences] is not set
     */
    open fun requirePreferences(): SharedPreferences {
        return preferences ?: throw IllegalStateException("preferences is not set")
    }

    /**
     * Register [PreferenceEditor]
     *
     * * Call this method on create [AppCompatActivity] or [Fragment].
     *
     * @param tag key for [PreferenceEditor]. this key is used for instanceState.
     * @param editor [PreferenceEditor]
     */
    open fun registerEditor(tag: String, editor: PreferenceEditor) {
        val currentPreferences = requirePreferences()
        editor.preferences = currentPreferences
        if (editor is DialogEditor) {
            editor.parent = parent
            editor.requestCode = editors.size + 1
            PreferenceLog.d(TAG) {
                "registerEditor: tag = $tag, editor.requestCode = ${editor.requestCode}"
            }
        }
        if (editor is FragmentEditor) {
            editor.roundTripManager = roundTripManager
            editor.requestKey = DialogUtil.getRequestKey(editors.size + 1)
        }
        editors[tag] = editor
    }

    /**
     * Returns [PreferenceEditor] binding with specific key
     *
     * @param tag key for [PreferenceEditor]
     * @return The [PreferenceEditor]. If no editor binding with [tag], returns null.
     */
    @Suppress("unused")
    fun findEditor(tag: String): PreferenceEditor? {
        return editors[tag]
    }

    /**
     * Restoring state of registered [PreferenceEditor]s
     *
     * * Call this method on [AppCompatActivity.onCreate], [Fragment.onCreate]
     *   after registering all [PreferenceEditor].
     *
     * @param savedInstanceState Saved instanceState
     */
    open fun restoreEditorState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            editors.entries.forEach {
                val editor = it.value
                if (editor is ScreenEditor) {
                    editor.instanceState = BundleCompat.getParcelable(
                        savedInstanceState,
                        it.key,
                        editor.instanceStateClass
                    )
                }
            }
        }
    }

    /**
     * Combine [PreferenceView] and registered [PreferenceEditor]
     *
     * [PreferenceView] タップ時に起動する [PreferenceEditor] を設定します。
     * * Call this method on [AppCompatActivity.onCreate] after [AppCompatActivity.setContentView],
     *   or [Fragment.onViewCreated]
     *
     * @param view [PreferenceView]
     */
    open fun setupPreferenceView(view: PreferenceView) {
        val editor = editors.values.find { it.isCompatibleView(view) }
            ?: throw IllegalArgumentException("Default editor not found")
        setupPreferenceView(view, editor)
    }

    /**
     * Combine [PreferenceView] and registered [PreferenceEditor]
     *
     * [PreferenceView] タップ時に起動する [PreferenceEditor] を設定します。
     * * If you use custom editor, use this method.
     * * Call this method on [AppCompatActivity.onCreate] after [AppCompatActivity.setContentView],
     *   or [Fragment.onViewCreated]
     *
     * @param view [PreferenceView]
     * @param editorTag Tag for [PreferenceEditor] registered with [registerEditor] method
     * @throws IllegalArgumentException [editorTag] is not used at calling [registerEditor] method
     */
    open fun setupPreferenceView(view: PreferenceView, editorTag: String) {
        val editor = editors[editorTag] ?: throw IllegalArgumentException("Unexpected editorTag")
        setupPreferenceView(view, editor)
    }

    /**
     * 設定項目のビューと、ビューをタップした時の処理の連携を初期化します。
     *
     * @param view     設定項目のビュー([PreferenceView])
     * @param editor   ビューをタップした時の処理を実装する [PreferenceEditor]
     */
    protected open fun setupPreferenceView(view: PreferenceView, editor: PreferenceEditor) {
        if (editor is DialogEditor) {
            editor.registerDialogCallback()
        }
        if (editor is FragmentEditor) {
            editor.registerFragmentCallback()
        }
        editor.attach(view)
        views.add(view)
    }

    /**
     * [PreferenceView] タップ時に起動する [PreferenceEditor] の設定を完了します。
     *
     * 画面上の全ての [PreferenceView] に対し [setupPreferenceView] をコールした後で、このメソッドを
     * 1回コールしてください。
     */
    open fun commitSetupPreferenceViews() {
        val currentPreferences = requirePreferences()
        updater.views = views.toTypedArray()
        updater.update(currentPreferences)
        updater.start(currentPreferences)
    }

    /**
     * 画面上の全ての [PreferenceView] の表示を更新します。
     */
    open fun updatePreferenceViews() {
        updater.update(requirePreferences())
    }

    /**
     * デフォルトの [PreferenceEditor] を登録します。
     */
    open fun setupDefaultEditors() {
        registerEditor(DEFAULT_EDITOR_TAG_SWITCH, SwitchPreferenceViewController())
        registerEditor(DEFAULT_EDITOR_TAG_STRING, StringDialogEditor())
        registerEditor(DEFAULT_EDITOR_TAG_NUMBER, NumberAndUnitDialogEditor())
        registerEditor(DEFAULT_EDITOR_TAG_NUMBER_SLIDER, SliderNumberDialogEditor())
        registerEditor(
            DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST,
            SingleSelectionIntListDialogEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY,
            SingleSelectionIntListActivityEditor(parent)
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_FRAGMENT,
            SingleSelectionIntListFragmentEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST,
            SingleSelectionStringListDialogEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY,
            SingleSelectionStringListActivityEditor(parent)
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_FRAGMENT,
            SingleSelectionStringListFragmentEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST,
            MultiSelectionIntListDialogEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_ACTIVITY,
            MultiSelectionIntListActivityEditor(parent)
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_FRAGMENT,
            MultiSelectionIntListFragmentEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST,
            MultiSelectionStringListDialogEditor()
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_ACTIVITY,
            MultiSelectionStringListActivityEditor(parent)
        )
        registerEditor(
            DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_FRAGMENT,
            MultiSelectionStringListFragmentEditor()
        )
        registerEditor(DEFAULT_EDITOR_TAG_TIME, TimeDialogEditor())
        registerEditor(DEFAULT_EDITOR_TAG_TIME, TimeDialogEditor())
        registerEditor(DEFAULT_EDITOR_TAG_TIME_ACTIVITY, TimeActivityEditor(parent))
        registerEditor(DEFAULT_EDITOR_TAG_TIME_FRAGMENT, TimeFragmentEditor())
        registerEditor(DEFAULT_EDITOR_TAG_TIME_PERIOD, TimePeriodActivityEditor(parent))
        registerEditor(DEFAULT_EDITOR_TAG_TIME_PERIOD_FRAGMENT, TimePeriodFragmentEditor())
    }

    open fun onCreate() {
        views.clear()
        updater = createPreferenceViewUpdater()
        parent = createPreferenceScreenParent()
        roundTripManager = createRoundTripManager()
    }

    open fun onResume() {
        val currentPreferences = requirePreferences()
        updater.update(currentPreferences)
        updater.start(currentPreferences)
    }

    open fun onPause() {
        updater.stop(requirePreferences())
    }

    open fun onDestroy() {
        editors.clear()
    }

    open fun onDestroyViews() {
        views.clear()
    }

    open fun onSaveInstanceState(outState: Bundle) {
        editors.entries.forEach {
            val (key, value) = it
            if (value is ScreenEditor) {
                outState.putParcelable(key, value.instanceState)
            }
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (editor in editors.values) {
            if (editor is DialogEditor &&
                editor.onDialogResult(requestCode, resultCode, data)
            ) {
                break
            }
        }
    }

    companion object {
        /** InstanceState key and tag: [StringDialogEditor] */
        const val DEFAULT_EDITOR_TAG_STRING = "ImoyaDefaultEditorString"

        /** InstanceState key and tag: [NumberAndUnitDialogEditor] */
        const val DEFAULT_EDITOR_TAG_NUMBER = "ImoyaDefaultEditorNumber"

        /** InstanceState key and tag: [NumberAndUnitDialogEditor] */
        const val DEFAULT_EDITOR_TAG_NUMBER_SLIDER = "ImoyaDefaultEditorNumberSlider"

        /** InstanceState key and tag: [SingleSelectionIntListDialogEditor] */
        const val DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST =
            "ImoyaDefaultEditorSingleSelectionIntList"

        /** InstanceState key and tag: [SingleSelectionIntListActivityEditor] */
        const val DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_ACTIVITY =
            "ImoyaDefaultEditorSingleSelectionIntListActivity"

        /** InstanceState key and tag: [SingleSelectionIntListFragmentEditor] */
        const val DEFAULT_EDITOR_TAG_SINGLE_SELECTION_INT_LIST_FRAGMENT =
            "ImoyaDefaultEditorSingleSelectionIntListFragment"

        /** InstanceState key and tag: [SingleSelectionStringListDialogEditor] */
        const val DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST =
            "ImoyaDefaultEditorSingleSelectionStringList"

        /** InstanceState key and tag: [SingleSelectionStringListActivityEditor] */
        const val DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_ACTIVITY =
            "ImoyaDefaultEditorSingleSelectionStringListActivity"

        /** InstanceState key and tag: [SingleSelectionStringListFragmentEditor] */
        const val DEFAULT_EDITOR_TAG_SINGLE_SELECTION_STRING_LIST_FRAGMENT =
            "ImoyaDefaultEditorSingleSelectionStringListFragment"

        /** InstanceState key and tag: [MultiSelectionIntListDialogEditor] */
        const val DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST =
            "ImoyaDefaultEditorMultiSelectionIntList"

        /** InstanceState key and tag: [MultiSelectionIntListActivityEditor] */
        const val DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_ACTIVITY =
            "ImoyaDefaultEditorMultiSelectionIntListActivity"

        /** InstanceState key and tag: [MultiSelectionIntListFragmentEditor] */
        const val DEFAULT_EDITOR_TAG_MULTI_SELECTION_INT_LIST_FRAGMENT =
            "ImoyaDefaultEditorMultiSelectionIntListFragment"

        /** InstanceState key and tag: [MultiSelectionStringListDialogEditor] */
        const val DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST =
            "ImoyaDefaultEditorMultiSelectionStringList"

        /** InstanceState key and tag: [MultiSelectionStringListActivityEditor] */
        const val DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_ACTIVITY =
            "ImoyaDefaultEditorMultiSelectionStringListActivity"

        /** InstanceState key and tag: [MultiSelectionStringListFragmentEditor] */
        const val DEFAULT_EDITOR_TAG_MULTI_SELECTION_STRING_LIST_FRAGMENT =
            "ImoyaDefaultEditorMultiSelectionStringListFragment"

        /** InstanceState key and tag: [TimeDialogEditor] */
        const val DEFAULT_EDITOR_TAG_TIME = "ImoyaDefaultEditorTime"

        /** InstanceState key and tag: [TimeActivityEditor] */
        const val DEFAULT_EDITOR_TAG_TIME_ACTIVITY = "ImoyaDefaultEditorTimeActivity"

        /** InstanceState key and tag: [TimeFragmentEditor] */
        const val DEFAULT_EDITOR_TAG_TIME_FRAGMENT = "ImoyaDefaultEditorTimeFragment"

        /** InstanceState key and tag: [TimePeriodActivityEditor] */
        const val DEFAULT_EDITOR_TAG_TIME_PERIOD = "ImoyaDefaultEditorTimePeriod"

        /** InstanceState key and tag: [TimePeriodFragmentEditor] */
        const val DEFAULT_EDITOR_TAG_TIME_PERIOD_FRAGMENT = "ImoyaDefaultEditorTimePeriodFragment"

        /** InstanceState key and tag: [SwitchPreferenceViewController] */
        const val DEFAULT_EDITOR_TAG_SWITCH = "ImoyaDefaultEditorSwitch"

        /**
         * Tag for log
         */
        private const val TAG = "PrefScreenLogic"
    }
}