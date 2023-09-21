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

package net.imoya.android.preference.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Sample implementation: preference views [Fragment] on plain [AppCompatActivity]
 *
 * @author IceImo-P
 */
class SamplePlainActivityForFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLog.v(TAG, "onCreate: start")

        setContentView(R.layout.plain_activity_for_fragment)

        // Place MainFragment on first start
        if (savedInstanceState == null) {
            AppLog.v(TAG, "onCreate: Setting up SamplePlainFragment")
            supportFragmentManager.beginTransaction()
                .add(R.id.content_frame, SamplePlainFragmentOnSamplePlainActivity(), "Fragment")
                .commit()
        }

        AppLog.v(TAG, "onCreate: end")
    }

    companion object {
        /**
         * Tag for log
         */
        const val TAG = "SamplePlainAct4Fragment"
    }}