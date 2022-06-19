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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

/**
 * Application starting screen [Fragment]
 *
 * @author IceImo-P
 */
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppLog.v(TAG, "onCreateView: start")

        val view: View = inflater.inflate(R.layout.top, container, false)

        view.findViewById<Button>(R.id.start_activity_sample).setOnClickListener {
            startActivity(Intent(context, SampleActivity::class.java))
        }

        view.findViewById<Button>(R.id.start_plain_activity_sample).setOnClickListener {
            startActivity(Intent(context, SamplePlainActivity::class.java))
        }

        view.findViewById<Button>(R.id.start_fragment_sample).setOnClickListener {
            startFragment(SampleFragment())
        }

        view.findViewById<Button>(R.id.start_plain_fragment_sample).setOnClickListener {
            startFragment(SamplePlainFragment())
        }

        activity?.title = getString(R.string.app_name)

        AppLog.v(TAG, "onCreateView: end")

        return view
    }

    private fun startFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                net.imoya.android.fragment.R.anim.fragment_enter, net.imoya.android.fragment.R.anim.fragment_exit,
                net.imoya.android.fragment.R.anim.fragment_pop_enter, net.imoya.android.fragment.R.anim.fragment_pop_exit
            )
            .replace(R.id.content_frame, fragment, fragment::class.simpleName)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }

    companion object {
        /**
         * Tag for log
         */
        const val TAG = "MainFragment"
    }
}