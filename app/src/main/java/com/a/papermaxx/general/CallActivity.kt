package com.a.papermaxx.general

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.a.papermaxx.chat.ui.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions

@AndroidEntryPoint
class CallActivity : JitsiMeetActivity() {

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onConferenceTerminated(data: MutableMap<String, Any>?) {
        super.onConferenceTerminated(data)

        chatViewModel.endCall(messageSender, chatId)
        chatViewModel.endCall(messageReceiver, chatId)

    }

    companion object {
        var chatId: String = ""
        private lateinit var messageReceiver: String
        private lateinit var messageSender: String

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
    }

}