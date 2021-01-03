package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import com.a.papermaxx.general.GeneralStrings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_complete_info.*

@AndroidEntryPoint
class CompleteInfoFragment : Fragment() {

    private val homeView: HomeViewModel by viewModels()
    private var grade = ""
    private var subject = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradeList = mutableListOf("N/A")
        val subjectList = mutableListOf("N/A")

        val gradeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, gradeList
        )

        val subjectAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, subjectList
        )

        homeView.gradeList.observe(viewLifecycleOwner, { list ->
            gradeAdapter.clear()
            gradeAdapter.addAll(list)
        })

        homeView.subjectList.observe(viewLifecycleOwner, { list ->
            subjectAdapter.clear()
            subjectAdapter.addAll(list)
        })

        grade_sp.adapter = gradeAdapter
        subject_sp.adapter = subjectAdapter

        grade_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                grade = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        subject_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                subject = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btn_confirm_info.setOnClickListener {
            if (validateInfo()) {
                homeView.setGrade()
                homeView.setSubject()
                findNavController().navigate(
                    CompleteInfoFragmentDirections.actionCompleteInfoFragmentToHomeFragment(
                        GeneralStrings.keySignUp
                    )
                )
            }
        }
    }

    private fun validateInfo(): Boolean {
        return when {
            grade == "N/A" -> {
                Toast.makeText(requireContext(), "set your grade", Toast.LENGTH_SHORT).show()
                false
            }
            subject == "N/A" -> {
                Toast.makeText(requireContext(), "set your subject", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

}