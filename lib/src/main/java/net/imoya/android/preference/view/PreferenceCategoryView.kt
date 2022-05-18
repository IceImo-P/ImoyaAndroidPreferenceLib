package net.imoya.android.preference.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.widget.TextViewCompat
import net.imoya.android.preference.R
import net.imoya.android.util.Log
import net.imoya.android.util.ViewUtil

/**
 * 設定項目ビュー
 *
 * 設定カテゴリ項目風の [View] です。
 *
 * Layout XML上で指定可能な attributes は次の通りです:
 *  * android:title([android.R.attr.title])
 * - ビューの title 文言を指定します。
 *  * app:titleIcon([R.attr.titleIcon])
 * - title 文言の前に表示するアイコン画像を指定します。
 *  * android:drawablePadding([android.R.attr.drawablePadding])
 * - アイコン画像と文言の距離を指定します。
 *  * android:dependency([android.R.attr.dependency])
 * - ビューの有効、無効を連動させる、
 * [SharedPreferences] のキーを指定します。
 *  * android:layout([android.R.attr.layout])
 * - ビューの内容を定義するレイアウトリソースを指定します。
 * 必要な内容は「カスタムレイアウト」の項を参照してください。
 *
 *  # カスタムレイアウト
 *
 * レイアウトXMLへ [PreferenceCategoryView] を配置する際、 android:layout
 * ([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、最低限次の規則に従ってください:
 *  * ルート要素は [FrameLayout] へキャスト可能なクラスとしてください。
 *  * title 文言表示のため、IDが &quot;@android:id/title&quot;
 * ([android.R.id.title])である [TextView] を配置してください。
 *
 */
@Suppress("unused")
open class PreferenceCategoryView : FrameLayout, PreferenceItemView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    @Suppress("ProtectedInFinal")
    protected open class SavedState : BaseSavedState {
        /**
         * ビューの title 文言
         */
        var title: String = ""

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        constructor(parcel: Parcel) : super(parcel) {
            title = parcel.readString() ?: ""
        }

        /**
         * 指定の [Parcel] へ、このオブジェクトの内容を保存します。
         *
         * @param out [Parcel]
         * @param flags  フラグ
         */
        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(title)
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
    @Suppress("ProtectedInFinal")
    protected var titleView: TextView? = null

    /**
     * ビューの有効、無効を連動させる、[SharedPreferences] のキー
     */
    @JvmField
    protected var mDependency: String? = null

    /**
     * ビューの有効、無効を連動させる、[SharedPreferences] のキー
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var dependency: String?
        get() = mDependency
        set(value) {
            mDependency = value
        }

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        Log.d(
            TAG
        ) { "PreferenceCategoryView#__construct(c,a,i): start. class = " + this.javaClass.simpleName + ", instance = " + super.toString() }
        init(context, attrs, defStyleAttr, 0)
        Log.d(
            TAG
        ) { "PreferenceCategoryView#__construct(c,a,i): end. class = " + this.javaClass.simpleName + ", instance = " + super.toString() }
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
        // Log.d(TAG, "PreferenceCategoryView#__construct(c,a,i1,i2): start. class = " + this.getClass().getSimpleName());
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    protected val defaultLayout: Int
        get() = R.layout.preference_category

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    protected open fun onCreateChildViews() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.isClickable = true
        rootView.isFocusable = true

        // 各ビューを取得して、フィールドに保持する
        titleView = findViewById(android.R.id.title)
    }

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    protected open fun loadAttributes(values: TypedArray) {
        Log.v(TAG, "loadAttributes: start")

        val title = values.getString(R.styleable.PreferenceView_android_title)
        titleView?.text = title
        if (titleView != null) {
            ViewUtil.setVisibleOrGone(titleView!!, title != null)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                titleView!!,
                values.getDrawable(R.styleable.PreferenceView_titleIcon), null, null, null
            )
        }
        mDependency = values.getString(R.styleable.PreferenceView_android_dependency)
        Log.d(TAG) { "loadAttributes: title = $title, dependency = $mDependency" }
    }

    /**
     * ビュー内部を初期化します。
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        Log.v(TAG) { "init: start. class = " + this.javaClass.simpleName }
        val values = context.obtainStyledAttributes(
            attrs, R.styleable.PreferenceCategoryView, defStyleAttr, defStyleRes
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
     * @param savedState 一時保存される [SavedState]
     */
    @CallSuper
    protected open fun onSaveInstanceState(savedState: SavedState) {
        savedState.title = title ?: ""
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
        if (titleView != null) {
            title = savedState.title
        }
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
     * title 文言
     */
    var title: String?
        get() = titleView?.text?.toString()
        set(title) {
            titleView?.text = title
        }

    /**
     * title 文言の先頭に表示するアイコン画像を設定します。
     *
     * @param icon [Drawable] 又はnull
     */
    fun setTitleIcon(icon: Drawable?) {
        if (titleView != null) {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                titleView!!, icon, null, null, null
            )
        }
    }

    /**
     * 表示を更新します。
     *
     * @param sharedPreferences [SharedPreferences]
     */
    open fun updateViews(sharedPreferences: SharedPreferences?) {
        Log.d(TAG) { "updateViews: title = $title dependency = $mDependency" }
        if (mDependency != null && sharedPreferences != null) {
            this.isEnabled = sharedPreferences.getBoolean(mDependency, false)
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
        if (key == mDependency) {
            updateViews(sharedPreferences)
            return true
        }
        return false
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
        private const val TAG = "PreferenceCategoryView"
    }
}