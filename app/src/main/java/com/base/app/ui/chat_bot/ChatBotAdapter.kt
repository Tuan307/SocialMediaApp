package com.base.app.ui.chat_bot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.response.Message
import com.base.app.databinding.MessageItemBinding

class ChatBotAdapter(private val list: ArrayList<Message>) :
    RecyclerView.Adapter<ChatBotAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = list[position]
            if (data.role == "assistant") {
                binding.tvMessage.visibility = View.GONE
                binding.tvBotMessage.text = data.content
            } else {
                binding.tvBotMessage.visibility = View.GONE
                binding.tvMessage.text = data.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}