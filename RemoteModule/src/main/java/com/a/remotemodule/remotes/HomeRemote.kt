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

    fun getUserInfo(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Username")
    }

    fun getUsersList(): DatabaseReference {
        return rootReference
            .child("Users")
    }

    fun usernameFromUid(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Username")
    }

    fun userDirect(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Directs")
    }

    fun putChatInDirect(base: String, target: String) {
        rootReference
            .child("Users")
            .child(base)
            .child("Directs")
            .child(target).setValue("")
    }

    fun getAdminId(): DatabaseReference {
        return rootReference
            .child("Dynamic")
            .child("adminId")
    }

    fun getGradeList(): DatabaseReference {
        return rootReference
            .child("Dynamic")
            .child("gradeList")
    }

    fun getSubjectList(): DatabaseReference {
        return rootReference
            .child("Dynamic")
            .child("subjectList")
    }

    fun setGrade(uid: String, grade: String) {
        rootReference
            .child("Users")
            .child(uid)
            .child("Grade")
            .setValue(grade)
    }

    fun sendVerifyRequest(uid: String) {
        rootReference
            .child("Tutors")
            .child("Status")
            .child(uid)
            .setValue("pending")
    }

    fun sendVerifyRequestDetails(uid: String, subject: String, place: String) {
        rootReference
            .child("Users")
            .child(uid)
            .child("Subject")
            .setValue(subject)
        rootReference
            .child("Users")
            .child(uid)
            .child("Education")
            .setValue(place)
    }

    fun setVerifyRequestWorking(uid: String) {
        rootReference
            .child("Tutors")
            .child("Status")
            .child(uid)
            .setValue("working")
    }

    fun setSubject(uid: String, subject: String) {
        rootReference
            .child("Users")
            .child(uid)
            .child("Subject")
            .setValue(subject)
    }

    fun setFullName(uid: String, name: String) {
        rootReference
            .child("Users")
            .child(uid)
            .child("Name")
            .setValue(name)
    }

    fun getFullName(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Name")
    }

    fun getGrade(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Grade")
    }

    fun getSubject(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Subject")
    }

    fun getReadyStatus(uid: String, subject: String): DatabaseReference {
        return rootReference
            .child("Tutors")
            .child("subjectTutors")
            .child(subject)
            .child(uid)
    }

    fun setTheStatusNotReady(uid: String, subject: String) {
        rootReference
            .child("Tutors")
            .child("subjectTutors")
            .child(subject)
            .child(uid)
            .setValue("not ready")
    }

    fun setTheStatusReady(uid: String, subject: String) {
        rootReference
            .child("Tutors")
            .child("subjectTutors")
            .child(subject)
            .child(uid)
            .setValue("ready")
    }

    fun getUserType(uid: String): DatabaseReference {
        return rootReference
            .child("Users")
            .child(uid)
            .child("Type")
    }

    fun getTutorVerifyRequest(uid: String): DatabaseReference {
        return rootReference
            .child("Tutors")
            .child("Status")
            .child(uid)
    }

    fun searchForTutors(subject: String): DatabaseReference {
        return rootReference
            .child("Tutors")
            .child("subjectTutors")
            .child(subject)
    }

    fun bringTutorToChat(subject: String, tutorId: String, uid: String) {
        rootReference
            .child("Tutors")
            .child("subjectTutors")
            .child(subject)
            .child(tutorId)
            .setValue(uid)
    }

    fun monitorFoundStudent(subject: String, uid: String): DatabaseReference {
        return rootReference
            .child("Tutors")
            .child("subjectTutors")
            .child(subject)
            .child(uid)
    }

    fun sendChangeSubjectRequest(message: String, uid: String) {
        rootReference
            .child("Tutors")
            .child("change_subject_message")
            .child(uid)
            .setValue(message)

    }

}