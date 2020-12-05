package com.a.papermaxx.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import kotlinx.android.synthetic.main.fragment_choose_auth.*

class ChooseAuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chs_btn_1.setOnClickListener {
            findNavController().navigate(ChooseAuthFragmentDirections.actionChooseAuthFragmentToLoginFragment())
        }
        chs_btn_2.setOnClickListener {
            findNavController().navigate(ChooseAuthFragmentDirections.actionChooseAuthFragmentToSignUpFragment())
        }
    }
}