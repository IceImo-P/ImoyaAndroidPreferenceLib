package net.imoya.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import net.imoya.android.preference.model.Time;
import net.imoya.android.util.Log;
import net.imoya.android.util.TimeUtil;

import java.util.Locale;

/**
 * {@link Time} を保存する設定項目ビュー
 */
public class TimePreferenceView extends TimePreferenceViewBase {
    private static final String TAG = "TimePreferenceView";

    public TimePreferenceView(Context context) {
        super(context);
    }

    public TimePreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimePreferenceView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String getValueViewText() {
        final State state = (State) this.state;
        final Time time = this.getTime(state.value);
        Log.d(TAG, "updateViews: time = " + time);
        return (time != null) ? this.getTimeText(time) : state.valueForNull;
    }

    private Time getTime(String s) {
        try {
            return (s != null ? Time.parse(s) : null);
        } catch (Exception e) {
            Log.v(TAG, "getTime: Exception", e);
            return null;
        }
    }

    private String getTimeText(Time time) {
        return TimeUtil.formatTime(
                this.getContext(), time.getHour(), time.getMinute(), ((State) this.state).hour24);
    }

    static String getTimeText(String format, int hour, int minute) {
        return format.replace("#hour#", String.valueOf(hour))
                .replace("#minute#", String.format(Locale.US, "%02d", minute));
    }
}
