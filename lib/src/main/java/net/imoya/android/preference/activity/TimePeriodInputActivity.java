package net.imoya.android.preference.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import net.imoya.android.fragment.BaseActivity;
import net.imoya.android.preference.R;
import net.imoya.android.preference.fragment.TimePickerFragment;
import net.imoya.android.preference.model.Time;
import net.imoya.android.preference.model.TimePeriod;
import net.imoya.android.util.Log;

import java.util.Objects;

/**
 * {@link TimePeriod} 入力画面
 */
public class TimePeriodInputActivity extends BaseActivity {
    public static final String EXTRA_TIME_PERIOD =
            TimePeriodInputActivity.class.getName() + ".TimePeriod";
    public static final String EXTRA_IS_24_HOUR_VIEW =
            TimePeriodInputActivity.class.getName() + ".Is24HourView";

    /**
     * 入力中の時刻種別
     */
    private enum InputStep {
        /**
         * 開始時刻
         */
        START,
        /**
         * 終了時刻
         */
        END
    }

    /**
     * 状態オブジェクト
     */
    private static class State implements Parcelable {
        /**
         * 現在編集中の {@link TimePeriod}
         */
        private final TimePeriod timePeriod;
        /**
         * 現在画面に表示している時刻種別
         */
        private InputStep inputStep;

        private State() {
            this.timePeriod = new TimePeriod();
            this.inputStep = InputStep.START;
        }

        private State(Parcel parcel) {
            this.timePeriod = parcel.readParcelable(TimePeriod.class.getClassLoader());
            this.inputStep = (InputStep) parcel.readSerializable();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.timePeriod, 0);
            dest.writeSerializable(this.inputStep);
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

    /**
     * 起動パラメータを {@link Intent} へ設定します。
     *
     * @param intent       {@link Intent}
     * @param timePeriod   {@link TimePeriod} の初期値
     * @param is24HourView 24時間表示フラグ
     */
    public static void putExtras(Intent intent, TimePeriod timePeriod, boolean is24HourView) {
        intent.putExtra(EXTRA_TIME_PERIOD, timePeriod);
        intent.putExtra(EXTRA_IS_24_HOUR_VIEW, is24HourView);
    }

    /**
     * {@link Activity#onActivityResult(int, int, Intent)} の引数より、入力結果の
     * {@link TimePeriod} を取得します。
     *
     * @param data {@link Activity#onActivityResult(int, int, Intent)} の引数である {@link Intent}
     * @return 入力結果の {@link TimePeriod}
     */
    public static TimePeriod getResultTimePeriod(Intent data) {
        return data.getParcelableExtra(EXTRA_TIME_PERIOD);
    }

    private static final String STATE_KEY = TimePeriodInputActivity.class.getName() + ".State";

    private static final String TAG = "TimePeriodInputActivity";

    private State state = null;
    private boolean is24HourView;

    @Override
    protected int getContentViewResourceId() {
        return R.layout.preference_time_period_input_activity;
    }

    @Override
    @NonNull
    protected Fragment getFirstFragment() {
        final TimePickerFragment fragment = new TimePickerFragment();
        final Time time = new Time();
        time.setHour(this.state.timePeriod.getStartHour());
        time.setMinute(this.state.timePeriod.getStartMinute());
        fragment.setTime(time);
        fragment.setIs24HourView(this.is24HourView);
        fragment.setScreenTitle(this.getString(R.string.preference_time_period_edit_start_title));
        return fragment;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.root;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");

        if (savedInstanceState == null) {
            this.state = new State();
            final TimePeriod timePeriod = this.getIntent().getParcelableExtra(EXTRA_TIME_PERIOD);
            if (timePeriod != null) {
                this.state.timePeriod.copyFrom(timePeriod);
            }
        } else {
            this.state = savedInstanceState.getParcelable(STATE_KEY);
        }
        this.is24HourView = this.getIntent().getBooleanExtra(EXTRA_IS_24_HOUR_VIEW, false);
        Log.d(TAG, "onCreate: timePeriod = " + this.state.timePeriod + ", step = " + this.state.inputStep.name() + ", is24 = " + this.is24HourView);

        super.onCreate(savedInstanceState);

        this.setResult(Activity.RESULT_CANCELED);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STATE_KEY, this.state);
    }

    @Override
    protected void setupActionBarOnFirstFragment(@NonNull ActionBar actionBar, boolean onCreate) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel_white_24dp);
    }

    @Override
    protected void setupActionBarOnDescendantFragment(@NonNull ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.preference_time_period_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok) {
            // OKボタンの場合
            final Time time = this.getCurrentTime();
            if (this.state.inputStep == InputStep.END) {
                Log.d(TAG, "onOptionsItemSelected: Complete");

                // 終了時刻を保存する
                this.state.timePeriod.setEndHour(time.getHour());
                this.state.timePeriod.setEndMinute(time.getMinute());

                // 呼び出し元画面へ遷移する
                final Intent data = new Intent();
                data.putExtra(EXTRA_TIME_PERIOD, this.state.timePeriod);
                this.setResult(Activity.RESULT_OK, data);
                this.finish();
            } else {
                Log.d(TAG, "onOptionsItemSelected: to EndTime");

                // 開始時刻を保存する
                this.state.timePeriod.setStartHour(time.getHour());
                this.state.timePeriod.setStartMinute(time.getMinute());

                // 終了時刻入力画面へ遷移する
                this.state.inputStep = InputStep.END;
                final TimePickerFragment fragment = new TimePickerFragment();
                final Time endTime = new Time(
                        this.state.timePeriod.getEndHour(), this.state.timePeriod.getEndMinute());
                fragment.setTime(endTime);
                fragment.setIs24HourView(this.is24HourView);
                fragment.setScreenTitle(
                        this.getString(R.string.preference_time_period_edit_end_title));

                this.replaceTo(R.id.root, fragment, null);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Log.d(TAG, "onSupportNavigateUp: Cancel");
            this.finish();
            return true;
        } else {
            // 終了時刻入力中か?
            if (this.state.inputStep == InputStep.END) {
                Log.d(TAG, "onSupportNavigateUp: to StartTime");

                // 終了時刻入力中の場合は、終了時刻を保存する
                final Time time = this.getCurrentTime();
                this.state.timePeriod.setEndHour(time.getHour());
                this.state.timePeriod.setEndMinute(time.getMinute());

                // 開始時刻入力画面へ遷移する
                this.state.inputStep = InputStep.START;
            }
            return super.onSupportNavigateUp();
        }
    }

    @NonNull
    private Time getCurrentTime() {
        final TimePickerFragment fragment =
                (TimePickerFragment) this.getSupportFragmentManager()
                        .findFragmentById(R.id.root);
        return Objects.requireNonNull(fragment).getTime();
    }
}
