package com.a.papermaxx.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.a.papermaxx.R
import com.a.papermaxx.databinding.UsersListItemBinding
import com.a.papermaxx.model.UserModel

class UserListAdapter(
    var list: MutableList<UserModel>
) :
    RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: UsersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            itemView.setOnClickListener {
                try {
                    it.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToChatFragment(
                            list[position].id,
                            false
                        )
                    )
                } catch (e: Exception) {
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: UsersListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.users_list_item, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.data = list[position]
    }

}
