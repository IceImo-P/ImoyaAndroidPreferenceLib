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

package net.imoya.android.preference.model.result

import android.os.Bundle
import androidx.annotation.CallSuper
import net.imoya.android.fragment.roundtrip.RoundTripClientFragment

/**
 * 編集画面である [RoundTripClientFragment] が返す結果のモデル
 */
open class EditorFragmentResult {
    /**
     * Result code
     */
    var resultCode: Int = 0

    constructor()

    constructor(resultCode: Int) {
        this.resultCode = resultCode
    }

    /**
     * [Bundle] の内容で初期化するコンストラクタ
     *
     * @param bundle [Bundle]
     */
    constructor(bundle: Bundle) {
        resultCode = bundle.getInt(KEY_RESULT_CODE)
    }

    /**
     * このオブジェクトの内容をコピーした [Bundle] を返します。
     *
     * @return このオブジェクトの内容をコピーした [Bundle]
     */
    @CallSuper
    open fun toBundle(): Bundle {
        val bundle = Bundle()

        bundle.putInt(KEY_RESULT_CODE, resultCode)

        return bundle
    }

    companion object {
        /**
         * Key at [Bundle] : Result code
         */
        const val KEY_RESULT_CODE = "imoya-preference-result-code"
    }
}