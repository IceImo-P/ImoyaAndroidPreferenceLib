package net.imoya.android.preference.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.TimeInputDialog
import net.imoya.android.preference.R
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.model.TimePeriod.Companion.parse
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.preference.view.TimePeriodPreferenceView
import net.imoya.android.util.Log

/**
 * [TimePeriod] 設定値編集コントローラ
 *
 *
 * [TimePeriodPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class TimePeriodPreferenceEditorOld(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCodeStart: Int,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    @JvmField
    protected val requestCodeEnd: Int
) : DialogPreferenceEditor(parent, preferences, requestCodeStart) {
    /**
     * リスナ
     */
    interface Listener {
        fun onEdit(editor: TimePeriodPreferenceEditorOld?)
    }

    /**
     * 状態オブジェクト
     */
    protected open class State : PreferenceEditor.State {
        /**
         * 現在編集中の [TimePeriod]
         */
        var timePeriod: TimePeriod? = null

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            timePeriod = parcel.readParcelable(TimePeriod::class.java.classLoader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(timePeriod, 0)
        }

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<State> = object : Creator<State> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): State {
                    return State(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<State?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    /**
     * [Context]
     */
    private val context: Context = parent.context

    /**
     * 24時間表示フラグ
     */
    private var show24Hour = false

    /**
     * リスナ
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var listener: Listener? = null

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: SingleValuePreferenceView) {
        super.setupState(view)
        (state as State).timePeriod = getTimePeriod(
            preferences, state.key!!
        )
        show24Hour = (view as TimePeriodPreferenceView).is24hourView
    }

    private fun getTimePeriod(sharedPreferences: SharedPreferences, key: String): TimePeriod {
        return try {
            parse(sharedPreferences.getString(key, null)!!)
        } catch (e: Exception) {
            Log.v(TAG, "getTimePeriod: Exception", e)
            TimePeriod()
        }
    }

    override fun showDialog(view: SingleValuePreferenceView) {
        showStartTimeInputDialog()
    }

    private fun showStartTimeInputDialog() {
        showInputDialog(
            (state as State).timePeriod!!.start.hour,
            (state as State).timePeriod!!.start.minute,
            requestCode,
            context.getString(R.string.preference_time_period_edit_start_title),
            context.getString(R.string.preference_dialog_next),
            context.getString(android.R.string.cancel)
        )
    }

    private fun showEndTimeInputDialog() {
        showInputDialog(
            (state as State).timePeriod!!.end.hour,
            (state as State).timePeriod!!.end.minute,
            requestCodeEnd,
            context.getString(R.string.preference_time_period_edit_end_title),
            context.getString(android.R.string.ok),
            context.getString(R.string.preference_dialog_back)
        )
    }

    private fun showInputDialog(
        hour: Int, minute: Int, requestCode: Int, title: String,
        positiveButtonTitle: String, negativeButtonTitle: String
    ) {
        TimeInputDialog.Builder(parent, requestCode)
            .setTitle(title)
            .setHour(hour)
            .setMinute(minute)
            .setIs24HourView(show24Hour)
            .setPositiveButtonTitle(positiveButtonTitle)
            .setNegativeButtonTitle(negativeButtonTitle)
            .setTag(TAG)
            .show()
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 入力値を保存する
                (state as State).timePeriod!!.start = Time(
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0),
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0),
                    0
                )
                // 終了時入力ダイアログを表示する
                showEndTimeInputDialog()
            }
            return true
        } else if (requestCode == requestCodeEnd) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 入力値を保存する
                (state as State).timePeriod!!.end = Time(
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0),
                    data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0),
                    0
                )
                saveInput(resultCode, data)
            } else {
                // 開始時入力ダイアログを表示する
                showStartTimeInputDialog()
            }
            return true
        }
        return false
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        preferences.edit()
            .putString(state.key, (state as State).timePeriod.toString())
            .apply()
        if (listener != null) {
            listener!!.onEdit(this)
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePeriodPreferenceEditor"
    }
}