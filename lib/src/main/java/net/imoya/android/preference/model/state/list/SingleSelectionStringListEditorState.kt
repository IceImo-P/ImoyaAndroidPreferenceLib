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

package net.imoya.android.preference.model.state.list

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.CallSuper
import net.imoya.android.preference.controller.editor.list.ListDialogEditor
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [ListDialogEditor] の状態オブジェクト
 */
open class SingleSelectionStringListEditorState :
    SingleSelectionListEditorState {
    /**
     * 選択肢の設定値リスト
     */
    var entryValues: Array<String> = arrayOf()

    constructor() : super()

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    @Suppress("unused")
    constructor(bundle: Bundle) : super(bundle) {
        entryValues = bundle.getStringArray(KEY_ENTRY_VALUES) ?: arrayOf()
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
        entryValues = bundle.getStringArray(KEY_ENTRY_VALUES) ?: arrayOf()
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val bundle = Bundle()
        bundle.putStringArray(KEY_ENTRY_VALUES, entryValues)
        dest.writeBundle(bundle)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putStringArray(KEY_ENTRY_VALUES, entryValues)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Values
         */
        const val KEY_ENTRY_VALUES = "entryValues"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<SingleSelectionStringListEditorState> = object : Creator<SingleSelectionStringListEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): SingleSelectionStringListEditorState {
                return SingleSelectionStringListEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<SingleSelectionStringListEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "SSelStrListEditState"
    }
}