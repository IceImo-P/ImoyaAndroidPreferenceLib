package net.imoya.android.preference.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.dialog.SingleChoiceDialog;
import net.imoya.android.preference.view.SingleValuePreferenceView;
import net.imoya.android.preference.view.StringListPreferenceView;

/**
 * 文字列値選択設定コントローラ
 * <p/>
 * {@link StringListPreferenceView} と組み合わせて使用することを想定しています。
 */
public class StringListPreferenceEditor extends DialogPreferenceEditor {
    /**
     * 状態オブジェクト
     */
    protected static class State extends DialogPreferenceEditor.State {
        /**
         * 選択肢の設定値リスト
         */
        public String[] entryValues;

        public State() {
            super();
        }

        private State(Parcel parcel) {
            super(parcel);
            this.entryValues = parcel.createStringArray();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeStringArray(this.entryValues);
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

    // private static final String TAG = "StringListPreferenceEditor";

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    public <T extends Fragment & DialogBase.Listener> StringListPreferenceEditor(
            T fragment, SharedPreferences preferences, int requestCode) {
        super(fragment, preferences, requestCode);
    }

    @Override
    protected DialogPreferenceEditor.State createState() {
        return new State();
    }

    @Override
    protected void setupState(@NonNull SingleValuePreferenceView view) {
        super.setupState(view);

        final StringListPreferenceView prefView = (StringListPreferenceView) view;
        ((State) this.state).entryValues = prefView.getEntryValues();
    }

    @Override
    protected void showDialog(SingleValuePreferenceView view) {
        final StringListPreferenceView prefView = (StringListPreferenceView) view;
        new SingleChoiceDialog.Builder(
                new DialogBase.BuilderParentFragment<>(
                        (Fragment & DialogBase.Listener) this.fragment
                ),
                this.getRequestCode())
                .setTitle(prefView.getTitle())
                .setItems(this.getEntries(prefView))
                .setSelectedPosition(prefView.getSelectedIndex(this.preferences))
                .setTag(prefView.getPreferenceKey())
                .show();
    }

    protected String[] getEntries(StringListPreferenceView prefView) {
        return prefView.getEntries();
    }

    @Override
    protected void saveInput(int resultCode, @NonNull Intent data) {
        final int selection = data.getIntExtra(SingleChoiceDialog.EXTRA_KEY_WHICH, 0);
        this.preferences.edit()
                .putString(this.state.key, ((State) this.state).entryValues[selection]).apply();
    }
}

