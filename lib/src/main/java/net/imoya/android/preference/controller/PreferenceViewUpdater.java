package net.imoya.android.preference.controller;

import android.content.SharedPreferences;

import net.imoya.android.preference.view.PreferenceView;
import net.imoya.android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@link PreferenceView} 自動更新コントローラ
 */
public class PreferenceViewUpdater implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "PreferenceViewUpdater";

    private final ArrayList<PreferenceView> views = new ArrayList<>();

    public void setViews(PreferenceView[] views) {
        Log.d(TAG, "setViews");
        this.views.clear();
        this.views.addAll(Arrays.asList(views));
    }

    public void clearViews() {
        Log.d(TAG, "clearViews");
        this.views.clear();
    }

    public void start(SharedPreferences preferences) {
        Log.d(TAG, "start");
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void stop(SharedPreferences preferences) {
        Log.d(TAG, "stop");
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void update(SharedPreferences sharedPreferences) {
        try {
            for (PreferenceView view : this.views) {
                view.updateViews(sharedPreferences);
            }
        } catch (Exception e) {
            Log.v(TAG, "update: Exception", e);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged");
        try {
            for (PreferenceView view : this.views) {
                view.onPreferenceChange(sharedPreferences, key);
            }
        } catch (Exception e) {
            Log.v(TAG, "onSharedPreferenceChanged: Exception", e);
        }
    }
}
