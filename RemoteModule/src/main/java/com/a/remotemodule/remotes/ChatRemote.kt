package com.a.remotemodule.remotes

import android.net.Uri
import com.a.remotemodule.general.GeneralStrings
import com.a.remotemodule.models.MessageModel
import com.google.android.gms.tasks.Task
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

    fun sendMessage(messageId: String, message: MessageModel, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child(messageId)
            .setValue(message)
    }

    fun sendPicture(messageId: String, message: MessageModel, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child(messageId)
            .setValue(message)
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

    fun changeLastSeen(uid: String, chatId: String, value: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("seen:$uid").setValue(value)
    }

    fun setOffline(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("online:$uid").setValue("offline")
    }

    fun startCall(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("call:$uid").setValue("calling")
    }

    fun establishCall(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("call:$uid").setValue("talking")
    }

    fun endCall(uid: String, chatId: String) {
        rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("call:$uid").setValue("endCall")
    }

    fun checkOnline(uid: String, chatId: String): DatabaseReference {
        return rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("online:$uid")
    }

    fun checkSeen(uid: String, chatId: String): DatabaseReference {
        return rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("seen:$uid")
    }

    fun checkCall(uid: String, chatId: String): DatabaseReference {
        return rootReference
            .child("ChatRooms")
            .child(chatId)
            .child("call:$uid")
    }

    fun uploadImage(filePathUri: Uri, chatId: String, filename: String): UploadTask {
        val storageReference2: StorageReference = storageReference.child(chatId).child(
            filename
        )
        return storageReference2.putFile(filePathUri)
    }

    fun downLoadPic(chatId: String, filename: String): Task<ByteArray> {
        val oneMegaByte: Long = GeneralStrings.FOUR_MEGABYTE
        return storageReference.child(chatId).child(filename).getBytes(oneMegaByte)
    }

}