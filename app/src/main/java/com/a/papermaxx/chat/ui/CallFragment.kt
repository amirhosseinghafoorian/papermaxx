package com.a.papermaxx.chat.ui

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
import com.a.papermaxx.databinding.FragmentCallBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_call.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallFragment : Fragment() {

    private lateinit var binding: FragmentCallBinding
    private var chatId: String = ""
    private lateinit var messageReceiver: String
    private lateinit var messageSender: String
    private lateinit var username: String
    private lateinit var situation: String

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageReceiver =
            arguments?.let { CallFragmentArgs.fromBundle(it).messageReceiver }.toString()

        messageSender =
            arguments?.let { CallFragmentArgs.fromBundle(it).messageSender }.toString()

        chatId =
            arguments?.let { CallFragmentArgs.fromBundle(it).chatId }.toString()

        username =
            arguments?.let { CallFragmentArgs.fromBundle(it).username }.toString()

        situation =
            arguments?.let { CallFragmentArgs.fromBundle(it).situation }.toString()

        modifyOnBackPressed()

    }

    private fun modifyOnBackPressed() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //      end call here
                    findNavController().navigate(
                        CallFragmentDirections.actionCallFragmentToChatFragment(
                            messageReceiver,
                            false
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_call, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            if (situation == "callReceiver") {
                binding.endOrReject = "Reject"
            } else {
                binding.endOrReject = "End"
            }

            chatViewModel.monitorCall(messageReceiver, chatId)

            handleCall()

            monitorCallStatus()

        }

    }

    private fun monitorCallStatus() {
//        chatViewModel.callStatus.observe(viewLifecycleOwner, { call ->
//
//            handleCall()
//
//        })
    }

    private fun handleCall() {
        if (situation == "callReceiver") {
            call_btn_answer_call.visibility = View.VISIBLE
            binding.endOrReject = "End"
            Toast.makeText(
                requireContext(),
                "$username is calling you ...",
                Toast.LENGTH_SHORT
            ).show()
        } else if (situation == "caller") {
            Toast.makeText(requireContext(), "calling $username ...", Toast.LENGTH_SHORT).show()
        }
    }

}