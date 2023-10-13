package com.base.app.ui.group.your_group.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.LayoutYourGroupBodyBinding
import com.base.app.ui.group.your_group.viewdata.GroupBodyViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/12/2023
 */
class GroupAllYourGroupAdapter :
    ListAdapter<GroupBodyViewData, GroupAllYourGroupAdapter.ViewHolder>(GroupAllGroupDiffUtil) {
    class ViewHolder(private val binding: LayoutYourGroupBodyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupBodyViewData) = with(binding) {
            textGroupName.text = data.groupName
            textGroupJoinedDate.text = data.groupJoinedDate
            Glide.with(root.context).load(data.groupImage).into(imageGroupAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutYourGroupBodyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object GroupAllGroupDiffUtil : ItemCallback<GroupBodyViewData>() {
        override fun areItemsTheSame(
            oldItem: GroupBodyViewData,
            newItem: GroupBodyViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GroupBodyViewData,
            newItem: GroupBodyViewData
        ): Boolean {
            return oldItem == newItem
        }

    }
}