package net.imoya.android.preference.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.dialog.TimeInputDialog;
import net.imoya.android.preference.R;
import net.imoya.android.preference.model.Time;
import net.imoya.android.preference.model.TimePeriod;
import net.imoya.android.preference.view.SingleValuePreferenceView;
import net.imoya.android.preference.view.TimePeriodPreferenceView;
import net.imoya.android.util.Log;

/**
 * {@link TimePeriod} 設定値編集コントローラ
 * <p/>
 * {@link TimePeriodPreferenceView} と組み合わせて使用することを想定しています。
 */
public class TimePeriodPreferenceEditorOld extends DialogPreferenceEditor {
    public interface Listener {
        void onEdit(TimePeriodPreferenceEditorOld editor);
    }

    /**
     * 状態オブジェクト
     */
    private static class State extends DialogPreferenceEditor.State {
        /**
         * 現在編集中の {@link TimePeriod}
         */
        private TimePeriod timePeriod;

        private State() {
            super();
        }

        private State(Parcel parcel) {
            super(parcel);
            this.timePeriod = parcel.readParcelable(TimePeriod.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.timePeriod, 0);
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

    private static final String TAG = "TimePeriodPreferenceEditor";

    private final Context context;
    private final int requestCodeEnd;

    private boolean show24Hour = false;
    private Listener listener = null;

    /**
     * コンストラクタ
     *
     * @param fragment         設定項目ビューを表示する {@link Fragment}
     * @param preferences      設定値が保存される {@link SharedPreferences}
     * @param requestCodeStart 設定ダイアログの識別に使用するリクエストコード
     * @param requestCodeEnd   設定ダイアログの識別に使用するリクエストコード
     * @param <T>              fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    public <T extends Fragment & DialogBase.Listener> TimePeriodPreferenceEditorOld(
            T fragment, SharedPreferences preferences, int requestCodeStart, int requestCodeEnd) {
        super(fragment, preferences, requestCodeStart);
        this.context = fragment.getContext();
        this.requestCodeEnd = requestCodeEnd;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected DialogPreferenceEditor.State createState() {
        return new State();
    }

    @Override
    protected void setupState(@NonNull SingleValuePreferenceView view) {
        super.setupState(view);

        ((State) this.state).timePeriod = this.getTimePeriod(this.preferences, this.state.key);
        this.show24Hour = ((TimePeriodPreferenceView) view).is24hourView();
    }

    private TimePeriod getTimePeriod(SharedPreferences sharedPreferences, String key) {
        try {
            return TimePeriod.parse(sharedPreferences.getString(key, null));
        } catch (Exception e) {
            Log.v(TAG, "getTimePeriod: Exception", e);
            return new TimePeriod();
        }
    }

    @Override
    protected void showDialog(SingleValuePreferenceView view) {
        this.showStartTimeInputDialog();
    }

    private void showStartTimeInputDialog() {
        this.showInputDialog(
                ((State) this.state).timePeriod.getStart().getHour(),
                ((State) this.state).timePeriod.getStart().getMinute(),
                this.requestCode,
                this.context.getString(R.string.preference_time_period_edit_start_title),
                this.context.getString(R.string.preference_dialog_next),
                this.context.getString(android.R.string.cancel));
    }

    private void showEndTimeInputDialog() {
        this.showInputDialog(
                ((State) this.state).timePeriod.getEnd().getHour(),
                ((State) this.state).timePeriod.getEnd().getMinute(),
                this.requestCodeEnd,
                this.context.getString(R.string.preference_time_period_edit_end_title),
                this.context.getString(android.R.string.ok),
                this.context.getString(R.string.preference_dialog_back));
    }

    private void showInputDialog(int hour, int minute, int requestCode, String title,
                                 String positiveButtonTitle, String negativeButtonTitle) {
        new TimeInputDialog.Builder(
                new DialogBase.BuilderParentFragment<>(
                        (Fragment & DialogBase.Listener) this.fragment
                ),
                requestCode)
                .setTitle(title)
                .setHour(hour)
                .setMinute(minute)
                .setIs24HourView(this.show24Hour)
                .setPositiveButtonTitle(positiveButtonTitle)
                .setNegativeButtonTitle(negativeButtonTitle)
                .setTag(TAG)
                .show();
    }

    public boolean onDialogResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 入力値を保存する
                ((State) this.state).timePeriod.setStart(
                        new Time(
                                data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0),
                                data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0),
                                0
                        )
                );
                // 終了時入力ダイアログを表示する
                this.showEndTimeInputDialog();
            }
            return true;
        } else if (requestCode == this.requestCodeEnd) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 入力値を保存する
                ((State) this.state).timePeriod.setEnd(
                        new Time(
                                data.getIntExtra(TimeInputDialog.EXTRA_KEY_HOUR, 0),
                                data.getIntExtra(TimeInputDialog.EXTRA_KEY_MINUTE, 0),
                                0
                        )
                );
                this.saveInput(resultCode, data);
            } else {
                // 開始時入力ダイアログを表示する
                this.showStartTimeInputDialog();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void saveInput(int resultCode, @NonNull Intent data) {
        this.preferences.edit()
                .putString(this.state.key, ((State) this.state).timePeriod.toString())
                .apply();
        if (this.listener != null) {
            this.listener.onEdit(this);
        }
    }
}
