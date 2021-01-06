package com.a.papermaxx.home.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.a.papermaxx.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.btn_logout
import kotlinx.android.synthetic.main.fragment_tutor_home.*

@AndroidEntryPoint
class TutorHomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var base: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        base =
            arguments?.let { TutorHomeFragmentArgs.fromBundle(it).base }.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutor_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewPagerInit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (base == "signUp") {
            homeViewModel.getAdminId()
        }

        homeViewModel.adminId.observe(viewLifecycleOwner, { id ->
            if (id != null) {
                // ***
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToChatFragment(
                        id,
                        true
                    )
                )
            }
        })

        tutor_btn_logout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun homeViewPagerInit() {
        val viewPagerAdapter = HomeViewPagerAdapter(childFragmentManager, lifecycle)
        viewPagerAdapter.addFragment(TutorHomeFragmentTab1(), "Messages")
        viewPagerAdapter.addFragment(TutorHomeFragmentTab2(), "work")
        tutor_home_view_pager.adapter = viewPagerAdapter
        tutor_home_view_pager.isUserInputEnabled = false
        TabLayoutMediator(
            (tutor_home_page_TL as TabLayout),
            (tutor_home_view_pager as ViewPager2)
        ) { tab, position ->
            tab.text = viewPagerAdapter.getName(position)
        }.attach()
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to logout ? ")
            .setPositiveButton("Yes") { _, _ ->
                homeViewModel.logout()
                // ***
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAuthentication())
            }
            .setNegativeButton("No") { dialog, _ -> // User cancelled the dialog
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }

}