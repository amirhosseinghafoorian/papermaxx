package com.a.papermaxx.general

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.a.papermaxx.R


class ChatAdapter(

    var list: MutableList<MessageModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder1(var binding: SentItemBinding) : RecyclerView.ViewHolder(binding.root)

    class MyViewHolder2(var binding: ReceivedItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (list[position].type == MessageType.SENT) {
            GeneralStrings.sentMessage
        } else GeneralStrings.receivedMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == GeneralStrings.sentMessage) {
            val binding: SentItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.sent_item, parent, false
            )
            MyViewHolder1(binding)
        } else {
            val binding: ReceivedItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.received_item, parent, false
            )
            MyViewHolder2(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == GeneralStrings.sentMessage) {
            (holder as MyViewHolder1).binding.data = list[position]
        } else {
            (holder as MyViewHolder2).binding.data = list[position]
        }
    }

    override fun getItemCount() = list.size

}