package com.a.papermaxx.general

import android.content.Context
import android.content.Intent
import android.util.Log
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions

class JitsiTest : JitsiMeetActivity() {

    override fun onConferenceTerminated(data: MutableMap<String, Any>?) {
        super.onConferenceTerminated(data)

        Log.i("baby", "end call here")
    }

    companion object {

        fun launchTest(context: Context, options: JitsiMeetConferenceOptions) {
            val intent = Intent(context, JitsiTest::class.java)
            intent.action = "org.jitsi.meet.CONFERENCE"
            intent.putExtra("JitsiMeetConferenceOptions", options)
            context.startActivity(intent)
        }

    }
}