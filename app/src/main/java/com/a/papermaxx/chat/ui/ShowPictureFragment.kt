package com.a.papermaxx.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a.papermaxx.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_show_picture.*

class ShowPictureFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels()
    private var chatId = ""
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatId =
            arguments?.let { ShowPictureFragmentArgs.fromBundle(it).chatId }.toString()

        fileName =
            arguments?.let { ShowPictureFragmentArgs.fromBundle(it).fileName }.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.loadedPic.observe(viewLifecycleOwner, {
            Glide.with(requireContext()).load(it).into(show_pic_iv)
        })

        chatViewModel.downLoadPic(chatId, fileName)

    }

}