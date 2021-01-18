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

    fun roomIdDecide(chatId: String) =
        chatIdDecider.decideVideoCallRoom(chatId)

    fun getAdminId() = homeRemote.getAdminId()

    fun putChatInDirect(base: String, target: String) = homeRemote.putChatInDirect(base, target)

    fun getUserInfo(uid: String) = homeRemote.getUserInfo(uid)

    fun getGradeList() = homeRemote.getGradeList()

    fun getSubjectList() = homeRemote.getSubjectList()

    fun setGrade(uid: String, grade: String) = homeRemote.setGrade(uid, grade)

    fun sendVerifyRequest(uid: String) = homeRemote.sendVerifyRequest(uid)

    fun sendVerifyRequestDetails(uid: String, subject: String, place: String) =
        homeRemote.sendVerifyRequestDetails(uid, subject, place)

    fun setVerifyRequestWorking(uid: String) = homeRemote.setVerifyRequestWorking(uid)

    fun setSubject(uid: String, subject: String) = homeRemote.setSubject(uid, subject)

    fun setFullName(uid: String, name: String) = homeRemote.setFullName(uid, name)

    fun getFullName(uid: String) = homeRemote.getFullName(uid)

    fun getGrade(uid: String) = homeRemote.getGrade(uid)

    fun getSubject(uid: String) = homeRemote.getSubject(uid)

    fun getReadyStatus(uid: String, subject: String) = homeRemote.getReadyStatus(uid, subject)

    fun setTheStatusReady(uid: String, subject: String) = homeRemote.setTheStatusReady(uid, subject)

    fun setTheStatusNotReady(uid: String, subject: String) =
        homeRemote.setTheStatusNotReady(uid, subject)

    fun getUserType(uid: String) = homeRemote.getUserType(uid)

    fun getTutorVerifyRequest(uid: String) = homeRemote.getTutorVerifyRequest(uid)

}