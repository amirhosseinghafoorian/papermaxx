package com.a.papermaxx.chat.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import com.a.papermaxx.databinding.FragmentTutorChatBinding
import com.a.papermaxx.general.CallActivity
import com.a.papermaxx.general.GeneralStrings
import com.a.remotemodule.models.CallState
import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.models.MessageType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_tutor_chat.*
import kotlinx.android.synthetic.main.receiver_bottom_sheet.*
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.io.IOException
import java.net.URL

@AndroidEntryPoint
class TutorChatFragment : Fragment(), ChatAdapter.OnPicClick {

    private lateinit var binding: FragmentTutorChatBinding
    private lateinit var messageReceiver: String
    private lateinit var messageSender: String
    private lateinit var myAdapter: ChatAdapter
    private var chatId: String = ""
    private var adminFirst: Boolean = false
    private var lastMessage = MessageModel("not", "", MessageType.RECEIVED_TEXT)
    private var readyMessage = MessageModel("not", "", MessageType.SENT_PIC)
    private var filePathUri: Uri? = null
    private var onlineKeep: Boolean = false

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageReceiver =
            arguments?.let { TutorChatFragmentArgs.fromBundle(it).messageReceiver }.toString()

        adminFirst =
            arguments.let { TutorChatFragmentArgs.fromBundle(it!!).adminFirst }

        messageSender = chatViewModel.currentUser()?.uid.toString()

        modifyOnBackPressed()

    }

    private fun modifyOnBackPressed() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(
                        TutorChatFragmentDirections.actionTutorChatFragmentToTutorHomeFragment(
                            GeneralStrings.keyChat
                        )
                    )
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tutor_chat, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            showMessageReceiverUsername()

            monitorOnlineStatus()

            isInDirectObserve()

            chatViewModel.usernameFromUid(messageReceiver)
            chatViewModel.isUserInDirect(
                chatViewModel.currentUser()?.uid.toString(),
                messageReceiver
            )

            chatViewModel.chatMessages.value = mutableListOf()

            myAdapter = chatViewModel.chatMessages.value?.let {
                ChatAdapter(
                    it,
                    chatId,
                    this@TutorChatFragment
                )
            }!!

            tutor_chat_recycler.apply {
                adapter = myAdapter
            }

            showMessages()

            monitorSeenStatus()

            monitorCallStatus()

            sendMessage()

            openImage()

            makeCall()

        }

    }

    private fun makeCall() {
        tutor_chat_call_ic.setOnClickListener {
            if (chatViewModel.onlineStatus.value == true) {
                chatViewModel.startCall(messageSender, chatId)
                chatViewModel.startRing(messageReceiver, chatId)
            } else {
                Toast.makeText(requireContext(), "receiver is not online", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showMessageReceiverUsername() {
        chatViewModel.receiverUsername.observe(viewLifecycleOwner, { name ->
            if (name != null) {
                binding.username = name
            }
        })
    }

    //      monitor receivers online status
    private fun monitorOnlineStatus() {
        chatViewModel.onlineStatus.observe(viewLifecycleOwner, { isOnline ->
            if (isOnline) {
                tutor_chat_online_tv.visibility = View.VISIBLE
            } else {
                tutor_chat_online_tv.visibility = View.GONE
            }
        })
    }

    //      monitor receivers seen status
    private fun monitorSeenStatus() {
        chatViewModel.seenStatus.observe(viewLifecycleOwner, { seen ->
            if (seen != null && lastMessage.id != "not") {
                if (seen) {
                    if (lastMessage.type == MessageType.SENT_TEXT) {
                        tutor_seen_icon.visibility = View.VISIBLE
                    }
                } else {
                    tutor_seen_icon.visibility = View.GONE
                }
            }
        })
    }

    private fun monitorCallStatus() {
        chatViewModel.callStatus.observe(viewLifecycleOwner, { call ->

            when (call) {
                CallState.CALLING -> {
                    showReceiverBottomSheet()
                }
                CallState.RINGING -> {
                    showCallerBottomSheet()
                }
                CallState.TALKING -> {
                    chatViewModel.establishCall(messageSender, chatId)
                    establishOnlineCall()
                }
                CallState.END_CALL -> {
                    chatViewModel.endCall(messageSender, chatId)
                }
                else -> {
                }
            }

        })
    }

    //      show a bottom sheet for calling
    private fun showCallerBottomSheet() {
        val buttonSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.caller_bottom_sheet, null)

        view.findViewById<MaterialCardView>(R.id.end_call_cv).setOnClickListener {
            buttonSheetDialog.dismiss()
            chatViewModel.endCall(messageSender, chatId)
        }
        val text = "calling " + binding.username + " ... "
        view.findViewById<MaterialTextView>(R.id.receive_call_name_tv).text = text
        buttonSheetDialog.setContentView(view)
        buttonSheetDialog.setCancelable(false)
        buttonSheetDialog.show()
    }

    //      show a bottom sheet for receiving calls
    private fun showReceiverBottomSheet() {
        val buttonSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.receiver_bottom_sheet, null)

        view.findViewById<MaterialCardView>(R.id.accept_call_cv).setOnClickListener {
            Toast.makeText(requireContext(), "call accept sample", Toast.LENGTH_SHORT).show()
            if (chatViewModel.callStatus.value == CallState.CALLING) {
                buttonSheetDialog.dismiss()
                chatViewModel.establishCall(messageSender, chatId)
                establishOnlineCall()
            } else {
                buttonSheetDialog.dismiss()
                Toast.makeText(requireContext(), "caller canceled the call", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        view.findViewById<MaterialCardView>(R.id.reject_call_cv).setOnClickListener {
            Toast.makeText(requireContext(), "call reject sample", Toast.LENGTH_SHORT).show()
            buttonSheetDialog.dismiss()
            chatViewModel.endCall(messageSender, chatId)
        }
        val text = binding.username + " is calling you ..."
        view.findViewById<MaterialTextView>(R.id.call_name_tv).text = text
        buttonSheetDialog.setContentView(view)
        buttonSheetDialog.setCancelable(false)
        buttonSheetDialog.show()

    }

    //      start a Jitsi call
    private fun establishOnlineCall() {
        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si/"))
            .setRoom(chatViewModel.roomIdDecide(chatId))
            .setAudioMuted(false)
            .setVideoMuted(false)
            .setAudioOnly(false)
            .setWelcomePageEnabled(false)
            .build()
        CallActivity.fillCallValues(chatId, messageReceiver, messageSender)
        CallActivity.launchTest(requireContext(), options)
    }

    //      show chat messages
    private fun showMessages() {
        chatViewModel.chatMessages.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                if (list.size > 0) {
                    myAdapter.list = list
//                    myAdapter.notifyDataSetChanged()
                    myAdapter.notifyItemInserted(list.lastIndex)
                    tutor_chat_recycler.scrollToPosition(list.size - 1)
                    lastMessage = list[list.lastIndex]
                    if (lastMessage.type == MessageType.RECEIVED_TEXT ||
                        lastMessage.type == MessageType.RECEIVED_PIC
                    ) {
                        tutor_seen_icon.visibility = View.GONE
                    } else if (
                        lastMessage.type == MessageType.SENT_TEXT ||
                        lastMessage.type == MessageType.SENT_PIC
                    ) {
                        if (chatViewModel.seenStatus.value == true) {
                            tutor_seen_icon.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    //      open an image from messages
    private fun openImage() {
        tutor_image_cv.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Image"),
                GeneralStrings.imageRequestCode
            )
        }

    }

    //      send a new message
    private fun sendMessage() {
        tutor_send_cv.setOnClickListener {
            if (tutor_chat_type_et.editText?.text?.isNotBlank() == true) {

                val message = MessageModel(
                    "",
                    tutor_chat_type_et.editText?.text.toString(),
                    MessageType.SENT_TEXT
                )

                if (chatViewModel.onlineStatus.value == false) {
                    chatViewModel.changeLastSeen(
                        messageReceiver,
                        chatId,
                        GeneralStrings.notSeen
                    )
                }
                if (readyMessage.id == "not") {
                    chatViewModel.sendMessage(message, chatId, messageSender)
                } else if (readyMessage.id == "yep") {
                    readyMessage.text = tutor_chat_type_et.editText?.text.toString()
                    uploadImage()
                }
                tutor_chat_type_et.editText?.setText("")
                filePathUri = null
                readyMessage.id = "not"

            }
        }
    }

    private fun isInDirectObserve() {
        chatViewModel.isInDirect.observe(viewLifecycleOwner, { isInDirect ->
            if (isInDirect != null) {
                chatId = chatViewModel.chatIdDecide(messageReceiver)

                if (!isInDirect) {
                    chatViewModel.createChatRoom(chatId)
                    chatViewModel.putChatInDirect(messageReceiver, messageSender)
                    chatViewModel.putChatInDirect(messageSender, messageReceiver)
                    chatViewModel.createOnlineStatus(messageSender, chatId)
                }

                if (adminFirst) {
                    val message = MessageModel(
                        "",
                        GeneralStrings.welcomeMessage,
                        MessageType.SENT_TEXT
                    )

                    chatViewModel.sendMessage(message, chatId, messageReceiver)
                }

                chatViewModel.setOnline(messageSender, chatId)
                chatViewModel.changeLastSeen(messageSender, chatId, GeneralStrings.seen)
                chatViewModel.openChat(chatId)
                chatViewModel.monitorOnlineStatus(messageReceiver, chatId)
                chatViewModel.monitorSeenStatus(messageReceiver, chatId)
                chatViewModel.monitorCall(messageReceiver, chatId)
            }
        })
    }

    //      uploads the image into firebase storage
    private fun uploadImage() {
        chatViewModel.uploadImage(filePathUri!!, chatId, readyMessage, messageSender)
    }

    //      select image result from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        preparePictureUri(requestCode, resultCode, data)

    }

    //      fill picture uri with the selected picture from gallery
    private fun preparePictureUri(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GeneralStrings.imageRequestCode &&
            resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            filePathUri = data.data!!
            try {
                readyMessage.id = "yep"
                tutor_chat_type_et.editText?.setText(GeneralStrings.newImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //      chat item view on click
    override fun onClick(filename: String) {
        findNavController().navigate(
            TutorChatFragmentDirections.actionTutorChatFragmentToTutorShowPictureFragment(
                chatId,
                filename,
                messageReceiver
            )
        )
    }

    //      set online
    override fun onResume() {
        super.onResume()
        onlineKeep = false
        if (chatId != "") {
            chatViewModel.setOnline(messageSender, chatId)
            chatViewModel.changeLastSeen(messageSender, chatId, GeneralStrings.seen)
        }
    }

    //      set offline
    override fun onPause() {
        super.onPause()
        if (!onlineKeep) {
            chatViewModel.setOffline(messageSender, chatId)
        }
    }

}