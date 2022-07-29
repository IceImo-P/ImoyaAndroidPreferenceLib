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

package net.imoya.android.preference.controller.editor.time

import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.TimeInputDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.controller.editor.DialogEditor
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.time.TimePreferenceView

/**
 * [Time] 設定値編集コントローラ
 *
 * * 設定画面は [TimeInputDialog] を使用します。
 * * [TimePreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimeDialogEditor(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent? = null,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCode: Int = Constants.DEFAULT_REQUEST_CODE
) : DialogEditor(parent, preferences, requestCode) {
    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is TimePreferenceView
    }

    override fun createState(): ScreenEditorState {
        return ScreenEditorState()
    }

    override fun showDialog(view: PreferenceView) {
        // 時刻入力ダイアログを表示する
        if (view !is TimePreferenceView) {
            throw IllegalArgumentException("View must be TimePreferenceView")
        }
        checkAndWarnRequestCode()
        val time = getTime(checkPreferences(), checkKey())

        TimeInputDialog.Builder(checkParent(), requestCode)
            .setHour(time.hour)
            .setMinute(time.minute)
            .setIs24HourView(view.is24hourView)
            .setTag(this.javaClass.name)
            .show()
    }

    private fun getTime(sharedPreferences: SharedPreferences, key: String): Time {
        return try {
            val stringValue = sharedPreferences.getString(key, null)
            if (stringValue != null) Time.parse(stringValue) else Time()
        } catch (e: Exception) {
            PreferenceLog.v(TAG, "getTime: Exception", e)
            Time()
        }
    }

    override fun saveInput(resultCode: Int, data: Intent?) {
        if (data == null) throw IllegalArgumentException("data is null")
        val time = Time()
        time.hour = data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0)
        time.minute = data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0)
        checkPreferences().edit().putString(checkKey(), time.toString()).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePreferenceEditor"
    }
}