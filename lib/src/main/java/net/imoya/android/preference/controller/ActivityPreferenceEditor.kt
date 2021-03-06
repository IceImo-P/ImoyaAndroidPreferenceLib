package net.imoya.android.preference.controller

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import net.imoya.android.preference.view.PreferenceView

/**
 * [Activity] を表示する設定値編集コントローラの共通実装
 *
 * [PreferenceView] タップ時に [Activity] へ遷移し、ユーザが画面へ入力した結果を
 * [SharedPreferences] へ保存するタイプの設定値編集コントローラ共通部分を実装します。
 */
abstract class ActivityPreferenceEditor(
    /**
     * 設定画面の親画面
     */
    protected val parent: PreferenceScreenParent,
    /**
     * 設定値が保存される [SharedPreferences]
     */
    preferences: SharedPreferences? = null
) : PreferenceEditor(preferences) {
    /**
     * 編集画面の [Activity] より結果を受け取る目的で使用する [ActivityResultLauncher]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected open val editorLauncher: ActivityResultLauncher<Intent> = parent.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult -> onEditorResult(result.resultCode, result.data) }

    /**
     * Returns [Intent] for start editor [Activity]
     *
     * @param view [PreferenceView] which has been tapped by user
     */
    protected abstract fun createEditorIntent(view: PreferenceView): Intent

    /**
     * 編集画面の入力結果を保存します。
     *
     * @param resultCode 編集画面が返した結果コード
     * @param data       編集画面が返した追加情報
     */
    protected abstract fun saveInput(resultCode: Int, data: Intent)

    override fun startEditorUI(view: PreferenceView) {
        // Start editor Activity
        editorLauncher.launch(createEditorIntent(view))
    }

    /**
     * 編集画面の入力結果を処理します。
     *
     * @param resultCode 編集画面が返した結果コード
     * @param data       編集画面が返した追加情報
     */
    protected open fun onEditorResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            // 入力値を保存する
            saveInput(resultCode, data)
        }
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "ActivityPreferenceEditor"
//    }
}