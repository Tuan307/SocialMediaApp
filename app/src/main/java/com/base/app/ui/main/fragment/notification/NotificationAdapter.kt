package com.base.app.ui.main.fragment.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.response.NotificationContent
import com.base.app.databinding.LayoutNotificationItemBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/4/2023
 */
class NotificationAdapter :
    ListAdapter<NotificationContent, NotificationAdapter.ViewHolder>(NotificationDiffUtil) {
    inner class ViewHolder(private val binding: LayoutNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationContent) = with(binding) {
            Glide.with(root.context).load(data.notificationUserId?.imageUrl).into(imgAvatar)
            txtUserName.text = data.notificationUserId?.userName
            txtNotification.text = data.text.toString()
            textTimeAgo.text = data.notificationTimeStamp
            if (data.isInvitation == true) {
                linearNotificationButton.visibility = View.VISIBLE
            } else {
                linearNotificationButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutNotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object NotificationDiffUtil : ItemCallback<NotificationContent>() {
        override fun areItemsTheSame(
            oldItem: NotificationContent,
            newItem: NotificationContent
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationContent,
            newItem: NotificationContent
        ): Boolean {
            return oldItem == newItem
        }

    }
}