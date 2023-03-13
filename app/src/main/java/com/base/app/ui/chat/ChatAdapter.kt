package com.base.app.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.User
import com.base.app.databinding.ItemChatUserBinding
import com.bumptech.glide.Glide

class ChatAdapter(
    private val context: Context,
    private val listener: OnItemClick
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            binding.apply {
                txtChatName.text = data.username
                Glide.with(context).load(data.imageurl).into(imgAvatar)
                root.setOnClickListener {
                    listener.onItemCLick(
                        data.id.toString(),
                        data.username.toString(),
                        data.imageurl.toString()
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

    private val diffUtil = object : ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    interface OnItemClick {
        fun onItemCLick(id: String, name: String, url: String)
    }
}