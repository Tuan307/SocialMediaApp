package com.base.app.ui.group.for_you.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.ItemForYouGroupHeaderBinding
import com.base.app.ui.group.for_you.viewdata.GroupItemYourGroupViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/18/2023
 */
class GroupForYouHeaderAdapter(private val list: List<GroupItemYourGroupViewData>) :
    RecyclerView.Adapter<GroupForYouHeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemForYouGroupHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupItemYourGroupViewData) = with(binding) {
            Glide.with(root.context).load(data.groupImage).into(imageGroup)
            Glide.with(root.context).load(data.groupImage).into(imageGroup)
            textGroupName.text = data.groupName
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
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}