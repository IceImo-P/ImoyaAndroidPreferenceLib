package net.imoya.android.preference.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            AppLog.v(TAG, "onCreate: Setting up MainFragment")
            supportFragmentManager.beginTransaction()
                .add(R.id.content_frame, MainFragment(), "Fragment")
                .commit()
        }

        AppLog.v(TAG, "onCreate: end")
    }

    companion object {
        const val TAG = "MainActivity"
    }
}