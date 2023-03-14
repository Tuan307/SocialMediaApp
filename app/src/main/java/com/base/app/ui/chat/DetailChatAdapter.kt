package com.base.app.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.ChatModel
import com.base.app.databinding.ItemChatBinding

class DetailChatAdapter(
    private val uid: String,
    private val list: ArrayList<ChatModel>
) : RecyclerView.Adapter<DetailChatAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatModel) {
            if (data.sender == uid) {
                binding.txtChat1.visibility = View.GONE
                binding.txtChat2.text = data.message
            } else {
                binding.txtChat2.visibility = View.GONE
                binding.txtChat1.text = data.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}