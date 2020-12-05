package com.a.domainmodule.domain

import com.a.domainmodule.inputValidation.ValidateInput
import com.a.remotemodule.remotes.HomeRemote
import com.a.remotemodule.remotes.UserRemote
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val validateInput: ValidateInput,
    private val userRemote: UserRemote,
    private val homeRemote: HomeRemote
) {

    fun validateName(name: String) = validateInput.checkNameValidation(name)

    fun validateEmail(email: String) = validateInput.checkEmailValidation(email)

    fun validatePassword(password: String) = validateInput.checkPasswordValidation(password)

    fun validateTheSamePassword(password: String, repeatPassword: String) =
        validateInput.checkTheSamePassword(password, repeatPassword)

    fun signUp(email: String, password: String): Task<AuthResult> =
        userRemote.signUp(email, password)

    fun login(email: String, password: String): Task<AuthResult> =
        userRemote.login(email, password)

    fun currentUser(): FirebaseUser? = homeRemote.currentUser()

    fun logout() = homeRemote.logout()
}