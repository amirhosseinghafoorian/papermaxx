package com.a.remotemodule.remotes

import android.net.Uri
import com.a.remotemodule.general.GeneralStrings
import com.a.remotemodule.models.MessageModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class ChatRemote @Inject constructor(
    private val rootReference: DatabaseReference,
    private val storageReference: StorageReference
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
            .child("message").setValue(GeneralStrings.keyText + ":" + message.text + ":" + senderId)
    }

    fun sendPicture(message: MessageModel, chatId: String, senderId: String) { // check again
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child(message.id)
            .child("message").setValue(GeneralStrings.keyPic + ":" + message.text + ":" + senderId)
    }

    fun createOnlineStatus(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("online:$uid").setValue("")
    }

    fun setOnline(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("online:$uid").setValue("online")
    }

    fun setOffline(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("online:$uid").setValue("offline")
    }

    fun checkSeen(uid: String, chatId: String): DatabaseReference {
        return rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("online:$uid")
    }

    fun uploadImage(filePathUri: Uri, chatId: String, filename: String): UploadTask {
        val storageReference2: StorageReference = storageReference.child(chatId).child(
            filename
        )
        return storageReference2.putFile(filePathUri)
    }
}