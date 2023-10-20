package com.base.app.ui.main.fragment.notification

import android.annotation.SuppressLint
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
class NotificationAdapter(private val listener: OnNotificationClick) :
    ListAdapter<NotificationContent, NotificationAdapter.ViewHolder>(NotificationDiffUtil) {
    inner class ViewHolder(private val binding: LayoutNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: NotificationContent, position: Int, listener: OnNotificationClick) =
            with(binding) {
                Glide.with(root.context).load(data.notificationUserId?.imageUrl).into(imgAvatar)
                txtUserName.text = data.notificationUserId?.userName
                if (data.isInvitation == true || data.isRequest == true) {
                    txtNotification.text = "${data.text} ${data.notificationGroupId?.groupName}"
                } else if (data.isPost == true) {
                    txtNotification.text = data.text.toString()
                }
                textTimeAgo.text = data.notificationTimeStamp
                if (data.isInvitation == true || data.isRequest == true) {
                    linearNotificationButton.visibility = View.VISIBLE
                } else {
                    linearNotificationButton.visibility = View.GONE
                }
                buttonCancelNotification.setOnClickListener {
                    listener.onReject(data, position)
                }
                buttonConfirmNotification.setOnClickListener {
                    listener.onConfirm(data, position)
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
        holder.bind(getItem(position), position, listener)
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

    interface OnNotificationClick {
        fun onConfirm(data: NotificationContent, position: Int)
        fun onReject(data: NotificationContent, position: Int)
    }
}