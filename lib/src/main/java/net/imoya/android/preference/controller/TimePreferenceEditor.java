package net.imoya.android.preference.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.dialog.TimeInputDialog;
import net.imoya.android.preference.model.Time;
import net.imoya.android.preference.view.SingleValuePreferenceView;
import net.imoya.android.preference.view.TimePreferenceView;
import net.imoya.android.util.Log;

/**
 * {@link Time} 設定値編集コントローラ
 * <p/>
 * {@link TimePreferenceView} と組み合わせて使用することを想定しています。
 */
public class TimePreferenceEditor extends DialogPreferenceEditor {
    /**
     * 状態オブジェクト
     */
    private static class State extends DialogPreferenceEditor.State {
        /**
         * 現在編集中の {@link Time}
         */
        private Time time;

        private State() {
            super();
        }

        private State(Parcel parcel) {
            super(parcel);
            this.time = parcel.readParcelable(Time.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.time, 0);
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
    private static final String TAG = "TimePreferenceEditor";

    private boolean show24Hour = false;

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    public <T extends Fragment & DialogBase.Listener> TimePreferenceEditor(
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
        ((State) this.state).time = this.getTime(this.preferences, this.state.key);
        this.show24Hour = ((TimePreferenceView) view).is24hourView();
    }

    private Time getTime(SharedPreferences sharedPreferences, String key) {
        try {
            return Time.parse(sharedPreferences.getString(key, null));
        } catch (Exception e) {
            Log.v(TAG, "getTime: Exception", e);
            return new Time();
        }
    }

    @Override
    protected void showDialog(SingleValuePreferenceView view) {
        // 時刻入力ダイアログを表示する
        new TimeInputDialog.Builder(
                new DialogBase.BuilderParentFragment<>(
                        (Fragment & DialogBase.Listener) this.fragment
                ),
                this.requestCode)
                .setHour(((State) this.state).time.getHour())
                .setMinute(((State) this.state).time.getMinute())
                .setIs24HourView(this.show24Hour)
                .setTag(TAG)
                .show();
    }

    @Override
    protected void saveInput(int resultCode, @NonNull Intent data) {
        final Time time = new Time();
        time.setHour(
                data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0));
        time.setMinute(
                data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0));
        this.preferences.edit().putString(this.state.key, time.toString()).apply();
    }
}
