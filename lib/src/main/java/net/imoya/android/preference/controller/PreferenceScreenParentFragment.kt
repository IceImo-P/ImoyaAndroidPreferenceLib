package net.imoya.android.preference.controller

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.imoya.android.dialog.DialogListener

@Suppress("unused")
open class PreferenceScreenParentFragment<T>(
    @Suppress("MemberVisibilityCanBePrivate")
    protected val fragment: T
) : PreferenceScreenParent where T : Fragment, T : DialogListener {
    override val context: Context
        get() = fragment.requireContext().applicationContext

    override val listener: DialogListener
        get() = fragment

    override val fragmentManager: FragmentManager
        get() = fragment.parentFragmentManager

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return fragment.registerForActivityResult(contract, callback)
    }

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        registry: ActivityResultRegistry,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return fragment.registerForActivityResult(contract, registry, callback)
    }
}