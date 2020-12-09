package com.a.papermaxx.chat.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.a.papermaxx.R
import com.a.papermaxx.databinding.FragmentChatBinding
import com.a.papermaxx.general.GeneralStrings
import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.models.MessageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class ChatFragment : Fragment() , ChatAdapter.OnPicClick {

    private lateinit var binding: FragmentChatBinding
    private lateinit var messageReceiver: String
    private lateinit var messageSender: String
    private lateinit var myAdapter: ChatAdapter
    private var chatId: String = ""
    private var adminFirst: Boolean = false
    private var lastMessage = MessageModel("not", "", MessageType.RECEIVED_TEXT)
    private var readyMessage = MessageModel("not", "", MessageType.SENT_PIC)
    private var filePathUri: Uri? = null

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageReceiver =
            arguments?.let { ChatFragmentArgs.fromBundle(it).messageReceiver }.toString()

        adminFirst =
            arguments.let { ChatFragmentArgs.fromBundle(it!!).adminFirst }

        messageSender = chatViewModel.currentUser()?.uid.toString()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(
                        ChatFragmentDirections.actionChatFragmentToHomeFragment(
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            chatViewModel.receiverUsername.observe(viewLifecycleOwner, { name ->
                if (name != null) {
                    binding.username = name
                }
            })

            chatViewModel.onlineStatus.observe(viewLifecycleOwner, { isOnline ->
                if (isOnline != null && lastMessage.id != "not") {
                    if (isOnline) {
                        if (lastMessage.type == MessageType.SENT_TEXT) {
                            seen_icon.visibility = View.VISIBLE
                        }
                    }
                }
            })

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
                    chatViewModel.openChat(chatId)
                    chatViewModel.monitorOnlineStatus(messageReceiver, chatId)
                }
            })

            chatViewModel.usernameFromUid(messageReceiver)
            chatViewModel.isUserInDirect(
                chatViewModel.currentUser()?.uid.toString(),
                messageReceiver
            )

            myAdapter = chatViewModel.chatMessages.value?.let { ChatAdapter(it,this@ChatFragment) }!!

            chat_recycler.apply {
                adapter = myAdapter
            }

            chatViewModel.chatMessages.observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    if (list.size > 0) {
                        myAdapter.list = list
                        myAdapter.notifyItemInserted(list.size - 1)
                        chat_recycler.scrollToPosition(list.size - 1)
                        lastMessage = list[list.size - 1]
                        if (lastMessage.type == MessageType.RECEIVED_TEXT) {
                            seen_icon.visibility = View.GONE
                        } else if (chatViewModel.onlineStatus.value == true) {
                            seen_icon.visibility = View.VISIBLE
                        }
                    }
                }
            })

            send_cv.setOnClickListener {
                if (chat_type_et.editText?.text?.isNotBlank() == true) {

                    val message = MessageModel(
                        "",
                        chat_type_et.editText?.text.toString(),
                        MessageType.SENT_TEXT
                    )

                    seen_icon.visibility = View.GONE
                    if (readyMessage.id == "not") {
                        chatViewModel.sendMessage(message, chatId, messageSender)
                    } else if (readyMessage.id == "yep") {
                        readyMessage.text = chat_type_et.editText?.text.toString()
                        uploadImage()
                    }
                    chat_type_et.editText?.setText("")
                    filePathUri = null
                    readyMessage.id = "not"
                }
            }

            image_cv.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    GeneralStrings.imageRequestCode
                )
            }

        }

    }

    private fun uploadImage() {
        chatViewModel.uploadImage(filePathUri!!, chatId, readyMessage, messageSender)
    }

    override fun onResume() {
        super.onResume()
        if (chatId != "") {
            chatViewModel.setOnline(messageSender, chatId)
        }
    }

    override fun onPause() {
        super.onPause()
        chatViewModel.setOffline(messageSender, chatId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GeneralStrings.imageRequestCode && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePathUri = data.data!!
            try {
                readyMessage.id = "yep"
                chat_type_et.editText?.setText(GeneralStrings.newImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(filename: String) {
        findNavController().navigate(ChatFragmentDirections.actionChatFragmentToShowPictureFragment(chatId , filename))
    }
}