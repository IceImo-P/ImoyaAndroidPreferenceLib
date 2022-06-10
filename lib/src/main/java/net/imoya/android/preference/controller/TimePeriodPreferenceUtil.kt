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
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.model.TimePeriod.Companion.parse

/**
 * [TimePeriod] 設定値関連ユーティリティ
 */
@Suppress("unused")
object TimePeriodPreferenceUtil {
    /**
     * 指定の [SharedPreferences] より、[TimePeriod]を取得して返します。
     *
     * @param sharedPreferences [SharedPreferences]
     * @param key               設定値のキー
     * @return 設定値が存在し読み取りに成功した場合は [TimePeriod], その他の場合は null
     */
    @JvmStatic
    fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod? {
        // Log.v(TAG, "getTimePeriod: start.")
        val stored = sharedPreferences.getString(key, null)
        return if (stored != null) {
            try {
                parse(stored)
            } catch (ex: Exception) {
                null
            }
        } else null
    }

    /**
     * 指定の時刻が、指定の
     * [SharedPreferences] に保存された設定値の時間範囲に含まれているか否かを返します。
     * 時間範囲は設定値の両端の時刻を含みます。
     *
     * @param sharedPreferences [SharedPreferences]
     * @param key               Preference の Key を表す文字列。
     * @param hourOfDay         時刻の時(0-23)
     * @param minute            時刻の分(0-59)
     * @return 正常な設定値が存在し、且つ指定の時刻が範囲に含まれている場合は true, それ以外の場合は false
     */
    @JvmStatic
    fun isInPeriod(
        sharedPreferences: SharedPreferences, key: String, hourOfDay: Int, minute: Int
    ): Boolean {
        val value = getTimePeriod(sharedPreferences, key)
        return value?.isInPeriod(hourOfDay, minute, 0) ?: false
    }
}