package net.imoya.android.preference.view

/**
 * 項目のクリックを通知します。
 */
interface OnPreferenceViewClickListener {
    /**
     * ユーザが項目をクリックした時に呼び出されます。
     * @param view 呼出元
     */
    fun onPreferenceViewClick(view: PreferenceView)
}