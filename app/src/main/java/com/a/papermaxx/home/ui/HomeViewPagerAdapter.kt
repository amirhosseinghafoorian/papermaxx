package com.a.papermaxx.home.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val listNames = mutableListOf<String>()
    private val listFragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return listFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragments[position]
    }

    fun addFragment(fragment: Fragment, name: String) {
        listFragments.add(fragment)
        listNames.add(name)
    }

    fun getName(position: Int): String = listNames[position]

}