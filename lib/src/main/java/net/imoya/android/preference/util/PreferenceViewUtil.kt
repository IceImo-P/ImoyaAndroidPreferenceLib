package net.imoya.android.preference.util

import net.imoya.android.preference.controller.DialogPreferenceEditor
import net.imoya.android.preference.controller.PreferenceEditor
import net.imoya.android.preference.controller.SwitchPreferenceViewController
import net.imoya.android.preference.view.OnPreferenceViewClickListener
import net.imoya.android.preference.view.PreferenceView
import net.imoya.android.preference.view.SwitchPreferenceView

object PreferenceViewUtil {
    /**
     * 設定項目のビューと、ビューをタップした時の処理の連携を初期化します。
     *
     * @param view 設定項目のビュー([PreferenceView])
     * @param editor ビューをタップした時の処理を実装する [PreferenceEditor]
     * @param outViews 画面に表示する [PreferenceView] のリスト。本メソッドをコールすると、リストへ view が追加されます。
     */
    @JvmStatic
    fun setupView(
        view: PreferenceView, editor: OnPreferenceViewClickListener,
        outViews: MutableList<PreferenceView>
    ) {
        view.onPreferenceViewClickListener = editor
        outViews.add(view)
    }

    /**
     * 設定項目のビューと [DialogPreferenceEditor] の連携を初期化します。
     *
     * @param view 設定項目のビュー([PreferenceView])
     * @param editor ビューをタップ時にダイアログを開く処理を実装する [DialogPreferenceEditor]
     * @param outViews 画面に表示する [PreferenceView] のリスト。本メソッドをコールすると、リストへ view が追加されます。
     */
    @JvmStatic
    fun setupView(
        view: PreferenceView, editor: DialogPreferenceEditor,
        outViews: MutableList<PreferenceView>
    ) {
        editor.registerDialogCallback()
        view.onPreferenceViewClickListener = editor
        outViews.add(view)
    }


    /**
     * 設定項目のビューと [SwitchPreferenceViewController] の連携を初期化します。
     *
     * @param view 設定項目のビュー([SwitchPreferenceView])
     * @param controller ビューをタップした時の処理を実装する [SwitchPreferenceViewController]
     * @param outViews 画面に表示する [PreferenceView] のリスト。本メソッドをコールすると、リストへ view が追加されます。
     */
    @JvmStatic
    fun setupView(
        view: SwitchPreferenceView, controller: SwitchPreferenceViewController,
        outViews: MutableList<PreferenceView>
    ) {
        view.onPreferenceChangeListener = controller
        outViews.add(view)
    }
}