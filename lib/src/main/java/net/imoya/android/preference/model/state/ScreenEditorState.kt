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
import androidx.annotation.CallSuper
import net.imoya.android.preference.controller.editor.ScreenEditor
import net.imoya.android.preference.util.PreferenceViewSavedStateUtil

/**
 * [ScreenEditor] の状態オブジェクト
 */
open class ScreenEditorState : Parcelable {
    /**
     * Preference key
     */
    var key: String? = null

    /**
     * Preference title
     */
    var title: String? = null

    constructor()

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) {
        key = bundle.getString(KEY_KEY)
        title = bundle.getString(KEY_TITLE)
    }

    /**
     * [Parcel] の内容で初期化するコンストラクタ
     *
     * @param parcel [Parcel]
     */
    protected constructor(parcel: Parcel) {
        key = PreferenceViewSavedStateUtil.readString(parcel, TAG)
        title = PreferenceViewSavedStateUtil.readString(parcel, TAG)
    }

    /**
     * 指定の [Parcel] へ、このオブジェクトの内容を保存します。
     *
     * @param dest [Parcel]
     * @param flags  フラグ
     */
    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(key)
        dest.writeString(title)
    }

    override fun describeContents() = 0

    /**
     * このオブジェクトの内容をコピーした [Bundle] を返します。
     *
     * @return このオブジェクトの内容をコピーした [Bundle]
     */
    @CallSuper
    open fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(KEY_KEY, key)
        bundle.putString(KEY_TITLE, title)
        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Preference key
         */
        const val KEY_KEY = "key"

        /**
         * Key at [Bundle] : Preference title
         */
        const val KEY_TITLE = "title"

        /**
         * [Parcelable] 対応用 [Creator]
         */
        @JvmField
        val CREATOR: Creator<ScreenEditorState> = object :
            Creator<ScreenEditorState> {
            /**
             * [Parcel] の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel [Parcel]
             * @return [Parcel] の内容を保持するオブジェクト
             */
            override fun createFromParcel(parcel: Parcel): ScreenEditorState {
                return ScreenEditorState(parcel)
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            override fun newArray(size: Int): Array<ScreenEditorState?> {
                return arrayOfNulls(size)
            }
        }

        /**
         * Tag for log
         */
        private const val TAG = "ScreenEditorState"
    }
}