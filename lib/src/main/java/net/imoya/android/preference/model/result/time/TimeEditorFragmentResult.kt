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

package net.imoya.android.preference.model.result.time

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.os.BundleCompat
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.fragment.editor.time.TimeEditorFragment
import net.imoya.android.preference.model.result.EditorFragmentResult
import net.imoya.android.preference.model.Time

/**
 * [TimeEditorFragment] の結果オブジェクト
 */
class TimeEditorFragmentResult : EditorFragmentResult {
    /**
     * Selected time
     */
    var selectedTime: Time? = null

    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(resultCode: Int) : super(resultCode)

    constructor(resultCode: Int, selectedTime: Time) : super(resultCode) {
        this.selectedTime = selectedTime
    }

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) : super(bundle) {
        selectedTime = try {
            BundleCompat.getParcelable(bundle, KEY_SELECTED_TIME, Time::class.java)
        } catch (e: Exception) {
            PreferenceLog.d(TAG, e)
            null
        }
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()

        bundle.putParcelable(KEY_SELECTED_TIME, selectedTime)

        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Selected index
         */
        const val KEY_SELECTED_TIME = "selectedTime"

        /**
         * Tag for log
         */
        private const val TAG = "TimeEditFragmentResult"
    }
}