/*
 * Copyright (C) 2023 IceImo-P
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

package net.imoya.android.preference.util

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import net.imoya.android.preference.PreferenceLog

/**
 * [Parcel] より値を取得する共通ロジック
 *
 * [Parcel] に期待した値が保存されていない時に、ログを出力しつつデフォルト値を返します。
 *
 * @author IceImo-P
 */
object PreferenceViewSavedStateUtil {
    @JvmStatic
    fun createBooleanArray(parcel: Parcel, tag: String): BooleanArray? {
        return try {
            if (parcel.dataAvail() > 0) {
                parcel.createBooleanArray()
                    ?: throw IllegalStateException("parcel.createBooleanArray returns null")
            } else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            null
        }
    }

    @JvmStatic
    fun createIntArray(parcel: Parcel, tag: String): IntArray? {
        return try {
            if (parcel.dataAvail() > 0) {
                parcel.createIntArray()
                    ?: throw IllegalStateException("parcel.createBooleanArray returns null")
            } else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            null
        }
    }

    @JvmStatic
    fun createStringArray(parcel: Parcel, tag: String): Array<String>? {
        return try {
            if (parcel.dataAvail() > 0) {
                parcel.createStringArray()
                    ?: throw IllegalStateException("parcel.createStringArray returns null")
            } else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            null
        }
    }

    @JvmStatic
    fun readByte(parcel: Parcel, tag: String, defaultValue: Byte = 0): Byte {
        return try {
            if (parcel.dataAvail() > 0) parcel.readByte() else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            defaultValue
        }
    }

    @JvmStatic
    fun readInt(parcel: Parcel, tag: String, defaultValue: Int = 0): Int {
        return try {
            if (parcel.dataAvail() > 0) parcel.readInt() else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            defaultValue
        }
    }

    @JvmStatic
    fun <T: Parcelable> readParcelable(parcel: Parcel, tag: String, classLoader: ClassLoader, clazz: Class<out T>, defaultValue: T): T {
        return try {
            if (parcel.dataAvail() > 0) {
                ParcelCompat.readParcelable(parcel, classLoader, clazz)
                    ?: throw IllegalArgumentException("TimePeriod not found")
            } else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            defaultValue
        }
    }

    @JvmStatic
    fun readString(parcel: Parcel, tag: String): String {
        return try {
            if (parcel.dataAvail() > 0) {
                parcel.readString()
                    ?: throw IllegalStateException("parcel.readString returns null")
            } else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            ""
        }
    }

    @JvmStatic
    fun readStringOrNull(parcel: Parcel, tag: String): String? {
        return try {
            if (parcel.dataAvail() > 0) parcel.readString() else throw IllegalStateException("parcel is empty")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            null
        }
    }
}