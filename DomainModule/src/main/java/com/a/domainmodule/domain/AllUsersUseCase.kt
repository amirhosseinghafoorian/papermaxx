package com.a.domainmodule.domain

import com.a.domainmodule.inputValidation.ChatIdDecider
import com.a.remotemodule.remotes.HomeRemote
import javax.inject.Inject

class AllUsersUseCase @Inject constructor(
    private val homeRemote: HomeRemote,
    private val chatIdDecider: ChatIdDecider
) {

    fun getUsersList() = homeRemote.getUsersList()

    fun usernameFromUid(uid: String) = homeRemote.usernameFromUid(uid)

    fun userDirect(uid: String) = homeRemote.userDirect(uid)

    fun chatIdDecide(senderId: String, receiverId: String) =
        chatIdDecider.decideChatIdFormat(senderId, receiverId)

    fun getAdminId() = homeRemote.getAdminId()

    fun putChatInDirect(base: String, target: String) = homeRemote.putChatInDirect(base, target)

    fun getUserInfo(uid: String) = homeRemote.getUserInfo(uid)

    fun getGradeList() = homeRemote.getGradeList()

    fun getSubjectList() = homeRemote.getSubjectList()

    fun setGrade(uid: String, grade: String) = homeRemote.setGrade(uid, grade)

    fun setSubject(uid: String, subject: String) = homeRemote.setSubject(uid, subject)

    fun getFullName(uid : String) = homeRemote.getFullName(uid)

    fun getGrade(uid : String) = homeRemote.getFullName(uid)

    fun getSubject(uid : String) = homeRemote.getFullName(uid)

}