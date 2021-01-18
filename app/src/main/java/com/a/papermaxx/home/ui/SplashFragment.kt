package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import com.a.papermaxx.general.GeneralStrings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.userType.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    "student" -> {
                        homeViewModel.getGrade() // check if student has completed his info
                    }
                    "tutor" -> {
                        homeViewModel.getTutorVerifyRequest()
                    }
                }
            }
        })

        homeViewModel.grade.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "null") {
                    findNavController().navigate(
                        SplashFragmentDirections
                            .actionSplashFragmentToCompleteInfoFragment()
                    )
                } else {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToHomeFragment(
                            GeneralStrings.keySplash
                        )
                    )
                }
            }
        })

        homeViewModel.tutorVerifyRequest.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "pending" || it == "not applied" || it == "verified") {
                    findNavController().navigate(
                        SplashFragmentDirections
                            .actionSplashFragmentToTutorVerifyFragment()
                    )
                } else if (it == "working") {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToTutorHomeFragment(
                            GeneralStrings.keySplash
                        )
                    )
                }
            }
        })

        if (homeViewModel.currentUser() == null) findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToNavigation()
        )
        else {
            homeViewModel.getUserType()
        }

    }
}