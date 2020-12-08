package com.a.domainmodule.domain

import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.remotes.ChatRemote
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRemote: ChatRemote
) {

    fun createChatRoom(name: String) = chatRemote.createChatRoom(name)

    fun getChatRoom(chatId: String) = chatRemote.getChatRoom(chatId)

    fun sendMessage(message: MessageModel, chatId: String, senderId: String) {
        message.id = System.currentTimeMillis().toString()
        chatRemote.sendMessage(message, chatId, senderId)
    }

    fun sendPicture(message: MessageModel, chatId: String, senderId: String) {
        message.id = System.currentTimeMillis().toString()
        chatRemote.sendPicture(message, chatId, senderId)
    }

    fun createOnlineStatus(uid: String, chatId: String) = chatRemote.createOnlineStatus(uid, chatId)

    fun setOnline(uid: String, chatId: String) = chatRemote.setOnline(uid, chatId)

    fun setOffline(uid: String, chatId: String) = chatRemote.setOffline(uid, chatId)

    fun checkSeen(uid: String, chatId: String) = chatRemote.checkSeen(uid, chatId)

}