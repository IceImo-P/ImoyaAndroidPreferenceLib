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

package net.imoya.android.preference.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * 時:分によって指定される両端を持つ期間モデル
 */
data class TimePeriod(
    /**
     * 開始時刻
     */
    var start: Time = Time(),
    /**
     * 終了時刻
     */
    var end: Time = Time()
) : Parcelable {
    /**
     * 値が有効であるか否かを返します。
     *
     * 値が有効であるとは [start], [end] いずれも有効である状態です。
     *
     * [Time] が有効である定義は [Time.isValid] を参照してください。
     *
     * @return 値が有効である場合は true, その他の場合は false
     * @see [Time.isValid]
     */
    @Suppress("unused")
    fun isValid(): Boolean = start.isValid() && end.isValid()

    /**
     * 指定の時刻が、範囲内であるか否かを返します。
     *
     * @param time 時刻
     * @return 指定の時刻が範囲内の場合は true, 範囲外である場合は false
     * @throws IllegalArgumentException [time] が有効ではありません([Time.isValid] が false を返しました)
     * @throws IllegalStateException この [TimePeriod] が有効ではありません([isValid] が false を返しました)
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun isInPeriod(time: Time): Boolean {
        require(time.isValid()) { "Illegal time($time)" }
        if (!isValid()) throw IllegalStateException("invalid")

        // start == end ならばその1点がこのインスタンスが表す範囲である。
        // start < end ならばその間(start, endを含む)がこのインスタンスが表す範囲である。
        // start > end ならばその間以外(start, endを含む)がこのインスタンスが表す範囲である。
        return when {
            start == end -> time == end
            start < end -> time in start..end
            else /* equivalent to start > end */ -> time >= start || time <= end
        }
    }

    /**
     * 指定の時刻が、範囲内であるか否かを返します。
     *
     * @param hourOfDay 時(0～23)
     * @param minute    分(0～59)
     * @param second    秒(0～59)
     * @return 指定の時刻が範囲内の場合は true, 範囲外である場合は false
     */
    @Suppress("unused")
    fun isInPeriod(hourOfDay: Int, minute: Int, second: Int = 0): Boolean {
        return isInPeriod(Time(hourOfDay, minute, second))
    }

    /**
     * 他の [TimePeriod] インスタンスの値のコピーをこのインスタンスに設定します。
     *
     * @param other 他のインスタンス
     */
    fun copyFrom(other: TimePeriod) {
        this.start = other.start.copy()
        this.end = other.end.copy()
    }

    override fun toString() = "$start-$end"

    override fun writeToParcel(dest: Parcel, flags: Int) {
        start.writeToParcel(dest, flags)
        end.writeToParcel(dest, flags)
    }

    override fun describeContents() = 0

    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "TimePeriod"

        /**
         * 文字列を解析して、対応する [TimePeriod] を返します。
         *
         * 文字列の書式は
         * &quot;&lt;Start&gt;-&lt;End&gt;&quot;
         * (例: 0:00:00-1:23:45, 3:4-5:6, 23:59-0:01)です。
         *
         *  * Start, End の省略はできません。
         *  * Start, End は [Time] の文字列形式です。書式は [Time.parse] のドキュメントをご参照ください。
         *  * Start と End の大小関係はチェックされません。
         *  Start, End が有効であれば([Time.isValid] が true を返せば)どのような大小関係も有効です。
         *
         * @param s 文字列
         * @return [TimePeriod]
         * @throws IllegalArgumentException 引数 s の書式が不正です。
         * @see [Time.parse]
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun parse(s: String): TimePeriod {
            // ハイフンの位置を探す
            val separatorPos = s.indexOf('-')
            require(separatorPos != -1) { "'-' not found" }
            require(separatorPos != 0) { "Start time not found" }
            require(separatorPos < s.length - 1) { "End time not found" }
            // ハイフンで区切る
            val start = s.substring(0, separatorPos)
            val end = s.substring(separatorPos + 1)

            // それぞれの時:分(:秒)を取得し、フィールドへ保存する
            return TimePeriod(Time.parse(start), Time.parse(end))
        }

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<TimePeriod> = object : Creator<TimePeriod> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): TimePeriod =
                TimePeriod(
                    Time.CREATOR.createFromParcel(parcel),
                    Time.CREATOR.createFromParcel(parcel)
                )

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<TimePeriod?> {
                return arrayOfNulls(size)
            }
        }
    }
}