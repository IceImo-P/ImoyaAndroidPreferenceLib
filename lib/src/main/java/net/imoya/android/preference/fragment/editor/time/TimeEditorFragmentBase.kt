package net.imoya.android.preference.fragment.editor.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import net.imoya.android.preference.R
import net.imoya.android.preference.fragment.editor.EditorFragment

/**
 * 時刻編集 [Fragment] の共通実装
 */
abstract class TimeEditorFragmentBase : EditorFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.imoya_preference_time_editor_fragment,
            container,
            false
        )
    }

    /**
     * サブタイトルを表示する [TextView]
     */
    protected open val subtitleView: TextView?
        get() = view?.findViewById(R.id.subtitle)

    /**
     * [TimePicker]
     */
    protected open val timePicker: TimePicker?
        get() = view?.findViewById(R.id.time)

}