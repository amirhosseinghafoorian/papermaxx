package com.a.papermaxx.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import com.a.papermaxx.home.ui.CompleteInfoFragment
import com.a.papermaxx.home.ui.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
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

        btn_update_profile.setOnClickListener {
            showUpdateProfileBottomSheet()
        }

        btn_change_full_name.setOnClickListener {
            showUpdateFullNameBottomSheet()
        }

    }

    private fun showUpdateFullNameBottomSheet() {
        val buttonSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.update_full_name_bottom_sheet, null)

        view.findViewById<MaterialButton>(R.id.btn_change_full_name).setOnClickListener {
            val newName =
                view.findViewById<TextInputLayout>(R.id.change_full_name_et).editText?.text.toString()
            if (newName.isNotBlank() && newName.isNotEmpty() && newName.length > 3 && newName.length < 20) {
                homeViewModel.setFullName(newName)
                buttonSheetDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Invalid Format", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSheetDialog.setContentView(view)
        buttonSheetDialog.show()
    }

    private fun showUpdateProfileBottomSheet() {
        val buttonSheetDialog = CompleteInfoFragment()
        buttonSheetDialog.show(requireActivity().supportFragmentManager, "")
    }

}