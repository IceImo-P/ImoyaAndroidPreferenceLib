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
     * @return 読み取った値が格納された [TimePeriod]
     */
    @JvmStatic
    fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod {
        // Log.d(TAG, "getTimePeriod: start.");
        val stored = sharedPreferences.getString(key, null)
        return if (stored != null) {
            try {
                parse(stored)
            } catch (ex: Exception) {
                TimePeriod()
            }
        } else TimePeriod()
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
     * @return 指定の時刻が範囲に含まれている場合は true, それ以外の場合は false。
     */
    @JvmStatic
    fun isInPeriod(
        sharedPreferences: SharedPreferences, key: String, hourOfDay: Int, minute: Int
    ): Boolean {
        val value = getTimePeriod(sharedPreferences, key)
        return value.isInPeriod(hourOfDay, minute, 0)
    }
}