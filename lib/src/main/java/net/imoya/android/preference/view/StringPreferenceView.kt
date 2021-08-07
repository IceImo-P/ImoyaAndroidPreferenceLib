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
public class StringPreferenceView extends StringPreferenceViewBase {
    /**
     * 状態オブジェクト
     */
    protected static class State extends StringPreferenceViewBase.State {
        /**
         * 最大入力可能文字数
         */
        public int maxLength;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.maxLength = state.maxLength;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            this.maxLength = in.readInt();
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeInt(this.maxLength);
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    private static class SavedState extends StringPreferenceViewBase.SavedState {
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

    private static final String TAG = "StringPreferenceView";

    public StringPreferenceView(Context context) {
        super(context);
    }

    public StringPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StringPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StringPreferenceView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        super.loadAttributes(values);
        Log.d(TAG, "loadAttributes: preferenceKey = " + this.getPreferenceKey());

        final State state = (State) this.state;
        state.maxLength = values.getInt(
                R.styleable.PreferenceView_android_maxLength, Integer.MAX_VALUE);

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
    protected String getValueViewText() {
        final State state = (State) this.state;
        return (state.value != null) ? state.value : state.valueForNull;
    }

    /**
     * デフォルト値を返します。
     *
     * @return デフォルト値
     */
    public String getDefaultValue() {
        return ((State) this.state).defaultValue;
    }

    /**
     * デフォルト値を設定します。
     *
     * @param defaultValue デフォルト値
     */
    public void setDefaultValue(String defaultValue) {
        ((State) this.state).defaultValue = defaultValue;
    }

    /**
     * 最大入力可能文字数を返します。
     *
     * @return 最大入力可能文字数
     */
    public int getMaxLength() {
        return ((State) this.state).maxLength;
    }

//    /**
//     * 最大入力可能文字数を設定します。
//     *
//     * @param maxLength 最大入力可能文字数
//     */
//    public void setMaxLength(int maxLength) {
//        ((State) this.state).maxLength = maxLength;
//    }
}
