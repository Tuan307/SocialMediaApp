package com.base.app.ui.main.fragment.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.NotificationItem
import com.base.app.databinding.LayoutNotificationItemBinding
import com.base.app.ui.main.fragment.notification.NotificationViewModel

class NotificationAdapter(
    val context: Context,
    val list: ArrayList<NotificationItem>,
    val viewModel: NotificationViewModel,
    val iCallBack: ICallBack
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: LayoutNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationItem) {
            binding.apply {
                viewModel.getUserInformation(context, imgAvatar, txtUserName, data.userid)
                if (data.ispost) {
                    imgNotificationPost.visibility = View.VISIBLE
                    viewModel.getPostItem(context, imgNotificationPost, data.postid)
                } else {
                    imgNotificationPost.visibility = View.INVISIBLE
                }
                notificationItem.setOnClickListener {
                    iCallBack.onItemClick(data)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        return ViewHolder(
            LayoutNotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ICallBack {
        fun onItemClick(data: NotificationItem)
    }
}