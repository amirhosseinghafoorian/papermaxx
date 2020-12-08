package com.a.papermaxx.chat.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.domainmodule.domain.AllUsersUseCase
import com.a.domainmodule.domain.ChatUseCase
import com.a.domainmodule.domain.SignUpUseCase
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

    ) : ViewModel() {

    var receiverUsername = MutableLiveData<String>()
    var isInDirect = MutableLiveData<Boolean>()
    var chatMessages = MutableLiveData<MutableList<MessageModel>>()
    var onlineStatus = MutableLiveData<Boolean>()

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
                val skipOnlineMessage = snapshot.key.toString()
                if (!skipOnlineMessage.startsWith("online")) {
                    val message = snapshot.child("message").value.toString()
                    val messageSenderId = message.substringAfterLast(':')
                    val messageText = message.substringBeforeLast(':')
                    val type: MessageType =
                        if (messageSenderId == currentUser()?.uid) {
                            MessageType.SENT_TEXT
                        } else {
                            MessageType.RECEIVED_TEXT
                        }

                    chatMessages.value?.add(
                        MessageModel(
                            snapshot.key.toString(),
                            messageText,
                            type
                        )
                    )
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

    fun sendMessage(message: MessageModel, chatId: String, senderId: String) =
        chatUseCase.sendMessage(message, chatId, senderId)

    fun sendPicture(message: MessageModel, chatId: String, senderId: String) =
        chatUseCase.sendPicture(message, chatId, senderId)

    fun createOnlineStatus(uid: String, chatId: String) =
        chatUseCase.createOnlineStatus(uid, chatId)

    fun setOnline(uid: String, chatId: String) = chatUseCase.setOnline(uid, chatId)

    fun setOffline(uid: String, chatId: String) = chatUseCase.setOffline(uid, chatId)

    fun monitorOnlineStatus(uid: String, chatId: String) {
        chatUseCase.checkSeen(uid, chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString() == "online") {
                    onlineStatus.postValue(true)
                } else if (snapshot.value.toString() == "offline") {
                    onlineStatus.postValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

}