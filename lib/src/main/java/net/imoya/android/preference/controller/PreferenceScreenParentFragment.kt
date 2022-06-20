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

package net.imoya.android.preference.controller

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.imoya.android.dialog.DialogListener
import java.lang.ref.WeakReference

/**
 * 設定画面の親画面が [Fragment] である場合に使用する、親画面の interface
 *
 * @param fragment 親画面の [Fragment]
 */
@Suppress("unused")
open class PreferenceScreenParentFragment<T>(
    fragment: T
) : PreferenceScreenParent where T : Fragment, T : DialogListener {
    /**
     * 親画面の [Fragment]
     */
    @JvmField
    protected val fragment: WeakReference<T> = WeakReference(fragment)

    override val context: Context
        get() = requireFragment().requireContext().applicationContext

    override val listener: DialogListener?
        get() = null

    override val fragmentManager: FragmentManager
        get() = requireFragment().childFragmentManager

    fun requireFragment(): T {
        return fragment.get() ?: throw IllegalStateException("Fragment is not set")
    }

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return requireFragment().registerForActivityResult(contract, callback)
    }

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        registry: ActivityResultRegistry,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return requireFragment().registerForActivityResult(contract, registry, callback)
    }
}