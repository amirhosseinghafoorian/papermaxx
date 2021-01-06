package com.a.papermaxx.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_tab1.*

@AndroidEntryPoint
class TutorHomeFragmentTab1 : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var myAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutor_home_tab1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myAdapter = homeViewModel.chatsList.value?.let { ChatListAdapter(it) }!!

        //      create a new one for this
        home_page_chat_list_recycler.apply {
            adapter = myAdapter
        }

        homeViewModel.chatsList.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                //      create a new one for this
                directs_progress_bar.visibility = View.GONE
                myAdapter.list = list
                myAdapter.notifyDataSetChanged()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        //      create a new one for this
        directs_progress_bar.visibility = View.VISIBLE
        homeViewModel.getChatsList(homeViewModel.currentUser()?.uid.toString())
    }
}