/*
 * Copyright (C) 2022-2023 IceImo-P
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

package net.imoya.android.preference.view.time

import android.content.Context
import android.util.AttributeSet
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.model.Time
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
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override val valueViewText: String
        get() {
            val time = getTime(currentValue)
            PreferenceLog.v(TAG) { "updateViews: time = $time" }
            return if (time != null) {
                this.getTimeText(time)
            } else {
                valueForNull ?: ""
            }
        }

    private fun getTime(s: String?): Time? {
        return try {
            if (!s.isNullOrEmpty()) Time.parse(s) else null
        } catch (e: Exception) {
            PreferenceLog.v(TAG, "getTime: Exception", e)
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