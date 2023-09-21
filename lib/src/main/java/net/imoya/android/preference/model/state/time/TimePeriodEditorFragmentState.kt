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

package net.imoya.android.preference.model.state.time

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.CallSuper
import androidx.core.os.BundleCompat
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.fragment.editor.time.TimePeriodEditorFragment
import net.imoya.android.preference.model.TimePeriod
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [TimePeriodEditorFragment] の状態オブジェクト
 */
open class TimePeriodEditorFragmentState : Parcelable {
    /**
     * 編集ステップ
     */
    enum class Step(val value: Int) {
        /** 開始時刻 */
        START_TIME(0),
        /** 終了時刻 */
        END_TIME(1);

        companion object {
            @JvmStatic
            fun from(value: Int): Step {
                return values().find { it.value == value }
                    ?: throw IllegalArgumentException("Illegal value")
            }
        }
    }

    /**
     * 編集中の [TimePeriod]
     */
    var timePeriod: TimePeriod

    /**
     * 編集ステップ
     */
    var step: Step

    /**
     * 指定した値で初期化するコンストラクタ
     */
    constructor(timePeriod: TimePeriod, step: Step = Step.START_TIME) {
        this.timePeriod = timePeriod
        this.step = step
    }

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) {
        timePeriod = getTimePeriodFrom(bundle)
        step = Step.from(bundle.getInt(KEY_STEP, Step.START_TIME.value))
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) {
        val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
        step = Step.from(bundle.getInt(KEY_STEP, Step.START_TIME.value))
        timePeriod = getTimePeriodFrom(bundle)
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        val bundle = Bundle()
        bundle.putInt(KEY_STEP, step.value)
        bundle.putParcelable(KEY_TIME_PERIOD, timePeriod)
        dest.writeBundle(bundle)
    }

    override fun describeContents(): Int = 0

    @CallSuper
    open fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(KEY_TIME_PERIOD, timePeriod)
        bundle.putInt(KEY_STEP, step.value)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Time
         */
        const val KEY_TIME_PERIOD = "per"

        /**
         * Key at [Bundle] : 編集ステップ
         */
        const val KEY_STEP = "st"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<TimePeriodEditorFragmentState> = object :
            Creator<TimePeriodEditorFragmentState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): TimePeriodEditorFragmentState {
                return TimePeriodEditorFragmentState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<TimePeriodEditorFragmentState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "TPEditFragmentState"

        /**
         * Get [timePeriod] value from state [Bundle]
         *
         * @param bundle [Bundle]
         * @return [TimePeriod]
         */
        private fun getTimePeriodFrom(bundle: Bundle): TimePeriod {
            return try {
                BundleCompat.getParcelable(bundle, KEY_TIME_PERIOD, TimePeriod::class.java)
                    ?: throw IllegalArgumentException("TimePeriod not found")
            } catch (e: Exception) {
                PreferenceLog.d(TAG, e)
                TimePeriod()
            }
        }
    }
}