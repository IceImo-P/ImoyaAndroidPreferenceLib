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
import net.imoya.android.preference.view.PreferenceView

/**
 * 設定値編集コントローラの共通実装
 *
 * 設定項目タップにより設定値を変更し、結果を [SharedPreferences] へ保存するコントローラ共通部分を実装します。
 */
@Suppress("unused")
abstract class PreferenceEditor(
    /**
     * [SharedPreferences] to read current value and write result
     */
    @JvmField var preferences: SharedPreferences? = null
) {
    /**
     * 指定の [PreferenceView] が、この [PreferenceEditor] と結合可能であるか否かを返します。
     *
     * Returns [PreferenceView] can combine with this or not.
     *
     * @param view [PreferenceView]
     * @return true if can, false otherwise
     */
    abstract fun isCompatibleView(view: PreferenceView): Boolean

    /**
     * 指定の設定項目ビューをタップした時に、このコントローラが呼び出されるよう設定します。
     *
     * @param view 設定項目ビュー
     */
    abstract fun attach(view: PreferenceView)

    /**
     * Check [preferences] is set
     *
     * @return [SharedPreferences]
     * @throws IllegalStateException [preferences] is not set
     */
    protected fun checkPreferences(): SharedPreferences {
        return preferences ?: throw IllegalStateException("preferences is not set")
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "PreferenceEditor"
//    }
}