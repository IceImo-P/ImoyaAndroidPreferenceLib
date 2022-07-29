package net.imoya.android.preference

import android.content.Intent
import android.view.View
import net.imoya.android.preference.controller.editor.PreferenceEditor

/**
 * 定数定義
 */
object Constants {
    /**
     * リクエストコードのデフォルト値
     */
    const val DEFAULT_REQUEST_CODE = Int.MIN_VALUE

    /**
     * Key of arguments, instance state and [Intent] extra: The state of [PreferenceEditor]
     */
    const val KEY_EDITOR_STATE = "editorState"

    /**
     * 起動パラメータの key: "Host" が指定した、 "client" を表示する領域となる [View] の ID
     */
    const val KEY_CONTAINER_ID = "containerId"
}