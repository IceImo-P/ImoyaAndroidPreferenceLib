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

package net.imoya.android.preference.controller

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.view.PreferenceView

/**
 * [PreferenceView] 自動更新コントローラ
 */
@Suppress("unused")
open class PreferenceViewUpdater : OnSharedPreferenceChangeListener {
    @Suppress("MemberVisibilityCanBePrivate")
    var views: Array<PreferenceView> = arrayOf()

    fun clearViews() {
        PreferenceLog.v(TAG, "clearViews")
        views = arrayOf()
    }

    /**
     * [PreferenceView] の自動更新を開始します。
     *
     * @param preferences 変更を監視する [SharedPreferences]
     */
    fun start(preferences: SharedPreferences) {
        PreferenceLog.v(TAG, "start")
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    /**
     * [PreferenceView] の自動更新を終了します。
     *
     * @param preferences 変更を監視していた [SharedPreferences] 。 [start] の引数と同じものを指定する。
     */
    fun stop(preferences: SharedPreferences) {
        PreferenceLog.v(TAG, "stop")
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * [PreferenceView] の表示を直ちに更新します。
     *
     * @param sharedPreferences 変更を監視している [SharedPreferences] 。 [start] の引数と同じものを指定する。
     */
    fun update(sharedPreferences: SharedPreferences) {
        try {
            for (view in views) {
                PreferenceLog.v(TAG) { "update: updating ${view.title}" }
                try {
                    view.updateViews(sharedPreferences)
                } catch (e1: Exception) {
                    PreferenceLog.v(TAG, { "update: Exception at ${view.title}" }, e1)
                }
            }
        } catch (e: Exception) {
            PreferenceLog.w(TAG, "update: Exception", e)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        PreferenceLog.v(TAG, "onSharedPreferenceChanged")
        try {
            for (view in views) {
                view.onPreferenceChange(sharedPreferences, key)
            }
        } catch (e: Exception) {
            PreferenceLog.w(TAG, "onSharedPreferenceChanged: Exception", e)
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "PreferenceViewUpdater"
    }
}