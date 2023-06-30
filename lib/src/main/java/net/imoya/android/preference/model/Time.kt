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

package net.imoya.android.preference.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import java.util.Locale

/**
 * 時:分:秒によって指定される時刻モデル
 */
data class Time(
    /**
     * 時
     */
    var hour: Int = 0,
    /**
     * 分
     */
    var minute: Int = 0,
    /**
     * 秒
     */
    var second: Int = 0
) : Parcelable, Comparable<Time> {
    /**
     * 値が有効であるか否かを返します。
     *
     * 値が有効であるとは、下記をすべて満たした状態です:
     *  * [hour] が 0 以上 23 以下である
     *  * [minute] が 0 以上 59 以下である
     *  * [second] が 0 以上 59 以下である
     *
     * @return 値が有効である場合は true, その他の場合は false
     */
    fun isValid(): Boolean = hour in 0..23 && minute in 0..59 && second in 0..59

    override fun compareTo(other: Time) =
        compareValuesBy(this, other, Time::hour, Time::minute, Time::second)

    override fun toString() = String.format(Locale.US, "%d:%02d:%02d", hour, minute, second)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(hour.toByte())
        dest.writeByte(minute.toByte())
        dest.writeByte(second.toByte())
    }

    override fun describeContents() = 0

    companion object {
        /**
         * 文字列を解析して、対応する [Time] を返します。
         *
         * 文字列の書式は &quot;&lt;[hour]&gt;:&lt;[minute]&gt;:&lt;[second]&gt;&quot;
         * (例: 1:23:45, 23:45:01, 0:00:00, 9:8:7(9:08:07 と等価))
         * または &quot;&lt;[hour]&gt;:&lt;[minute]&gt;&quot;
         * (例: 1:23, 23:45, 0:00, 9:8(9:08 と等価)) です。
         *
         *  * [hour], [minute] を省略することはできません。
         *  例えば &quot;0:&quot; などは [IllegalArgumentException] となります。
         *  * [second] は省略可能で、0を指定した場合と等価となります。 (12:34 と 12:34:00 は等価)
         *  * [hour] は 0～23, [minute] および [second] は 0～59 の整数値を、10進数で記載する必要があります。
         *
         * @param s 文字列
         * @return [Time]
         * @throws IllegalArgumentException 引数 s の書式が不正です。
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun parse(s: String): Time {
            // 時と分を区切る : を探す
            val separatorPos1 = s.indexOf(':')
            require(separatorPos1 != -1) { "':' not found" }
            require(separatorPos1 != 0) { "Hour not found" }
            require(separatorPos1 < s.length - 1) { "Minutes not found" }
            // 分と秒を区切る : を探す
            val separatorPos2 = s.indexOf(':', separatorPos1 + 1)
            val hasSecond = separatorPos2 != -1 && separatorPos2 < s.length - 1
            // コロンで区切る
            val hour = s.substring(0, separatorPos1)
            val minute = if (separatorPos2 != -1) s.substring(
                separatorPos1 + 1,
                separatorPos2
            ) else s.substring(separatorPos1 + 1)
            val second = if (hasSecond) s.substring(separatorPos2 + 1) else ""

            // 時, 分, 秒を取得し、フィールドへ保存する
            val result = Time()
            try {
                result.hour = Integer.parseInt(hour, 10)
                result.minute = Integer.parseInt(minute, 10)
                result.second = if (hasSecond) Integer.parseInt(second, 10) else 0
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException(e)
            }
            require(result.isValid()) { "invalid value($s)" }
            return result
        }

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<Time> = object : Creator<Time> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): Time =
                parcel.let {
                    Time(
                        it.readByte().toInt(),
                        it.readByte().toInt(),
                        it.readByte().toInt()
                    )
                }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<Time?> = arrayOfNulls(size)
        }
    }
}