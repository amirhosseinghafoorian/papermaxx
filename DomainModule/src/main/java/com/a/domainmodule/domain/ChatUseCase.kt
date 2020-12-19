package com.a.domainmodule.domain

import android.net.Uri
import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.remotes.ChatRemote
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRemote: ChatRemote
) {

    fun createChatRoom(name: String) = chatRemote.createChatRoom(name)

    fun getChatRoom(chatId: String) = chatRemote.getChatRoom(chatId)

    fun sendMessage(message: MessageModel, chatId: String, senderId: String) {
        val id = System.currentTimeMillis().toString()
        message.id = senderId
        chatRemote.sendMessage(id, message, chatId)
    }

    fun sendPicture(messageId: String, message: MessageModel, chatId: String, senderId: String) {
        message.id = senderId
        chatRemote.sendPicture(messageId, message, chatId)
    }

    fun uploadImage(filePathUri: Uri, chatId: String, filename: String) =
        chatRemote.uploadImage(filePathUri, chatId, filename)

    fun createOnlineStatus(uid: String, chatId: String) = chatRemote.createOnlineStatus(uid, chatId)

    fun setOnline(uid: String, chatId: String) = chatRemote.setOnline(uid, chatId)

    fun changeLastSeen(uid: String, chatId: String , value : String) = chatRemote.changeLastSeen(uid, chatId , value)

    fun setOffline(uid: String, chatId: String) = chatRemote.setOffline(uid, chatId)

    fun startCall(uid: String, chatId: String) = chatRemote.startCall(uid, chatId)

    fun startRing(uid: String, chatId: String) = chatRemote.startRing(uid, chatId)

    fun establishCall(uid: String, chatId: String) = chatRemote.establishCall(uid, chatId)

    fun endCall(uid: String, chatId: String) = chatRemote.endCall(uid, chatId)

    fun checkOnline(uid: String, chatId: String) = chatRemote.checkOnline(uid, chatId)

    fun checkSeen(uid: String, chatId: String) = chatRemote.checkSeen(uid, chatId)

    fun checkCall(uid: String, chatId: String) = chatRemote.checkCall(uid, chatId)

    fun downLoadPic(chatId: String, filename: String) = chatRemote.downLoadPic(chatId, filename)

}

