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
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.TimeInputDialog
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.TimePreferenceView

/**
 * [Time] 設定値編集コントローラ
 *
 *
 * [TimePreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimePreferenceEditor(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCode: Int
) : DialogPreferenceEditor(parent, preferences, requestCode) {
    /**
     * 状態オブジェクト
     */
    protected open class State : PreferenceEditor.State {
        /**
         * 現在編集中の [Time]
         */
        var time: Time? = null

        constructor() : super()

        protected constructor(parcel: Parcel) : super(parcel) {
            time = parcel.readParcelable(Time::class.java.classLoader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(time, 0)
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
    @Suppress("MemberVisibilityCanBePrivate")
    protected var show24Hour = false

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        (state as State).time = getTime(preferences, state.key!!)
        show24Hour = (view as TimePreferenceView).is24hourView
    }

    private fun getTime(sharedPreferences: SharedPreferences, key: String): Time {
        return try {
            Time.parse(sharedPreferences.getString(key, null)!!)
        } catch (e: Exception) {
            PreferenceLog.v(TAG, "getTime: Exception", e)
            Time()
        }
    }

    override fun showDialog(view: PreferenceView) {
        // 時刻入力ダイアログを表示する
        TimeInputDialog.Builder(parent, requestCode)
            .setHour((state as State).time!!.hour)
            .setMinute((state as State).time!!.minute)
            .setIs24HourView(show24Hour)
            .setTag(this.javaClass.name)
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val time = Time()
        time.hour = data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0)
        time.minute = data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0)
        preferences.edit().putString(state.key, time.toString()).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePreferenceEditor"
    }
}