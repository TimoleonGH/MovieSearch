package com.lamti.moviesearch.ui.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lamti.moviesearch.R
import kotlinx.android.synthetic.main.fragment_summary.view.*

class SummaryFragment : RoundedBottomSheetDialogFragment() {

    companion object {
        private var summaryText = "no summary"
        fun getInstance(overview: String): SummaryFragment {
            val frag = SummaryFragment()
            summaryText = overview
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_summary, container,false)
        view.summary_text_TV.text = summaryText
        return view
    }

}