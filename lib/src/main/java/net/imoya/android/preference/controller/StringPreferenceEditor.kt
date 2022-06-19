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
import android.text.InputType
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.InputDialog
import net.imoya.android.dialog.TextInputDialog
import net.imoya.android.preference.Constants
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.StringPreferenceView

/**
 * 文字列設定値編集コントローラ
 *
 * [StringPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class StringPreferenceEditor(
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
         * 入力欄のヒント文字列
         */
        var hint: String? = null

        /**
         * 入力文字タイプ
         */
        var inputType = InputType.TYPE_CLASS_TEXT

        /**
         * 最大入力可能文字数
         */
        var maxLength = Int.MAX_VALUE

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            hint = parcel.readString()
            inputType = parcel.readInt()
            maxLength = parcel.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(hint)
            dest.writeInt(inputType)
            dest.writeInt(maxLength)
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

    fun setInputType(inputType: Int) {
        (state as State).inputType = inputType
    }

    fun setHint(hint: String?) {
        (state as State).hint = hint
    }

    override fun isCompatibleView(view: PreferenceView): Boolean {
        return view is StringPreferenceView
    }

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: PreferenceView) {
        super.setupState(view)
        if (view !is StringPreferenceView) {
            throw IllegalArgumentException("View must be StringPreferenceView")
        }
        (state as State).maxLength = view.maxLength
    }

    override fun showDialog(view: PreferenceView) {
        val currentPreferences = checkPreferences()
        val currentParent = checkParent()
        val key = checkKey()
        if (view !is StringPreferenceView) {
            throw IllegalArgumentException("View must be StringPreferenceView")
        }
        checkAndWarnRequestCode()
        val currentState = state as State

        TextInputDialog.Builder(currentParent, requestCode)
            .setTitle(view.title ?: "")
            .setInputType(currentState.inputType)
            .setMaxLength(currentState.maxLength)
            .setHint(currentState.hint)
            .setText(currentPreferences.getString(key, "") ?: "")
            .setTag(key)
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val currentPreferences = checkPreferences()
        val key = checkKey()
        val value = data.getStringExtra(InputDialog.EXTRA_KEY_INPUT_VALUE)
        currentPreferences.edit().putString(key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "StringPreferenceEditor"
    }
}