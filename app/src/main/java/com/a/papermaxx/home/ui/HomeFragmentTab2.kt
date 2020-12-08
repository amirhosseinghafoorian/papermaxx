package com.a.papermaxx.home.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_tab2.*

@AndroidEntryPoint
class HomeFragmentTab2 : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_tab2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myAdapter = homeViewModel.usersList.value?.let { UserListAdapter(it) }

        home_page_user_list_recycler.apply {
            adapter = myAdapter
        }

        homeViewModel.usersList.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                users_progress_bar.visibility = View.GONE
                myAdapter?.list = list
                myAdapter?.notifyDataSetChanged()
            }
        })

        home_page_search_user.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Todo here should be uncommented for the release
//                if (s.toString() != "") {
                users_progress_bar.visibility = View.VISIBLE
                homeViewModel.getUsersList(s.toString())
//                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

}