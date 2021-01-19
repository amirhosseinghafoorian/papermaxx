package com.a.papermaxx.home.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.domainmodule.domain.AllUsersUseCase
import com.a.domainmodule.domain.SignUpUseCase
import com.a.papermaxx.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
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
    var gradeList = MutableLiveData<MutableList<String>>()
    var subjectList = MutableLiveData<MutableList<String>>()
    var adminId = MutableLiveData<String>()
    var fullName = MutableLiveData<String>()
    var grade = MutableLiveData<String>()
    var subject = MutableLiveData<String>()
    var userType = MutableLiveData<String>()
    var tutorVerifyRequest = MutableLiveData<String>()
    var tutorReadyStatus = MutableLiveData<String>()
    var foundTutor = MutableLiveData<String>()

    init {
        usersList.value = mutableListOf()
        chatsList.value = mutableListOf()
        gradeList.value = mutableListOf()
        subjectList.value = mutableListOf()
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

    fun getGradeList() {
        allUsersUseCase.getGradeList()
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    gradeList.value?.add(snapshot.value.toString())
                    gradeList.postValue(gradeList.value)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun getSubjectList() {
        allUsersUseCase.getSubjectList()
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val sub = snapshot.value.toString()
                    subjectList.value?.add(sub)
                    subjectList.postValue(subjectList.value)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun setGrade(grade: String) = allUsersUseCase.setGrade(currentUser()?.uid.toString(), grade)

    fun sendVerifyRequest() = allUsersUseCase.sendVerifyRequest(currentUser()?.uid.toString())

    fun sendVerifyRequestDetails(subject: String, place: String) =
        allUsersUseCase.sendVerifyRequestDetails(currentUser()?.uid.toString(), subject, place)

    fun setVerifyRequestWorking() =
        allUsersUseCase.setVerifyRequestWorking(currentUser()?.uid.toString())

    fun setSubject(subject: String) =
        allUsersUseCase.setSubject(currentUser()?.uid.toString(), subject)

    fun setFullName(name: String) = allUsersUseCase.setFullName(currentUser()?.uid.toString(), name)

    fun getAdminId() {
        allUsersUseCase.getAdminId()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    adminId.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getFullName() {
        allUsersUseCase.getFullName(currentUser()?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    fullName.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getGrade() {
        allUsersUseCase.getGrade(currentUser()?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    grade.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getSubject() {
        allUsersUseCase.getSubject(currentUser()?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    subject.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getReadyStatus(subject: String) {
        allUsersUseCase.getReadyStatus(currentUser()?.uid.toString(), subject)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    tutorReadyStatus.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun setTheStatusReady(subject: String) =
        allUsersUseCase.setTheStatusReady(currentUser()?.uid.toString(), subject)

    fun setTheStatusNotReady(subject: String) =
        allUsersUseCase.setTheStatusNotReady(currentUser()?.uid.toString(), subject)

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

    fun getChatsListHelper(list: MutableList<UserModel>) {
        chatsList.postValue(list)
    }

    fun searchForTutors(subject: String) {
        allUsersUseCase.searchForTutors(subject)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.value.toString() == "ready") {
                        var found = true
                        for (i in 0 until chatsList.value?.size!!) {
                            if (chatsList.value!![i].id == snapshot.key.toString()) {
                                found = false
                            }
                        }
                        if (found) {
                            foundTutor.postValue(snapshot.key.toString())
                        }

                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // if message delete added
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun bringTutorToChat(subject: String, tutorId: String, uid: String) =
        allUsersUseCase.bringTutorToChat(subject, tutorId, uid)

    fun sendChangeSubjectRequest(message: String) =
        allUsersUseCase.sendChangeSubjectRequest(message, currentUser()?.uid.toString())

}