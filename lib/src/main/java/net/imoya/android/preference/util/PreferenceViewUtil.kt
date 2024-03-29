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

package net.imoya.android.preference.util

import net.imoya.android.preference.controller.editor.DialogEditor
import net.imoya.android.preference.controller.editor.PreferenceEditor
import net.imoya.android.preference.view.PreferenceView

@Suppress("unused")
object PreferenceViewUtil {
    /**
     * 設定項目のビューと、ビューをタップした時の処理の連携を初期化します。
     *
     * @param view     設定項目のビュー([PreferenceView])
     * @param editor   ビューをタップした時の処理を実装する [PreferenceEditor]
     * @param outViews 画面に表示する [PreferenceView] のリスト。本メソッドをコールすると、リストへ
     *                 [view] が追加されます。
     */
    @JvmStatic
    fun setupView(
        view: PreferenceView, editor: PreferenceEditor,
        outViews: MutableList<PreferenceView>
    ) {
        if (editor is DialogEditor) {
            editor.registerDialogCallback()
        }
        editor.attach(view)
        outViews.add(view)
    }
}