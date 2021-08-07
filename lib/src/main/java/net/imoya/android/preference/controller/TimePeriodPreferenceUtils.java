package net.imoya.android.preference.controller;

import android.content.SharedPreferences;

import net.imoya.android.preference.model.TimePeriod;

/**
 * {@link TimePeriod} 設定値関連ユーティリティ
 */
public class TimePeriodPreferenceUtils {
    /**
     * 指定の {@link SharedPreferences} より、{@link TimePeriod}を取得して返します。
     *
     * @param sharedPreferences {@link SharedPreferences}
     * @param key               設定値のキー
     * @return 読み取った値が格納された {@link TimePeriod}
     */
    private static TimePeriod getTimePeriod(SharedPreferences sharedPreferences, String key) {
//        Log.d(TAG, "getTimePeriod: start.");
        String stored = sharedPreferences.getString(key, null);
        try {
            return TimePeriod.parse(stored);
        } catch (Exception ex) {
            return new TimePeriod();
        }
    }

    /**
     * 指定の時刻が、指定の
     * SharedPreferencesに保存された設定値の時間範囲に含まれているか否かを返します。
     * 時間範囲は設定値の両端の時刻を含みます。
     *
     * @param sharedPreferences SharedPreferences
     * @param key               PreferenceのKeyを表す文字列。
     * @param hourOfDay         時刻の時(0-23)
     * @param minute            時刻の分(0-59)
     * @return 指定の時刻が範囲に含まれている場合はtrue, それ以外の場合はfalse。
     */
    public static boolean isInPeriod(
            SharedPreferences sharedPreferences, String key, int hourOfDay, int minute) {
        TimePeriod value = getTimePeriod(sharedPreferences, key);
        return value.isInPeriod(hourOfDay, minute, 0);
    }
}
