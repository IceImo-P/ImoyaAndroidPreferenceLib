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

import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.SingleChoiceDialog
import net.imoya.android.log.LogUtil
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.view.ListPreferenceView
import net.imoya.android.preference.view.PreferenceView

abstract class ListPreferenceEditor(
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
) : DialogPreferenceEditor(parent, preferences, requestCode) {
    /**
     * 状態オブジェクト
     */
    protected open class State : PreferenceEditor.State {
        /**
         * 選択肢の文言リスト
         */
        var entries: Array<String> = arrayOf()

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            entries =
                parcel.createStringArray() ?: throw RuntimeException("entries is null")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeStringArray(entries)
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

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is ListPreferenceView) {
            throw IllegalArgumentException("View must be ListPreferenceView")
        }
        (state as State).entries = view.entries
    }

    override fun showDialog(view: PreferenceView) {
        val currentPreferences = checkPreferences()
        val currentParent = checkParent()
        val key = checkKey()
        if (view !is ListPreferenceView) {
            throw IllegalArgumentException("View must be ListPreferenceView")
        }
        checkAndWarnRequestCode()
        val entries: Array<String> = (state as State).entries
        val selectedIndex = view.getSelectedIndex(currentPreferences)

        PreferenceLog.v(TAG) {
            "showDialog: title = ${view.title}, entries = ${LogUtil.logString(entries)}" +
                    ", selectedIndex = $selectedIndex, key = $key"
        }

        SingleChoiceDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setItems(entries)
            .setSelectedPosition(selectedIndex)
            .setTag(key)
            .show()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "ListPreferenceEditor"
    }
}