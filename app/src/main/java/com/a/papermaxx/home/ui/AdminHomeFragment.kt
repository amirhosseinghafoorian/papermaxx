package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
            Toast.makeText(requireContext(), "navigated to add subject", Toast.LENGTH_SHORT).show()
        }

        btn_admin_verify_tutor.setOnClickListener {
            Toast.makeText(requireContext(), "navigated to admin verify tutor", Toast.LENGTH_SHORT)
                .show()
        }

        btn_change_tutor_subject.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "navigated to change tutor subject",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}