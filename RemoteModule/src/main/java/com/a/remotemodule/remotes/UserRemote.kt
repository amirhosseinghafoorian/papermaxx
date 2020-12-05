package com.a.remotemodule.remotes

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject


class UserRemote
@Inject constructor(
    private val auth: FirebaseAuth,
    private val rootReference: DatabaseReference
) {

    fun signUp(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword("$email@gmail.com", password)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword("$email@gmail.com", password)
    }

    fun setUserInfo(name: String, username: String) {
        rootReference
            .child("Users")
            .child(auth.currentUser?.uid.toString())
            .child("Name").setValue(name)
        rootReference
            .child("Users")
            .child(auth.currentUser?.uid.toString())
            .child("Username").setValue(username)
        rootReference
            .child("Users")
            .child(auth.currentUser?.uid.toString())
            .child("Directs").setValue("")
    }

}
