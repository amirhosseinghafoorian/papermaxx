package com.a.papermaxx.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_tab2.*

@AndroidEntryPoint
class HomeFragmentTab2 : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var subject = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_tab2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subjectList = mutableListOf("")

        val subjectAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, subjectList
        )

        homeViewModel.subjectList.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                if (list.size > 0) {
                    subjectAdapter.clear()
                    subjectAdapter.add("")
                    subjectAdapter.addAll(list)
                }
            }
        })

        homeViewModel.foundTutor.observe(viewLifecycleOwner, {
            if (it != null) {
                homeViewModel.bringTutorToChat(
                    subject,
                    it,
                    homeViewModel.currentUser()?.uid.toString()
                )
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToChatFragment(
                        it,
                        false
                    )
                )
            }
        })

        search_tutor_subject_sp.adapter = subjectAdapter

        homeViewModel.getSubjectList()

        btn_search_tutor.setOnClickListener {
            homeViewModel.getChatsList(homeViewModel.currentUser()?.uid.toString())
            homeViewModel.searchForTutors(subject)
        }

        search_tutor_subject_sp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                @SuppressLint("ResourceAsColor")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent.selectedItem.toString() != "") {
                        subject = parent.selectedItem.toString()
                        btn_search_tutor.isEnabled = true
                        btn_search_tutor.text = "Search for tutor"
                    } else {
                        btn_search_tutor.isEnabled = false
                        btn_search_tutor.text = "Disabled until subject is set"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }

}