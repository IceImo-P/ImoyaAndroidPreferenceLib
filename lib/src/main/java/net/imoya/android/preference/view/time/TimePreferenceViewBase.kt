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

package net.imoya.android.preference.view.time

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.View
import net.imoya.android.preference.PreferenceLog
import net.imoya.android.preference.R
import net.imoya.android.preference.model.Time
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.StringPreferenceViewBase

/**
 * [Time] を取り扱う設定項目ビューの共通部分
 */
abstract class TimePreferenceViewBase : StringPreferenceViewBase {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : StringPreferenceViewBase.SavedState {
        /**
         * 24時間表示フラグ
         */
        var is24HourView = false

        /**
         * 値が null の場合に、編集画面へ初期表示する時刻
         */
        var timeForNull: Time = Time(0, 0, 0)

        /**
         * 未選択時(未保存時)に表示する文字列
         */
        var defaultLabel: String = ""

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            val flags = parcel.createBooleanArray()
            if (flags == null || flags.isEmpty()) {
                throw RuntimeException("parcel.createBooleanArray returns null or empty array")
            }
            is24HourView = flags[0]
            timeForNull = try {
                Time.parse(parcel.readString() ?: "0:00")
            } catch (e: Exception) {
                Time(0, 0, 0)
            }
            defaultLabel = parcel.readString() ?: ""
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeBooleanArray(booleanArrayOf(is24HourView))
            out.writeString(timeForNull.toString())
            out.writeString(defaultLabel)
        }

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<SavedState> = object : Creator<SavedState> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    /**
     * 24時間表示フラグ
     */
    @JvmField
    protected var mIs24hourView = false

    /**
     * 24時間表示フラグ
     */
    var is24hourView: Boolean
        get() = mIs24hourView
        set(value) {
            mIs24hourView = value
        }

    /**
     * 値が null の場合に、編集画面へ初期表示する時刻
     */
    lateinit var timeForNull: Time

    /**
     * 未選択時に表示する文字列
     */
    lateinit var defaultLabel: String

    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : this(context, null)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    override fun loadAttributes(values: TypedArray) {
        PreferenceLog.v(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        PreferenceLog.v(TAG) { "loadAttributes: preferenceKey = $preferenceKey" }
        mIs24hourView = values.getBoolean(R.styleable.PreferenceView_is24HourView, false)

        timeForNull = try {
            Time.parse(defaultValue ?: "")
        } catch (e: Exception) {
            Time(0, 0, 0)
        }

        defaultLabel = values.getString(R.styleable.PreferenceView_defaultLabel)
            ?: defaultLabelDefaultText
        PreferenceLog.v(TAG) { "loadAttributes: defaultLabel = $defaultLabel" }
    }

    /**
     * [defaultLabel] のデフォルト値
     */
    open val defaultLabelDefaultText
        get() = context.getString(R.string.imoya_preference_not_set)

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.is24HourView = mIs24hourView
            savedState.timeForNull = timeForNull
            savedState.defaultLabel = defaultLabel
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            mIs24hourView = savedState.is24HourView
            timeForNull = savedState.timeForNull
            defaultLabel = savedState.defaultLabel
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "TimePreferenceView"
    }
}