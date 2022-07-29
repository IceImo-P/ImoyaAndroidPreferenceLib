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

package net.imoya.android.preference.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import net.imoya.android.dialog.DialogListener
import net.imoya.android.fragment.BaseFragment
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.controller.editor.*
import net.imoya.android.preference.controller.editor.list.*
import net.imoya.android.preference.controller.editor.time.*
import net.imoya.android.preference.controller.PreferenceFragmentController
import net.imoya.android.preference.view.*
import net.imoya.android.preference.view.list.*
import net.imoya.android.preference.view.time.*

/**
 * [PreferenceView] を配置する機能を追加した [BaseFragment]
 *
 * @author IceImo-P
 */
abstract class PreferenceBaseFragment : BaseFragment(), DialogListener {
    /** [PreferenceView] を配置する機能の実装 */
    private lateinit var pref: PreferenceFragmentController

    /** [PreferenceView] 自動更新コントローラ */
    @Suppress("unused")
    protected val prefUpdater: PreferenceViewUpdater
        get() = pref.updater

    /** ダイアログへ設定する親画面オブジェクト */
    @Suppress("unused")
    protected val prefParent: PreferenceScreenParent
        get() = pref.parent

    /**
     * この [Fragment] の最も外側の [ViewGroup] を返します。
     */
    protected abstract val rootView: ViewGroup

    /**
     * [PreferenceView] を配置する機能の実装を生成して返します。
     *
     * デフォルトの実装は、 [PreferenceFragmentController] の新しいインスタンスを返します。
     *
     * @return [PreferenceView] を配置する機能の実装
     */
    protected open fun createPreferenceFragmentController(): PreferenceFragmentController {
        return PreferenceFragmentController()
    }

    /**
     * 編集対象の [SharedPreferences] を返します。
     *
     * * デフォルトの実装では、 [PreferenceManager.getDefaultSharedPreferences] を返します。
     *   他の [SharedPreferences] を使用する場合は、このメソッドを override してください。
     *
     * @return [SharedPreferences]
     */
    protected open fun getTargetPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    /**
     * デフォルト以外の [PreferenceEditor] を登録するメソッドです。
     *
     * * デフォルトの [PreferenceEditor] は既に登録済みのため、 [registerEditor] のコールは不要です。
     * * デフォルト以外の [PreferenceEditor] を使用する場合は、このクラスを継承したクラスで
     *   [onRegisterCustomEditors] メソッドを override して、
     *   [registerEditor] メソッドをコールしてください。
     */
    protected open fun onRegisterCustomEditors() {}

    /**
     * 編集対象の [SharedPreferences] を返します。
     *
     * * [getTargetPreferences] メソッドが返した [SharedPreferences] を返します。
     *
     * @return [SharedPreferences]
     */
    @Suppress("unused")
    protected fun requirePreferences(): SharedPreferences = pref.requirePreferences()

    /**
     * この画面で使用する [PreferenceEditor] を追加します。
     *
     * * デフォルトの [PreferenceEditor] は既に登録済みのため、 [registerEditor] のコールは不要です。
     * * デフォルト以外の [PreferenceEditor] を使用する場合は、このクラスを継承したクラスで
     *   [onRegisterCustomEditors] メソッドを override して、
     *   [registerEditor] メソッドをコールしてください。
     *
     * @param tag この画面で使用する [PreferenceEditor] を識別する文字列。
     *            この文字列は、 instanceState の保存キーとしても使用されます。
     * @param editor 使用する [PreferenceEditor]
     */
    @Suppress("unused")
    protected fun registerEditor(tag: String, editor: PreferenceEditor) =
        pref.registerEditor(tag, editor)

    /**
     * Combine [PreferenceView] and default [PreferenceEditor]
     *
     * [PreferenceView] タップ時に起動する、デフォルトの [PreferenceEditor] を設定します。
     *
     * デフォルトの [PreferenceEditor] と、対応する [PreferenceView] の組み合わせは下記の通りです。
     * * [SingleSelectionIntListDialogEditor] - [SingleSelectionIntListPreferenceView]
     * * [NumberAndUnitDialogEditor] - [NumberAndUnitPreferenceView]
     * * [SingleSelectionStringListDialogEditor] - [SingleSelectionStringListPreferenceView]
     * * [StringDialogEditor] - [StringPreferenceView]
     * * [SwitchPreferenceViewController] - [SwitchPreferenceView]
     * * [TimePeriodActivityEditor] - [TimePeriodPreferenceView]
     * * [TimeDialogEditor] - [TimePreferenceView]
     *
     * @param view [PreferenceView]
     */
    @Suppress("unused")
    protected fun setupPreferenceView(view: PreferenceView) =
        pref.setupPreferenceView(view)

    /**
     * Combine [PreferenceView] and [PreferenceEditor]
     *
     * [PreferenceView] タップ時に起動する [PreferenceEditor] を設定します。
     * * デフォルト以外の [PreferenceEditor] を使用する場合は、 [registerEditor] メソッドをコールして
     *   カスタム [PreferenceEditor] を登録し、画面へ [PreferenceView] を配置した後でこのメソッドをコールして、
     *   [registerEditor] メソッドコール時の tag 引数と同じ文字列を指定してください。
     *
     * @param view [PreferenceView]
     * @param editorTag [registerEditor] メソッドの引数 tag に設定した文字列
     */
    @Suppress("unused")
    protected fun setupPreferenceView(view: PreferenceView, editorTag: String) =
        pref.setupPreferenceView(view, editorTag)

    /**
     * [PreferenceView] タップ時に起動する [PreferenceEditor] の設定を完了します。
     *
     * 画面上の全ての [PreferenceView] に対し [setupPreferenceView] をコールした後で、このメソッドを
     * 1回コールしてください。
     */
    @Suppress("unused")
    protected fun commitSetupPreferenceViews() = pref.commitSetupPreferenceViews()

    /**
     * 画面上の全ての [PreferenceView] の表示を更新します。
     */
    @Suppress("unused")
    protected fun updatePreferenceViews() = pref.updatePreferenceViews()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create PreferenceFragmentController
        val pref = createPreferenceFragmentController()
        this.pref = pref

        // Default process
        pref.onCreateFragment(this, this)

        // Set up preferences first
        pref.preferences = getTargetPreferences()

        // Registering default editors
        pref.setupDefaultEditors()

        // Registering non-default editors
        onRegisterCustomEditors()

        // Restore editors state (if savedInstanceState exists)
        pref.restoreEditorState(savedInstanceState)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()

        pref.roundTripManager.containerId = rootView.id
        pref.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()

        pref.onPause()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        pref.onDestroy()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()

        pref.onDestroyViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        PreferenceLog.v(TAG, "onSaveInstanceState: start")

        pref.onSaveInstanceState(outState)

        PreferenceLog.v(TAG, "onSaveInstanceState: end")
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?) {
        pref.onDialogResult(requestCode, resultCode, data)
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "PreferenceFragment"
    }
}