package net.imoya.android.preference.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.core.widget.TextViewCompat
import net.imoya.android.preference.R
import net.imoya.android.util.Log
import net.imoya.android.util.ViewUtil

/**
 * 設定項目ビュー
 *
 * 設定項目風の [View] です。
 *
 * Layout XML上で指定可能な attributes は次の通りです:
 *
 *  * android:title([android.R.attr.title]) - ビューの title 文言を指定します。
 *  * android:summary([android.R.attr.summary]) - ビューの summary 文言を指定します。
 *  * app:note([R.attr.note]) - ビューの note 文言を指定します。
 *  * app:titleIcon([R.attr.titleIcon]) - title 文言の前に表示するアイコン画像を指定します。
 *  * app:summaryIcon([R.attr.summaryIcon]) - summary 文言の前に表示するアイコン画像を指定します。
 *  * app:noteIcon([R.attr.noteIcon]) - note 文言の前に表示するアイコン画像を指定します。
 *  * android:drawablePadding([android.R.attr.drawablePadding]) - アイコン画像と文言の距離を指定します。
 *  * android:dependency([android.R.attr.dependency]) - ビューの有効、無効を連動させる、
 * [SharedPreferences] のキーを指定します。
 *  * android:layout([android.R.attr.layout]) - ビューの内容を定義するレイアウトリソースを指定します。
 *
 * 必要な内容は「カスタムレイアウト」の項を参照してください。
 *
 * # カスタムレイアウト
 *
 * レイアウトXMLへ [PreferenceView] を配置する際、 android:layout ([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、最低限次の規則に従ってください:
 *
 *  * ルート要素は [LinearLayout] へキャスト可能なクラスとしてください。
 *  * ルート要素のIDは &quot;@android:id/content&quot ([android.R.id.content])としてください。
 *  * title 文言表示のため、IDが &quot;@android:id/title&quot; ([android.R.id.title])である
 *  [TextView] を配置してください。
 *  * summary 文言表示のため、IDが &quot;@android:id/summary&quot; ([android.R.id.summary])である
 *  [TextView] を配置してください。
 *  * note 文言表示のため、IDが &quot;@+id/note&quot; ([R.id.note])である [TextView] を配置してください。
 */
open class PreferenceView : LinearLayout, PreferenceItemView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : BaseSavedState {
        /**
         * ビューの title 文言
         */
        var title: String = ""

        /**
         * ビューの summary 文言
         */
        var summary: String = ""

        /**
         * ビューの note 文言
         */
        var note: String = ""

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        @SuppressLint("NewApi")
        protected constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        @RequiresApi(Build.VERSION_CODES.N)
        protected constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            title = parcel.readString()
                ?: throw IllegalStateException("parcel.readString() returns null")
            summary = parcel.readString()
                ?: throw IllegalStateException("parcel.readString() returns null")
            note = parcel.readString()
                ?: throw IllegalStateException("parcel.readString() returns null")
        }

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState)

        /**
         * 指定の [Parcel] へ、このオブジェクトの内容を保存します。
         *
         * @param out [Parcel]
         * @param flags  フラグ
         */
        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(title)
            out.writeString(summary)
            out.writeString(note)
        }

        /**
         * [Parcel] へ保存する内容の特記事項を返却します。
         *
         * @return 特記事項を表す値、又は特別な内容を保存しないことを表す0
         */
        override fun describeContents() = 0

        companion object {
            /**
             * [Parcelable] 対応用 [Creator]
             */
            @JvmField
            val CREATOR: Creator<SavedState> = object : Creator<SavedState> {
                /**
                 * [Parcel] の内容を保持するオブジェクトを生成して返します。
                 *
                 * @param parcel [Parcel]
                 * @return [Parcel] の内容を保持するオブジェクト
                 */
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                /**
                 * オブジェクトの配列を生成して返します。
                 *
                 * @param size 配列のサイズ
                 * @return 配列
                 */
                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    /**
     * title 表示用 [View]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var titleView: TextView

    /**
     * summary 表示用 [View]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var summaryView: TextView

    /**
     * note 表示用 [View]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var noteView: TextView

    /**
     * ビューの有効、無効を連動させる、[SharedPreferences] のキー
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var dependency: String? = null

    /**
     * ビューのクリックを通知する [OnPreferenceViewClickListener]
     */
    var onPreferenceViewClickListener: OnPreferenceViewClickListener? = null

    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : this(context, null)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        Log.d(
            TAG
        ) { "PreferenceView#__construct(c,a,i): start. class = ${this.javaClass.simpleName}, instance = ${super.toString()}" }
        init(context, attrs, defStyleAttr, 0)
        Log.d(
            TAG
        ) { "PreferenceView#__construct(c,a,i): end. class = ${this.javaClass.simpleName}, instance = ${super.toString()}" }
    }

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        Log.d(
            TAG
        ) { "PreferenceView#__construct(c,a,i1,i2): start. class = ${this.javaClass.simpleName}, instance = ${super.toString()}" }
        init(context, attrs, defStyleAttr, defStyleRes)
        Log.d(
            TAG
        ){ "PreferenceView#__construct(c,a,i1,i2): end. class = ${this.javaClass.simpleName}, instance = ${super.toString()}" }
    }

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    protected open val defaultLayout: Int
        get() = R.layout.preference

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    protected open fun onCreateChildViews() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.isClickable = true
        rootView.isFocusable = true

        // 各ビューを取得して、フィールドに保持する
        titleView = findViewById(android.R.id.title)
        summaryView = findViewById(android.R.id.summary)
        noteView = findViewById(R.id.note)
        // this.widgets = (LinearLayout) this.findViewById(android.R.id.widget_frame);
    }

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    @CallSuper
    protected open fun loadAttributes(values: TypedArray) {
        Log.v(TAG) { "${this.javaClass.simpleName}: loadAttributes: start" }

        val title = values.getString(R.styleable.PreferenceView_android_title)
        titleView.text = title
        ViewUtil.setVisibleOrGone(titleView, title?.isNotEmpty() ?: false)
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            titleView,
            values.getDrawable(R.styleable.PreferenceView_titleIcon), null, null, null
        )

        val summary = values.getString(R.styleable.PreferenceView_android_summary)
        summaryView.text = summary
        ViewUtil.setVisibleOrGone(summaryView, summary?.isNotEmpty() ?: false)
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            summaryView,
            values.getDrawable(R.styleable.PreferenceView_summaryIcon), null, null, null
        )

        val note = values.getString(R.styleable.PreferenceView_note)
        noteView.text = note
        ViewUtil.setVisibleOrGone(noteView, note?.isNotEmpty() ?: false)
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            noteView,
            values.getDrawable(R.styleable.PreferenceView_noteIcon), null, null, null
        )

        dependency = values.getString(R.styleable.PreferenceView_android_dependency)
        Log.d(
            TAG
        ) { "${this.javaClass.simpleName}: loadAttributes: title = $title, summary = $summary, note = $note, dependency = $dependency" }
    }

    /**
     * ビュー内部を初期化します。
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        Log.v(TAG) { "init: start. class = " + this.javaClass.simpleName }
        val values = context.obtainStyledAttributes(
            attrs, R.styleable.PreferenceView, defStyleAttr, defStyleRes
        )
        try {
            val layoutId = values.getResourceId(
                R.styleable.PreferenceView_android_layout, defaultLayout
            )
            LayoutInflater.from(context).inflate(layoutId, this)
            onCreateChildViews()
            loadAttributes(values)
        } finally {
            values.recycle()
        }
        this.isClickable = false
        this.isFocusable = false
        findViewById<View>(android.R.id.content).setOnClickListener(clickListener)
        invalidate()
        requestLayout()
        Log.v(TAG, "init: end")
    }

    /**
     * 再起動時に一時保存する [SavedState] を生成して返します。
     *
     * @param superState 親クラスの保存情報
     * @return [SavedState]
     */
    protected open fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    /**
     * 再起動に伴う状態一時保存時の処理を行います。
     * 派生クラスは必要に応じて、このタイミングで保存したい情報を
     * [SavedState] へ保存してください。
     *
     * @param savedState 一時保存する [SavedState]
     */
    @CallSuper
    protected open fun onSaveInstanceState(savedState: SavedState) {
        savedState.title = this.title ?: ""
        savedState.summary = this.summary ?: ""
        savedState.note = this.note ?: ""
    }

    /**
     * 再起動時に一時保存するデータを返します。
     *
     * @return 再起動時に一時保存するデータ
     */
    override fun onSaveInstanceState(): Parcelable? {
        val savedState = createSavedState(super.onSaveInstanceState())
        this.onSaveInstanceState(savedState)
        return savedState
    }

    /**
     * 再起動後に一時保存されたデータを復元します。
     *
     * @param savedState 一時保存された [SavedState]
     */
    @CallSuper
    protected open fun onRestoreState(savedState: SavedState) {
        // ビューへ反映する
        title = savedState.title
        summary = savedState.summary
        note = savedState.note
    }

    /**
     * 再起動後に一時保存されたデータを復元します。
     *
     * @param state 再起動時に一時保存されたデータ
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            onRestoreState(state)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    /**
     * [View] クリック時の処理を行います。
     */
    protected open fun onClickRootView() {
        Log.v(TAG, "onClickRootView: start")

        // リスナへ通知する
        if (onPreferenceViewClickListener != null) {
            onPreferenceViewClickListener!!.onPreferenceViewClick(this)
        }
    }

    /**
     * ルート [View] クリック時の処理
     */
    private val clickListener = OnClickListener {
        Log.v(TAG, "onClick: start")
//        if (it.id == android.R.id.content) {
//            onClickRootView()
//        }
        onClickRootView()
    }

    /**
     * ビューのクリックを通知する [View.OnClickListener] を設定します。
     *
     * [PreferenceView] へ [View.OnClickListener] を設定することは避けてください。
     * 設定すると、クリック時に [OnPreferenceViewClickListener]
     * が動作しない等の問題が発生します。
     *
     * @param l [View.OnClickListener] or null
     */
    override fun setOnClickListener(l: OnClickListener?) {
        Log.w(TAG, "Called PreferenceView.setOnClickListener")
        super.setOnClickListener(l)
    }

    /**
     * title 文言
     */
    var title: String?
        get() = titleView.text?.toString()
        set(value) {
            titleView.text = value
            ViewUtil.setVisibleOrGone(titleView, value?.isNotEmpty() ?: false)
            invalidate()
            requestLayout()
        }

    /**
     * title 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon [Drawable] 又はnull
     */
    @Suppress("unused")
    fun setTitleIcon(icon: Drawable?) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            titleView, icon, null, null, null
        )
    }

    /**
     * summary 文言
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var summary: String?
        get() = summaryView.text?.toString()
        set(value) {
            summaryView.text = value
            ViewUtil.setVisibleOrGone(summaryView, value?.isNotEmpty() ?: false)
            invalidate()
            requestLayout()
        }

    /**
     * summary 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon [Drawable] 又はnull
     */
    @Suppress("unused")
    fun setSummaryIcon(icon: Drawable?) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            summaryView, icon, null, null, null
        )
    }

    /**
     * note 文言
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var note: String?
        get() = noteView.text?.toString()
        set(value) {
            noteView.text = value
            ViewUtil.setVisibleOrGone(noteView, value?.isNotEmpty() ?: false)
            invalidate()
            requestLayout()
        }

    /**
     * note 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon [Drawable] 又はnull
     */
    @Suppress("unused")
    fun setNoteIcon(icon: Drawable?) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            noteView, icon, null, null, null
        )
    }

    /**
     * 表示を更新します。
     *
     * @param sharedPreferences [SharedPreferences]
     */
    open fun updateViews(sharedPreferences: SharedPreferences) {
        Log.d(TAG) { "updateViews: title = $title, dependency = $dependency" }
        if (dependency != null) {
            this.isEnabled = sharedPreferences.getBoolean(dependency, false)
        }
    }

    /**
     * [SharedPreferences] 更新時の処理を行います。
     *
     * @param sharedPreferences 更新された [SharedPreferences]
     * @param key 更新された項目のキー
     * @return 処理を行った場合は true, そうでない場合は false
     */
    override fun onPreferenceChange(
        sharedPreferences: SharedPreferences, key: String
    ): Boolean {
        return if (key == dependency) {
            updateViews(sharedPreferences)
            true
        } else false
    }

    override fun setEnabled(enabled: Boolean) {
        Log.d(TAG) { "setEnabled: enabled = $enabled" }
        super.setEnabled(enabled)

        // 全ての子ビューへ反映する
        ViewUtil.setEnabledDescendants(this, enabled)
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "PreferenceView"
    }
}