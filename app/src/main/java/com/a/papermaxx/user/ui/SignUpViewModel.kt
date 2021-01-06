package com.a.papermaxx.user.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.domainmodule.domain.AllUsersUseCase
import com.a.domainmodule.domain.SignUpUseCase
import com.a.domainmodule.domain.UserInfoUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SignUpViewModel
@ViewModelInject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val userInfoUseCase: UserInfoUseCase,
    private val allUsersUseCase: AllUsersUseCase
) : ViewModel() {

    val currentUser = MutableLiveData<Task<AuthResult>>()
    var userType = MutableLiveData<String>()
    var tutorVerifyRequest = MutableLiveData<String>()

    fun currentUser(): FirebaseUser? = signUpUseCase.currentUser()

    fun validateName(name: String) = signUpUseCase.validateName(name)

    fun validateEmail(email: String) = signUpUseCase.validateEmail(email)

    fun validatePassword(password: String) = signUpUseCase.validatePassword(password)

    fun validateTheSamePassword(password: String, repeatPassword: String) =
        signUpUseCase.validateTheSamePassword(password, repeatPassword)

    fun signUp(email: String, password: String) {
        val result = signUpUseCase.signUp(email, password)
        result.addOnCompleteListener {
            currentUser.postValue(result)
        }
    }

    fun login(email: String, password: String) {
        val result = signUpUseCase.login(email, password)
        result.addOnCompleteListener {
            currentUser.postValue(result)
        }
    }

    fun logout() = signUpUseCase.logout()

    fun setUserInfo(name: String, username: String) = userInfoUseCase.setUserInfo(name, username)

    fun setTutorInfo(name: String, username: String) = userInfoUseCase.setTutorInfo(name, username)

    fun getUserType() {
        allUsersUseCase.getUserType(currentUser()?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userType.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getTutorVerifyRequest() {
        allUsersUseCase.getTutorVerifyRequest(currentUser()?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    tutorVerifyRequest.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

}