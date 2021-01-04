package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_complete_info.*

@AndroidEntryPoint
class CompleteInfoFragment : BottomSheetDialogFragment() {

    private val homeView: HomeViewModel by viewModels()
    private var grade = ""
    private var subject = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val gradeList = mutableListOf("")
        val subjectList = mutableListOf("")

        val gradeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, gradeList
        )

        val subjectAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, subjectList
        )

        homeView.gradeList.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                if (list.size > 0) {
                    gradeAdapter.clear()
                    gradeAdapter.add("")
                    gradeAdapter.addAll(list)
                    confirm_pb_2.visibility = View.GONE
                }
            }
        })

        homeView.subjectList.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                if (list.size > 0) {
                    subjectAdapter.clear()
                    subjectAdapter.add("")
                    subjectAdapter.addAll(list)
                    confirm_pb_1.visibility = View.GONE
                }
            }
        })

        grade_sp.adapter = gradeAdapter
        subject_sp.adapter = subjectAdapter

        homeView.getGradeList()
        homeView.getSubjectList()
        homeView.getAdminId()

        grade_sp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                grade = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        subject_sp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                subject = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btn_confirm_info.setOnClickListener {
            if (validateInfo()) {
                homeView.setGrade(grade)
                homeView.setSubject(subject)
                Toast.makeText(requireContext(), "navigated", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(
//                    CompleteInfoFragmentDirections.actionCompleteInfoFragmentToHomeFragment(
//                        GeneralStrings.keySignUp
//                    )
//                )
            }
        }
    }


    private fun validateInfo(): Boolean {
        return when {
            grade == "" -> {
                Toast.makeText(requireContext(), "set your grade", Toast.LENGTH_SHORT).show()
                false
            }
            subject == "" -> {
                Toast.makeText(requireContext(), "set your subject", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

}