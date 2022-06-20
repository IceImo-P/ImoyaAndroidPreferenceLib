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

import androidx.appcompat.app.AppCompatActivity
import net.imoya.android.dialog.DialogListener
import net.imoya.android.preference.controller.*
import net.imoya.android.preference.view.PreferenceView
import java.lang.ref.WeakReference

/**
 * [AppCompatActivity] へ [PreferenceView] を配置するための追加機能を実装および提供します。
 *
 * @author IceImo-P
 */
open class PreferenceActivityController<T> :
    PreferenceScreenController() where T : AppCompatActivity, T : DialogListener {

    /**
     * [PreferenceView] を配置する [AppCompatActivity]
     */
    lateinit var activity: WeakReference<T>

    /**
     * [AppCompatActivity.onCreate] の処理
     *
     * [AppCompatActivity.onCreate] で、このメソッドをコールしてください。
     *
     * @param activity [PreferenceView] を配置する [AppCompatActivity]。
     * [AppCompatActivity] は [DialogListener] を実装する必要があります。
     */
    fun onCreateActivity(activity: T) {
        this.activity = WeakReference(activity)

        super.onCreate()
    }

    /**
     * [PreferenceView] を配置する [AppCompatActivity] を返します。
     *
     * @return [PreferenceView] を配置する [AppCompatActivity]
     */
    @Suppress("weaker")
    protected open fun requireActivity(): T {
        return activity.get() ?: throw IllegalStateException("Activity is not initialized")
    }

    override fun createPreferenceScreenParent(): PreferenceScreenParent {
        return PreferenceScreenParentActivity(requireActivity())
    }
}