package net.imoya.android.preference.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppLog.v(TAG, "onCreateView: start")

        val view: View = inflater.inflate(R.layout.top, container, false)

        view.findViewById<Button>(R.id.start_activity_sample).setOnClickListener {
            startActivity(Intent(context, SampleActivity::class.java))
        }

        view.findViewById<Button>(R.id.start_fragment_sample).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    net.imoya.android.fragment.R.anim.fragment_enter, net.imoya.android.fragment.R.anim.fragment_exit,
                    net.imoya.android.fragment.R.anim.fragment_pop_enter, net.imoya.android.fragment.R.anim.fragment_pop_exit
                )
                .replace(R.id.content_frame, SampleFragment(), "SampleFragment")
                .addToBackStack("SampleFragment")
                .commit()
        }

        AppLog.v(TAG, "onCreateView: end")

        return view
    }

    companion object {
        const val TAG = "MainFragment"
    }
}