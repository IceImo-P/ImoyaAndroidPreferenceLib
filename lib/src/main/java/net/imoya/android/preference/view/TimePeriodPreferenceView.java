package net.imoya.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import net.imoya.android.preference.R;
import net.imoya.android.preference.model.TimePeriod;
import net.imoya.android.util.Log;
import net.imoya.android.util.TimeUtil;

/**
 * {@link TimePeriod} を保存する設定項目ビュー
 */
public class TimePeriodPreferenceView extends TimePreferenceViewBase {
    private static final String TAG = "TimePeriodPreferenceView";

    public TimePeriodPreferenceView(Context context) {
        super(context);
    }

    public TimePeriodPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePeriodPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimePeriodPreferenceView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String getValueViewText() {
        final State state = (State) this.state;
        final TimePeriod period = this.getTimePeriod(state.value);
        Log.d(TAG, "updateViews: period = " + period);
        return (period != null) ? this.getTimePeriodText(period) : state.valueForNull;
    }

    private TimePeriod getTimePeriod(String s) {
        try {
            return (s != null ? TimePeriod.parse(s) : null);
        } catch (Exception e) {
            Log.v(TAG, "getTimePeriod: Exception", e);
            return null;
        }
    }

    private String getTimePeriodText(TimePeriod period) {
        final Context context = this.getContext();
        final String format = context.getString(
                R.string.preference_time_period_value_format);
        final boolean hour24 = ((State) this.state).hour24;
        return format.replace("#start#",
                TimeUtil.formatTime(
                        this.getContext(), period.getStartHour(), period.getStartMinute(), hour24))
                .replace("#end#",
                        TimeUtil.formatTime(
                                this.getContext(),
                                period.getEndHour(), period.getEndMinute(), hour24));
    }
}
