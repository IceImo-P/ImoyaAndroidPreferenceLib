package net.imoya.android.preference.controller

import net.imoya.android.preference.model.TimePeriod.Companion.parse
import net.imoya.android.util.Log
import net.imoya.android.preference.activity.TimePeriodInputActivity.Companion.putExtras
import net.imoya.android.preference.activity.TimePeriodInputActivity.Companion.getResultTimePeriod
import android.content.SharedPreferences
import androidx.activity.result.ActivityResultLauncher
import android.content.Intent
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.activity.TimePeriodInputActivity
import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import java.lang.Exception

/**
 * [TimePeriod] 設定値編集コントローラ
 *
 *
 * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimePeriodPreferenceEditor(
    /**
     * 設定画面の親画面
     */
    protected val parent: PreferenceScreenParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences,
    /**
     * 設定画面の識別に使用するリクエストコード
     */
    protected val requestCode: Int
) : PreferenceEditor(preferences) {
    /**
     * リスナ
     */
    interface Listener {
        /**
         * 入力完了時コールバック
         *
         * @param editor [TimePeriodPreferenceEditor]
         */
        fun onEdit(editor: TimePeriodPreferenceEditor)
    }

    /**
     * [Context]
     */
    protected val context: Context = parent.context

    @Suppress("MemberVisibilityCanBePrivate")
    protected var show24Hour = false

    /**
     * リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var listener: Listener? = null

    @Suppress("MemberVisibilityCanBePrivate")
    protected val editorLauncher: ActivityResultLauncher<Intent>

    init {
        editorLauncher = parent.registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult -> onEditorResult(result.resultCode, result.data) }
    }

    override fun createState(): State {
        return State()
    }

    override fun setupState(view: SingleValuePreferenceView) {
        super.setupState(view)
        show24Hour = (view as TimePeriodPreferenceView).is24hourView
    }

    override fun startEditorUI(view: SingleValuePreferenceView) {
        // 入力画面を開始する
        val intent = Intent(context, TimePeriodInputActivity::class.java)
        putExtras(
            intent,
            getTimePeriod(preferences, view.preferenceKey),
            show24Hour
        )
        // fragment.startActivityForResult(intent, this.requestCode)
        editorLauncher.launch(intent)
    }

    protected open fun onEditorResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            // 入力値を保存する
            val period = getResultTimePeriod(data)
            preferences.edit()
                .putString(state.key, period.toString())
                .apply()
            if (listener != null) {
                listener!!.onEdit(this)
            }
        }
    }

    private fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod {
        return try {
            parse(sharedPreferences.getString(key, null)!!)
        } catch (e: Exception) {
            Log.v(TAG, "getTimePeriod: Exception", e)
            TimePeriod()
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePeriodPreferenceEditor"
    }
}