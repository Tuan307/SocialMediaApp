package com.base.app.ui.group.add_group.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.ItemInviteMemberBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/11/2023
 */
class InviteMemberAdapter(
    private val listener: OnInvite
) : ListAdapter<DatingUser, InviteMemberAdapter.ViewHolder>(InviteMemberDiffUtil) {

    inner class ViewHolder(private val binding: ItemInviteMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) = with(binding) {
            val user = getItem(position)
            Glide.with(root.context).load(user.imageUrl).into(imageInviteAvatar)
            textInviteFullName.text = user.fullName
            textInviteUserName.text = user.userName
            if (user.hasChosen == true) {
                buttonHasInviteMember.visibility = View.VISIBLE
                buttonInviteMember.visibility = View.GONE
            } else {
                buttonInviteMember.visibility = View.VISIBLE
                buttonHasInviteMember.visibility = View.GONE
            }
            buttonInviteMember.setOnClickListener {
                listener.onInviteClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemInviteMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    private object InviteMemberDiffUtil : ItemCallback<DatingUser>() {
        override fun areItemsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem == newItem
        }

    }

    interface OnInvite {
        fun onInviteClick(position: Int)
    }
}