package com.a.papermaxx.general

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.chat.ui.CallFragmentDirections
import com.a.papermaxx.chat.ui.ChatViewModel
import com.a.remotemodule.models.CallState
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions

class CallActivity : JitsiMeetActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    var chatId: String = ""
    private lateinit var messageReceiver: String
    private lateinit var messageSender: String

    override fun onConferenceTerminated(data: MutableMap<String, Any>?) {
        super.onConferenceTerminated(data)

        chatViewModel.endCall(messageSender , chatId)
        // end call here
    }

//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)

//        chatViewModel.monitorCall(messageReceiver, chatId)

//        monitorCallStatus()
//    }

    fun fillCallValues(chatId: String, messageReceiver: String, messageSender: String) {
        this.chatId = chatId
        this.messageReceiver = messageReceiver
        this.messageSender = messageSender
    }

    fun launchTest(context: Context, options: JitsiMeetConferenceOptions) {
        val intent = Intent(context, CallActivity::class.java)
        intent.action = "org.jitsi.meet.CONFERENCE"
        intent.putExtra("JitsiMeetConferenceOptions", options)
        context.startActivity(intent)
    }

    private fun monitorCallStatus() {
        chatViewModel.callStatus.observe(this, { call ->

            if (call == CallState.TALKING) {
                chatViewModel.establishCall(messageSender, chatId)
            } else if (call == CallState.END_CALL) {
                chatViewModel.endCall(messageSender, chatId)
            }

        })
    }

}