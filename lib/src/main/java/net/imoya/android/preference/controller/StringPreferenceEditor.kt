package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.InputType
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.InputDialog
import net.imoya.android.dialog.TextInputDialog
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.preference.view.StringPreferenceView

/**
 * 文字列設定値編集コントローラ
 *
 * [StringPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class StringPreferenceEditor(
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
) : DialogPreferenceEditor(parent, preferences, requestCode) {
    /**
     * 状態オブジェクト
     */
    protected open class State : PreferenceEditor.State {
        /**
         * 入力欄のヒント文字列
         */
        var hint: String? = null

        /**
         * 入力文字タイプ
         */
        var inputType = InputType.TYPE_CLASS_TEXT

        /**
         * 最大入力可能文字数
         */
        var maxLength = Int.MAX_VALUE

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            hint = parcel.readString()
            inputType = parcel.readInt()
            maxLength = parcel.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(hint)
            dest.writeInt(inputType)
            dest.writeInt(maxLength)
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

    fun setInputType(inputType: Int) {
        (state as State).inputType = inputType
    }

    fun setHint(hint: String?) {
        (state as State).hint = hint
    }

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    override fun setupState(view: SingleValuePreferenceView) {
        super.setupState(view)
        val prefView = view as StringPreferenceView
        (state as State).maxLength = prefView.maxLength
    }

    override fun showDialog(view: SingleValuePreferenceView) {
        TextInputDialog.Builder(parent, requestCode)
            .setTitle(view.title ?: "")
            .setInputType((state as State).inputType)
            .setMaxLength((state as State).maxLength)
            .setHint((state as State).hint)
            .setText(preferences.getString(state.key, "") ?: "")
            .setTag(view.preferenceKey)
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val text = data.getStringExtra(InputDialog.EXTRA_KEY_INPUT_VALUE)
        preferences.edit().putString(state.key, text).apply()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "StringPreferenceEditor";
//    }
}