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

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import net.imoya.android.dialog.DialogListener
import java.lang.ref.WeakReference

/**
 * 設定画面の親画面が [Fragment] である場合に使用する、親画面の interface
 *
 * @param fragment 親画面の [Fragment]
 */
@Suppress("unused")
open class PreferenceScreenParentFragment(
    fragment: Fragment,
    listener: DialogListener
) : PreferenceScreenParent {
    /**
     * 親画面の [Fragment]
     */
    @JvmField
    protected val fragmentRef: WeakReference<Fragment> = WeakReference(fragment)

    /**
     * [DialogListener]
     */
    @JvmField
    protected val dialogListenerRef: WeakReference<DialogListener> = WeakReference(listener)

    override val context: Context
        get() = requireFragment().requireContext().applicationContext

    override val listener: DialogListener
        get() = requireListener()

    override val fragmentManager: FragmentManager
        get() = requireFragment().childFragmentManager

    override val lifecycleOwner: LifecycleOwner
        get() = requireFragment().viewLifecycleOwner

    override val activity: Activity
        get() = requireFragment().requireActivity()

    /**
     * Returns [Fragment]
     *
     * @return [Fragment]
     * @throws Fragment is not set
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun requireFragment(): Fragment {
        return fragmentRef.get() ?: throw IllegalStateException("Fragment is not set")
    }

    /**
     * [DialogListener] or null
     */
    val dialogListener: DialogListener?
        get() = dialogListenerRef.get()

    /**
     * Returns [DialogListener]
     *
     * @return [DialogListener]
     * @throws DialogListener is not set
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun requireListener(): DialogListener {
        return dialogListenerRef.get() ?: throw IllegalStateException("DialogListener is not set")
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