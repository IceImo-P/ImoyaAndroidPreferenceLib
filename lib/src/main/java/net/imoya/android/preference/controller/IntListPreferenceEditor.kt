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

package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogBase
import net.imoya.android.dialog.DialogParent
import net.imoya.android.preference.Constants
import net.imoya.android.preference.view.IntListPreferenceView
import net.imoya.android.preference.view.PreferenceView

/**
 * 整数値選択設定コントローラ
 *
 * [IntListPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class IntListPreferenceEditor(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent? = null,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCode: Int = Constants.DEFAULT_REQUEST_CODE
) : ListPreferenceEditor(parent, preferences, requestCode) {
    /**
     * 状態オブジェクト
     */
    protected open class State : ListPreferenceEditor.State {
        /**
         * 選択肢の設定値リスト
         */
        var entryValues: IntArray = intArrayOf()

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            entryValues =
                parcel.createIntArray() ?: throw RuntimeException("entryValues is null")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeIntArray(entryValues)
        }

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<State> = object : Creator<State> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): State {
                    return State(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<State?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is IntListPreferenceView
    }

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is IntListPreferenceView) {
            throw IllegalArgumentException("View must be IntListPreferenceView")
        }
        (state as State).entryValues = view.entryValues
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val currentPreferences = checkPreferences()
        val key = checkKey()
        val selection = data.getIntExtra(DialogBase.EXTRA_KEY_WHICH, -1)
        val entryValues = (state as State).entryValues

        if (selection == -1) {
            currentPreferences.edit().remove(key).apply()
        } else if (selection < 0 || selection >= entryValues.size) {
            throw RuntimeException(
                "Illegal selection: $selection of entries(size = ${entryValues.size})"
            )
        } else {
            currentPreferences.edit().putInt(key, entryValues[selection]).apply()
        }
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "IntListPreferenceEditor"
//    }
}