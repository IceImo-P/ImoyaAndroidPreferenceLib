package net.imoya.android.preference.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.preference.view.SingleValuePreferenceView;
import net.imoya.android.util.Log;

/**
 * 設定値編集コントローラの共通実装
 * <p/>
 * 設定項目タップ時に設定ダイアログを表示し、ダイアログの結果を保存するタイプの
 * 設定値編集コントローラ共通部分を実装します。
 */
abstract class DialogPreferenceEditor extends PreferenceEditor {
    private static final String TAG = "DialogPreferenceEditor";

    protected final int requestCode;

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    <T extends Fragment & DialogBase.Listener> DialogPreferenceEditor(
            T fragment, SharedPreferences preferences, int requestCode) {
        super(fragment, preferences);
        this.requestCode = requestCode;
    }

    protected int getRequestCode() {
        return this.requestCode;
    }

    @Override
    protected void startEditorUI(@NonNull SingleValuePreferenceView view) {
        this.showDialog(view);
    }

    /**
     * 設定ダイアログを表示します。
     *
     * @param view タップされた項目設定ビュー
     */
    protected abstract void showDialog(SingleValuePreferenceView view);

    /**
     * 設定ダイアログが閉じられた時の処理を行います。
     *
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param resultCode  ダイアログが返した結果コード
     * @param data        ダイアログが返した追加情報
     * @return このコントローラが処理した場合はtrue, そうでない場合はfalse
     */
    public boolean onDialogResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == this.getRequestCode()) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                final String tag = data.getStringExtra(DialogBase.EXTRA_KEY_TAG);
                Log.d(TAG, "onActivityResult: tag = " + tag + " requestCode = " + requestCode);
                if (this.state == null) {
                    Log.e(TAG, "onActivityResult: state == null (requestCode may be duplicated)");
                    return false;
                }
                // 入力値を保存する
                this.saveInput(resultCode, data);
            }
            return true;
        }
        return false;
    }

    /**
     * 設定ダイアログの入力結果を保存します。
     *
     * @param resultCode ダイアログが返した結果コード
     * @param data       ダイアログが返した追加情報
     */
    protected abstract void saveInput(int resultCode, @NonNull Intent data);
}
