package net.imoya.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import net.imoya.android.preference.R;
import net.imoya.android.preference.model.Time;
import net.imoya.android.util.Log;

/**
 * {@link Time} を取り扱う設定項目ビューの共通部分
 */
@SuppressWarnings("unused")
public abstract class TimePreferenceViewBase extends StringPreferenceViewBase {
    /**
     * 状態オブジェクト
     */
    protected static class State extends StringPreferenceViewBase.State {
        /**
         * 24時間表示フラグ
         */
        boolean hour24;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.hour24 = state.hour24;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            final boolean[] flags = in.createBooleanArray();
            this.hour24 = flags[0];
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeBooleanArray(new boolean[] {this.hour24});
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected static class SavedState extends StringPreferenceViewBase.SavedState {
        /**
         * コンストラクタ
         *
         * @param superState {@link View} の状態
         * @param state      現在の状態が保存されている、状態オブジェクト
         */
        SavedState(Parcelable superState, State state) {
            super(superState, state);
        }

        /**
         * {@link Parcel} の内容で初期化するコンストラクタ
         *
         * @param parcel {@link Parcel}
         */
        private SavedState(Parcel parcel) {
            super(parcel);
        }

        @Override
        protected PreferenceView.State createState() {
            return new State();
        }

        /**
         * {@link Parcelable} 対応用 {@link Creator}
         */
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            /**
             * {@link Parcel} の内容を保持するオブジェクトを生成して返します。
             *
             * @param parcel {@link Parcel}
             * @return {@link Parcel} の内容を保持するオブジェクト
             */
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /**
             * オブジェクトの配列を生成して返します。
             *
             * @param size 配列のサイズ
             * @return 配列
             */
            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private static final String TAG = "TimePreferenceView";

    public TimePreferenceViewBase(Context context) {
        super(context);
    }

    public TimePreferenceViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePreferenceViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimePreferenceViewBase(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean is24HourView() {
        return ((State) this.state).hour24;
    }

    public void setIs24HourView(boolean is24HourView) {
        ((State) this.state).hour24 = is24HourView;
    }

    @Override
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        super.loadAttributes(values);
        Log.d(TAG, "loadAttributes: preferenceKey = " + this.getPreferenceKey());

        final State state = (State) this.state;
        state.hour24 = values.getBoolean(R.styleable.PreferenceView_is24HourView, false);
    }

    @Override
    protected PreferenceView.State createState() {
        return new State();
    }

    @Override
    protected SavedState createSavedState(Parcelable superState) {
        return new SavedState(superState, (State) this.state);
    }
}
