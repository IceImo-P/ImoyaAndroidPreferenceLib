package net.imoya.android.preference.fragment

import net.imoya.android.util.Log
import net.imoya.android.fragment.BaseFragment
import android.widget.TimePicker
import android.os.Bundle
import net.imoya.android.util.TimePickerHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.imoya.android.preference.R
import net.imoya.android.preference.model.Time

/**
 * [TimePicker] を表示する [Fragment]
 */
class TimePickerFragment : BaseFragment() {
    /**
     * 時刻
     */
    private val time = Time()

    /**
     * [TimePicker]
     */
    private lateinit var picker: TimePicker

    /**
     * 24時間表示フラグを設定します。
     *
     * @param is24HourView 24時間表示フラグ
     */
    fun setIs24HourView(is24HourView: Boolean) {
        val original = this.arguments
        val arguments = original ?: Bundle()
        arguments.putBoolean(ARGUMENT_IS_24_HOUR_VIEW, is24HourView)
        if (original == null) {
            this.arguments = arguments
        }
    }

    /**
     * [TimePicker] の時刻を取得します。
     *
     * @return 時刻
     */
    fun getTime(): Time {
        loadTime()
        return Time(time.hour, time.minute, time.second)
    }

    /**
     * [TimePicker] の時刻を設定します。
     *
     * @param time 時刻
     */
    fun setTime(time: Time) {
        val original = this.arguments
        val arguments = original ?: Bundle()
        arguments.putParcelable(ARGUMENT_TIME, time)
        if (original == null) {
            this.arguments = arguments
        }
    }

    /**
     * 画面のタイトル文言を設定します。
     *
     * @param title 画面のタイトル文言
     */
    fun setScreenTitle(title: String) {
        val original = this.arguments
        val arguments = original ?: Bundle()
        arguments.putString(ARGUMENT_TITLE, title)
        if (original == null) {
            this.arguments = arguments
        }
    }

    private fun loadTime() {
        val pickerHelper = TimePickerHelper(picker)
        time.hour = pickerHelper.getHour()
        time.minute = pickerHelper.getMinute()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        loadTime()
        outState.putParcelable(ARGUMENT_TIME, time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val time: Time = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(ARGUMENT_TIME)
                ?: throw RuntimeException("savedInstanceState[$ARGUMENT_TIME] is not set")
        } else {
            requireArguments().getParcelable(ARGUMENT_TIME)
                ?: throw RuntimeException("arguments[$ARGUMENT_TIME] is not set")
        }
        Log.d(TAG, "onCreate: time = $time")
        this.time.hour = time.hour
        this.time.minute = time.minute
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.preference_time_input_fragment, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        picker = view.findViewById(R.id.time)
        TimePickerHelper(picker).setHourAndMinute(time.hour, time.minute)
        picker.setIs24HourView(requireArguments().getBoolean(ARGUMENT_IS_24_HOUR_VIEW))
        val title = requireArguments().getString(ARGUMENT_TITLE)
        Log.d(TAG, "onViewCreated: title = $title")
        this.setTitle(title)
    }

    companion object {
        /**
         * 再生成時保存データキー:時刻
         */
        private val ARGUMENT_TIME = TimePickerFragment::class.java.name + ".CurrentTime"

        /**
         * 再生成時保存データキー:24時間表示フラグ
         */
        private val ARGUMENT_IS_24_HOUR_VIEW = TimePickerFragment::class.java.name + ".Is24HourView"

        /**
         * 再生成時保存データキー:タイトル文言
         */
        private val ARGUMENT_TITLE = TimePickerFragment::class.java.name + ".Title"

        /**
         * Tag for log
         */
        private const val TAG = "TimePickerFragment"
    }
}