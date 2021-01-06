package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tutor_home_tab2.*

@AndroidEntryPoint
class TutorHomeFragmentTab2 : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutor_home_tab2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.subject.observe(viewLifecycleOwner, { sub ->
            if (sub != null) {
                homeViewModel.getReadyStatus(sub)
            }
        })

        homeViewModel.tutorReadyStatus.observe(viewLifecycleOwner, { state ->
            if (state != null) {
                ready_tutor_status_result_tv.text = state
            }
        })

        homeViewModel.getSubject()

        btn_ready_tutor.setOnClickListener {
            if (homeViewModel.tutorReadyStatus.value == "ready") {
                homeViewModel.setTheStatusNotReady(homeViewModel.subject.value.toString())
                btn_ready_tutor.text = "ready"
            } else if (homeViewModel.tutorReadyStatus.value == "not ready") {
                homeViewModel.setTheStatusReady(homeViewModel.subject.value.toString())
                btn_ready_tutor.text = "not ready"
            }

        }

    }

}