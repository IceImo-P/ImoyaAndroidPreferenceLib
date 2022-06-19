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

import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.activity.TimePeriodInputActivity
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView

/**
 * [TimePeriod] 設定値編集コントローラ
 *
 * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimePeriodPreferenceEditor(
    /**
     * 設定画面の親画面
     */
    parent: PreferenceScreenParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null
) : ActivityPreferenceEditor(parent, preferences) {
    /**
     * リスナ
     */
    fun interface Listener {
        /**
         * 入力完了時コールバック
         *
         * @param editor [TimePeriodPreferenceEditor]
         * @param key    Key of [SharedPreferences]
         * @param value  User-input [TimePeriod] (or null)
         */
        fun onEdit(editor: TimePeriodPreferenceEditor, key: String, value: TimePeriod?)
    }

    /**
     * リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var listener: Listener? = null

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is TimePeriodPreferenceView
    }

    override fun createState(): State {
        return State()
    }

    override fun createEditorIntent(view: PreferenceView): Intent {
        if (view !is TimePeriodPreferenceView) {
            throw IllegalArgumentException("View must be TimePeriodPreferenceView")
        }

        val intent = Intent(parent.context, TimePeriodInputActivity::class.java)
        TimePeriodInputActivity.putExtras(
            intent,
            getTimePeriod(checkPreferences(), checkKey()),
            view.is24hourView
        )
        return intent
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val key = checkKey()
        val period = TimePeriodInputActivity.getResultTimePeriod(data)

        checkPreferences().edit()
            .putString(key, period.toString())
            .apply()

        listener?.onEdit(this, key, period)
    }

    private fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod {
        return try {
            val stringValue = sharedPreferences.getString(key, null)
            if (stringValue != null) TimePeriod.parse(stringValue) else TimePeriod()
        } catch (e: Exception) {
            PreferenceLog.v(TAG, "getTimePeriod: Exception", e)
            TimePeriod()
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePeriodPreferenceEditor"
    }
}