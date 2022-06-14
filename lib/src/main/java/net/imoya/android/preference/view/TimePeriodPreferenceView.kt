/*
 * Copyright (C) 2022 IceImo-P
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.imoya.android.preference.view

import android.content.Context
import android.util.AttributeSet
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.model.TimePeriod
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
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override val valueViewText: String
        get() {
            val period = getTimePeriod(mCurrentValue)
            PreferenceLog.v(TAG) { "updateViews: period = $period" }
            return if (period != null) {
                getTimePeriodText(period)
            } else {
                mValueForNull ?: ""
            }
        }

    private fun getTimePeriod(s: String?): TimePeriod? {
        return try {
            if (s != null) TimePeriod.parse(s) else null
        } catch (e: Exception) {
            PreferenceLog.v(TAG, "getTimePeriod: Exception", e)
            null
        }
    }

    private fun getTimePeriodText(period: TimePeriod): String {
        val context = this.context
        val format = context.getString(
            R.string.imoya_preference_time_period_value_format
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