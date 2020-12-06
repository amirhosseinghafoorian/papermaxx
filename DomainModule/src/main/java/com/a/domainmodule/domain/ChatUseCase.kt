package com.a.domainmodule.domain

import com.a.domainmodule.inputValidation.GetCurrentTime
import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.remotes.ChatRemote
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRemote: ChatRemote,
    private val currentTime: GetCurrentTime
) {

    fun createChatRoom(name: String) = chatRemote.createChatRoom(name)

    fun getChatRoom(chatId: String) = chatRemote.getChatRoom(chatId)

    fun sendMessage(message: MessageModel, chatId: String, senderId: String) {
        message.id =
            currentTime.customDateToString("year", null) + "_" +
                    currentTime.customDateToString("month", null) + "_" +
                    currentTime.customDateToString("day", null) + "_" +
                    currentTime.customDateToString("hour", null) + "_" +
                    currentTime.customDateToString("minute", null) + "_" +
                    currentTime.customDateToString("second", null) + "_" +
                    currentTime.customDateToString("milli", null)
        chatRemote.sendMessage(message, chatId, senderId)
    }

    fun createOnlineStatus(uid: String, chatId: String) = chatRemote.createOnlineStatus(uid, chatId)

    fun setOnline(uid: String, chatId: String) = chatRemote.setOnline(uid, chatId)

    fun setOffline(uid: String, chatId: String) = chatRemote.setOffline(uid, chatId)

    fun checkSeen(uid: String, chatId: String) = chatRemote.checkSeen(uid, chatId)

}