package com.base.app.ui.group.detail_group.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.group.response.RequestModel
import com.base.app.databinding.ItemGroupRequestBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/24/2023
 */
class GroupRequestAdapter(private val listener: OnGroupRequestItem) :
    ListAdapter<RequestModel, GroupRequestAdapter.ViewHolder>(GroupRequestDiffUtil) {
    class ViewHolder(private val binding: ItemGroupRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RequestModel, position: Int, listener: OnGroupRequestItem) = with(binding) {
            textName.text = data.fromInvitedUserId?.fullName
            textUserName.text = data.fromInvitedUserId?.userName
            Glide.with(root.context).load(data.fromInvitedUserId?.imageUrl).into(imageAvatar)
            buttonApproveRequest.setOnClickListener {
                listener.onAgree(data, position)
            }
            buttonCancelRequest.setOnClickListener {
                listener.onReject(data, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGroupRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position, listener)
    }

    private object GroupRequestDiffUtil : ItemCallback<RequestModel>() {
        override fun areItemsTheSame(oldItem: RequestModel, newItem: RequestModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RequestModel, newItem: RequestModel): Boolean {
            return oldItem == newItem
        }
    }

    interface OnGroupRequestItem {
        fun onAgree(data: RequestModel, position: Int)
        fun onReject(data: RequestModel, position: Int)
    }
}