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
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.controller.editor.list.MultiSelectionListActivityEditor
import net.imoya.android.preference.controller.editor.list.MultiSelectionListDialogEditor
import net.imoya.android.preference.controller.editor.list.MultiSelectionListFragmentEditor
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [MultiSelectionListDialogEditor], [MultiSelectionListFragmentEditor],
 * [MultiSelectionListActivityEditor] の状態オブジェクト
 */
open class MultiSelectionListEditorState : ListEditorState {
    /**
     * [entries] に対応する項目の選択状態リスト
     */
    var checkedList: BooleanArray = BooleanArray(0)

    constructor() : super()

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) : super(bundle) {
        checkedList = try {
            bundle.getBooleanArray(KEY_SELECTED_INDICES)
                ?: throw IllegalArgumentException("SelectedIndices not found")
        } catch (e: Exception) {
            PreferenceLog.d(TAG, e)
            BooleanArray(0)
        }
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
        checkedList = bundle.getBooleanArray(KEY_SELECTED_INDICES) ?: BooleanArray(0)
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val bundle = Bundle()
        bundle.putBooleanArray(KEY_SELECTED_INDICES, checkedList)
        dest.writeBundle(bundle)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putBooleanArray(KEY_SELECTED_INDICES, checkedList)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : [checkedList]
         */
        const val KEY_SELECTED_INDICES = "selectedIndices"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<MultiSelectionListEditorState> = object :
            Creator<MultiSelectionListEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): MultiSelectionListEditorState {
                return MultiSelectionListEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<MultiSelectionListEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "MSelListEditState"
    }
}