package net.imoya.android.preference.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.imoya.android.fragment.BaseFragment;
import net.imoya.android.preference.R;
import net.imoya.android.preference.model.Time;
import net.imoya.android.util.Log;
import net.imoya.android.util.TimePickerHelper;

/**
 * {@link TimePicker} を表示する {@link Fragment}
 */
public class TimePickerFragment extends BaseFragment {
    private static final String ARGUMENT_TIME =
            TimePickerFragment.class.getName() + ".CurrentTime";
    private static final String ARGUMENT_IS_24_HOUR_VIEW =
            TimePickerFragment.class.getName() + ".Is24HourView";
    private static final String ARGUMENT_TITLE =
            TimePickerFragment.class.getName() + ".Title";

    private static final String TAG = "TimePickerFragment";

    private final Time time = new Time();

    private TimePicker picker = null;

    public void setIs24HourView(boolean is24HourView) {
        final Bundle original = this.getArguments();
        final Bundle arguments = (original != null) ? original : new Bundle();
        arguments.putBoolean(ARGUMENT_IS_24_HOUR_VIEW, is24HourView);
        if (original == null) {
            this.setArguments(arguments);
        }
    }

    @NonNull
    public Time getTime() {
        this.loadTime();
        return this.time;
    }

    public void setTime(@NonNull Time time) {
        final Bundle original = this.getArguments();
        final Bundle arguments = (original != null) ? original : new Bundle();
        arguments.putParcelable(ARGUMENT_TIME, time);
        if (original == null) {
            this.setArguments(arguments);
        }
    }

    public void setScreenTitle(@Nullable String title) {
        final Bundle original = this.getArguments();
        final Bundle arguments = (original != null) ? original : new Bundle();
        arguments.putString(ARGUMENT_TITLE, title);
        if (original == null) {
            this.setArguments(arguments);
        }
    }

    private void loadTime() {
        final TimePicker picker = this.picker;
        if (picker != null) {
            final TimePickerHelper pickerHelper = new TimePickerHelper(picker);
            this.time.setHour(pickerHelper.getHour());
            this.time.setMinute(pickerHelper.getMinute());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        this.loadTime();
        outState.putParcelable(ARGUMENT_TIME, this.time);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Time time;
        if (savedInstanceState != null) {
            time = savedInstanceState.getParcelable(ARGUMENT_TIME);
        } else {
            time = this.requireArguments().getParcelable(ARGUMENT_TIME);
        }
        Log.d(TAG, "onCreate: time = " + time);

        if (time != null) {
            this.time.setHour(time.getHour());
            this.time.setMinute(time.getMinute());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preference_time_input_fragment, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.picker = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.picker = view.findViewById(R.id.time);
        new TimePickerHelper(this.picker)
                .setHourAndMinute(this.time.getHour(), this.time.getMinute());
        this.picker.setIs24HourView(this.requireArguments().getBoolean(ARGUMENT_IS_24_HOUR_VIEW));

        final String title = this.requireArguments().getString(ARGUMENT_TITLE);
        Log.d(TAG, "onViewCreated: title = " + title);
        this.setTitle(title);
    }
}
