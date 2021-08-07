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
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import net.imoya.android.preference.R;
import net.imoya.android.util.Log;

/**
 * 文字列値設定項目ビュー
 * <p>設定項目風の {@link View} です。
 * {@link SharedPreferences} に保存される {@link String} 型の設定値を、
 * {@link TextView} へ表示します。</p>
 * <p>Layout XML上で指定可能な attributes は、{@link PreferenceView},
 * {@link SingleValuePreferenceView} に以下を加えたものとなります:<ul>
 * <li>android:defaultValue({@link android.R.attr#defaultValue}) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値とする文字列を指定します。</li>
 * <li>app:valueForNull({@link R.attr#valueForNull}) -
 * 設定値が null の場合に表示する文字列を指定します。</li>
 * </ul></p>
 * <h2>カスタムレイアウト</h2>
 * <p>レイアウトXMLへ {@link SwitchPreferenceView} を配置する際、 android:layout
 * ({@link android.R.attr#layout})
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。</p>
 * <p>適用するレイアウトは、 {@link PreferenceView}
 * の説明に記載された規則に加え、最低限次の規則に従ってください:<ul>
 * <li>設定値をユーザへ表示するため、IDが &quot;@android:id/text1&quot;
 * ({@link android.R.id#text1})である {@link TextView} を配置してください。</li>
 * </ul></p>
 */
public abstract class StringPreferenceViewBase extends SingleValuePreferenceView {
    /**
     * 状態オブジェクト
     */
    protected static class State extends SingleValuePreferenceView.State {
        /**
         * 現在の設定値
         */
        public String value;
        /**
         * デフォルト値
         */
        public String defaultValue;
        /**
         * null時に表示する文言
         */
        public String valueForNull;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.value = state.value;
                this.defaultValue = state.defaultValue;
                this.valueForNull = state.valueForNull;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            this.value = in.readString();
            this.defaultValue = in.readString();
            this.valueForNull = in.readString();
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeString(this.value);
            out.writeString(this.defaultValue);
            out.writeString(this.valueForNull);
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected static class SavedState extends SingleValuePreferenceView.SavedState {
        /**
         * コンストラクタ
         *
         * @param superState {@link View} の状態
         * @param state 現在の状態が保存されている、状態オブジェクト
         */
        SavedState(Parcelable superState, State state) {
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

    private static final String TAG = "StringPreferenceView";

    /**
     * 値を表示する {@link TextView}
     */
    protected TextView valueView;

    public StringPreferenceViewBase(Context context) {
        super(context);
    }

    public StringPreferenceViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StringPreferenceViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StringPreferenceViewBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @LayoutRes
    protected int getDefaultLayout() {
        return R.layout.preference_string;
    }

    @Override
    protected void onCreateChildViews() {
        super.onCreateChildViews();

        this.valueView = this.findViewById(android.R.id.text1);
    }

    @Override
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        super.loadAttributes(values);
        Log.d(TAG, "loadAttributes: preferenceKey = " + this.getPreferenceKey());

        final State state = (State) this.state;
        state.defaultValue = values.getString(R.styleable.PreferenceView_android_defaultValue);
        state.valueForNull = values.getString(R.styleable.PreferenceView_valueForNull);
        if (state.valueForNull == null) {
            state.valueForNull = "null";
        }

        Log.d(TAG, "loadAttributes: defaultValue = " + state.defaultValue + ", valueForNull = " + state.valueForNull);
    }

    @Override
    protected PreferenceView.State createState() {
        return new State();
    }

    @Override
    protected SavedState createSavedState(Parcelable superState) {
        return new SavedState(superState, (State) this.state);
    }

    @Override
    public void updateViews(SharedPreferences sharedPreferences) {
        final String key = this.getPreferenceKey();
        Log.d(TAG, "updateViews: key = " + key);
        super.updateViews(sharedPreferences);

        if (key != null && sharedPreferences != null) {
            final State state = (State) this.state;
            state.value = sharedPreferences.getString(key, state.defaultValue);
            Log.d(TAG, "updateViews: value = \"" + state.value + "\"");
            this.valueView.setText(this.getValueViewText());
        }
    }

    /**
     * ビューへ表示する値の文字列を取得します。
     *
     * @return 表示用の文字列
     */
    protected abstract String getValueViewText();
}
