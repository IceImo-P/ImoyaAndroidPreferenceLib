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

package net.imoya.android.preference.model.state

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.InputType
import net.imoya.android.preference.controller.editor.StringDialogEditor
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [StringDialogEditor] の状態オブジェクト
 */
open class StringEditorState : ScreenEditorState {
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
        val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
        hint = bundle.getString(KEY_HINT)
        inputType = bundle.getInt(KEY_INPUT_TYPE, InputType.TYPE_CLASS_TEXT)
        maxLength = bundle.getInt(KEY_MAX_LENGTH, Int.MAX_VALUE)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val bundle = Bundle()
        bundle.putString(KEY_HINT ,hint)
        bundle.putInt(KEY_INPUT_TYPE, inputType)
        bundle.putInt(KEY_MAX_LENGTH, maxLength)
        dest.writeBundle(bundle)
    }

    companion object {
        /**
         * Key at [Bundle] : [hint]
         */
        const val KEY_HINT = "hint"

        /**
         * Key at [Bundle] : [inputType]
         */
        const val KEY_INPUT_TYPE = "inTyp"

        /**
         * Key at [Bundle] : [maxLength]
         */
        const val KEY_MAX_LENGTH = "maxLen"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<StringEditorState> = object :
            Creator<StringEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): StringEditorState {
                return StringEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<StringEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "StringEditorState"
    }
}
