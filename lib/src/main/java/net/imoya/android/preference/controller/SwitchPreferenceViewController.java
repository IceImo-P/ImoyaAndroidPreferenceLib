package net.imoya.android.preference.controller;

import android.content.SharedPreferences;

import net.imoya.android.preference.view.PreferenceView;
import net.imoya.android.preference.view.SwitchPreferenceView;

/**
 * スイッチ付き設定項目ビュー用、デフォルトコントローラ
 * <p/>
 * スイッチ付き設定項目ビューの操作に対応する、デフォルトの処理を実装します。<ul>
 * <li>{@link SharedPreferences} へ、スイッチの状態を保存します。</li>
 * </ul>
 */
public class SwitchPreferenceViewController
        implements SwitchPreferenceView.OnPreferenceChangeListener {
    private final SharedPreferences preferences;

    public SwitchPreferenceViewController(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void attach(SwitchPreferenceView view) {
        view.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onPreferenceChange(PreferenceView view) {
        if (view instanceof SwitchPreferenceView) {
            final SwitchPreferenceView switchView = (SwitchPreferenceView) view;
            this.preferences.edit()
                    .putBoolean(switchView.getPreferenceKey(), switchView.getIsOn())
                    .apply();
        }
    }
}
