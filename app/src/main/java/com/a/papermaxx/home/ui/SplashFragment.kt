package com.a.papermaxx.home.ui

import android.os.Bundle
import android.os.Handler
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

        // use get user type here

        val handler = Handler()
        handler.postDelayed({
            if (homeViewModel.currentUser() == null) findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToNavigation()
            )
            else findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToHomeFragment(
                    GeneralStrings.keySplash
                )
            )
        }, 1000)
    }
}