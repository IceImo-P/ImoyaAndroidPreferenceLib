package net.imoya.android.preference.controller

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import net.imoya.android.dialog.DialogListener

@Suppress("unused")
open class PreferenceScreenParentActivity<T>(
    @Suppress("MemberVisibilityCanBePrivate")
    protected val activity: T
) : PreferenceScreenParent where T : AppCompatActivity, T : DialogListener {
    override val context: Context
    get() = activity.applicationContext

    override val listener: DialogListener
    get() = activity

    override val fragmentManager: FragmentManager
    get() = activity.supportFragmentManager

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return activity.registerForActivityResult(contract, callback)
    }

    override fun <I : Any, O : Any> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        registry: ActivityResultRegistry,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return activity.registerForActivityResult(contract, registry, callback)
    }
}