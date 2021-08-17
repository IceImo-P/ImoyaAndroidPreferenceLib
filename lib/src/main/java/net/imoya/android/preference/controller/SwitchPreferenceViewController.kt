package net.imoya.android.preference.controller

import android.content.SharedPreferences
import net.imoya.android.preference.view.OnPreferenceChangeListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SwitchPreferenceView

/**
 * スイッチ付き設定項目ビュー用、デフォルトコントローラ
 *
 * スイッチ付き設定項目ビューの操作に対応する、デフォルトの処理を実装します。
 *  * [SharedPreferences] へ、スイッチの状態を保存します。
 */
@Suppress("unused")
class SwitchPreferenceViewController(private val preferences: SharedPreferences) :
    OnPreferenceChangeListener {

    fun attach(view: SwitchPreferenceView) {
        view.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(view: PreferenceView) {
        if (view is SwitchPreferenceView) {
            preferences.edit()
                .putBoolean(view.preferenceKey, view.getIsOn())
                .apply()
        }
    }
}