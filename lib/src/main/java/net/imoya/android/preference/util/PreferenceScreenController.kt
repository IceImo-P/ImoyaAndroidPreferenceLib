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

package net.imoya.android.preference.util

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SwitchPreferenceView

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
    private val editors = HashMap<String, PreferenceEditor>()

    /** Views for register [PreferenceViewUpdater] */
    private val views = ArrayList<PreferenceView>()

    /** [PreferenceViewUpdater] */
    lateinit var updater: PreferenceViewUpdater

    /** [PreferenceScreenParent] */
    lateinit var parent: PreferenceScreenParent

    /** [SharedPreferences] for edit through [PreferenceEditor]s and [PreferenceView]s */
    var preferences: SharedPreferences? = null

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
        if (editor is DialogPreferenceEditor) {
            editor.parent = parent
            editor.requestCode = editors.size + 1
            PreferenceLog.d(TAG) {
                "registerEditor: tag = $tag, editor.requestCode = ${editor.requestCode}"
            }
        }
        editors[tag] = editor
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
                it.value.instanceState = savedInstanceState.getParcelable(it.key)
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
        if (view is SwitchPreferenceView) {
            PreferenceViewUtil.setupView(
                view, SwitchPreferenceViewController(requirePreferences()), views
            )
        } else {
            val editor = editors.values.find { it.isCompatibleView(view) }
                ?: throw IllegalArgumentException("Default editor not found")
            PreferenceViewUtil.setupView(view, editor, views)
        }
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
        PreferenceViewUtil.setupView(view, editor, views)
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
        registerEditor(DEFAULT_EDITOR_TAG_STRING, StringPreferenceEditor())
        registerEditor(DEFAULT_EDITOR_TAG_NUMBER, NumberAndUnitPreferenceEditor())
        registerEditor(DEFAULT_EDITOR_TAG_INT_LIST, IntListPreferenceEditor())
        registerEditor(DEFAULT_EDITOR_TAG_STRING_LIST, StringListPreferenceEditor())
        registerEditor(DEFAULT_EDITOR_TAG_TIME, TimePreferenceEditor())
        registerEditor(DEFAULT_EDITOR_TAG_TIME_PERIOD, TimePeriodPreferenceEditor(parent))
    }

    open fun onCreate() {
        views.clear()
        updater = PreferenceViewUpdater()
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
            outState.putParcelable(it.key, it.value.instanceState)
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (editor in editors.values) {
            if (editor is DialogPreferenceEditor &&
                editor.onDialogResult(requestCode, resultCode, data)
            ) {
                break
            }
        }
    }

    companion object {
        /** InstanceState key and tag: StringPreferenceEditor */
        private const val DEFAULT_EDITOR_TAG_STRING = "ImoyaDefaultEditorString"

        /** InstanceState key and tag: NumberAndUnitPreferenceEditor */
        private const val DEFAULT_EDITOR_TAG_NUMBER = "ImoyaDefaultEditorNumber"

        /** InstanceState key and tag: IntListPreferenceEditor */
        private const val DEFAULT_EDITOR_TAG_INT_LIST = "ImoyaDefaultEditorIntList"

        /** InstanceState key and tag: StringListPreferenceEditor */
        private const val DEFAULT_EDITOR_TAG_STRING_LIST = "ImoyaDefaultEditorStringList"

        /** InstanceState key and tag: TimePreferenceEditor */
        private const val DEFAULT_EDITOR_TAG_TIME = "ImoyaDefaultEditorTime"

        /** InstanceState key and tag: TimePeriodPreferenceEditor */
        private const val DEFAULT_EDITOR_TAG_TIME_PERIOD = "ImoyaDefaultEditorTimePeriod"

        /**
         * Tag for log
         */
        private const val TAG = "PrefScreenLogic"
    }
}