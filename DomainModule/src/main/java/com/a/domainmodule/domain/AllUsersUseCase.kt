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

}