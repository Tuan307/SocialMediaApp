package com.base.app.ui.group.for_you.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.ItemForYouGroupHeaderBinding
import com.base.app.ui.group.for_you.viewdata.GroupItemYourGroupViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/18/2023
 */
class GroupForYouHeaderAdapter(
    private val list: List<GroupItemYourGroupViewData>,
    private val listener: OnForYouGroupHeader
) :
    RecyclerView.Adapter<GroupForYouHeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemForYouGroupHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupItemYourGroupViewData, listener: OnForYouGroupHeader) = with(binding) {
            if (data.isLast) {
                textGroupName.visibility = View.GONE
                imageSeeMore.visibility = View.VISIBLE
            } else {
                textGroupName.text = data.groupName
                textGroupName.visibility = View.VISIBLE
                imageSeeMore.visibility = View.GONE
            }
            root.setOnClickListener {
                listener.onGroupClick(data)
            }
            Glide.with(root.context).load(data.groupImage).into(imageGroup)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemForYouGroupHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnForYouGroupHeader {
        fun onGroupClick(data: GroupItemYourGroupViewData)
    }
}