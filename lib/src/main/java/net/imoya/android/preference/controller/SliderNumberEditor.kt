package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.widget.SeekBar
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.SeekBarInputDialog
import net.imoya.android.preference.view.NumberAndUnitPreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.util.Log

/**
 * [SeekBar] 付き整数値設定コントローラ
 *
 * [NumberAndUnitPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class SliderNumberEditor(
    parent: DialogParent, preferences: SharedPreferences, requestCode: Int
) : NumberAndUnitPreferenceEditor(parent, preferences, requestCode) {
    override fun showDialog(view: SingleValuePreferenceView) {
        Log.d(
            TAG, "SliderNumberEditor.showDialog: title = ${view.title}"
                    + ", minValue = ${(state as State).minValue}"
                    + ", maxValue = ${(state as State).maxValue}"
                    + ", defaultValue = ${(state as State).defaultValue}"
        )
        SeekBarInputDialog.Builder(parent, requestCode)
            .setTitle(view.title ?: "")
            .setMin((state as State).minValue)
            .setMax((state as State).maxValue)
            .setValue(
                preferences.getInt(
                    state.key, (state as State).defaultValue
                )
            )
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val value = data.getIntExtra(
            SeekBarInputDialog.EXTRA_KEY_INPUT_VALUE, (state as State).defaultValue
        )
        preferences.edit().putInt(state.key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "SliderNumberEditor"
    }
}