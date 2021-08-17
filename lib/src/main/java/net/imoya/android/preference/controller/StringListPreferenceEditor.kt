package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import net.imoya.android.dialog.DialogBase
import net.imoya.android.dialog.DialogParent
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.preference.view.StringListPreferenceView

/**
 * 文字列値選択設定コントローラ
 *
 * [StringListPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class StringListPreferenceEditor(
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
        var entryValues: Array<String> = arrayOf()

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            entryValues =
                parcel.createStringArray() ?: throw RuntimeException("entryValues is null")
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeStringArray(entryValues)
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
        val prefView = view as StringListPreferenceView
        (state as State).entryValues = prefView.entryValues
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val selection = data.getIntExtra(DialogBase.EXTRA_KEY_WHICH, 0)
        preferences.edit()
            .putString(state.key, (state as State).entryValues[selection]).apply()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "StringListPreferenceEditor"
//    }
}