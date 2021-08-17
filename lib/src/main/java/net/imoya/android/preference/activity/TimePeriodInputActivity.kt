package net.imoya.android.preference.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import net.imoya.android.fragment.BaseActivity
import net.imoya.android.preference.R
import net.imoya.android.preference.fragment.TimePickerFragment
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.util.Log

/**
 * [TimePeriod] 入力画面
 */
open class TimePeriodInputActivity : BaseActivity() {
    /**
     * 入力中の時刻種別
     */
    protected enum class InputStep {
        /**
         * 開始時刻
         */
        START,

        /**
         * 終了時刻
         */
        END
    }

    /**
     * 状態オブジェクト
     */
    protected open class State : Parcelable {
        /**
         * 現在編集中の [TimePeriod]
         */
        val timePeriod: TimePeriod

        /**
         * 現在画面に表示している時刻種別
         */
        var inputStep: InputStep

        constructor() {
            timePeriod = TimePeriod()
            inputStep = InputStep.START
        }

        protected constructor(parcel: Parcel) {
            timePeriod = parcel.readParcelable(TimePeriod::class.java.classLoader)
                ?: throw RuntimeException("TimePeriod is not set in Parcel")
            inputStep = parcel.readSerializable() as InputStep?
                ?: throw RuntimeException("InputStep is not set in Parcel")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeParcelable(timePeriod, 0)
            dest.writeSerializable(inputStep)
        }

        override fun describeContents() = 0

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            @Suppress("unused")
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
     * 現在の状態
     */
    private var state: State? = null

    /**
     * 24時間表示フラグ
     */
    private var is24HourView = false

    override val contentViewResourceId: Int
        get() = R.layout.preference_time_period_input_activity

    override val firstFragment: Fragment
        get() {
            val fragment = TimePickerFragment()
            val time = Time()
            time.hour = state!!.timePeriod.start.hour
            time.minute = state!!.timePeriod.start.minute
            fragment.setTime(time)
            fragment.setIs24HourView(is24HourView)
            fragment.setScreenTitle(this.getString(R.string.preference_time_period_edit_start_title))
            return fragment
        }

    override val fragmentContainerId: Int
        get() = R.id.root

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: start")

        if (savedInstanceState == null) {
            state = State()
            val timePeriod: TimePeriod? = this.intent.getParcelableExtra(EXTRA_TIME_PERIOD)
            if (timePeriod != null) {
                state!!.timePeriod.copyFrom(timePeriod)
            }
        } else {
            state = savedInstanceState.getParcelable(STATE_KEY)
        }
        is24HourView = this.intent.getBooleanExtra(EXTRA_IS_24_HOUR_VIEW, false)
        Log.d(
            TAG,
            "onCreate: timePeriod = " + state!!.timePeriod + ", step = " + state!!.inputStep.name + ", is24 = " + is24HourView
        )

        super.onCreate(savedInstanceState)

        this.setResult(RESULT_CANCELED)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_KEY, state)
    }

    override fun setupActionBarOnFirstFragment(actionBar: ActionBar, onCreate: Boolean) {
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel_white_24dp)
    }

    override fun setupActionBarOnDescendantFragment(actionBar: ActionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menuInflater.inflate(R.menu.preference_time_period_input, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ok) {
            // OKボタンの場合
            val (hour, minute) = currentTime
            if (state!!.inputStep == InputStep.END) {
                Log.d(TAG, "onOptionsItemSelected: Complete")

                // 終了時刻を保存する
                state!!.timePeriod.end = Time(hour, minute, 0)

                // 呼び出し元画面へ遷移する
                val data = Intent()
                data.putExtra(EXTRA_TIME_PERIOD, state!!.timePeriod)
                this.setResult(RESULT_OK, data)
                finish()
            } else {
                Log.d(TAG, "onOptionsItemSelected: to EndTime")

                // 開始時刻を保存する
                state!!.timePeriod.start = Time(hour, minute, 0)

                // 終了時刻入力画面へ遷移する
                state!!.inputStep = InputStep.END
                val fragment = TimePickerFragment()
                val endTime = Time(
                    state!!.timePeriod.end.hour,
                    state!!.timePeriod.end.minute,
                    0
                )
                fragment.setTime(endTime)
                fragment.setIs24HourView(is24HourView)
                fragment.setScreenTitle(
                    this.getString(R.string.preference_time_period_edit_end_title)
                )
                replaceTo(R.id.root, fragment, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (this.supportFragmentManager.backStackEntryCount == 0) {
            Log.d(TAG, "onSupportNavigateUp: Cancel")
            finish()
            true
        } else {
            // 終了時刻入力中か?
            if (state!!.inputStep == InputStep.END) {
                Log.d(TAG, "onSupportNavigateUp: to StartTime")

                // 終了時刻入力中の場合は、終了時刻を保存する
                val (hour, minute) = currentTime
                state!!.timePeriod.end = Time(hour, minute, 0)

                // 開始時刻入力画面へ遷移する
                state!!.inputStep = InputStep.START
            }
            super.onSupportNavigateUp()
        }
    }

    private val currentTime: Time
        get() {
            val fragment = this.supportFragmentManager
                .findFragmentById(R.id.root) as TimePickerFragment?
                ?: throw RuntimeException("TimePickerFragment not found")
            return fragment.getTime()
        }

    companion object {
        /**
         * 起動パラメータ, 結果パラメータキー:時刻
         */
        val EXTRA_TIME_PERIOD = TimePeriodInputActivity::class.java.name + ".TimePeriod"

        /**
         * 起動パラメータキー:24時間表示フラグ
         */
        val EXTRA_IS_24_HOUR_VIEW = TimePeriodInputActivity::class.java.name + ".Is24HourView"

        /**
         * 起動パラメータを [Intent] へ設定します。
         *
         * @param intent       [Intent]
         * @param timePeriod   [TimePeriod] の初期値
         * @param is24HourView 24時間表示フラグ
         */
        @JvmStatic
        fun putExtras(intent: Intent, timePeriod: TimePeriod?, is24HourView: Boolean) {
            intent.putExtra(EXTRA_TIME_PERIOD, timePeriod)
            intent.putExtra(EXTRA_IS_24_HOUR_VIEW, is24HourView)
        }

        /**
         * [Activity.onActivityResult] の引数より、入力結果の
         * [TimePeriod] を取得します。
         *
         * @param data [Activity.onActivityResult] の引数である [Intent]
         * @return 入力結果の [TimePeriod]
         */
        @JvmStatic
        fun getResultTimePeriod(data: Intent): TimePeriod? {
            return data.getParcelableExtra(EXTRA_TIME_PERIOD)
        }

        /**
         * 再生成時保存データキー
         */
        private val STATE_KEY = TimePeriodInputActivity::class.java.name + ".State"

        /**
         * Tag for log
         */
        private const val TAG = "TimePeriodInputActivity"
    }
}