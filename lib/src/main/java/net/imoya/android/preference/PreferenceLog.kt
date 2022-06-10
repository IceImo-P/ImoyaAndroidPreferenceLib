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

package net.imoya.android.preference

import android.content.Context
import net.imoya.android.log.LogLevel
import net.imoya.android.log.StaticLogTemplate

/**
 * Log for ImoyaAndroidPreferenceLib
 *
 * * Interfaces are compatible with [android.util.Log].
 * * If you will setup minimum output log level for ImoyaAndroidPreferenceLib,
 *   set string resource named "imoya_android_preference_log_level" and call [init] on
 *   created application or activity.
 *
 * @author IceImo-P
 */
@Suppress("unused")
object PreferenceLog : StaticLogTemplate(LogLevel.NONE) {
    /**
     * Initialize log output
     *
     * @param context [Context]
     */
    @JvmStatic
    fun init(context: Context) {
        super.init(context, R.string.imoya_android_preference_log_level)
    }

    /**
     * Send a VERBOSE log message if minimum output log level <= [net.imoya.android.log.LogLevel.VERBOSE]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @JvmStatic
    fun v(tag: String?, msg: String) = PreferenceLog.vImpl(tag, msg)

    /**
     * Send a VERBOSE log message if minimum output log level <= [net.imoya.android.log.LogLevel.VERBOSE]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     */
    @JvmStatic
    fun v(tag: String?, msgCallback: () -> String) = PreferenceLog.vImpl(tag, msgCallback)

    /**
     * Send a VERBOSE log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.VERBOSE]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun v(tag: String?, msg: String?, tr: Throwable?) = PreferenceLog.vImpl(tag, msg, tr)

    /**
     * Send a VERBOSE log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.VERBOSE]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun v(tag: String?, msgCallback: () -> String, tr: Throwable?) =
        PreferenceLog.vImpl(tag, msgCallback, tr)

    /**
     * Send a VERBOSE log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.VERBOSE]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    @JvmStatic
    fun v(tag: String?, tr: Throwable) = PreferenceLog.vImpl(tag, tr)

    /**
     * Send a DEBUG log message if minimum output log level <= [net.imoya.android.log.LogLevel.DEBUG]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @JvmStatic
    fun d(tag: String?, msg: String) = PreferenceLog.dImpl(tag, msg)

    /**
     * Send a DEBUG log message if minimum output log level <= [net.imoya.android.log.LogLevel.DEBUG]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     */
    @JvmStatic
    fun d(tag: String?, msgCallback: () -> String) = PreferenceLog.dImpl(tag, msgCallback)

    /**
     * Send a DEBUG log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.DEBUG]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun d(tag: String?, msg: String?, tr: Throwable?) = PreferenceLog.dImpl(tag, msg, tr)

    /**
     * Send a DEBUG log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.DEBUG]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun d(tag: String?, msgCallback: () -> String, tr: Throwable?) =
        PreferenceLog.dImpl(tag, msgCallback, tr)

    /**
     * Send a DEBUG log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.DEBUG]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    @JvmStatic
    fun d(tag: String?, tr: Throwable) = PreferenceLog.dImpl(tag, tr)

    /**
     * Send a INFO log message if minimum output log level <= [net.imoya.android.log.LogLevel.INFO]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @JvmStatic
    fun i(tag: String?, msg: String?) = PreferenceLog.iImpl(tag, msg)

    /**
     * Send a INFO log message if minimum output log level <= [net.imoya.android.log.LogLevel.INFO]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     */
    @JvmStatic
    fun i(tag: String?, msgCallback: () -> String) = PreferenceLog.iImpl(tag, msgCallback)

    /**
     * Send a INFO log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.INFO]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun i(tag: String?, msg: String?, tr: Throwable?) = PreferenceLog.iImpl(tag, msg, tr)

    /**
     * Send a INFO log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.INFO]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun i(tag: String?, msgCallback: () -> String, tr: Throwable?) =
        PreferenceLog.iImpl(tag, msgCallback, tr)

    /**
     * Send a INFO log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.INFO]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    @JvmStatic
    fun i(tag: String?, tr: Throwable) = PreferenceLog.iImpl(tag, tr)

    /**
     * Send a WARN log message if minimum output log level <= [net.imoya.android.log.LogLevel.WARN]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @JvmStatic
    fun w(tag: String?, msg: String) = PreferenceLog.wImpl(tag, msg)

    /**
     * Send a WARN log message if minimum output log level <= [net.imoya.android.log.LogLevel.WARN]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     */
    @JvmStatic
    fun w(tag: String?, msgCallback: () -> String) = PreferenceLog.wImpl(tag, msgCallback)

    /**
     * Send a WARN log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.WARN]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun w(tag: String?, msg: String?, tr: Throwable?) = PreferenceLog.wImpl(tag, msg, tr)

    /**
     * Send a WARN log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.WARN]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun w(tag: String?, msgCallback: () -> String, tr: Throwable?) =
        PreferenceLog.wImpl(tag, msgCallback, tr)

    /**
     * Send a WARN log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.WARN]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    @JvmStatic
    fun w(tag: String?, tr: Throwable?) = PreferenceLog.wImpl(tag, tr)

    /**
     * Send a ERROR log message if minimum output log level <= [net.imoya.android.log.LogLevel.ERROR]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @JvmStatic
    fun e(tag: String?, msg: String) = PreferenceLog.eImpl(tag, msg)

    /**
     * Send a ERROR log message if minimum output log level <= [net.imoya.android.log.LogLevel.ERROR]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     */
    @JvmStatic
    fun e(tag: String?, msgCallback: () -> String) = PreferenceLog.eImpl(tag, msgCallback)

    /**
     * Send a ERROR log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.ERROR]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun e(tag: String?, msg: String?, tr: Throwable?) = PreferenceLog.eImpl(tag, msg, tr)

    /**
     * Send a ERROR log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.ERROR]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun e(tag: String?, msgCallback: () -> String, tr: Throwable?) =
        PreferenceLog.eImpl(tag, msgCallback, tr)

    /**
     * Send a ERROR log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.ERROR]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    @JvmStatic
    fun e(tag: String?, tr: Throwable) = PreferenceLog.eImpl(tag, tr)

    /**
     * Send a WTF log message if minimum output log level <= [net.imoya.android.log.LogLevel.WTF]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @JvmStatic
    fun wtf(tag: String?, msg: String?) = PreferenceLog.wtfImpl(tag, msg)

    /**
     * Send a WTF log message if minimum output log level <= [net.imoya.android.log.LogLevel.WTF]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     */
    @JvmStatic
    fun wtf(tag: String?, msgCallback: () -> String) = PreferenceLog.wtfImpl(tag, msgCallback)

    /**
     * Send a WTF log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.WTF]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun wtf(tag: String?, msg: String?, tr: Throwable?) = PreferenceLog.wtfImpl(tag, msg, tr)

    /**
     * Send a WTF log message and log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.WTF]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msgCallback The function which returns message you would like logged.
     * @param tr An exception to log
     */
    @JvmStatic
    fun wtf(tag: String?, msgCallback: () -> String, tr: Throwable?) =
        PreferenceLog.wtfImpl(tag, msgCallback, tr)

    /**
     * Send a WTF log the exception if minimum output log level <= [net.imoya.android.log.LogLevel.WTF]
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    @JvmStatic
    fun wtf(tag: String?, tr: Throwable) = PreferenceLog.wtfImpl(tag, tr)
}