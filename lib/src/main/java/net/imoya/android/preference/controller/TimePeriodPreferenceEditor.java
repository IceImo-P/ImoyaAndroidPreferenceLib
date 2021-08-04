package net.imoya.android.preference.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.imoya.android.dialog.DialogBase;
import net.imoya.android.preference.activity.TimePeriodInputActivity;
import net.imoya.android.preference.model.TimePeriod;
import net.imoya.android.preference.view.SingleValuePreferenceView;
import net.imoya.android.preference.view.TimePeriodPreferenceView;
import net.imoya.android.util.Log;

/**
 * {@link TimePeriod} 設定値編集コントローラ
 * <p/>
 * {@link TimePeriodPreferenceView} と組み合わせて使用することを想定しています。
 */
public class TimePeriodPreferenceEditor extends PreferenceEditor {
    public interface Listener {
        void onEdit(TimePeriodPreferenceEditor editor);
    }

    private static final String TAG = "TimePeriodPreferenceEditor";

    private final Context context;

    protected final int requestCode;
    protected boolean show24Hour = false;
    protected Listener listener = null;

    protected final ActivityResultLauncher<Intent> editorLauncher;

    /**
     * コンストラクタ
     *
     * @param fragment    設定項目ビューを表示する {@link Fragment}
     * @param preferences 設定値が保存される {@link SharedPreferences}
     * @param requestCode 設定ダイアログの識別に使用するリクエストコード
     * @param <T>         fragment は {@link DialogBase.Listener} を実装した {@link Fragment} であること
     */
    public <T extends Fragment & DialogBase.Listener> TimePeriodPreferenceEditor(
            @NonNull T fragment, @NonNull SharedPreferences preferences, int requestCode) {
        super(fragment, preferences);
        this.context = fragment.getContext();
        this.requestCode = requestCode;
        this.editorLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> this.onEditorResult(result.getResultCode(), result.getData()));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void setupState(@NonNull SingleValuePreferenceView view) {
        super.setupState(view);

        this.show24Hour = ((TimePeriodPreferenceView) view).is24HourView();
    }

    private TimePeriod getTimePeriod(SharedPreferences sharedPreferences, String key) {
        try {
            return TimePeriod.parse(sharedPreferences.getString(key, null));
        } catch (Exception e) {
            Log.v(TAG, "getTimePeriod: Exception", e);
            return new TimePeriod();
        }
    }


    @Override
    protected void startEditorUI(@NonNull SingleValuePreferenceView view) {
        // 入力画面を開始する
        final Intent intent = new Intent(this.context, TimePeriodInputActivity.class);
        TimePeriodInputActivity.putExtras(
                intent,
                this.getTimePeriod(this.preferences, view.getPreferenceKey()),
                this.show24Hour);
//        this.fragment.startActivityForResult(intent, this.requestCode);
        this.editorLauncher.launch(intent);
    }

    protected void onEditorResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            // 入力値を保存する
            final TimePeriod period = TimePeriodInputActivity.getResultTimePeriod(data);
            this.preferences.edit()
                    .putString(this.state.key, period.toString())
                    .apply();
            if (this.listener != null) {
                this.listener.onEdit(this);
            }
        }
    }

}
