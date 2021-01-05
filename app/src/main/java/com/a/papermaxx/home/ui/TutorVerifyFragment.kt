package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tutor_verify.*

@AndroidEntryPoint
class TutorVerifyFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutor_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel.tutorVerifyRequest.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "pending") {
                    btn_verify_tutor.visibility = View.GONE
                    verify_subject_et.visibility = View.GONE
                    verify_education_place_et.visibility = View.GONE
                    verify_message_tv.visibility = View.VISIBLE
                } else if (it == "not applied") {
                    btn_verify_tutor.visibility = View.VISIBLE
                    verify_subject_et.visibility = View.VISIBLE
                    verify_education_place_et.visibility = View.VISIBLE
                }
            }
        })

        homeViewModel.getTutorVerifyRequest()

        btn_verify_tutor.setOnClickListener {
            if (validate()) {
                homeViewModel.getTutorVerifyRequest()
                homeViewModel.sendVerifyRequest()
            }
        }
    }

    private fun validate(): Boolean {
        val subject = verify_subject_et.editText?.text.toString()
        val education = verify_education_place_et.editText?.text.toString()
        return when {
            subject.isEmpty() -> {
                Toast.makeText(requireContext(), "subject can not be empty", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            subject.length < 3 -> {
                Toast.makeText(requireContext(), "subject too short", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            subject.length > 50 -> {
                Toast.makeText(requireContext(), "subject to long", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            education.isEmpty() -> {
                Toast.makeText(requireContext(), "education can not be empty", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            education.length < 5 -> {
                Toast.makeText(requireContext(), "subject too short", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            education.length > 50 -> {
                Toast.makeText(requireContext(), "subject too long", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            else -> true
        }
    }

}