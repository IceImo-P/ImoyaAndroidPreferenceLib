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

package net.imoya.android.preference.controller.editor

import android.content.SharedPreferences
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 編集画面を表示する設定値編集コントローラの共通実装
 *
 * [PreferenceView] タップ時に編集画面へ遷移し、ユーザが編集画面で入力・選択した結果を
 * [SharedPreferences] へ保存するタイプの、設定値編集コントローラ共通部分を実装します。
 */
abstract class ScreenEditor(
    /**
     * [SharedPreferences] to read current value and write result
     */
    preferences: SharedPreferences? = null
) : PreferenceEditor(preferences), OnPreferenceViewClickListener {
    /** 状態オブジェクト */
    protected lateinit var state: ScreenEditorState

    /**
     * 状態オブジェクトの新しいインスタンスを生成して返します。
     *
     * @return 状態オブジェクト
     */
    protected abstract fun createState(): ScreenEditorState

    /**
     * 指定の [Parcelable] が利用可能な状態オブジェクトであるか否かを返します。
     *
     * @param parcelable [Parcelable]
     * @return 利用可能である場合は true, その他の場合は false
     */
    protected open fun isCompatibleState(parcelable: Parcelable): Boolean {
        return parcelable is ScreenEditorState
    }

    /**
     * [Fragment] 再起動時に一時保存するデータ, データなしの場合は null
     */
    open var instanceState: Parcelable?
        get() = if (::state.isInitialized) state else null
        set(value) {
            if (value != null && isCompatibleState(value)) {
                state = value as ScreenEditorState
            } else if (value != null) {
                throw RuntimeException("Incompatible parcelable")
            }
        }

    override fun attach(view: PreferenceView) {
        view.onPreferenceViewClickListener = this
    }

    /**
     * [PreferenceView] がタップされた時の処理を行います。
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    override fun onPreferenceViewClick(view: PreferenceView) {
        // Retrieve properties(ex. key for SharedPreferences) from PreferenceView
        setupState(view)

        // Start editor UI
        startEditorUI(view)
    }

    /**
     * 状態オブジェクトを初期化し、
     * [PreferenceView] が保持する情報を状態オブジェクトへ保存します。
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    @CallSuper
    protected open fun setupState(view: PreferenceView) {
        state = createState()
        state.title = view.title
        if (view is SingleValuePreferenceView) {
            state.key = view.preferenceKey
        }
    }

    /**
     * 編集画面へ遷移します。
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    protected abstract fun startEditorUI(view: PreferenceView)

    /**
     * Check Preference key is set
     *
     * @return Preference key
     * @throws IllegalStateException Preference key is not set
     */
    protected fun checkKey(): String {
        return state.key ?: throw IllegalStateException("Preference key is not set")
    }
}