package com.a.papermaxx.home.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.domainmodule.domain.AllUsersUseCase
import com.a.domainmodule.domain.SignUpUseCase
import com.a.papermaxx.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeViewModel
@ViewModelInject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val allUsersUseCase: AllUsersUseCase
) : ViewModel() {

    var usersList = MutableLiveData<MutableList<UserModel>>()
    var chatsList = MutableLiveData<MutableList<UserModel>>()
    var adminId = MutableLiveData<String>()

    init {
        usersList.value = mutableListOf()
        chatsList.value = mutableListOf()
    }

    fun currentUser(): FirebaseUser? = signUpUseCase.currentUser()

    fun logout() = signUpUseCase.logout()

    fun getUsersList(text: String) {
        allUsersUseCase.getUsersList()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<UserModel>()
                    snapshot.children.forEach {
                        val value = it.child("Username").value.toString()
                        if (value.startsWith(text))
                            if ("$value@gmail.com" != currentUser()?.email)
                                result.add(
                                    UserModel(
                                        it.key.toString(),
                                        value,
                                        it.child("Name").value.toString()
                                    )
                                )
                    }
                    usersList.postValue(result)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    fun getChatsList(cUid: String) {
        allUsersUseCase.userDirect(cUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var conditionItem = ""
                    val result = mutableListOf<UserModel>()
                    val count = dataSnapshot.childrenCount
                    var counter = 1L
                    dataSnapshot.children.forEach {
                        allUsersUseCase.getUserInfo(it.key.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val currentModel = UserModel(
                                        it.key.toString(),
                                        dataSnapshot.value.toString(),
                                        ""
                                    )
                                    result.add(
                                        currentModel
                                    )
                                    if (currentModel.id == conditionItem) getChatsListHelper(result)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                        if (counter == count) {
                            conditionItem = it.key.toString()
                        } else counter++
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getAdminId(){
        allUsersUseCase.getAdminId()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    adminId.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getChatsListHelper(list: MutableList<UserModel>) {
        chatsList.postValue(list)
    }
}