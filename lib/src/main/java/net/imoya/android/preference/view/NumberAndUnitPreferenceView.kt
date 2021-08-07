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
 * 単位付き数値設定項目ビュー
 * <p>設定項目風の {@link View} です。
 * {@link SharedPreferences} に保存される int 型の設定値と、プログラムが指定する単位文字列を、
 * {@link TextView} へ表示します。</p>
 * <p>Layout XML上で指定可能な attributes は、{@link PreferenceView},
 * {@link SingleValuePreferenceView} に以下を加えたものとなります:<ul>
 * <li>android:defaultValue({@link android.R.attr#defaultValue}) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値とする整数値を指定します。</li>
 * <li>app:unit({@link R.attr#unit}) - ビューへ表示する単位文字列を指定します。</li>
 * </ul></p>
 * <h2>カスタムレイアウト</h2>
 * <p>レイアウトXMLへ {@link SwitchPreferenceView} を配置する際、 android:layout
 * ({@link android.R.attr#layout})
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。</p>
 * <p>適用するレイアウトは、 {@link PreferenceView}
 * の説明に記載された規則に加え、最低限次の規則に従ってください:<ul>
 * <li>設定値をユーザへ表示するため、IDが &quot;@android:id/text1&quot;
 * ({@link android.R.id#text1})である {@link TextView} を配置してください。</li>
 * <li>単位をユーザへ表示するため、IDが &quot;@+id/unit&quot;
 * ({@link R.id#unit})である {@link TextView} を配置してください。</li>
 * </ul></p>
 */
public class NumberAndUnitPreferenceView extends SingleValuePreferenceView {
    /**
     * 状態オブジェクト
     */
    protected static class State extends SingleValuePreferenceView.State {
        /**
         * 現在の設定値
         */
        public int value;
        /**
         * デフォルト値
         */
        public int defaultValue;
        /**
         * 設定可能な最小値
         */
        public int minValue = Integer.MIN_VALUE;
        /**
         * 設定可能な最大値
         */
        public int maxValue = Integer.MAX_VALUE;
        /**
         * 単位
         */
        public String unit;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.value = state.value;
                this.defaultValue = state.defaultValue;
                this.minValue = state.minValue;
                this.maxValue = state.maxValue;
                this.unit = state.unit;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            this.value = in.readInt();
            this.defaultValue = in.readInt();
            this.minValue = in.readInt();
            this.maxValue = in.readInt();
            this.unit = in.readString();
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeInt(this.value);
            out.writeInt(this.defaultValue);
            out.writeInt(this.minValue);
            out.writeInt(this.maxValue);
            out.writeString(this.unit);
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

    private static final String TAG = "NumberAndUnitPreferenceView";

    /**
     * 値を表示する {@link TextView}
     */
    protected TextView valueView;
    /**
     * 単位を表示する {@link TextView}
     */
    protected TextView unitView;

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public NumberAndUnitPreferenceView(Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     */
    public NumberAndUnitPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     */
    public NumberAndUnitPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public NumberAndUnitPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @LayoutRes
    protected int getDefaultLayout() {
        return R.layout.preference_number_unit;
    }

    @Override
    protected void onCreateChildViews() {
        super.onCreateChildViews();

        this.valueView = this.findViewById(android.R.id.text1);
        this.unitView = this.findViewById(R.id.unit);
    }

    @Override
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        super.loadAttributes(values);
        Log.d(TAG, "loadAttributes: preferenceKey = " + this.getPreferenceKey());

        final State state = (State) this.state;
        state.defaultValue = values.getInt(R.styleable.PreferenceView_android_defaultValue, 0);
        state.minValue = values.getInt(R.styleable.PreferenceView_minValue, 0);
        state.maxValue = values.getInt(R.styleable.PreferenceView_maxValue, 0);
        this.setUnit(values.getString(R.styleable.PreferenceView_unit));

        Log.d(TAG, "loadAttributes: defaultValue = " + state.defaultValue + ", unit = " + state.unit);
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
        final String preferenceKey = this.getPreferenceKey();
        Log.d(TAG, "updateViews: preferenceKey = " + preferenceKey);
        super.updateViews(sharedPreferences);

        if (preferenceKey != null && sharedPreferences != null) {
            final State state = (State) this.state;
            state.value = sharedPreferences.getInt(preferenceKey, state.defaultValue);
            Log.d(TAG, "updateViews: value = " + state.value);
            this.valueView.setText(String.valueOf(state.value));
        }
    }

    /**
     * デフォルト値を返します。
     *
     * @return デフォルト値
     */
    public int getDefaultValue() {
        return ((State) this.state).defaultValue;
    }

    /**
     * デフォルト値を設定します。
     *
     * @param defaultValue デフォルト値
     */
    public void setDefaultValue(int defaultValue) {
        ((State) this.state).defaultValue = defaultValue;
    }

    /**
     * 設定可能な最小値を取得します。
     *
     * @return 設定可能な最小値
     */
    public int getMinValue() {
        return ((State) this.state).minValue;
    }

    /**
     * 設定可能な最小値を設定します。
     *
     * @param minValue 設定可能な最小値
     */
    public void setMinValue(int minValue) {
        ((State) this.state).minValue = minValue;
    }

    /**
     * 設定可能な最大値を取得します。
     *
     * @return 設定可能な最大値
     */
    public int getMaxValue() {
        return ((State) this.state).maxValue;
    }

    /**
     * 設定可能な最大値を設定します。
     *
     * @param maxValue 設定可能な最大値
     */
    public void setMaxValue(int maxValue) {
        ((State) this.state).maxValue = maxValue;
    }

    /**
     * 単位を取得します。
     *
     * @return 単位を表す文字列
     */
    public String getUnit() {
        return ((State) this.state).unit;
    }

    /**
     * 単位を設定します。
     *
     * @param unit 単位を表す文字列
     */
    public void setUnit(String unit) {
        ((State) this.state).unit = unit;

        // 表示へ反映する
        this.unitView.setText(unit);
        this.unitView.setVisibility((unit != null && unit.length() > 0) ? View.VISIBLE : View.GONE);
    }
}
