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

package net.imoya.android.preference.activity.editor

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import net.imoya.android.fragment.ArgumentsUtil
import net.imoya.android.preference.Constants
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R

/**
 * 設定画面の [Fragment] を表示する [AppCompatActivity] の abstract
 */
abstract class PreferenceEditorActivityWithFragment : AppCompatActivity() {
    /**
     * 最初に表示する [Fragment]
     */
    abstract val editorFragment: Fragment

    /**
     * この [AppCompatActivity] へ適用する、レイアウトリソースの ID
     */
    open val contentViewLayoutId: Int = R.layout.imoya_preference_editor_activity_with_fragment

    @CallSuper
    protected open fun setFirstFragmentArguments(arguments: Bundle) {
        arguments.putBundle(
            Constants.KEY_EDITOR_STATE,
            intent.getBundleExtra(Constants.KEY_EDITOR_STATE)
        )
        arguments.putInt(
            Constants.KEY_CONTAINER_ID,
            R.id.content_frame
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceLog.v(TAG, "onCreate: start")

        setContentView(contentViewLayoutId)

        setSupportActionBar(toolbar)

        // Place first Fragment on first start
        if (savedInstanceState == null) {
            PreferenceLog.v(TAG, "onCreate: Setting up first Fragment")
            val fragment = editorFragment
            ArgumentsUtil.setArgument(fragment) {
                setFirstFragmentArguments(it)
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.content_frame, fragment, "Fragment")
                .commit()
        }
    }

    override fun finish() {
        super.finish()
        afterFinish()
    }

    protected open val toolbar: Toolbar
        get() = findViewById(R.id.toolbar)

    /**
     * 画面終了時の処理
     */
    protected open fun afterFinish() {
        // 画面遷移時アニメーションを設定
        overridePendingTransition(
            R.anim.imoya_preference_activity_finish_enter,
            R.anim.imoya_preference_activity_finish_exit
        )
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "PrefEditorActivityWithF"
    }
}