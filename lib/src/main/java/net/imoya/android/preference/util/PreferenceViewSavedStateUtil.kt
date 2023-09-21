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

import android.os.Bundle
import android.os.Parcel
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
    fun readBundle(parcel: Parcel, tag: String, classLoader: ClassLoader?): Bundle {
        return try {
            parcel.readBundle(classLoader)
                ?: throw IllegalStateException("parcel.readBundle returns null")
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            Bundle()
        }
    }

    @JvmStatic
    fun readByte(parcel: Parcel, tag: String, defaultValue: Byte = 0): Byte {
        return try {
            parcel.readByte()
        } catch (e: Exception) {
            PreferenceLog.d(tag, e)
            defaultValue
        }
    }
}