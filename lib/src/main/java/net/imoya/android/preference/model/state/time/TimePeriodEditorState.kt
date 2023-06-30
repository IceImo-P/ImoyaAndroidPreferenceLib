/*
 * Copyright (C) 2022-2023 IceImo-P
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
import net.imoya.android.preference.controller.editor.time.TimePeriodActivityEditor
import net.imoya.android.preference.model.state.ScreenEditorState
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.util.BundleUtil

/**
 * [TimePeriodActivityEditor] の状態オブジェクト
 */
open class TimePeriodEditorState : ScreenEditorState {
    /**
     * 編集中の [TimePeriod]
     */
    var timePeriod: TimePeriod? = null

    /**
     * [timePeriod] が null である時に、代わりに画面へ表示する時刻の指定
     */
    var timePeriodForNull: TimePeriod = TimePeriod(
        Time(0, 0, 0), Time(0, 0, 0)
    )

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
        timePeriod = BundleUtil.getParcelable(bundle, KEY_TIME_PERIOD, TimePeriod::class.java)
        timePeriodForNull =
            BundleUtil.getParcelable(bundle, KEY_TIME_PERIOD_FOR_NULL, TimePeriod::class.java)
                ?: TimePeriod(Time(0, 0, 0), Time(0, 0, 0))
        is24hourView = bundle.getBoolean(KEY_IS_24_HOUR_VIEW)
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        val startHour = parcel.readByte()
        val startMinute = parcel.readByte()
        val startSecond = parcel.readByte()
        val endHour = parcel.readByte()
        val endMinute = parcel.readByte()
        val endSecond = parcel.readByte()
        timePeriod = if (startHour >= 0) TimePeriod(
            Time(startHour.toInt(), startMinute.toInt(), startSecond.toInt()),
            Time(endHour.toInt(), endMinute.toInt(), endSecond.toInt()),
        ) else null
        timePeriodForNull = TimePeriod(
            Time(parcel.readByte().toInt(), parcel.readByte().toInt(), parcel.readByte().toInt()),
            Time(parcel.readByte().toInt(), parcel.readByte().toInt(), parcel.readByte().toInt()),
        )
        is24hourView = parcel.readByte().toInt() == 1
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val currentTime = timePeriod
        if (currentTime != null) {
            dest.writeByte(currentTime.start.hour.toByte())
            dest.writeByte(currentTime.start.minute.toByte())
            dest.writeByte(currentTime.start.second.toByte())
            dest.writeByte(currentTime.end.hour.toByte())
            dest.writeByte(currentTime.end.minute.toByte())
            dest.writeByte(currentTime.end.second.toByte())
        } else {
            dest.writeByte(-1)
            dest.writeByte(-1)
            dest.writeByte(-1)
            dest.writeByte(-1)
            dest.writeByte(-1)
            dest.writeByte(-1)
        }
        dest.writeByte(timePeriodForNull.start.hour.toByte())
        dest.writeByte(timePeriodForNull.start.minute.toByte())
        dest.writeByte(timePeriodForNull.start.second.toByte())
        dest.writeByte(timePeriodForNull.end.hour.toByte())
        dest.writeByte(timePeriodForNull.end.minute.toByte())
        dest.writeByte(timePeriodForNull.end.second.toByte())
        dest.writeByte(if (is24hourView) 1 else 0)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putParcelable(KEY_TIME_PERIOD, timePeriod)
        bundle.putParcelable(KEY_TIME_PERIOD_FOR_NULL, timePeriodForNull)
        bundle.putBoolean(KEY_IS_24_HOUR_VIEW, is24hourView)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Time
         */
        const val KEY_TIME_PERIOD = "timePeriod"

        /**
         * Key at [Bundle] : Initial value if [timePeriod] is null
         */
        const val KEY_TIME_PERIOD_FOR_NULL = "timePeriodForNull"

        /**
         * Key at [Bundle] : 24時間表示フラグ
         */
        const val KEY_IS_24_HOUR_VIEW = "is24HourView"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<TimePeriodEditorState> = object :
            Creator<TimePeriodEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): TimePeriodEditorState {
                return TimePeriodEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<TimePeriodEditorState?> {
                return arrayOfNulls(size)
            }
        }
    }
}