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

package net.imoya.android.preference.controller.editor.list

import android.content.SharedPreferences

/**
 * 一覧選択設定ユーティリティー
 */
object ListEditorUtil {
    /**
     * Key of extra, state, result, etc.: Selection
     */
    const val KEY_SELECTION = "listSelection"

    /**
     * 一覧より選択された1項目の値を、 [SharedPreferences] へ保存します。
     *
     * @param preferences [SharedPreferences] to write key and value
     * @param key The key of preference
     * @param entrySize Count of list items
     * @param selection ユーザーが選択した項目の位置(0 to [entrySize]-1)。ユーザーが何も選択しなかった場合は -1
     * @param saveValue ユーザーが1項目を選択した場合にコールされる、コールバック関数
     */
    @JvmStatic
    fun saveInput(
        preferences: SharedPreferences,
        key: String,
        entrySize: Int,
        selection: Int,
        saveValue: () -> Unit
    ) {
        if (selection == -1) {
            preferences.edit().remove(key).apply()
        } else if (selection < 0 || selection >= entrySize) {
            throw RuntimeException(
                "Illegal selection: $selection of entries(size = $entrySize)"
            )
        } else {
            saveValue()
        }
    }

    /**
     * 一覧より選択された1項目の値を、 [SharedPreferences] へ保存します。
     *
     * @param preferences [SharedPreferences] to write key and value
     * @param key The key of preference
     * @param entryValues The list of values
     * @param selection ユーザーが選択した項目の位置(0 to [entryValues].size-1)
     */
    @JvmStatic
    fun saveInput(
        preferences: SharedPreferences,
        key: String,
        entryValues: IntArray,
        selection: Int
    ) {
        saveInput(preferences, key, entryValues.size, selection) {
            preferences.edit().putInt(key, entryValues[selection]).apply()
        }
    }

    /**
     * 一覧より選択された1項目の値を、 [SharedPreferences] へ保存します。
     *
     * @param preferences [SharedPreferences] to write key and value
     * @param key The key of preference
     * @param entryValues The list of values
     * @param selection ユーザーが選択した項目の位置(0 to [entryValues].size-1)
     */
    @JvmStatic
    fun saveInput(
        preferences: SharedPreferences,
        key: String,
        entryValues: Array<String>,
        selection: Int
    ) {
        saveInput(preferences, key, entryValues.size, selection) {
            preferences.edit().putString(key, entryValues[selection]).apply()
        }
    }

    /**
     * 一覧より選択された複数項目の値を、 [SharedPreferences] へ保存します。
     *
     * @param preferences [SharedPreferences] to write key and value
     * @param key The key of preference
     * @param entrySize Count of list items
     * @param checkedList リストの項目に対応する、選択有無のリスト
     * @param saveValue ユーザーが1項目以上を選択した場合にコールされる、コールバック関数
     */
    @JvmStatic
    fun saveInput(
        preferences: SharedPreferences,
        key: String,
        entrySize: Int,
        checkedList: BooleanArray,
        saveValue: () -> Unit
    ) {
        if (checkedList.size != entrySize) {
            throw RuntimeException(
                "Illegal size: checkedList.size = ${checkedList.size}, entries.size = $entrySize"
            )
        }
        if (!checkedList.any { it }) {
            preferences.edit().remove(key).apply()
        } else {
            saveValue()
        }
    }

    /**
     * 一覧より選択された複数項目の値を、カンマ区切りの文字列形式で [SharedPreferences] へ保存します。
     *
     * @param preferences [SharedPreferences] to write key and value
     * @param key The key of preference
     * @param entryValues The list of values
     * @param checkedList リストの項目に対応する、選択有無のリスト
     * @param createPreferenceValue 実際に [SharedPreferences] へ保存する値を生成する関数
     */
    @JvmStatic
    fun saveInput(
        preferences: SharedPreferences,
        key: String,
        entryValues: IntArray,
        checkedList: BooleanArray,
        createPreferenceValue: (IntArray, BooleanArray) -> String
    ) {
        saveInput(preferences, key, entryValues.size, checkedList) {
            preferences
                .edit()
                .putString(key, createPreferenceValue(entryValues, checkedList))
                .apply()
        }
    }

    /**
     * 一覧より選択された複数項目の値を、文字列形式で [SharedPreferences] へ保存します。
     *
     * @param preferences [SharedPreferences] to write key and value
     * @param key The key of preference
     * @param entryValues The list of values
     * @param checkedList リストの項目に対応する、選択有無のリスト
     * @param createPreferenceValue 実際に [SharedPreferences] へ保存する値を生成する関数
     */
    @JvmStatic
    fun saveInput(
        preferences: SharedPreferences,
        key: String,
        entryValues: Array<String>,
        checkedList: BooleanArray,
        createPreferenceValue: (Array<String>, BooleanArray) -> String
    ) {
        saveInput(preferences, key, entryValues.size, checkedList) {
            preferences
                .edit()
                .putString(key, createPreferenceValue(entryValues, checkedList))
                .apply()
        }
    }

    /**
     * 一覧より選択された複数項目の値を、 [SharedPreferences] へ保存する文字列形式へ変換します。
     *
     * @param entryValues The list of values
     * @param checkedList リストの項目に対応する、選択有無のリスト
     * @return [SharedPreferences] へ保存する文字列
     */
    @JvmStatic
    fun createPreferenceValue(entryValues: IntArray, checkedList: BooleanArray): String {
        return entryValues.filterIndexed { index, _ -> checkedList[index] }
            .joinToString(separator = ",") { it.toString() }
    }

    /**
     * 一覧より選択された複数項目の値を、 [SharedPreferences] へ保存する文字列形式へ変換します。
     *
     * @param entryValues The list of values
     * @param checkedList リストの項目に対応する、選択有無のリスト
     * @return [SharedPreferences] へ保存する文字列
     */
    @JvmStatic
    fun createPreferenceValue(entryValues: Array<String>, checkedList: BooleanArray): String {
        return entryValues.filterIndexed { index, _ -> checkedList[index] }
            .joinToString(separator = ",")
    }
}