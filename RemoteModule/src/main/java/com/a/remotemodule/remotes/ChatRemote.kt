package com.a.remotemodule.remotes

import com.a.remotemodule.models.MessageModel
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class ChatRemote @Inject constructor(
    private val rootReference: DatabaseReference
) {

    fun createChatRoom(name: String) {
        rootReference
            .child("ChatRooms")
            .child(name).setValue("")
    }

    fun getChatRoom(chatId: String): DatabaseReference {
        return rootReference
            .child("ChatRooms")
            .child(chatId)
    }

    fun sendMessage(message: MessageModel, chatId: String, senderId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child(message.id)
            .child("message").setValue(message.text + ":" + senderId)
    }
}