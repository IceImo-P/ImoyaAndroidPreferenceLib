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
import net.imoya.android.util.LogUtil;

/**
 * 設定値が整数値である、 {@link ListPreferenceView} の実装
 */
@SuppressWarnings("unused")
public class IntListPreferenceView extends ListPreferenceView {
    /**
     * 状態オブジェクト
     */
    private static class State extends ListPreferenceView.State {
        /**
         * 選択肢の設定値整数リスト
         */
        private int[] entryValues;
        /**
         * 現在の設定値
         */
        private int value;
        /**
         * デフォルト値
         */
        private int defaultValue;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.entryValues = state.entryValues;
                this.value = state.value;
                this.defaultValue = state.defaultValue;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            this.entryValues = in.createIntArray();
            this.value = in.readInt();
            this.defaultValue = in.readInt();
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeIntArray(this.entryValues);
            out.writeInt(this.value);
            out.writeInt(this.defaultValue);
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    private static class SavedState extends ListPreferenceView.SavedState {
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

    private static final String TAG = "StringListPreferenceView";

    public IntListPreferenceView(Context context) {
        super(context);
    }

    public IntListPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntListPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IntListPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    @Override
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        super.loadAttributes(values);
        Log.d(TAG, "loadAttributes: preferenceKey = " + this.getPreferenceKey());

        final State state = (State) this.state;
        final int entryValuesId = values.getResourceId(
                R.styleable.PreferenceView_android_entryValues, 0);
        state.entryValues = (entryValuesId != 0
                ? values.getResources().getIntArray(entryValuesId) : null);
        state.defaultValue = values.getInt(R.styleable.PreferenceView_android_defaultValue, 0);

        Log.d(TAG, "loadAttributes: entryValues = "
                + LogUtil.logString(state.entryValues)
                + ", defaultValue = " + state.defaultValue);

        if (state.entries.length != state.entryValues.length) {
            throw new RuntimeException("entries.length != entryValues.length");
        }
    }

    /**
     * 状態オブジェクトを生成して返します。
     *
     * @return 状態オブジェクト
     */
    @Override
    protected PreferenceView.State createState() {
        return new State();
    }

    /**
     * 再起動時に一時保存する {@link SavedState} を生成して返します。
     *
     * @param superState 親クラスの保存情報
     * @return {@link SavedState}
     */
    @Override
    protected SavedState createSavedState(Parcelable superState) {
        return new SavedState(superState, (State) this.state);
    }

    /**
     * 選択肢リストに於いて、現在選択されている位置を返します。
     *
     * @param sharedPreferences {@link SharedPreferences}
     * @return 選択位置を表すインデックス
     */
    @Override
    public int getSelectedIndex(SharedPreferences sharedPreferences) {
        // 現在の設定値を取得する
        final State state = (State) this.state;
        state.value = sharedPreferences.getInt(this.getPreferenceKey(), state.defaultValue);

        // 選択肢に於ける位置を検索する
        int selectedIndex = 0;
        for (int i = 0; i < state.entryValues.length; i++) {
            if (state.entryValues[i] == state.value) {
                selectedIndex = i;
                break;
            }
        }
        return selectedIndex;
    }

    /**
     * 選択肢の設定値リストを返します。
     *
     * @return 選択肢の設定値リスト
     */
    public int[] getEntryValues() {
        return ((State) state).entryValues;
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
}
