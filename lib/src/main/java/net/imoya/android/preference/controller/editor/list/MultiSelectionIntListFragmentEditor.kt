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

package net.imoya.android.preference.controller.editor.list

import android.content.SharedPreferences
import android.os.Bundle
import net.imoya.android.fragment.roundtrip.RoundTripManager
import net.imoya.android.preference.Constants
import net.imoya.android.preference.fragment.editor.list.MultiSelectionListEditorFragment
import net.imoya.android.preference.model.result.list.MultiSelectionListEditorFragmentResult
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.state.list.MultiSelectionIntListEditorState
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.list.MultiSelectionIntListPreferenceView

/**
 * 数値複数選択設定コントローラ
 *
 * * 選択画面は [MultiSelectionListEditorFragment] を使用します。
 * * [MultiSelectionIntListPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class MultiSelectionIntListFragmentEditor(
    /**
     * [RoundTripManager]
     */
    roundTripManager: RoundTripManager? = null,
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null,
    /**
     * 編集画面の識別に使用するリクエストキー
     */
    requestKey: String? = null
) : MultiSelectionListFragmentEditor(roundTripManager, preferences, requestKey) {

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is MultiSelectionIntListPreferenceView
    }

    override fun createState(): ScreenEditorState {
        return MultiSelectionIntListEditorState()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is MultiSelectionIntListPreferenceView) {
            throw IllegalArgumentException("View must be IntListPreferenceView")
        }
        (state as MultiSelectionIntListEditorState).entryValues = view.entryValues
    }

    override fun startEditorUI(view: PreferenceView) {
        val fragment = MultiSelectionListEditorFragment()
        val arguments = Bundle()
        arguments.putBundle(
            Constants.KEY_EDITOR_STATE,
            (state as MultiSelectionIntListEditorState).toBundle()
        )
        fragment.arguments = arguments
        checkRoundTripManager().start(checkRequestKey(), fragment)
    }

    override fun onEditorResult(result: Bundle) {
        val currentPreferences = checkPreferences()
        val key = checkKey()

        val checkedList = MultiSelectionListEditorFragmentResult(result).checkedList
        val entryValues = (state as MultiSelectionIntListEditorState).entryValues

        ListEditorUtil.saveInput(
            currentPreferences,
            key,
            entryValues,
            checkedList
        ) { e, c -> createPreferenceValue(e, c) }
    }

    /**
     * [SharedPreferences] へ保存する値を生成して返します。
     *
     * @param entryValues 各項目の設定値リスト
     * @param checkedList 各項目の選択状態リスト
     * @return [SharedPreferences] へ保存する値
     */
    open fun createPreferenceValue(entryValues: IntArray, checkedList: BooleanArray): String {
        return ListEditorUtil.createPreferenceValue(entryValues, checkedList)
    }

    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "MSelIntListFPrefEditor"
    }
}