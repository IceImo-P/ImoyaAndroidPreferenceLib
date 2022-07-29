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

package net.imoya.android.preference.type

/**
 * 定数: 単一項目選択時の操作方法
 */
enum class SingleSelectionType(
    /**
     * ID
     */
    val id: Int
) {

    /**
     * OK/Cancel ボタンにより確定
     */
    OK_CANCEL(0),

    /**
     * 項目クリックにより確定
     */
    ITEM_CLICK(1);

    companion object {
        /**
         * ID に対応する [SingleSelectionType] を返します。
         *
         * @param id ID
         * @return ID に対応する [SingleSelectionType]
         * @throws IllegalArgumentException 不正なIDです。
         */
        @JvmStatic
        fun from(id: Int): SingleSelectionType {
            return values().find { it.id == id } ?: throw IllegalArgumentException("Illegal ID")
        }
    }

}