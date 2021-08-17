package net.imoya.android.preference.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.model.Time.Companion.parse
import net.imoya.android.util.Log
import net.imoya.android.util.TimeUtil

/**
 * [Time] を保存する設定項目ビュー
 */
class TimePreferenceView : TimePreferenceViewBase {
    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : this(context, null)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    @Suppress("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override val valueViewText: String
        get() {
            val time = getTime(currentValue)
            Log.d(TAG, "updateViews: time = $time")
            return if (time != null) {
                this.getTimeText(time)
            } else {
                valueForNull ?: ""
            }
        }

    private fun getTime(s: String?): Time? {
        return try {
            if (s != null) parse(s) else null
        } catch (e: Exception) {
            Log.v(TAG, "getTime: Exception", e)
            null
        }
    }

    private fun getTimeText(time: Time): String {
        return TimeUtil.formatTime(
            this.context, time.hour, time.minute, is24hourView
        )
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePreferenceView"
    }
}