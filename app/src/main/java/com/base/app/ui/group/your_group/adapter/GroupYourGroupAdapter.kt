package com.base.app.ui.group.your_group.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.databinding.LayoutYourGroupBodyBinding
import com.base.app.databinding.LayoutYourGroupHeaderBinding
import com.base.app.ui.group.your_group.viewdata.GroupBodyViewData
import com.base.app.ui.group.your_group.viewdata.GroupHeaderViewData
import com.base.app.ui.group.your_group.viewdata.GroupViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/12/2023
 */
class GroupYourGroupAdapter(private val listener: OnShowAllGroup) :
    ListAdapter<GroupViewData, RecyclerView.ViewHolder>(GroupViewData.GroupViewDataDiffUtil) {

    class HeaderViewHolder(val binding: LayoutYourGroupHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupHeaderViewData, listener: OnShowAllGroup) = with(binding) {
            textTitleHeader.text = data.title
            imageShowList.setOnClickListener {
                listener.onShowAllList(data.title)
            }
        }
    }

    class BodyViewHolder(val binding: LayoutYourGroupBodyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupBodyViewData, listener: OnShowAllGroup) = with(binding) {
            textGroupName.text = data.groupName
            textGroupJoinedDate.text = data.groupJoinedDate
            Glide.with(root.context).load(data.groupImage).into(imageGroupAvatar)
            imageGroupAvatar.setOnClickListener {
                listener.onShowDetailGroup(data.id,data.groupName)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)

        return when (viewType) {
            R.layout.layout_your_group_header -> {
                HeaderViewHolder(binding as LayoutYourGroupHeaderBinding)
            }
            R.layout.layout_your_group_body -> {
                BodyViewHolder(binding as LayoutYourGroupBodyBinding)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position) as GroupHeaderViewData, listener)
            }
            is BodyViewHolder -> {
                holder.bind(getItem(position) as GroupBodyViewData, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutRes
    }

    interface OnShowAllGroup {
        fun onShowAllList(type: String)
        fun onShowDetailGroup(groupId: Long,groupName:String)
    }
}