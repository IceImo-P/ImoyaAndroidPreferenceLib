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
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.LayoutRes;

import net.imoya.android.preference.R;
import net.imoya.android.util.Log;

/**
 * スイッチ付き設定項目ビュー
 * <p/>
 * <p>設定項目風の {@link View} です。
 * {@link SharedPreferences} に保存される boolean 型の設定値を、
 * {@link Switch} で表示します。</p>
 * <p>このビューが有効な状態で、ユーザが {@link SwitchPreferenceView}
 * をクリックした場合、スイッチの状態が切り替わり、 {@link OnPreferenceChangeListener}
 * の {@link OnPreferenceChangeListener#onPreferenceChange(PreferenceView)}
 * メソッドが呼び出されます。プログラムは同メソッドに於いて、引数 view の
 * {@link SwitchPreferenceView#getIsOn()} メソッドが返す値を、 {@link SharedPreferences}
 * へ保存する等の処理を行ってください。</p>
 * <p>Layout XML上で指定可能な attributes は、{@link PreferenceView},
 * {@link SingleValuePreferenceView} に以下を加えたものとなります:<ul>
 * <li>android:defaultValue({@link android.R.attr#defaultValue})
 * - このビューに表示する設定値が未保存の場合に使用する、デフォルト値の boolean を指定します。</li>
 * </ul></p>
 * <h2>カスタムレイアウト</h2>
 * <p>レイアウトXMLへ {@link SwitchPreferenceView} を配置する際、 android:layout
 * ({@link android.R.attr#layout})
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。</p>
 * <p>適用するレイアウトは、 {@link PreferenceView}
 * の説明に記載された規則に加え、最低限次の規則に従ってください:<ul>
 * <li>設定値をユーザへ表示するため、IDが &quot;@+id/check&quot;
 * ({@link R.id#check})である {@link CompoundButton} の派生オブジェクトを配置してください。</li>
 * </ul></p>
 */
public class SwitchPreferenceView extends SingleValuePreferenceView {
    /**
     * 設定値の変更を通知します。
     * <p/>
     * {@link PreferenceView} は、あくまで表示を行う {@link View} です。
     * ユーザが {@link PreferenceView} を操作して状態を変更した場合、変更後の値は
     * {@link PreferenceView} を使用するプログラムが、
     * {@link SharedPreferences} へ保存する必要があります。
     */
    public interface OnPreferenceChangeListener {
        /**
         * 設定値が変更された直後に呼び出されます。
         *
         * @param view 設定値が変更された {@link PreferenceView}
         */
        void onPreferenceChange(PreferenceView view);
    }

    /**
     * 状態オブジェクト
     */
    private static class State extends SingleValuePreferenceView.State {
        /**
         * 現在のON/OFF状態
         */
        private boolean value;
        /**
         * デフォルト値
         */
        private boolean defaultValue;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.value = state.value;
                this.defaultValue = state.defaultValue;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            boolean[] values = in.createBooleanArray();
            this.value = values[0];
            this.defaultValue = values[1];
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeBooleanArray(new boolean[]{this.value, this.defaultValue});
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    private static class SavedState extends SingleValuePreferenceView.SavedState {
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
    /**
     * {@link OnPreferenceChangeListener}
     */
    protected OnPreferenceChangeListener onPreferenceChangeListener = null;

    private static final String TAG = "SwitchPreferenceView";

    /**
     * ビュー上のスイッチを表す {@link CompoundButton}
     */
    private CompoundButton compoundButton;

    /**
     * 現在のON/OFF状態
     */
    private boolean value;
    /**
     * デフォルト値
     */
    private boolean defaultValue;

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public SwitchPreferenceView(Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     */
    public SwitchPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     */
    public SwitchPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public SwitchPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 設定値の変更を通知する {@link OnPreferenceChangeListener} を設定します。
     *
     * @param l {@link OnPreferenceChangeListener} 又はnull
     */
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener l) {
        this.onPreferenceChangeListener = l;
    }

    @LayoutRes
    protected int getDefaultLayout() {
        return R.layout.preference_switch;
    }

    @Override
    protected void onCreateChildViews() {
        super.onCreateChildViews();

        this.compoundButton = this.findViewById(R.id.check);
    }

    @Override
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        super.loadAttributes(values);

        this.defaultValue = values.getBoolean(
                R.styleable.PreferenceView_android_defaultValue, false);
        Log.d(TAG, "loadAttributes: defaultValue = " + this.defaultValue);
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
     * 現在のON/OFF状態を取得します。
     *
     * @return 現在のON/OFF状態
     */
    public boolean getIsOn() {
        return this.value;
    }

    @Override
    public void updateViews(SharedPreferences sharedPreferences) {
        final String preferenceKey = this.getPreferenceKey();
        Log.d(TAG, "updateViews: preferenceKey = " + preferenceKey);
        super.updateViews(sharedPreferences);
        if (preferenceKey != null && sharedPreferences != null) {
            this.value = sharedPreferences.getBoolean(preferenceKey, this.defaultValue);
            Log.d(TAG, "updateViews: value = " + this.value);
            this.compoundButton.setChecked(this.value);
        }
    }

    @Override
    protected void onClickRootView() {
        super.onClickRootView();

        // ON/OFF状態を切り替える
        Log.d(TAG, "onClickRootView: before = " + this.value + ", after = " + !this.value);
        this.value = !this.value;
        this.compoundButton.setChecked(this.value);

        // リスナへ変更を通知する
        if (this.onPreferenceChangeListener != null) {
            this.onPreferenceChangeListener.onPreferenceChange(this);
        }
    }
}
