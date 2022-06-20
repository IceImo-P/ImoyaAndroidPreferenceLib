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

import androidx.fragment.app.Fragment
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.view.PreferenceView
import java.lang.ref.WeakReference

/**
 * [Fragment] へ [PreferenceView] を配置するための追加機能を実装および提供します。
 *
 * @author IceImo-P
 */
open class PreferenceFragmentController<T> :
    PreferenceScreenController() where T : Fragment, T : DialogListener {

    /**
     * [PreferenceView] を配置する [Fragment]
     */
    lateinit var fragment: WeakReference<T>

    /**
     * [Fragment.onCreate] の処理
     *
     * [Fragment.onCreate] で、このメソッドをコールしてください。
     *
     * @param fragment [PreferenceView] を配置する [Fragment]。
     * [Fragment] は [DialogListener] を実装する必要があります。
     */
    fun onCreateFragment(fragment: T) {
        this.fragment = WeakReference(fragment)

        super.onCreate()
    }

    /**
     * [PreferenceView] を配置する [Fragment] を返します。
     *
     * @return [PreferenceView] を配置する [Fragment]
     */
    protected open fun requireFragment(): T {
        return fragment.get() ?: throw IllegalStateException("Fragment is not initialized")
    }

    override fun createPreferenceScreenParent(): PreferenceScreenParent {
        return PreferenceScreenParentFragment(requireFragment())
    }
}