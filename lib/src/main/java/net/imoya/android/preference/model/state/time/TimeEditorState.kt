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

package net.imoya.android.preference.model.state.time

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.CallSuper
import androidx.core.os.BundleCompat
import net.imoya.android.preference.controller.editor.time.TimeDialogEditor
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [TimeDialogEditor] の状態オブジェクト
 */
open class TimeEditorState : ScreenEditorState {
    /**
     * 編集中の時刻
     */
    var time: Time? = null

    /**
     * [time] が null である時に、代わりに画面へ表示する時刻の指定
     */
    var timeForNull: Time = Time(0, 0, 0)

    /**
     * 24時間表示フラグ
     */
    var is24hourView: Boolean = false

    constructor() : super()

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) : super(bundle) {
        time = BundleCompat.getParcelable(bundle, KEY_TIME, Time::class.java)
        timeForNull = BundleCompat.getParcelable(bundle, KEY_TIME_FOR_NULL, Time::class.java)
            ?: Time(0, 0, 0)
        is24hourView = bundle.getBoolean(KEY_IS_24_HOUR_VIEW)
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        val hour = PreferenceViewSavedStateUtil.readByte(parcel, TAG)
        val minute = PreferenceViewSavedStateUtil.readByte(parcel, TAG)
        val second = PreferenceViewSavedStateUtil.readByte(parcel, TAG)
        time = if (hour >= 0) Time(hour.toInt(), minute.toInt(), second.toInt()) else null
        val hourForNull = PreferenceViewSavedStateUtil.readByte(parcel, TAG)
        val minuteForNull = PreferenceViewSavedStateUtil.readByte(parcel, TAG)
        val secondForNull = PreferenceViewSavedStateUtil.readByte(parcel, TAG)
        timeForNull = Time(hourForNull.toInt(), minuteForNull.toInt(), secondForNull.toInt())
        is24hourView = PreferenceViewSavedStateUtil.readByte(parcel, TAG).toInt() == 1
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val currentTime = time
        if (currentTime != null) {
            dest.writeByte(currentTime.hour.toByte())
            dest.writeByte(currentTime.minute.toByte())
            dest.writeByte(currentTime.second.toByte())
        } else {
            dest.writeByte(-1)
            dest.writeByte(-1)
            dest.writeByte(-1)
        }
        dest.writeByte(timeForNull.hour.toByte())
        dest.writeByte(timeForNull.minute.toByte())
        dest.writeByte(timeForNull.second.toByte())
        dest.writeByte(if (is24hourView) 1 else 0)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putParcelable(KEY_TIME, time)
        bundle.putParcelable(KEY_TIME_FOR_NULL, timeForNull)
        bundle.putBoolean(KEY_IS_24_HOUR_VIEW, is24hourView)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Time
         */
        const val KEY_TIME = "time"

        /**
         * Key at [Bundle] : Initial value if [time] is null
         */
        const val KEY_TIME_FOR_NULL = "timeForNull"

        /**
         * Key at [Bundle] : 24時間表示フラグ
         */
        const val KEY_IS_24_HOUR_VIEW = "is24HourView"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<TimeEditorState> = object :
            Creator<TimeEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): TimeEditorState {
                return TimeEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<TimeEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "TimeEditorState"
    }
}