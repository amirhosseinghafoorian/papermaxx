package com.a.papermaxx.chat.ui

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.domainmodule.domain.AllUsersUseCase
import com.a.domainmodule.domain.ChatUseCase
import com.a.domainmodule.domain.SignUpUseCase
import com.a.papermaxx.general.FileExtension
import com.a.papermaxx.general.GeneralStrings
import com.a.remotemodule.models.CallState
import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.models.MessageType
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatViewModel @ViewModelInject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val chatUseCase: ChatUseCase,
    private val allUsersUseCase: AllUsersUseCase,
    private val fileExtension: FileExtension

) : ViewModel() {

    var receiverUsername = MutableLiveData<String>()
    var isInDirect = MutableLiveData<Boolean>()
    var chatMessages = MutableLiveData<MutableList<MessageModel>>()
    var onlineStatus = MutableLiveData<Boolean>()
    var seenStatus = MutableLiveData<Boolean>()
    var callStatus = MutableLiveData<CallState>()
    var loadedPic = MutableLiveData<ByteArray>()

    init {
        chatMessages.value = mutableListOf()
    }

    fun currentUser(): FirebaseUser? = signUpUseCase.currentUser()

    fun usernameFromUid(uid: String) {
        allUsersUseCase.usernameFromUid(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    receiverUsername.postValue(dataSnapshot.value.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

    }

    fun isUserInDirect(cUid: String, uid: String) {
        allUsersUseCase.userDirect(cUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var result = false
                    dataSnapshot.children.forEach {
                        if (it.key.toString() == uid) result = true
                    }
                    isInDirect.postValue(result)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun chatIdDecide(receiverId: String) =
        allUsersUseCase.chatIdDecide(currentUser()?.uid.toString(), receiverId)

    fun createChatRoom(name: String) = chatUseCase.createChatRoom(name)

    fun putChatInDirect(base: String, target: String) =
        allUsersUseCase.putChatInDirect(base, target)

    fun openChat(chatId: String) {
        val chat = chatUseCase.getChatRoom(chatId)
        chat.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val skipInvalidMessage = snapshot.key.toString()
                if (!skipInvalidMessage.startsWith("online") &&
                    !skipInvalidMessage.startsWith("seen") &&
                    !skipInvalidMessage.startsWith("call")
                ) {
                    val messageSenderId = snapshot.child("id").value.toString()
                    val messageType = snapshot.child("type").value.toString()
                    val messageText = snapshot.child("text").value.toString()
                    val url = snapshot.child("url").value.toString()
                    val type: MessageType =
                        if (messageSenderId == currentUser()?.uid) {
                            if (messageType == "SENT_TEXT") {
                                MessageType.SENT_TEXT
                            } else {
                                MessageType.SENT_PIC
                            }
                        } else {
                            if (messageType == "SENT_TEXT") {
                                MessageType.RECEIVED_TEXT
                            } else {
                                MessageType.RECEIVED_PIC
                            }
                        }

                    if (messageType == "SENT_TEXT") {
                        chatMessages.value?.add(
                            MessageModel(
                                snapshot.key.toString(),
                                messageText,
                                type
                            )
                        )
                    } else {
                        chatMessages.value?.add(
                            MessageModel(
                                snapshot.key.toString(),
                                messageText,
                                type,
                                url
                            )
                        )
                    }
                    chatMessages.postValue(chatMessages.value)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // if message edit added
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // if message delete added
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun uploadImage(filePathUri: Uri, chatId: String, message: MessageModel, senderId: String) {
        val id = System.currentTimeMillis().toString()
        val filename = "$id." + fileExtension.getFileExtension(
            filePathUri
        )
        message.url = filename
        chatUseCase.uploadImage(filePathUri, chatId, filename).addOnSuccessListener {
            sendPicture(id, message, chatId, senderId)
        }
    }

    fun sendMessage(message: MessageModel, chatId: String, senderId: String) =
        chatUseCase.sendMessage(message, chatId, senderId)

    private fun sendPicture(
        messageId: String,
        message: MessageModel,
        chatId: String,
        senderId: String
    ) =
        chatUseCase.sendPicture(messageId, message, chatId, senderId)

    fun createOnlineStatus(uid: String, chatId: String) =
        chatUseCase.createOnlineStatus(uid, chatId)

    fun setOnline(uid: String, chatId: String) = chatUseCase.setOnline(uid, chatId)

    fun changeLastSeen(uid: String, chatId: String, value: String) =
        chatUseCase.changeLastSeen(uid, chatId, value)

    fun setOffline(uid: String, chatId: String) = chatUseCase.setOffline(uid, chatId)

    fun startCall(uid: String, chatId: String) = chatUseCase.startCall(uid, chatId)

    fun establishCall(uid: String, chatId: String) = chatUseCase.establishCall(uid, chatId)

    fun endCall(uid: String, chatId: String) = chatUseCase.endCall(uid, chatId)

    fun monitorOnlineStatus(uid: String, chatId: String) {
        chatUseCase.checkOnline(uid, chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString() == GeneralStrings.online) {
                    onlineStatus.postValue(true)
                } else if (snapshot.value.toString() == GeneralStrings.offline) {
                    onlineStatus.postValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun monitorSeenStatus(uid: String, chatId: String) {
        chatUseCase.checkSeen(uid, chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString() == GeneralStrings.seen) {
                    seenStatus.postValue(true)
                } else if (snapshot.value.toString() == GeneralStrings.notSeen) {
                    seenStatus.postValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun monitorCall(uid: String, chatId: String) {
        chatUseCase.checkCall(uid, chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when {
                    snapshot.value.toString() == "calling" -> {
                        callStatus.postValue(CallState.CALLING)
                    }
                    snapshot.value.toString() == "talking" -> {
                        callStatus.postValue(CallState.TALKING)
                    }
                    snapshot.value.toString() == "endCall" -> {
                        callStatus.postValue(CallState.END_CALL)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun downLoadPic(chatId: String, filename: String) {
        chatUseCase.downLoadPic(chatId, filename).addOnSuccessListener {
            loadedPic.postValue(it)
        }
    }

}