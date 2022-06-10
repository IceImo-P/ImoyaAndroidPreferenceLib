package net.imoya.android.preference.app

import android.app.Application
import net.imoya.android.dialog.DialogLog
import net.imoya.android.preference.PreferenceLog

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLog.init(applicationContext)
        PreferenceLog.init(applicationContext)
        DialogLog.init(applicationContext)
    }
}