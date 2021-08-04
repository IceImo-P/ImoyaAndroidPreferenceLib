package net.imoya.android.preference.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.dialog.TextInputDialog;
import net.imoya.android.preference.view.SingleValuePreferenceView;
import net.imoya.android.preference.view.StringPreferenceView;

/**
 * 文字列設定値編集コントローラ
 * <p/>
 * {@link StringPreferenceView} と組み合わせて使用することを想定しています。
 */
public class StringPreferenceEditor extends DialogPreferenceEditor {
    /**
     * 状態オブジェクト
     */
    private static class State extends DialogPreferenceEditor.State {
        /**
         * 入力欄のヒント文字列
         */
        private String hint;
        /**
         * 入力文字タイプ
         */
        private int inputType = InputType.TYPE_CLASS_TEXT;
        /**
         * 最大入力可能文字数
         */
        private int maxLength = Integer.MAX_VALUE;

        private State() {
            super();
        }

        private State(Parcel parcel) {
            super(parcel);
            this.hint = parcel.readString();
            this.inputType = parcel.readInt();
            this.maxLength = parcel.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.hint);
            dest.writeInt(this.inputType);
            dest.writeInt(this.maxLength);
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

    // private static final String TAG = "StringPreferenceEditor";

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    public <T extends Fragment & DialogBase.Listener> StringPreferenceEditor(
            T fragment, SharedPreferences preferences, int requestCode) {
        super(fragment, preferences, requestCode);
    }

    public void setInputType(int inputType) {
        ((State) this.state).inputType = inputType;
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

        final StringPreferenceView prefView = (StringPreferenceView) view;
        ((State) this.state).maxLength = prefView.getMaxLength();
    }

    @Override
    protected void showDialog(SingleValuePreferenceView view) {
        new TextInputDialog.Builder(
                new DialogBase.BuilderParentFragment<>(
                        (Fragment & DialogBase.Listener) this.fragment
                ),
                this.getRequestCode())
                .setTitle(view.getTitle())
                .setInputType(((State) this.state).inputType)
                .setMaxLength(((State) this.state).maxLength)
                .setHint(((State) this.state).hint)
                .setText(this.preferences.getString(this.state.key, ""))
                .setTag(view.getPreferenceKey())
                .show();
    }

    @Override
    protected void saveInput(int resultCode, @NonNull Intent data) {
        final String text = data.getStringExtra(TextInputDialog.EXTRA_KEY_INPUT_VALUE);
        this.preferences.edit().putString(this.state.key, text).apply();
    }
}
