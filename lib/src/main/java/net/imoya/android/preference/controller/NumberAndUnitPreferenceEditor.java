package net.imoya.android.preference.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.dialog.NumberInputDialog;
import net.imoya.android.preference.view.NumberAndUnitPreferenceView;
import net.imoya.android.preference.view.SingleValuePreferenceView;

/**
 * 単位付き整数設定値編集コントローラ
 * <p/>
 * {@link NumberAndUnitPreferenceView} と組み合わせて使用することを想定しています。
 */
public class NumberAndUnitPreferenceEditor extends DialogPreferenceEditor {
    /**
     * 状態オブジェクト
     */
    protected static class State extends DialogPreferenceEditor.State {
        /**
         * デフォルト値
         */
        public int defaultValue;
        /**
         * 設定可能な最小値
         */
        public int minValue = Integer.MIN_VALUE;
        /**
         * 設定可能な最大値
         */
        public int maxValue = Integer.MAX_VALUE;
        /**
         * 単位
         */
        public String unit;
        /**
         * 入力欄のヒント文字列
         */
        public String hint;

        protected State() {
            super();
        }

        protected State(Parcel parcel) {
            super(parcel);
            this.defaultValue = parcel.readInt();
            this.minValue = parcel.readInt();
            this.maxValue = parcel.readInt();
            this.unit = parcel.readString();
            this.hint = parcel.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.defaultValue);
            dest.writeInt(this.minValue);
            dest.writeInt(this.maxValue);
            dest.writeString(this.unit);
            dest.writeString(this.hint);
        }

        /**
         * {@link Parcelable} 対応用 {@link Creator}
         */
        public static final Creator<State> CREATOR = new Creator<State>() {
            /**
             * {@link Parcel} の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel {@link Parcel}
             * @return {@link Parcel} の内容を保持するオブジェクト
             */
            @Override
            public State createFromParcel(Parcel parcel) {
                return new State(parcel);
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };
    }

    // private static final String TAG = "NumberAndUnitPreferenceEditor";

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    public <T extends Fragment & DialogBase.Listener> NumberAndUnitPreferenceEditor(
            T fragment, SharedPreferences preferences, int requestCode) {
        super(fragment, preferences, requestCode);
    }

    public void setHint(String hint) {
        ((State) this.state).hint = hint;
    }

    @Override
    protected DialogPreferenceEditor.State createState() {
        return new State();
    }

    @Override
    protected void setupState(@NonNull SingleValuePreferenceView view) {
        super.setupState(view);

        NumberAndUnitPreferenceView prefView = (NumberAndUnitPreferenceView) view;
        ((State) this.state).defaultValue = prefView.getDefaultValue();
        ((State) this.state).minValue = prefView.getMinValue();
        ((State) this.state).maxValue = prefView.getMaxValue();
        ((State) this.state).unit = prefView.getUnit();
    }

    @Override
    protected void showDialog(SingleValuePreferenceView view) {
        final int inputType;
        if (((State) this.state).minValue < 0) {
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
        } else {
            inputType = InputType.TYPE_CLASS_NUMBER;
        }
        new NumberInputDialog.Builder(
                new DialogBase.BuilderParentFragment<>(
                        (Fragment & DialogBase.Listener) this.fragment
                ),
                this.getRequestCode())
                .setTitle(view.getTitle())
                .setInputType(inputType)
                .setHint(((State) this.state).hint)
                .setNumber(
                        this.preferences.getInt(this.state.key, ((State) this.state).defaultValue))
                .setTag(view.getPreferenceKey())
                .show();
    }

    @Override
    protected void saveInput(int resultCode, @NonNull Intent data) {
        final int value = data.getIntExtra(
                NumberInputDialog.EXTRA_KEY_INPUT_VALUE, ((State) this.state).defaultValue);
        this.preferences.edit().putInt(this.state.key, value).apply();
    }
}
