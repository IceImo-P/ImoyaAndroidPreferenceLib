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

package net.imoya.android.preference.model.state.list

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.CallSuper
import net.imoya.android.preference.controller.editor.list.ListActivityEditor
import net.imoya.android.preference.controller.editor.list.ListDialogEditor
import net.imoya.android.preference.controller.editor.list.ListFragmentEditor
import net.imoya.android.preference.type.SingleSelectionType
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [ListDialogEditor], [ListFragmentEditor], [ListActivityEditor] の状態オブジェクト
 */
open class SingleSelectionListEditorState : ListEditorState {
    /**
     * 選択済みのインデックス位置, 未選択の場合は [NO_SELECTION] (-1)
     */
    var selectedIndex: Int = NO_SELECTION

    /**
     * 単一項目選択時の操作方法
     */
    var singleSelectionType: SingleSelectionType = SingleSelectionType.OK_CANCEL

    constructor() : super()

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) : super(bundle) {
        selectedIndex = bundle.getInt(KEY_SELECTED_INDEX, NO_SELECTION)
        singleSelectionType = SingleSelectionType.from(bundle.getInt(KEY_SINGLE_SELECTION_TYPE))
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        val bundle = PreferenceViewSavedStateUtil.readBundle(parcel, TAG, javaClass.classLoader)
        selectedIndex = bundle.getInt(KEY_SELECTED_INDEX, NO_SELECTION)
        singleSelectionType = SingleSelectionType.from(
            bundle.getInt(KEY_SINGLE_SELECTION_TYPE, SingleSelectionType.OK_CANCEL.id)
        )
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        val bundle = Bundle()
        bundle.putInt(KEY_SELECTED_INDEX, selectedIndex)
        bundle.putInt(KEY_SINGLE_SELECTION_TYPE, singleSelectionType.id)
        dest.writeBundle(bundle)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putInt(KEY_SELECTED_INDEX, selectedIndex)
        bundle.putInt(KEY_SINGLE_SELECTION_TYPE, singleSelectionType.id)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Selected index
         */
        const val KEY_SELECTED_INDEX = "selIdx"

        /**
         * Key at [Bundle] : 単一項目選択時の操作方法
         */
        const val KEY_SINGLE_SELECTION_TYPE = "singleSelType"

        /**
         * [selectedIndex] value for no selection
         */
        const val NO_SELECTION = -1

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<SingleSelectionListEditorState> = object :
            Creator<SingleSelectionListEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): SingleSelectionListEditorState {
                return SingleSelectionListEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<SingleSelectionListEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "SSelListEditState"
    }
}