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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import net.imoya.android.dialog.DialogListener
import java.lang.ref.WeakReference

/**
 * 設定画面の親画面が [AppCompatActivity] である場合に使用する、親画面の interface
 *
 * @param activity 親画面の [AppCompatActivity]
 */
@Suppress("unused")
open class PreferenceScreenParentActivity<T>(
    activity: T
) : PreferenceScreenParent where T : AppCompatActivity, T : DialogListener {
    /**
     * 親画面の [AppCompatActivity]
     */
    @JvmField
    protected val activity: WeakReference<T> = WeakReference(activity)

    override val context: Context
        get() = requireActivity().applicationContext

    override val listener: DialogListener?
        get() = requireActivity()

    override val fragmentManager: FragmentManager
        get() = requireActivity().supportFragmentManager

    @Suppress("weaker")
    fun requireActivity(): T {
        return activity.get() ?: throw IllegalStateException("Activity is not set")
    }

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return requireActivity().registerForActivityResult(contract, callback)
    }

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        registry: ActivityResultRegistry,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return requireActivity().registerForActivityResult(contract, registry, callback)
    }
}