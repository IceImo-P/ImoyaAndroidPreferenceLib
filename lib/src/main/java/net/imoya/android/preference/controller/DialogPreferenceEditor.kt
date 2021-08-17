package net.imoya.android.preference.controller

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import net.imoya.android.dialog.DialogBase
import net.imoya.android.dialog.DialogParent
import net.imoya.android.preference.view.SingleValuePreferenceView
import net.imoya.android.util.Log

/**
 * 設定値編集コントローラの共通実装
 *
 *
 * 設定項目タップ時に設定ダイアログを表示し、ダイアログの結果を保存するタイプの
 * 設定値編集コントローラ共通部分を実装します。
 */
abstract class DialogPreferenceEditor(
    /**
     * 設定ダイアログの親画面
     */
    @JvmField
    protected val parent: DialogParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences,
    /**
     * 設定ダイアログの識別に使用するリクエストコード
     */
    @JvmField
    protected val requestCode: Int
) : PreferenceEditor(preferences) {

    override fun startEditorUI(view: SingleValuePreferenceView) {
        showDialog(view)
    }

    /**
     * 設定ダイアログを表示します。
     *
     * @param view タップされた項目設定ビュー
     */
    protected abstract fun showDialog(view: SingleValuePreferenceView)

    /**
     * 設定ダイアログが閉じられた時の処理を行います。
     *
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param resultCode  ダイアログが返した結果コード
     * @param data        ダイアログが返した追加情報
     * @return このコントローラが処理した場合はtrue, そうでない場合はfalse
     */
    open fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return if (
            requestCode == this.requestCode &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {
            val tag = data.getStringExtra(DialogBase.EXTRA_KEY_TAG)
            Log.d(TAG, "onDialogResult: tag = $tag requestCode = $requestCode")
            // 入力値を保存する
            saveInput(resultCode, data)

            true
        } else false
    }

    /**
     * 設定ダイアログの入力結果を保存します。
     *
     * @param resultCode ダイアログが返した結果コード
     * @param data       ダイアログが返した追加情報
     */
    protected abstract fun saveInput(resultCode: Int, data: Intent)

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "DialogPreferenceEditor"
    }
}