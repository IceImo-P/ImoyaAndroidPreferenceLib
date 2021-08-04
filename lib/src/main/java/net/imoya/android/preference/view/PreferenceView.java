package net.imoya.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;

import net.imoya.android.preference.R;
import net.imoya.android.util.Log;

/**
 * 設定項目ビュー
 * <p/>
 * <p>設定項目風の {@link View} です。</p>
 * <p>Layout XML上で指定可能な attributes は次の通りです:<ul>
 * <li>android:title({@link android.R.attr#title})
 * - ビューの title 文言を指定します。</li>
 * <li>android:summary({@link android.R.attr#summary})
 * - ビューの summary 文言を指定します。</li>
 * <li>app:note({@link R.attr#note})
 * - ビューの note 文言を指定します。</li>
 * <li>app:titleIcon({@link R.attr#titleIcon})
 * - title 文言の前に表示するアイコン画像を指定します。</li>
 * <li>app:summaryIcon({@link R.attr#summaryIcon})
 * - summary 文言の前に表示するアイコン画像を指定します。</li>
 * <li>app:noteIcon({@link R.attr#noteIcon})
 * - note 文言の前に表示するアイコン画像を指定します。</li>
 * <li>android:drawablePadding({@link android.R.attr#drawablePadding})
 * - アイコン画像と文言の距離を指定します。</li>
 * <li>android:dependency({@link android.R.attr#dependency})
 * - ビューの有効、無効を連動させる、
 * {@link SharedPreferences} のキーを指定します。</li>
 * <li>android:layout({@link android.R.attr#layout})
 * - ビューの内容を定義するレイアウトリソースを指定します。
 * 必要な内容は「カスタムレイアウト」の項を参照してください。</li>
 * </ul></p>
 * <h2>カスタムレイアウト</h2>
 * <p>レイアウトXMLへ {@link PreferenceView} を配置する際、 android:layout
 * ({@link android.R.attr#layout})
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。</p>
 * <p>適用するレイアウトは、最低限次の規則に従ってください:<ul>
 * <li>ルート要素は {@link LinearLayout} へキャスト可能なクラスとしてください。</li>
 * <li>ルート要素のIDは &quot;@android:id/content&quot
 * ({@link android.R.id#content})としてください。</li>
 * <li>title 文言表示のため、IDが &quot;@android:id/title&quot;
 * ({@link android.R.id#title})である {@link TextView} を配置してください。</li>
 * <li>summary 文言表示のため、IDが &quot;@android:id/summary&quot;
 * ({@link android.R.id#summary})である {@link TextView} を配置してください。</li>
 * <li>note 文言表示のため、IDが &quot;@+id/note&quot;
 * ({@link R.id#note})である {@link TextView} を配置してください。</li>
 * </ul></p>
 */
public class PreferenceView extends LinearLayout implements PreferenceItemView {
    /**
     * 項目のクリックを通知します。
     */
    public interface OnPreferenceClickListener {
        /**
         * ユーザが項目をクリックした時に呼び出されます。
         * @param view 呼出元
         */
        void onPreferenceClick(@NonNull PreferenceView view);
    }

    /**
     * 状態オブジェクト
     */
    protected static class State {
        /**
         * ビューの title 文言
         */
        public String title = null;
        /**
         * ビューの summary 文言
         */
        public String summary = null;
        /**
         * ビューの note 文言
         */
        public String note = null;

        /**
         * 状態オブジェクトの内容を、自身へコピーします。
         *
         * @param source コピー元
         */
        protected void copyFrom(State source) {
            this.title = source.title;
            this.summary = source.summary;
            this.note = source.note;
        }

        /**
         * {@link Parcel} の内容を読み取り、自身へ保存します。
         *
         * @param in {@link Parcel}
         */
        protected void readFromParcel(Parcel in) {
            this.title = in.readString();
            this.summary = in.readString();
            this.note = in.readString();
        }

        /**
         * 自身の内容を {@link Parcel} へ保存します。
         *
         * @param out {@link Parcel}
         */
        protected void writeToParcel(Parcel out) {
            out.writeString(this.title);
            out.writeString(this.summary);
            out.writeString(this.note);
        }
    }

    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected static class SavedState extends BaseSavedState {
        /**
         * 状態オブジェクト
         */
        public final State state;

        /**
         * コンストラクタ
         *
         * @param superState {@link View} の状態
         * @param state 現在の状態が保存されている、状態オブジェクト
         */
        protected SavedState(Parcelable superState, State state) {
            super(superState);
            this.state = state;
        }

        /**
         * {@link Parcel} の内容で初期化するコンストラクタ
         *
         * @param parcel {@link Parcel}
         */
        protected SavedState(Parcel parcel) {
            super(parcel);

            this.state = this.createState();
            this.state.readFromParcel(parcel);
        }

        /**
         * 状態オブジェクトを生成します。
         *
         * @return 状態オブジェクト
         */
        protected State createState() {
            return new State();
        }

        /**
         * 指定の {@link Parcel} へ、このオブジェクトの内容を保存します。
         *
         * @param out {@link Parcel}
         * @param flags  フラグ
         */
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            this.state.writeToParcel(out);
        }

        /**
         * {@link Parcel} へ保存する内容の特記事項を返却します。
         *
         * @return 特記事項を表す値、又は特別な内容を保存しないことを表す0
         */
        @Override
        public int describeContents() {
            return 0;
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
     * title 表示用 {@link View}
     */
    protected TextView titleView = null;
    /**
     * summary 表示用 {@link View}
     */
    protected TextView summaryView = null;
    /**
     * note 表示用 {@link View}
     */
    protected TextView noteView = null;
//    private LinearLayout widgets;

    /**
     * 状態オブジェクト
     */
    protected final State state;
    /**
     * ビューの有効、無効を連動させる、{@link SharedPreferences} のキー
     */
    private String dependency = null;
    /**
     * {@link OnPreferenceClickListener}
     */
    protected OnPreferenceClickListener onPreferenceClickListener = null;

    private static final String TAG = "PreferenceView";

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public PreferenceView(Context context) {
        super(context);
        Log.d(TAG, "PreferenceView#__construct(c): start. class = " + this.getClass().getSimpleName() + ", instance = " + super.toString());
        this.state = this.createState();
        this.init(context, null, 0, 0);
        Log.d(TAG, "PreferenceView#__construct(c): end. class = " + this.getClass().getSimpleName() + ", instance = " + super.toString());
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     */
    public PreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "PreferenceView#__construct(c,a): start. class = " + this.getClass().getSimpleName() + ", instance = " + super.toString());
        this.state = this.createState();
        this.init(context, attrs, 0, 0);
        Log.d(TAG, "PreferenceView#__construct(c,a): end. class = " + this.getClass().getSimpleName() + ", instance = " + super.toString());
    }

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     */
    public PreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "PreferenceView#__construct(c,a,i): start. class = " + this.getClass().getSimpleName() + ", instance = " + super.toString());
        this.state = this.createState();
        this.init(context, attrs, defStyleAttr, 0);
        Log.d(TAG, "PreferenceView#__construct(c,a,i): end. class = " + this.getClass().getSimpleName() + ", instance = " + super.toString());
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
    public PreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        Log.d(TAG, "PreferenceView#__construct(c,a,i1,i2): start. class = " + this.getClass().getSimpleName());
        this.state = this.createState();
        this.init(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @LayoutRes
    protected int getDefaultLayout() {
        return R.layout.preference;
    }

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    protected void onCreateChildViews() {
        final View rootView = this.findViewById(android.R.id.content);
        rootView.setClickable(true);
        rootView.setFocusable(true);

        // 各ビューを取得して、フィールドに保持する
        this.titleView = this.findViewById(android.R.id.title);
        this.summaryView = this.findViewById(android.R.id.summary);
        this.noteView = this.findViewById(R.id.note);
//        this.widgets = (LinearLayout) this.findViewById(android.R.id.widget_frame);
    }

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    protected void loadAttributes(TypedArray values) {
        Log.d(TAG, "loadAttributes: start");
        this.state.title = values.getString(R.styleable.PreferenceView_android_title);
        this.state.summary = values.getString(R.styleable.PreferenceView_android_summary);
        this.titleView.setText(this.state.title);
        this.titleView.setVisibility(this.state.title != null ? View.VISIBLE : View.GONE);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this.titleView,
                values.getDrawable(R.styleable.PreferenceView_titleIcon), null, null, null);
        this.summaryView.setText(this.state.summary);
        this.summaryView.setVisibility(this.state.summary != null ? View.VISIBLE : View.GONE);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this.summaryView,
                values.getDrawable(R.styleable.PreferenceView_summaryIcon), null, null, null);
        this.noteView.setText(this.state.note);
        this.noteView.setVisibility(this.state.note != null ? View.VISIBLE : View.GONE);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this.noteView,
                values.getDrawable(R.styleable.PreferenceView_noteIcon), null, null, null);

        this.dependency = values.getString(R.styleable.PreferenceView_android_dependency);

        Log.d(TAG, "loadAttributes: title = " + this.state.title + ", summary = " + this.state.summary + ", dependency = " + this.dependency);
    }

    /**
     * ビュー内部を初期化します。
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet}
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Log.d(TAG, "init: start. class = " + this.getClass().getSimpleName());
        final TypedArray values = context.obtainStyledAttributes(
                attrs, R.styleable.PreferenceView, defStyleAttr, defStyleRes);
        try {
            final int layoutId = values.getResourceId(
                    R.styleable.PreferenceView_android_layout, this.getDefaultLayout());
            LayoutInflater.from(context).inflate(layoutId, this);
            this.onCreateChildViews();
            this.loadAttributes(values);
        } finally {
            values.recycle();
        }

        this.setClickable(false);
        this.setFocusable(false);
        this.findViewById(android.R.id.content).setOnClickListener(this.clickListener);
        Log.d(TAG, "init: end");
    }

    /**
     * 状態オブジェクトを生成して返します。
     *
     * @return 状態オブジェクト
     */
    protected State createState() {
        return new State();
    }

    /**
     * 再起動時に一時保存する {@link SavedState} を生成して返します。
     *
     * @param superState 親クラスの保存情報
     * @return {@link SavedState}
     */
    protected SavedState createSavedState(Parcelable superState) {
        return new SavedState(superState, this.state);
    }

    /**
     * 再起動に伴う状態一時保存時の処理を行います。
     * 派生クラスは必要に応じて、このタイミングで保存したい情報を
     * {@link SavedState} へ保存してください。
     *
     * @param savedState 一時保存される {@link SavedState}
     */
    @SuppressWarnings("unused")
    protected void onSaveInstanceState(SavedState savedState) {
        // 何もしない(既にStateに保存されている内容が保存されれば問題ない)
    }

    /**
     * 再起動時に一時保存するデータを返します。
     *
     * @return 再起動時に一時保存するデータ
     */
    @Override
    protected final Parcelable onSaveInstanceState() {
        final SavedState savedState = this.createSavedState(super.onSaveInstanceState());
        this.onSaveInstanceState(savedState);
        return savedState;
    }

    /**
     * 再起動後に一時保存されたデータを復元します。
     *
     * @param savedState 一時保存された {@link SavedState}
     */
    protected void onRestoreState(SavedState savedState) {
        // SavedState が含む State の内容を取得する
        this.state.copyFrom(savedState.state);

        // ビューへ反映する
        if (this.titleView != null) {
            this.setTitle(savedState.state.title);
        }
        if (this.summaryView != null) {
            this.setSummary(savedState.state.summary);
        }
        if (this.noteView != null) {
            this.setNote(savedState.state.note);
        }
    }

    /**
     * 再起動後に一時保存されたデータを復元します。
     *
     * @param state 再起動時に一時保存されたデータ
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            final SavedState mySavedState = (SavedState) state;
            super.onRestoreInstanceState(mySavedState.getSuperState());
            this.onRestoreState(mySavedState);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    /**
     * {@link View} がクリックされた時の処理を行います。
     */
    protected void onClickRootView() {
        Log.d(TAG, "onClickRootView: start");

        // リスナへ通知する
        if (this.onPreferenceClickListener != null) {
            this.onPreferenceClickListener.onPreferenceClick(this);
        }
    }

    /**
     * ルート {@link View} クリック時の処理
     */
    private final OnClickListener clickListener = view -> {
        Log.d(TAG, "onClick: start");
        if (view.getId() == android.R.id.content) {
            PreferenceView.this.onClickRootView();
        }
    };

    /**
     * ビューのクリックを通知する {@link OnClickListener} を設定します。
     * <p/>
     * {@link PreferenceView} へ {@link OnClickListener} を設定することは避けてください。
     * 設定すると、クリック時に {@link OnPreferenceClickListener}
     * が動作しない等の問題が発生します。
     *
     * @param l {@link OnClickListener} 又はnull
     */
    @Override
    public void setOnClickListener(OnClickListener l) {
        Log.d(TAG, "setOnClickListener");
        super.setOnClickListener(l);
    }

    /**
     * ビューのクリックを通知する {@link OnPreferenceClickListener} を設定します。
     *
     * @param l {@link OnPreferenceClickListener} 又はnull
     */
    public void setOnPreferenceClickListener(OnPreferenceClickListener l) {
        this.onPreferenceClickListener = l;
    }

    /**
     * title 文言を取得します。
     *
     * @return title 文言
     */
    public String getTitle() {
        return this.state.title;
    }

    /**
     * title 文言を設定します。
     *
     * @param title title 文言
     */
    public void setTitle(String title) {
        this.state.title = title;
        this.titleView.setText(title);
    }

    /**
     * title 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon {@link Drawable} 又はnull
     */
    public void setTitleIcon(Drawable icon) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                this.titleView, icon, null, null, null);
    }

    /**
     * summary 文言を取得します。
     *
     * @return summary 文言
     */
    public String getSummary() {
        return this.state.summary;
    }

    /**
     * summary 文言を設定します。
     *
     * @param summary summary 文言
     */
    public void setSummary(String summary) {
        this.state.summary = summary;
        this.summaryView.setText(summary);
        this.summaryView.setVisibility(summary != null ? View.VISIBLE : View.GONE);
    }

    /**
     * summary 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon {@link Drawable} 又はnull
     */
    public void setSummaryIcon(Drawable icon) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                this.summaryView, icon, null, null, null);
    }

    /**
     * note 文言を取得します。
     *
     * @return note 文言
     */
    public String getNote() {
        return this.state.note;
    }

    /**
     * note 文言を設定します。
     *
     * @param note note 文言
     */
    public void setNote(String note) {
        this.state.note = note;
        this.noteView.setText(note);
        this.noteView.setVisibility(note != null ? View.VISIBLE : View.GONE);
    }

    /**
     * note 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon {@link Drawable} 又はnull
     */
    public void setNoteIcon(Drawable icon) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                this.noteView, icon, null, null, null);
    }

    /**
     * 表示を更新します。
     *
     * @param sharedPreferences {@link SharedPreferences}
     */
    public void updateViews(SharedPreferences sharedPreferences) {
        Log.d(TAG, "updateViews: title = " + this.state.title + " dependency = " + this.dependency);
        if (this.dependency != null && sharedPreferences != null) {
            this.setEnabled(sharedPreferences.getBoolean(this.dependency, false));
        }
    }

    /**
     * {@link SharedPreferences} 更新時の処理を行います。
     *
     * @param sharedPreferences 更新された {@link SharedPreferences}
     * @param key 更新された項目のキー
     * @return 処理を行った場合は true, そうでない場合は false
     */
    public boolean onPreferenceChange(SharedPreferences sharedPreferences, String key) {
        if (key != null && key.equals(this.dependency)) {
            this.updateViews(sharedPreferences);
            return true;
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        Log.d(TAG, "setEnabled: enabled = " + enabled);
        super.setEnabled(enabled);

        // 全ての子ビューへ反映する
        final int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            setEnabled(this.getChildAt(i), enabled);
        }
    }

    private static void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            final int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = ((ViewGroup) view).getChildAt(i);
                setEnabled(child, enabled);
            }
        }
    }
}
