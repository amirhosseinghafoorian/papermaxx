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
import com.a.papermaxx.general.GeneralStrings
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var base: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        base =
            arguments?.let { HomeFragmentArgs.fromBundle(it).base }.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewPagerInit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (base == "signUp") {
            // it should take the admin id dynamic
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToChatFragment(
                    GeneralStrings.adminId
                )
            )
        }

        btn_logout.setOnClickListener {
            showLogoutDialog()
        }

    }

    private fun homeViewPagerInit() {
        val viewPagerAdapter = HomeViewPagerAdapter(childFragmentManager, lifecycle)
        viewPagerAdapter.addFragment(HomeFragmentTab1(), "Messages")
        viewPagerAdapter.addFragment(HomeFragmentTab2(), "Users")
        home_view_pager.adapter = viewPagerAdapter
        home_view_pager.isUserInputEnabled = false
        TabLayoutMediator(
            (home_page_TL as TabLayout),
            (home_view_pager as ViewPager2)
        ) { tab, position ->
            tab.text = viewPagerAdapter.getName(position)
        }.attach()
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to logout ? ")
            .setPositiveButton("Yes") { _, _ ->
                homeViewModel.logout()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAuthentication())
            }
            .setNegativeButton("No") { dialog, _ -> // User cancelled the dialog
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }

}