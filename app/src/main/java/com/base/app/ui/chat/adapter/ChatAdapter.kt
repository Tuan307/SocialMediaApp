package com.base.app.ui.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.chat.RecentChatModel
import com.base.app.databinding.ItemChatUserBinding
import com.bumptech.glide.Glide

class ChatAdapter(
    private val context: Context,
    private val listener: OnItemClick,
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RecentChatModel) {
            with(binding) {
                txtChatName.text = data.targetName
                textTimeAgo.text = data.createdAt
                textLastMessage.text = data.lastMessage
                Glide.with(context).load(data.targetImage).into(imgAvatar)
                root.setOnClickListener {
                    listener.onItemCLick(
                        data.targetId.toString(),
                        data.targetName.toString(),
                        data.targetImage.toString()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil = object : ItemCallback<RecentChatModel>() {
        override fun areItemsTheSame(oldItem: RecentChatModel, newItem: RecentChatModel): Boolean {
            return oldItem.targetId == newItem.targetId
        }

        override fun areContentsTheSame(
            oldItem: RecentChatModel,
            newItem: RecentChatModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    interface OnItemClick {
        fun onItemCLick(id: String, name: String, url: String)
    }
}