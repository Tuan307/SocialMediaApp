package com.base.app.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.ChatModel
import com.base.app.databinding.ItemChatBinding

class DetailChatAdapter(
    private val uid: String,
) : ListAdapter<ChatModel, DetailChatAdapter.ViewHolder>(DetailChatDiffUtil) {
    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatModel) {
            if (data.sender == uid) {
                binding.txtChat1.visibility = View.GONE
                binding.txtChat2.visibility = View.VISIBLE
                binding.txtChat2.text = data.message
            } else {
                binding.txtChat2.visibility = View.GONE
                binding.txtChat1.visibility = View.VISIBLE
                binding.txtChat1.text = data.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(currentList[position])


    private object DetailChatDiffUtil : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.receiver == newItem.receiver && oldItem.sender == newItem.sender
                    && oldItem.message == newItem.message && oldItem.timestamp == newItem.timestamp
        }

    }
}