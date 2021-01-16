package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import kotlinx.android.synthetic.main.fragment_admin_home.*

class AdminHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_subject.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections
                    .actionAdminHomeFragmentToAdminAddSubjectFragment()
            )
        }

        btn_admin_verify_tutor.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections
                    .actionAdminHomeFragmentToAdminVerifyTutorFragment()
            )
        }

        btn_change_tutor_subject.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections
                    .actionAdminHomeFragmentToAdminChangeTutorSubjectFragment()
            )
        }

        admin_btn_logout.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections
                    .actionAdminHomeFragmentToAuthentication()
            )
        }

    }

}