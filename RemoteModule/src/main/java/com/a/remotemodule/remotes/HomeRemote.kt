package com.a.remotemodule.remotes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class HomeRemote
@Inject constructor(
    private val auth: FirebaseAuth,
    private val rootReference: DatabaseReference
) {

    fun currentUser(): FirebaseUser? = auth.currentUser

    fun logout() = auth.signOut()

    fun getUserInfo(uid : String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Username")
    }

    fun getUsersList(): DatabaseReference {
        return rootReference
            .child("Users")
    }

    fun usernameFromUid(uid : String) : DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Username")
    }

    fun userDirect(uid : String) : DatabaseReference{
        return rootReference
            .child("Users")
            .child(uid)
            .child("Directs")
    }

    fun putChatInDirect(base : String , target : String){
        rootReference
            .child("Users")
            .child(base)
            .child("Directs")
            .child(target).setValue("")
    }

}