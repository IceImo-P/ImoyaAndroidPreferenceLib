package net.imoya.android.preference.view

import android.content.SharedPreferences
import android.view.View

/**
 * 設定値の変更を通知します。
 *
 * [PreferenceView] は、あくまで表示を行う [View] です。
 * ユーザが [PreferenceView] を操作して状態を変更した場合、変更後の値は
 * [PreferenceView] を使用するプログラムが、
 * [SharedPreferences] へ保存する必要があります。
 */
interface OnPreferenceChangeListener {
    /**
     * 設定値が変更された直後に呼び出されます。
     *
     * @param view 設定値が変更された [PreferenceView]
     */
    fun onPreferenceChange(view: PreferenceView)
}