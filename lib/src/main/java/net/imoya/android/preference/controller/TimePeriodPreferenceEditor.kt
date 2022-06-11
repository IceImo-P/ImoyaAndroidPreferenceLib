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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.activity.TimePeriodInputActivity
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView

/**
 * [TimePeriod] 設定値編集コントローラ
 *
 *
 * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimePeriodPreferenceEditor(
    /**
     * 設定画面の親画面
     */
    protected val parent: PreferenceScreenParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences,
    /**
     * 設定画面の識別に使用するリクエストコード
     */
    protected val requestCode: Int
) : PreferenceEditor(preferences) {
    /**
     * リスナ
     */
    interface Listener {
        /**
         * 入力完了時コールバック
         *
         * @param editor [TimePeriodPreferenceEditor]
         */
        fun onEdit(editor: TimePeriodPreferenceEditor)
    }

    /**
     * [Context]
     */
    protected val context: Context = parent.context

    @Suppress("MemberVisibilityCanBePrivate")
    protected var show24Hour = false

    /**
     * リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var listener: Listener? = null

    @Suppress("MemberVisibilityCanBePrivate")
    protected val editorLauncher: ActivityResultLauncher<Intent>

    init {
        editorLauncher = parent.registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult -> onEditorResult(result.resultCode, result.data) }
    }

    override fun createState(): State {
        return State()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        show24Hour = (view as TimePeriodPreferenceView).is24hourView
    }

    override fun startEditorUI(view: PreferenceView) {
        if (view !is SingleValuePreferenceView) {
            throw RuntimeException("View must be SingleValuePreferenceView")
        }
        // 入力画面を開始する
        val intent = Intent(context, TimePeriodInputActivity::class.java)
        TimePeriodInputActivity.putExtras(
            intent,
            getTimePeriod(preferences, view.preferenceKey),
            show24Hour
        )
        // fragment.startActivityForResult(intent, this.requestCode)
        editorLauncher.launch(intent)
    }

    protected open fun onEditorResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            // 入力値を保存する
            val period = TimePeriodInputActivity.getResultTimePeriod(data)
            preferences.edit()
                .putString(state.key, period.toString())
                .apply()
            if (listener != null) {
                listener!!.onEdit(this)
            }
        }
    }

    private fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod {
        return try {
            TimePeriod.parse(sharedPreferences.getString(key, null)!!)
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