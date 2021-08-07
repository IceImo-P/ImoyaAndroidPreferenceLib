package net.imoya.android.preference.view

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import net.imoya.android.preference.R
import net.imoya.android.util.Log
import net.imoya.android.util.LogUtil

/**
 * 固定の選択肢より選択する設定項目ビュー
 *
 * 設定項目風の [View] です。
 * [SharedPreferences] に保存される1個の設定値に対応する文字列を、
 * [TextView] へ表示します。
 *
 * Layout XML上で指定可能な attributes は、[PreferenceView],
 * [SingleValuePreferenceView] に以下を加えたものとなります:
 *  * android:entries([android.R.attr.entries]) -
 * 各選択肢の表示用文字列が設定された、文字列の配列リソースを指定します。
 *  * android:entryValues([android.R.attr.entryValues]) -
 * 各選択肢の実際の設定値が設定された、配列のリソースを指定します。
 *  * android:defaultValue([android.R.attr.defaultValue]) -
 * このビューに表示する設定値が未保存の場合に使用する、デフォルト値を指定します。
 * この属性が存在しない場合は、最初の選択肢をデフォルト値とします。
 *
 * <h2>カスタムレイアウト</h2>
 *
 * レイアウトXMLへ [SwitchPreferenceView] を配置する際、 android:layout
 * ([android.R.attr.layout])
 * 属性を指定することで、自由に定義されたレイアウトを適用することができます。
 *
 * 適用するレイアウトは、 [PreferenceView]
 * の説明に記載された規則に加え、最低限次の規則に従ってください:
 *  * 設定値をユーザへ表示するため、IDが &quot;@android:id/text1&quot;
 * ([android.R.id.text1])である [TextView] を配置してください。
 *
 */
abstract class ListPreferenceView : SingleValuePreferenceView {
    /**
     * 再起動時に保存する状態オブジェクト定義
     */
    protected open class SavedState : SingleValuePreferenceView.SavedState {
        /**
         * 選択肢の表示用文字列リスト
         */
        var entries: Array<String>

        /**
         * コンストラクタ
         *
         * @param superState [View] の状態
         */
        constructor(superState: Parcelable?) : super(superState) {
            entries = arrayOf()
        }

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         */
        constructor(parcel: Parcel) : this(parcel, null)

        /**
         * [Parcel] の内容で初期化するコンストラクタ
         *
         * @param parcel [Parcel]
         * @param loader [ClassLoader]
         */
        constructor(parcel: Parcel, loader: ClassLoader?) : super(parcel, loader) {
            entries = parcel.createStringArray() ?: arrayOf()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeStringArray(entries)
        }

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
     * 選択肢の表示用文字列リスト
     */
    var entries: Array<String> = arrayOf()
        get() = field.clone()

    /**
     * 選択中の項目を表示する [TextView]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var selectionView: TextView

    /**
     * コンストラクタ
     *
     * @param context [Context]
     */
    constructor(context: Context) : super(context)

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     */
    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    /**
     * コンストラクタ
     *
     * @param context [Context]
     * @param attrs [AttributeSet]
     * @param defStyleAttr 適用するスタイル属性値
     * @param defStyleRes 適用するスタイルのリソースID
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!, attrs, defStyleAttr, defStyleRes
    )

    /**
     * デフォルトのレイアウトリソースIDを返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    override val defaultLayout: Int
        get() = R.layout.preference_string

    /**
     * 子ビューの生成直後に呼び出されます。
     */
    override fun onCreateChildViews() {
        super.onCreateChildViews()
        selectionView = findViewById(android.R.id.text1)
    }

    /**
     * 独自のXML属性値を読み取ります。
     *
     * @param values 取得した属性値
     */
    override fun loadAttributes(values: TypedArray) {
        Log.d(TAG, "loadAttributes: start")
        super.loadAttributes(values)
        Log.d(TAG, "loadAttributes: preferenceKey = $preferenceKey")
        val entriesId = values.getResourceId(R.styleable.PreferenceView_android_entries, 0)
        entries =
            if (entriesId != 0) values.resources.getStringArray(entriesId)
            else throw RuntimeException("entry_values is not defined at layout XML")
        Log.d(TAG, "loadAttributes: entries = ${LogUtil.logString(entries)}")
    }

    override fun createSavedState(superState: Parcelable?): SavedState {
        return SavedState(superState)
    }

    override fun onSaveInstanceState(savedState: PreferenceView.SavedState) {
        super.onSaveInstanceState(savedState)
        if (savedState is SavedState) {
            savedState.entries = entries
        }
    }

    override fun onRestoreState(savedState: PreferenceView.SavedState) {
        super.onRestoreState(savedState)
        if (savedState is SavedState) {
            entries = savedState.entries
        }
    }

    /**
     * 表示を更新します。
     *
     * @param sharedPreferences [SharedPreferences]
     */
    override fun updateViews(sharedPreferences: SharedPreferences?) {
        val key = preferenceKey
        Log.d(TAG, "updateViews: key = $key")
        super.updateViews(sharedPreferences)
        if (sharedPreferences != null) {
            val index = getSelectedIndex(sharedPreferences)
            Log.d(TAG, "updateViews: index = $index")
            selectionView.text = entries[index]
        }
    }

    /**
     * 選択肢リストに於いて、現在選択されている位置を返します。
     *
     * @param sharedPreferences [SharedPreferences]
     * @return 選択位置を表すインデックス
     */
    abstract fun getSelectedIndex(sharedPreferences: SharedPreferences): Int

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
        return if (!super.onPreferenceChange(sharedPreferences, key)
            && key == preferenceKey
        ) {
            updateViews(sharedPreferences)
            true
        } else {
            false
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "ListPreferenceView"
    }
}