package com.base.app.ui.main.fragment.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.NotificationItem
import com.base.app.databinding.LayoutNotificationItemBinding
import com.base.app.ui.main.fragment.notification.NotificationViewModel
import com.bumptech.glide.Glide

class NotificationAdapter1(
    val context: Context,
    val list: ArrayList<NotificationItem>,
    val viewModel: NotificationViewModel,
    val iCallBack: ICallBack
) : RecyclerView.Adapter<NotificationAdapter1.ViewHolder>() {

    inner class ViewHolder(private val binding: LayoutNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationItem) {
            with(binding) {
                txtNotification.text = data.text
               // viewModel.getUserInformation(context, imgAvatar, txtUserName, data.userid)
                if (data.ispost) {
//                    imgNotificationPost.visibility = View.VISIBLE
//                    Glide.with(context).load(data.imageUrl).into(imgNotificationPost)
//                    //viewModel.getPostItem(context, imgNotificationPost, data.postid)
//                } else {
//                    imgNotificationPost.visibility = View.INVISIBLE
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
    ): NotificationAdapter1.ViewHolder {
        return ViewHolder(
            LayoutNotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationAdapter1.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ICallBack {
        fun onItemClick(data: NotificationItem)
    }
}