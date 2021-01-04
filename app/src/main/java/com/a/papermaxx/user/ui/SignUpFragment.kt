package com.a.papermaxx.user.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel.currentUser.observe(viewLifecycleOwner, { result ->
            if (result == null || result.exception?.message != null) {
                Toast.makeText(
                    requireContext(),
                    "Failed : ${result.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("baby", result.exception?.message.toString())
            } else {
                signUpViewModel.setUserInfo(
                    signUp_et_4.editText?.text.toString(),
                    signUp_et_1.editText?.text.toString()
                )
                if (!cb_is_tutor.isChecked) {
                    findNavController().navigate(
                        SignUpFragmentDirections.actionGlobalCompleteInfoFragment()
                    )
                }
            }
        })

        btn_sign_up.setOnClickListener {
            val email = signUp_et_1.editText?.text.toString()
            val password = signUp_et_2.editText?.text.toString()
            val repeatPassword = signUp_et_3.editText?.text.toString()
            val name = signUp_et_4.editText?.text.toString()
            if (validateInputs(email.toLowerCase(Locale.ROOT), password, repeatPassword, name)) {
                signUpViewModel.signUp(email.toLowerCase(Locale.ROOT), password)
            }
        }
    }

    private fun validateInputs(
        email: String,
        password: String,
        repeatPassword: String,
        name: String
    ): Boolean {
        signUp_et_1.editText?.clearFocus()
        signUp_et_2.editText?.clearFocus()
        signUp_et_3.editText?.clearFocus()
        signUp_et_4.editText?.clearFocus()
        when {
            signUpViewModel.validateName(name) != "" -> {
                signUp_et_4.editText?.error = signUpViewModel.validateName(name)
                signUp_et_4.editText?.requestFocus()
                return false
            }
            signUpViewModel.validateEmail(email) != "" -> {
                signUp_et_1.editText?.error = signUpViewModel.validateEmail(email)
                signUp_et_1.editText?.requestFocus()
                return false
            }
            signUpViewModel.validatePassword(password) != "" -> {
                signUp_et_2.editText?.error = signUpViewModel.validatePassword(password)
                signUp_et_2.editText?.requestFocus()
                return false
            }
            signUpViewModel.validateTheSamePassword(password, repeatPassword) != "" -> {
                signUp_et_3.editText?.error =
                    signUpViewModel.validateTheSamePassword(password, repeatPassword)
                signUp_et_3.editText?.requestFocus()
                return false
            }
        }
        return true
    }

}