package com.base.app.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.ItemChatUserBinding
import com.bumptech.glide.Glide

class ChatAdapter(
    private val context: Context,
    private val listener: OnItemClick,
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DatingUser) {
            with(binding) {
                txtChatName.text = data.userName
                textTimeAgo.text = data.lastOnline
                Glide.with(context).load(data.imageUrl).into(imgAvatar)
                root.setOnClickListener {
                    listener.onItemCLick(
                        data.userId.toString(),
                        data.userName.toString(),
                        data.imageUrl.toString()
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

    private val diffUtil = object : ItemCallback<DatingUser>() {
        override fun areItemsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    interface OnItemClick {
        fun onItemCLick(id: String, name: String, url: String)
    }
}