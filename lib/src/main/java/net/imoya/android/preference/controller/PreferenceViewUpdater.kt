package net.imoya.android.preference.controller

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.util.Log

/**
 * [PreferenceView] 自動更新コントローラ
 */
@Suppress("unused")
open class PreferenceViewUpdater : OnSharedPreferenceChangeListener {
    @Suppress("MemberVisibilityCanBePrivate")
    var views: Array<PreferenceView> = arrayOf()

    fun clearViews() {
        Log.d(TAG, "clearViews")
        views = arrayOf()
    }

    fun start(preferences: SharedPreferences) {
        Log.d(TAG, "start")
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    fun stop(preferences: SharedPreferences) {
        Log.d(TAG, "stop")
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun update(sharedPreferences: SharedPreferences) {
        try {
            for (view in views) {
                Log.v(TAG, "update: updating ${view.title}")
                try {
                    view.updateViews(sharedPreferences)
                } catch (e1: Exception) {
                    Log.v(TAG, "update: Exception at ${view.title}", e1)
                }
            }
        } catch (e: Exception) {
            Log.v(TAG, "update: Exception", e)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        Log.d(TAG, "onSharedPreferenceChanged")
        try {
            for (view in views) {
                view.onPreferenceChange(sharedPreferences, key)
            }
        } catch (e: Exception) {
            Log.v(TAG, "onSharedPreferenceChanged: Exception", e)
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "PreferenceViewUpdater"
    }
}