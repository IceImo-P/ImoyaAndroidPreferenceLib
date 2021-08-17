package net.imoya.android.preference.view

import android.content.SharedPreferences

/**
 * 設定項目ビュー共通メソッド定義
 */
interface PreferenceItemView {
    /**
     * [SharedPreferences] 更新時の処理を行います。
     *
     * @param sharedPreferences 更新された [SharedPreferences]
     * @param key 更新された項目のキー
     * @return 処理を行った場合は true, そうでない場合は false
     */
    fun onPreferenceChange(sharedPreferences: SharedPreferences, key: String): Boolean
}