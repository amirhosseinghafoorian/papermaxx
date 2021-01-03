package com.a.papermaxx.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import com.a.papermaxx.home.ui.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profile_username_tv.text = homeViewModel.currentUser()?.email?.substringBeforeLast('@')

        homeViewModel.fullName.observe(viewLifecycleOwner, {
            if (it != null) {
                profile_name_tv.text = it
            }
        })

        homeViewModel.subject.observe(viewLifecycleOwner, {
            if (it != null) {
                subject_tv.text = it
            }
        })

        homeViewModel.grade.observe(viewLifecycleOwner, {
            if (it != null) {
                grade_tv.text = it
            }
        })

        homeViewModel.getFullName()
        homeViewModel.getSubject()
        homeViewModel.getGrade()
    }

}