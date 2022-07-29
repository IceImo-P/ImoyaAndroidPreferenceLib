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

package net.imoya.android.preference.view.list

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import net.imoya.android.log.LogUtil
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 固定の選択肢より複数個を選択する設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される複数個の設定値に対応する文字列を [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView], [ListPreferenceView] に以下を加えたものとなります:
 *  * app:defaultLabel([R.attr.defaultLabel]) -
 * このビューに表示する設定値が1個も選択されていないか、設定値が未保存の場合に表示する、デフォルトの文字列を指定します。
 * この属性が存在しない場合は、固定の文字列("(Nothing)", "(選択なし)")を使用します。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [MultiSelectionListPreferenceView] を配置する際、 android:layout([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは [PreferenceView] の説明に記載された規則に従ってください。
 */
abstract class MultiSelectionListPreferenceView : ListPreferenceView {
    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : this(context, null)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    override val defaultLayout: Int
        get() = R.layout.imoya_preference_string

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    override fun onCreateChildViews() {
        super.onCreateChildViews()
        selectionView = findViewById(android.R.id.text1)
    }

    override val defaultLabelDefaultText: String
        get() = context.getString(R.string.imoya_preference_not_selected)

    /**
     * 表示を更新します。
     *
     * @param sharedPreferences [SharedPreferences]
     */
    override fun updateViews(sharedPreferences: SharedPreferences) {
        PreferenceLog.v(TAG) { "updateViews: key = $preferenceKey" }
        super.updateViews(sharedPreferences)
        val selection = getCheckedList(sharedPreferences) ?: BooleanArray(entries.size)
        PreferenceLog.v(TAG) {
            "updateViews: entries = ${LogUtil.logString(entries)}," +
                    " indices = ${LogUtil.logString(selection)}"
        }
        if (entries.size != selection.size) {
            PreferenceLog.w(TAG, "updateViews: WARN: entries.size != indices.size")
        }
        selectionView.text = getSelectionText(selection)
        invalidate()
        requestLayout()
    }

    /**
     * 選択肢リストに於いて、各項目の選択状態リストを返します。
     *
     * @param sharedPreferences [SharedPreferences]
     * @return 各項目の選択状態リスト, 未設定時は null
     */
    abstract fun getCheckedList(sharedPreferences: SharedPreferences): BooleanArray?

    /**
     * ビューへ表示する、選択中の項目を説明する文字列を返します。
     *
     * @param selection 各項目の選択状態リスト
     * @return ビューへ表示する、選択中の項目を説明する文字列
     */
    open fun getSelectionText(selection: BooleanArray): String {
        val selectedEntries = entries
            .filterIndexed { index, _ -> selection[index] }
        return if (selectedEntries.isNotEmpty()) {
            selectedEntries.joinToString()
        } else {
            defaultLabel
        }
    }

    /**
     * [SharedPreferences] 更新時の処理を行います。
     *
     * @param sharedPreferences 更新された [SharedPreferences]
     * @param key 更新された項目のキー
     * @return 処理を行った場合は true, そうでない場合は false
     */
    override fun onPreferenceChange(
        sharedPreferences: SharedPreferences, key: String
    ): Boolean {
        return if (!super.onPreferenceChange(sharedPreferences, key)
            && key == preferenceKey
        ) {
            updateViews(sharedPreferences)
            true
        } else {
            false
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "ListPreferenceView"
    }
}