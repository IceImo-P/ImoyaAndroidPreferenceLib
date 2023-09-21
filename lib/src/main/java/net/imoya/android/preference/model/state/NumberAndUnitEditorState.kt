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

package net.imoya.android.preference.model.state

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.preference.controller.editor.NumberAndUnitDialogEditor
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [NumberAndUnitDialogEditor] の状態オブジェクト
 */
open class NumberAndUnitEditorState : ScreenEditorState {
    /**
     * デフォルト値
     */
    var defaultValue = 0

    /**
     * 設定可能な最小値
     */
    var minValue = Int.MIN_VALUE

    /**
     * 設定可能な最大値
     */
    var maxValue = Int.MAX_VALUE

    /**
     * 単位
     */
    var unit: String? = null

    /**
     * 入力欄のヒント文字列
     */
    var hint: String? = null

    constructor() : super()

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
        defaultValue = bundle.getInt(KEY_DEFAULT_VALUE, 0)
        minValue = bundle.getInt(KEY_MIN_VALUE, Int.MIN_VALUE)
        maxValue = bundle.getInt(KEY_MAX_VALUE, Int.MAX_VALUE)
        unit = bundle.getString(KEY_UNIT)
        hint = bundle.getString(KEY_HINT)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val bundle = Bundle()
        bundle.putInt(KEY_DEFAULT_VALUE, defaultValue)
        bundle.putInt(KEY_MIN_VALUE, minValue)
        bundle.putInt(KEY_MAX_VALUE, maxValue)
        bundle.putString(KEY_UNIT, unit)
        bundle.putString(KEY_HINT, hint)
        dest.writeBundle(bundle)
    }

    companion object {
        /**
         * Key at [Bundle] : [defaultValue]
         */
        const val KEY_DEFAULT_VALUE = "def"

        /**
         * Key at [Bundle] : [minValue]
         */
        const val KEY_MIN_VALUE = "min"

        /**
         * Key at [Bundle] : [maxValue]
         */
        const val KEY_MAX_VALUE = "max"

        /**
         * Key at [Bundle] : [unit]
         */
        const val KEY_UNIT = "unit"

        /**
         * Key at [Bundle] : [hint]
         */
        const val KEY_HINT = "hint"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<NumberAndUnitEditorState> = object :
            Creator<NumberAndUnitEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): NumberAndUnitEditorState {
                return NumberAndUnitEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<NumberAndUnitEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "NumAndUnitEditorState"
    }
}