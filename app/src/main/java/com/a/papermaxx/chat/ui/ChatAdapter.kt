package com.a.papermaxx.chat.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.a.papermaxx.R
import com.a.papermaxx.databinding.ReceivedImageBinding
import com.a.papermaxx.databinding.ReceivedItemBinding
import com.a.papermaxx.databinding.SentImageBinding
import com.a.papermaxx.databinding.SentItemBinding
import com.a.papermaxx.general.GeneralStrings
import com.a.remotemodule.models.MessageModel
import com.a.remotemodule.models.MessageType


class ChatAdapter(

    var list: MutableList<MessageModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder1(var binding: SentItemBinding) : RecyclerView.ViewHolder(binding.root)

    class MyViewHolder2(var binding: ReceivedItemBinding) : RecyclerView.ViewHolder(binding.root)

    class MyViewHolder3(var binding: SentImageBinding) : RecyclerView.ViewHolder(binding.root)

    class MyViewHolder4(var binding: ReceivedImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (list[position].type) {
            MessageType.SENT_TEXT -> {
                GeneralStrings.sentTextMessage
            }
            MessageType.RECEIVED_TEXT -> {
                GeneralStrings.receivedTextMessage
            }
            MessageType.SENT_PIC -> {
                GeneralStrings.sentPicMessage
            }
            MessageType.RECEIVED_PIC -> {
                GeneralStrings.receivedPicMessage
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GeneralStrings.sentTextMessage -> {
                val binding: SentItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.sent_item, parent, false
                )
                MyViewHolder1(binding)
            }
            GeneralStrings.receivedTextMessage -> {
                val binding: ReceivedItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.received_item, parent, false
                )
                MyViewHolder2(binding)
            }
            GeneralStrings.sentPicMessage -> {
                val binding: SentImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.sent_image, parent, false
                )
                MyViewHolder3(binding)
            }
            else -> {
                val binding: ReceivedImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.received_image, parent, false
                )
                MyViewHolder4(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == GeneralStrings.sentTextMessage -> {
                (holder as MyViewHolder1).binding.data = list[position]
            }
            getItemViewType(position) == GeneralStrings.receivedTextMessage -> {
                (holder as MyViewHolder2).binding.data = list[position]
            }
            getItemViewType(position) == GeneralStrings.sentPicMessage -> {
                (holder as MyViewHolder3).binding.data = list[position]
            }
            getItemViewType(position) == GeneralStrings.receivedPicMessage -> {
                (holder as MyViewHolder4).binding.data = list[position]
            }
        }
    }

    override fun getItemCount() = list.size

}