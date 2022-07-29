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
import net.imoya.android.preference.model.state.ScreenEditorState

/**
 * [ListDialogEditor], [ListFragmentEditor], [ListActivityEditor] の状態オブジェクト
 */
open class ListEditorState : ScreenEditorState {
    /**
     * 選択肢の文言リスト
     */
    var entries: Array<String> = arrayOf()

    constructor() : super()

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) : super(bundle) {
        entries = bundle.getStringArray(KEY_LABELS) ?: arrayOf()
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) : super(parcel) {
        entries =
            parcel.createStringArray() ?: throw RuntimeException("entries is null")
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeStringArray(entries)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putStringArray(KEY_LABELS, entries)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Labels
         */
        const val KEY_LABELS = "labels"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<ListEditorState> = object :
            Creator<ListEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): ListEditorState {
                return ListEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<ListEditorState?> {
                return arrayOfNulls(size)
            }
        }
    }
}