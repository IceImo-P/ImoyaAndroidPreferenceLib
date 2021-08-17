package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogBase
import net.imoya.android.dialog.DialogParent
import net.imoya.android.preference.view.IntListPreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView

/**
 * 整数値選択設定コントローラ
 *
 * [IntListPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class IntListPreferenceEditor(
    /**
     * 設定ダイアログの親画面
     */
    parent: DialogParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    requestCode: Int
) : ListPreferenceEditor(parent, preferences, requestCode) {
    /**
     * 状態オブジェクト
     */
    protected open class State : ListPreferenceEditor.State {
        /**
         * 選択肢の設定値リスト
         */
        var entryValues: IntArray = intArrayOf()

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            entryValues =
                parcel.createIntArray() ?: throw RuntimeException("entryValues is null")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeIntArray(entryValues)
        }

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<State> = object : Creator<State> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): State {
                    return State(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<State?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: SingleValuePreferenceView) {
        super.setupState(view)
        val prefView = view as IntListPreferenceView
        (state as State).entryValues = prefView.entryValues
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        if (state.key == null) {
            throw RuntimeException("Property key is null")
        }
        val selection = data.getIntExtra(DialogBase.EXTRA_KEY_WHICH, -1)
        val entryValues = (state as State).entryValues
        if (selection == -1) {
            preferences.edit().remove(state.key).apply()
        } else if (selection < 0 || selection >= entryValues.size) {
            throw RuntimeException(
                "Illegal selection: $selection of entries(size = ${entryValues.size})"
            )
        } else {
            preferences.edit().putInt(state.key, entryValues[selection]).apply()
        }
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "IntListPreferenceEditor"
//    }
}