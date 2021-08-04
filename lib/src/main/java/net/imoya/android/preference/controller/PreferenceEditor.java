package net.imoya.android.preference.controller;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.preference.view.PreferenceView;
import net.imoya.android.preference.view.SingleValuePreferenceView;

/**
 * 設定値編集コントローラの共通実装
 * <p/>
 * 設定項目タップ時に設定ダイアログを表示し、ダイアログの結果を保存するタイプの
 * 設定値編集コントローラ共通部分を実装します。
 */
public abstract class PreferenceEditor
        implements PreferenceView.OnPreferenceClickListener {
    /**
     * 状態オブジェクト
     */
    protected static class State implements Parcelable {
        /**
         * 設定キー
         */
        public String key;

        protected State() {
        }

        protected State(Parcel parcel) {
            this.key = parcel.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.key);
        }

        @Override
        public int describeContents() {
            return 0;
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

    // private static final String TAG = "PreferenceEditor";

    @NonNull
    protected final Fragment fragment;
    @NonNull
    protected final SharedPreferences preferences;

    protected State state;

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    <T extends Fragment & DialogBase.Listener> PreferenceEditor(
            @NonNull T fragment, @NonNull SharedPreferences preferences) {
        this.fragment = fragment;
        this.preferences = preferences;
    }

    /**
     * 指定の設定項目ビューをタップした時に、このコントローラが呼び出されるよう設定します。
     *
     * @param view 設定項目ビュー
     */
    public void attach(@NonNull SingleValuePreferenceView view) {
        view.setOnPreferenceClickListener(this);
    }

    /**
     * {@link Fragment} 再起動時に一時保存するデータを取得します。
     *
     * @return 一時保存するデータを含んだ {@link Parcelable}
     */
    public Parcelable onSaveInstanceState() {
        return this.state;
    }

    /**
     * {@link Fragment} 再起動時に、一時保存されたデータを復元します。
     *
     * @param parcelable {@link PreferenceEditor#onSaveInstanceState} が出力した
     * {@link Parcelable}
     */
    public void onRestoreInstanceState(Parcelable parcelable) {
        this.state = (State) parcelable;
    }

    /**
     * 設定項目ビューがタップされた時の処理を行います。
     *
     * @param view タップされた項目設定ビュー
     */
    @Override
    public void onPreferenceClick(@NonNull PreferenceView view) {
        final SingleValuePreferenceView prefView = (SingleValuePreferenceView) view;

        // SingleValuePreferenceView より、設定情報を取得する
        this.setupState(prefView);

        // 入力画面を開始する
        this.startEditorUI(prefView);
    }

    /**
     * {@link SingleValuePreferenceView} より、設定情報を取得します。
     *
     * @param view {@link SingleValuePreferenceView}
     */
    protected void setupState(@NonNull SingleValuePreferenceView view) {
        this.state = this.createState();
        this.state.key = view.getPreferenceKey();
    }

    /**
     * 状態オブジェクトの新しいインスタンスを生成して返します。
     *
     * @return 状態オブジェクト
     */
    protected State createState() {
        return new State();
    }

    /**
     * 設定画面を表示します。
     *
     * @param view タップされた項目設定ビュー
     */
    protected abstract void startEditorUI(@NonNull SingleValuePreferenceView view);
}
