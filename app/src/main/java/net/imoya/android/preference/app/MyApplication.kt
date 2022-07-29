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

import android.app.Application
import net.imoya.android.dialog.DialogLog
import net.imoya.android.fragment.FragmentLog
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.util.UtilLog

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLog.init(applicationContext)
        PreferenceLog.init(applicationContext)
        DialogLog.init(applicationContext)
        FragmentLog.init(applicationContext)
        UtilLog.init(applicationContext)
    }
}