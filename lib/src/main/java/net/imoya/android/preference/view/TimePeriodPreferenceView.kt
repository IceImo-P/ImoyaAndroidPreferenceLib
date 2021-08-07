package net.imoya.android.preference.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import net.imoya.android.preference.R
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.model.TimePeriod.Companion.parse
import net.imoya.android.util.Log
import net.imoya.android.util.TimeUtil.formatTime

/**
 * [TimePeriod] を保存する設定項目ビュー
 */
class TimePeriodPreferenceView : TimePreferenceViewBase {
    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : super(context)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    override val valueViewText: String
        get() {
            val period = getTimePeriod(currentValue)
            Log.d(TAG, "updateViews: period = $period")
            return if (period != null) {
                getTimePeriodText(period)
            } else {
                valueForNull ?: ""
            }
        }

    private fun getTimePeriod(s: String?): TimePeriod? {
        return try {
            if (s != null) parse(s) else null
        } catch (e: Exception) {
            Log.v(TAG, "getTimePeriod: Exception", e)
            null
        }
    }

    private fun getTimePeriodText(period: TimePeriod): String {
        val context = this.context
        val format = context.getString(
            R.string.preference_time_period_value_format
        )
        val hour24 = is24hourView
        return format.replace(
            "#start#",
            formatTime(
                context, period.start.hour, period.start.minute, hour24
            )
        )
            .replace(
                "#end#",
                formatTime(
                    context,
                    period.end.hour, period.end.minute, hour24
                )
            )
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePeriodPreferenceView"
    }
}