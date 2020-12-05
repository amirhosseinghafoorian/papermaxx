package com.a.papermaxx.chat.ui

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

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var messageReceiver: String
    private lateinit var messageSender: String
    private lateinit var myAdapter: ChatAdapter
    private lateinit var chatId: String
    private var adminFirst: Boolean = false

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

            chatViewModel.isInDirect.observe(viewLifecycleOwner, { isInDirect ->
                if (isInDirect != null) {
                    chatId = chatViewModel.chatIdDecide(messageReceiver)

                    if (adminFirst) {
                        val message = MessageModel(
                            "",
                            "Welcome to PaperMaxx",
                            MessageType.SENT
                        )

                        chatViewModel.sendMessage(message, chatId, messageReceiver)
                    }

                    if (!isInDirect) {
                        chatViewModel.createChatRoom(chatId)
                        chatViewModel.putChatInDirect(messageReceiver, messageSender)
                        chatViewModel.putChatInDirect(messageSender, messageReceiver)
                    }
                    chatViewModel.openChat(chatId)
                }
            })

            chatViewModel.usernameFromUid(messageReceiver)
            chatViewModel.isUserInDirect(
                chatViewModel.currentUser()?.uid.toString(),
                messageReceiver
            )

            myAdapter = chatViewModel.chatMessages.value?.let { ChatAdapter(it) }!!

            chat_recycler.apply {
                adapter = myAdapter
            }

            chatViewModel.chatMessages.observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    myAdapter.list = list
                    myAdapter.notifyDataSetChanged()
                    chat_recycler.scrollToPosition(list.size - 1)
                }
            })

            send_cv.setOnClickListener {

                if (chat_type_et.editText?.text?.isNotBlank() == true) {

                    val message = MessageModel(
                        "",
                        chat_type_et.editText?.text.toString(),
                        MessageType.SENT
                    )

                    chatViewModel.sendMessage(message, chatId, messageSender)

                    // here it should check that if it is a sent message gone the seen icon
                    // and if it is a received message visible the icon

                    chat_type_et.editText?.setText("")

                }
            }

        }

    }

    override fun onPause() {
        super.onPause()
        // disable seen icon
    }
}