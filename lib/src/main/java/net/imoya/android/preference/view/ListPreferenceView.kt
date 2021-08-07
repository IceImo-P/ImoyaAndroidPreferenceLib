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

import java.util.Arrays;

/**
 * 固定の選択肢より選択する設定項目ビュー
 * <p>設定項目風の {@link View} です。
 * {@link SharedPreferences} に保存される1個の設定値に対応する文字列を、
 * {@link TextView} へ表示します。</p>
 * <p>Layout XML上で指定可能な attributes は、{@link PreferenceView},
 * {@link SingleValuePreferenceView} に以下を加えたものとなります:<ul>
 * <li>android:entries({@link android.R.attr#entries}) -
 * 各選択肢の表示用文字列が設定された、文字列の配列リソースを指定します。</li>
 * <li>android:entryValues({@link android.R.attr#entryValues}) -
 * 各選択肢の実際の設定値が設定された、配列のリソースを指定します。</li>
 * <li>android:defaultValue({@link android.R.attr#defaultValue}) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値を指定します。
 * この属性が存在しない場合は、最初の選択肢をデフォルト値とします。</li>
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
public abstract class ListPreferenceView extends SingleValuePreferenceView {
    /**
     * 状態オブジェクト
     */
    protected static class State extends SingleValuePreferenceView.State {
        /**
         * 選択肢の表示用文字列リスト
         */
        protected String[] entries;

        @Override
        protected void copyFrom(PreferenceView.State source) {
            super.copyFrom(source);

            if (source instanceof State) {
                final State state = (State) source;
                this.entries = state.entries;
            }
        }

        @Override
        protected void readFromParcel(Parcel in) {
            super.readFromParcel(in);

            this.entries = in.createStringArray();
        }

        @Override
        protected void writeToParcel(Parcel out) {
            super.writeToParcel(out);

            out.writeStringArray(this.entries);
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
        SavedState(Parcel parcel) {
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

    private static final String TAG = "ListPreferenceView";

    /**
     * 選択中の項目を表示する {@link TextView}
     */
    protected TextView selectionView;

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public ListPreferenceView(Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     */
    public ListPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     */
    public ListPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public ListPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @LayoutRes
    protected int getDefaultLayout() {
        return R.layout.preference_string;
    }

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    @Override
    protected void onCreateChildViews() {
        super.onCreateChildViews();

        this.selectionView = this.findViewById(android.R.id.text1);
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
        final int entriesId = values.getResourceId(R.styleable.PreferenceView_android_entries, 0);
        state.entries = (entriesId != 0 ? values.getResources().getStringArray(entriesId) : null);

        Log.d(TAG, "loadAttributes: entries = " + (state.entries != null ? Arrays.asList(state.entries) : "null"));
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
     * 表示を更新します。
     *
     * @param sharedPreferences {@link SharedPreferences}
     */
    @Override
    public void updateViews(SharedPreferences sharedPreferences) {
        final String key = this.getPreferenceKey();
        Log.d(TAG, "updateViews: key = " + key);
        super.updateViews(sharedPreferences);

        if (key != null && sharedPreferences != null) {
            final State state = (State) this.state;
            final int index = this.getSelectedIndex(sharedPreferences);
            Log.d(TAG, "updateViews: index = " + index);
            this.selectionView.setText(state.entries[index]);
        }
    }

    /**
     * 選択肢リストに於いて、現在選択されている位置を返します。
     *
     * @param sharedPreferences {@link SharedPreferences}
     * @return 選択位置を表すインデックス
     */
    public abstract int getSelectedIndex(SharedPreferences sharedPreferences);

    /**
     * 選択肢の表示用文字列リストを返します。
     *
     * @return 文字列リスト
     */
    public String[] getEntries() {
        return ((State) this.state).entries.clone();
    }

    /**
     * {@link SharedPreferences} 更新時の処理を行います。
     *
     * @param sharedPreferences 更新された {@link SharedPreferences}
     * @param key 更新された項目のキー
     * @return 処理を行った場合は true, そうでない場合は false
     */
    @Override
    public boolean onPreferenceChange(SharedPreferences sharedPreferences, String key) {
        if (!super.onPreferenceChange(sharedPreferences, key)
                && key != null && key.equals(this.getPreferenceKey())) {
            this.updateViews(sharedPreferences);
            return true;
        }
        return false;
    }
}
