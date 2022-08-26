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

package net.imoya.android.preference.fragment.editor

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import net.imoya.android.fragment.roundtrip.RoundTripClientFragment
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.util.ResourceUtil

/**
 * 設定値編集 UI を持つ [Fragment] の abstract
 */
abstract class EditorFragment : RoundTripClientFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferenceLog.v(TAG, "onViewCreated: start")

        // Setup action bar
        val actionBar = if (requireActivity() is AppCompatActivity)
            (requireActivity() as AppCompatActivity).supportActionBar else null
        val fakeActionBar = this.fakeActionBar
        if (actionBar != null) {
            PreferenceLog.v(TAG, "onViewCreated: supportActionBar != null")
            PreferenceLog.d(TAG) { "onViewCreated: actionBar = $actionBar" }
            fakeActionBar?.visibility = View.GONE
            setupActionBar(actionBar)
        } else if (fakeActionBar != null) {
            PreferenceLog.v(TAG, "onViewCreated: supportActionBar == null")
            fakeActionBar.visibility = View.VISIBLE
            backButtonOnFakeActionBar?.setOnClickListener { onClickBackButton() }
            setupFakeActionBar(fakeActionBar)
        }

        // Setup menu
        (requireActivity() as MenuHost).addMenuProvider(createMenuProvider())

        // Setup bottom buttons
        val buttons: View? = buttonsView
        if (buttons != null) {
            buttons.visibility = View.VISIBLE
            okButtonOnButtonsView?.setOnClickListener { onClickOkButton() }
            cancelButtonOnButtonsView?.setOnClickListener { onClickBackButton() }
        }

        PreferenceLog.v(TAG, "onViewCreated: end")
    }

    /**
     * この [Fragment] で使用する [MenuProvider] を返します。
     *
     * @return [MenuProvider]
     */
    protected open fun createMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                this@EditorFragment.onCreateMenu(menu, menuInflater)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return this@EditorFragment.onMenuItemSelected(menuItem)
            }
        }
    }

    /**
     * この [Fragment] で使用するメニューを生成します。
     *
     * @param menu the menu to inflate the new menu items into
     * @param menuInflater the inflater to be used to inflate the updated menu
     */
    protected open fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // デフォルトではメニュー無し(HomeButton のみ)
    }

    /**
     * メニュー項目押下時の処理
     *
     * @param menuItem 押下された項目
     * @return 処理した場合は true, その他の場合は false
     */
    protected open fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                // Home ボタンは戻るボタン扱いとする
                onClickBackButton()
                true
            }
            else -> false
        }
    }

    /**
     * [ActionBar] を初期化します。
     *
     * @param actionBar [ActionBar]
     */
    abstract fun setupActionBar(actionBar: ActionBar)

    /**
     * [ActionBar] が存在しない環境で表示する、代替 UI を初期化します。
     *
     * @param fakeActionBar [ActionBar] の代替 [View]
     */
    abstract fun setupFakeActionBar(fakeActionBar: View)

    /**
     * 戻るボタン押下時の処理
     */
    protected open fun onClickBackButton() {
        parentFragmentManager.popBackStack()
    }

    /**
     * OKボタン押下時の処理
     */
    protected abstract fun onClickOkButton()

    /**
     * 疑似アクションバーの [View]
     */
    protected open val fakeActionBar: View?
        get() = view?.findViewById(R.id.fake_action_bar)

    /**
     * [fakeActionBar] 上のタイトル文言 [TextView]
     */
    protected open val titleViewOnFakeActionBar: TextView?
        get() = fakeActionBar?.findViewById(R.id.title)

    /**
     * [fakeActionBar] 上の戻るボタン
     */
    protected open val backButtonOnFakeActionBar: ImageButton?
        get() = fakeActionBar?.findViewById(R.id.back)

    /**
     * OK, Cancel ボタンが並ぶ枠の [View]
     */
    protected open val buttonsView: View?
        get() = view?.findViewById(R.id.buttons)

    /**
     * [buttonsView] 上の OK ボタン
     */
    protected open val okButtonOnButtonsView: Button?
        get() = buttonsView?.findViewById(R.id.ok)

    /**
     * [buttonsView] 上の Cancel ボタン
     */
    protected open val cancelButtonOnButtonsView: Button?
        get() = buttonsView?.findViewById(R.id.cancel)

    /**
     * 戻るボタンへ表示する画像を返します。
     *
     * @param context [Context]
     * @return 画像の [Drawable]
     */
    protected open fun backButtonImage(context: Context): Drawable {
        return actionButtonDrawable(context, R.drawable.ic_baseline_arrow_back_24)
    }

    /**
     * Returns image [Drawable] for action button
     *
     * @param context [Context]
     * @param id Resource ID of [Drawable]
     * @return [Drawable] for action button
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun actionButtonDrawable(context: Context, @DrawableRes id: Int): Drawable {
        return requireNotNull(ResourceUtil.loadDrawable(context, id))
    }

    /**
     * Returns [Bundle] of state object from savedInstanceState or arguments
     *
     * @param savedInstanceState "savedInstanceState" argument of [onCreate], [onViewCreated], etc.
     * @param key Key of state object
     * @return [Bundle] of editor state object
     * @throws IllegalStateException The [Bundle] not found in [savedInstanceState] or [getArguments]
     */
    protected open fun getEditorStateBundle(
        savedInstanceState: Bundle? = null,
        key: String = Constants.KEY_EDITOR_STATE,
    ): Bundle {
        return if (savedInstanceState != null) {
            savedInstanceState.getBundle(key)
                ?: throw IllegalStateException("$key savedInstanceState is not set")
        } else {
            arguments?.getBundle(key)
                ?: throw IllegalStateException("$key argument is not set")
        }
    }

    /**
     * Save [Bundle] of state object to InstanceState
     *
     * @param outState "outState" argument of [onSaveInstanceState]
     * @param stateBundle [Bundle] of editor state object
     * @param key Key of state object
     */
    protected open fun saveEditorStateToInstanceState(
        outState: Bundle,
        stateBundle: Bundle,
        key: String = Constants.KEY_EDITOR_STATE
    ) {
        outState.putBundle(key, stateBundle)
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "ListPreferenceEditorFragment"
    }
}