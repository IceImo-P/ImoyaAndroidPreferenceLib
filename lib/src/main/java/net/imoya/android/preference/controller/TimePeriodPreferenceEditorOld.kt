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
import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.TimeInputDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView

/**
 * [TimePeriod] 設定値編集コントローラ
 *
 * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimePeriodPreferenceEditorOld(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent?,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences?,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCodeStart: Int = Constants.DEFAULT_REQUEST_CODE,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    @Suppress("weaker")
    var requestCodeEnd: Int = Constants.DEFAULT_REQUEST_CODE
) : DialogPreferenceEditor(parent, preferences, requestCodeStart) {
    /**
     * リスナ
     */
    interface Listener {
        fun onEdit(editor: TimePeriodPreferenceEditorOld?)
    }

    /**
     * 状態オブジェクト
     */
    protected open class State : PreferenceEditor.State {
        /**
         * 現在編集中の [TimePeriod]
         */
        var timePeriod: TimePeriod? = null

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            timePeriod = parcel.readParcelable(TimePeriod::class.java.classLoader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(timePeriod, 0)
        }

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<State> = object : Creator<State> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): State {
                    return State(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<State?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    /**
     * 24時間表示フラグ
     */
    private var show24Hour = false

    /**
     * リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var listener: Listener? = null

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is TimePeriodPreferenceView
    }

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        val currentPreferences = checkPreferences()
        (state as State).timePeriod = getTimePeriod(
            currentPreferences, state.key!!
        )
        show24Hour = (view as TimePeriodPreferenceView).is24hourView
    }

    private fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod {
        return try {
            TimePeriod.parse(sharedPreferences.getString(key, null)!!)
        } catch (e: Exception) {
            PreferenceLog.v(TAG, "getTimePeriod: Exception", e)
            TimePeriod()
        }
    }

    override fun showDialog(view: PreferenceView) {
        showStartTimeInputDialog()
    }

    private fun showStartTimeInputDialog() {
        val context = checkParent().context
        checkAndWarnRequestCode()
        showInputDialog(
            (state as State).timePeriod!!.start.hour,
            (state as State).timePeriod!!.start.minute,
            requestCode,
            context.getString(R.string.imoya_preference_time_period_edit_start_title),
            context.getString(R.string.imoya_preference_dialog_next),
            context.getString(android.R.string.cancel)
        )
    }

    private fun showEndTimeInputDialog() {
        val context = checkParent().context
        showInputDialog(
            (state as State).timePeriod!!.end.hour,
            (state as State).timePeriod!!.end.minute,
            requestCodeEnd,
            context.getString(R.string.imoya_preference_time_period_edit_end_title),
            context.getString(android.R.string.ok),
            context.getString(R.string.imoya_preference_dialog_back)
        )
    }

    private fun showInputDialog(
        hour: Int, minute: Int, requestCode: Int, title: String,
        positiveButtonTitle: String, negativeButtonTitle: String
    ) {
        val currentParent = checkParent()
        checkAndWarnRequestCode()

        TimeInputDialog.Builder(currentParent, requestCode)
            .setTitle(title)
            .setHour(hour)
            .setMinute(minute)
            .setIs24HourView(show24Hour)
            .setPositiveButtonTitle(positiveButtonTitle)
            .setNegativeButtonTitle(negativeButtonTitle)
            .setTag(TAG)
            .show()
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 入力値を保存する
                (state as State).timePeriod!!.start = Time(
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0),
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0),
                    0
                )
                // 終了時入力ダイアログを表示する
                showEndTimeInputDialog()
            }
            return true
        } else if (requestCode == requestCodeEnd) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 入力値を保存する
                (state as State).timePeriod!!.end = Time(
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0),
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0),
                    0
                )
                saveInput(resultCode, data)
            } else {
                // 開始時入力ダイアログを表示する
                showStartTimeInputDialog()
            }
            return true
        }
        return false
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        checkPreferences().edit()
            .putString(state.key, (state as State).timePeriod.toString())
            .apply()
        listener?.onEdit(this)
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePeriodPreferenceEditor"
    }
}