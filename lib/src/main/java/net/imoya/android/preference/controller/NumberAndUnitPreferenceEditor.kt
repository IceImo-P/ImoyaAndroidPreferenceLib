package net.imoya.android.preference.controller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.InputType
import androidx.annotation.CallSuper
import net.imoya.android.dialog.DialogParent
import net.imoya.android.dialog.InputDialog
import net.imoya.android.dialog.NumberInputDialog
import net.imoya.android.preference.view.NumberAndUnitPreferenceView
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.util.Log

/**
 * 単位表示付き整数設定値編集コントローラ
 *
 * [NumberAndUnitPreferenceView] と組み合わせて使用することを想定しています。
 */
@Suppress("unused")
open class NumberAndUnitPreferenceEditor(
    parent: DialogParent, preferences: SharedPreferences, requestCode: Int
) : DialogPreferenceEditor(parent, preferences, requestCode) {
    /**
     * 状態オブジェクト
     */
    protected open class State : PreferenceEditor.State {
        /**
         * デフォルト値
         */
        var defaultValue = 0

        /**
         * 設定可能な最小値
         */
        var minValue = Int.MIN_VALUE

        /**
         * 設定可能な最大値
         */
        var maxValue = Int.MAX_VALUE

        /**
         * 単位
         */
        var unit: String? = null

        /**
         * 入力欄のヒント文字列
         */
        var hint: String? = null

        constructor() : super()

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        protected constructor(parcel: Parcel) : super(parcel) {
            defaultValue = parcel.readInt()
            minValue = parcel.readInt()
            maxValue = parcel.readInt()
            unit = parcel.readString()
            hint = parcel.readString()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(defaultValue)
            dest.writeInt(minValue)
            dest.writeInt(maxValue)
            dest.writeString(unit)
            dest.writeString(hint)
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

    fun setHint(hint: String?) {
        (state as State).hint = hint
    }

    override fun createState(): PreferenceEditor.State {
        return State()
    }

    @CallSuper
    override fun setupState(view: SingleValuePreferenceView) {
        super.setupState(view)
        val prefView = view as NumberAndUnitPreferenceView
        Log.d(
            TAG
        ) {
            "setupState: defaultValue = ${prefView.defaultValue}, minValue = ${prefView.minValue}, maxValue = ${prefView.maxValue}, unit = ${prefView.unit}"
        }
        (state as State).defaultValue = prefView.defaultValue
        (state as State).minValue = prefView.minValue
        (state as State).maxValue = prefView.maxValue
        (state as State).unit = prefView.unit
    }

    override fun showDialog(view: SingleValuePreferenceView) {
        val inputType =
            if ((state as State).minValue < 0) {
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            } else {
                InputType.TYPE_CLASS_NUMBER
            }
        NumberInputDialog.Builder(parent, requestCode)
            .setTitle(view.title ?: "")
            .setInputType(inputType)
            .setHint((state as State).hint)
            .setNumber(
                preferences.getInt(state.key, (state as State).defaultValue)
            )
            .setTag(view.preferenceKey)
            .show()
    }

    override fun saveInput(resultCode: Int, data: Intent) {
        val value = data.getIntExtra(
            InputDialog.EXTRA_KEY_INPUT_VALUE, (state as State).defaultValue
        )
        preferences.edit().putInt(state.key, value).apply()
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "NumberAndUnitPreferenceEditor"
    }
}