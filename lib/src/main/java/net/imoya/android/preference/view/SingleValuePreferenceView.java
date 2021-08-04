package net.imoya.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import net.imoya.android.preference.R;
import net.imoya.android.util.Log;

/**
 * {@link SharedPreferences} に於ける、1件の設定項目を表すビュー
 * <p/>
 * <p>設定項目風の {@link View} です。</p>
 * <p>Layout XML上で指定可能な attributes は、{@link PreferenceView}
 * に以下を加えたものとなります:<ul>
 * <li>android:preferenceKey({@link android.R.attr#key})
 * - このビューに表示する設定値が保存される、{@link SharedPreferences} のキーを指定します。</li>
 * </ul></p>
 */
public abstract class SingleValuePreferenceView extends PreferenceView {
    /**
     * 状態オブジェクト
     */
    protected static class State extends PreferenceView.State {
        /**
         * この項目の設定値を {@link SharedPreferences} へ保存する際のキー
         */
        private String preferenceKey = null;

        /**
         * 状態オブジェクトの内容を、自身へコピーします。
         *
         * @param source コピー元
         */
        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.preferenceKey = state.preferenceKey;
            }
        }

        /**
         * {@link Parcel} の内容を読み取り、自身へ保存します。
         *
         * @param in {@link Parcel}
         */
        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            this.preferenceKey = in.readString();
        }

        /**
         * 自身の内容を {@link Parcel} へ保存します。
         *
         * @param out {@link Parcel}
         */
        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeString(this.preferenceKey);
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected static class SavedState extends PreferenceView.SavedState {
        /**
         * コンストラクタ
         *
         * @param superState {@link View} の状態
         * @param state 現在の状態が保存されている、状態オブジェクト
         */
        protected SavedState(Parcelable superState, State state) {
            super(superState, state);
        }

        /**
         * {@link Parcel} の内容で初期化するコンストラクタ
         *
         * @param parcel {@link Parcel}
         */
        protected SavedState(Parcel parcel) {
            super(parcel);
        }

        /**
         * 状態オブジェクトを生成します。
         *
         * @return 状態オブジェクト
         */
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

    private static final String TAG = "SingleValuePreferenceView";

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public SingleValuePreferenceView(Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     */
    public SingleValuePreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     */
    public SingleValuePreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SingleValuePreferenceView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void loadAttributes(TypedArray values) {
        super.loadAttributes(values);

        final State state = (State) this.state;
        state.preferenceKey = values.getString(R.styleable.PreferenceView_android_key);
        Log.d(TAG, "onSaveInstanceState: preferenceKey = " + ((State) this.state).preferenceKey);
    }

    @Override
    protected PreferenceView.State createState() {
        return new State();
    }

    @Override
    protected SavedState createSavedState(Parcelable superState) {
        return new SavedState(superState, (State) this.state);
    }

    /**
     * この項目の設定値を {@link SharedPreferences} へ保存する際のキーを取得します。
     *
     * @return キー文字列
     */
    public String getPreferenceKey() {
        Log.d(TAG, "getPreferenceKey: preferenceKey = " + ((State) this.state).preferenceKey);
        return ((State) this.state).preferenceKey;
    }

    /**
     * この項目の設定値を {@link SharedPreferences} へ保存する際のキーを設定します。
     *
     * @param preferenceKey キー文字列
     */
    public void setPreferenceKey(String preferenceKey) {
        Log.d(TAG, this.getClass().getSimpleName() + ".setPreferenceKey: preferenceKey = " + preferenceKey);
        ((State) this.state).preferenceKey = preferenceKey;
    }

    @Override
    public boolean onPreferenceChange(SharedPreferences sharedPreferences, String key) {
        boolean result = super.onPreferenceChange(sharedPreferences, key);
        if (key != null && key.equals(this.getPreferenceKey())) {
            this.updateViews(sharedPreferences);
            result = true;
        }
        return result;
    }
}
