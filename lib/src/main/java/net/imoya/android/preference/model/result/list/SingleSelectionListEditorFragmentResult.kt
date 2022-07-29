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

package net.imoya.android.preference.model.result.list

import android.os.Bundle
import androidx.annotation.CallSuper
import net.imoya.android.preference.controller.editor.list.ListEditorUtil
import net.imoya.android.preference.fragment.editor.list.SingleSelectionListEditorFragment
import net.imoya.android.preference.model.result.EditorFragmentResult

/**
 * [SingleSelectionListEditorFragment] の結果オブジェクト
 */
open class SingleSelectionListEditorFragmentResult : EditorFragmentResult {
    /**
     * Selected index
     */
    var selectedIndex: Int = -1

    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(resultCode: Int) : super(resultCode)

    constructor(resultCode: Int, selectedIndex: Int) : super(resultCode) {
        this.selectedIndex = selectedIndex
    }

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) : super(bundle) {
        selectedIndex = bundle.getInt(ListEditorUtil.KEY_SELECTION, -1)
    }

    @CallSuper
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()

        bundle.putInt(ListEditorUtil.KEY_SELECTION, selectedIndex)

        return bundle
    }
}