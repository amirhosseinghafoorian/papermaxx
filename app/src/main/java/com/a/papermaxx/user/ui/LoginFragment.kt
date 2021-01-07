package com.a.papermaxx.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import com.a.papermaxx.general.GeneralStrings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel.userType.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "student") {
                    signUpViewModel.getSubject()
                } else if (it == "tutor") {
                    signUpViewModel.getTutorVerifyRequest()
                }
            }
        })

        signUpViewModel.tutorVerifyRequest.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "pending" || it == "not applied" || it == "verified") {
                    findNavController().navigate(
                        LoginFragmentDirections.actionGlobalTutorVerifyFragment()
                    )
                } else if (it == "working") {
                    findNavController().navigate(
                        LoginFragmentDirections.actionGlobalTutorHomeFragment(
                            GeneralStrings.keyLogin
                        )
                    )
                }
            }
        })

        signUpViewModel.currentUser.observe(viewLifecycleOwner, { result ->
            if (result == null || result.exception?.message != null) {
                Toast.makeText(
                    requireContext(),
                    "Failed : ${result.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                signUpViewModel.getUserType()
            }
        })

        signUpViewModel.subject.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "null") {
                    findNavController().navigate(
                        LoginFragmentDirections.actionGlobalCompleteInfoFragment()
                    )
                } else {
                    findNavController().navigate(
                        LoginFragmentDirections.actionGlobalHomeFragment(
                            GeneralStrings.keyLogin
                        )
                    )
                }
            }
        })

        btn_login.setOnClickListener {
            val email = login_et_1.editText?.text.toString()
            val password = login_et_2.editText?.text.toString()
            if (validateInputs(email.toLowerCase(Locale.ROOT), password)) {
                signUpViewModel.logout()
                signUpViewModel.login(email.toLowerCase(Locale.ROOT), password)
            }
        }
    }

    private fun validateInputs(
        email: String,
        password: String
    ): Boolean {
        login_et_1.editText?.clearFocus()
        login_et_2.editText?.clearFocus()
        when {
            signUpViewModel.validateEmail(email) != "" -> {
                login_et_1.editText?.error = signUpViewModel.validateEmail(email)
                login_et_1.editText?.requestFocus()
                return false
            }
            signUpViewModel.validatePassword(password) != "" -> {
                login_et_2.editText?.error = signUpViewModel.validatePassword(password)
                login_et_2.editText?.requestFocus()
                return false
            }
        }
        return true
    }
}