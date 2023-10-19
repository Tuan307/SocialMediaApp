package com.base.app.ui.group.explore_group.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.group.response.GroupData
import com.base.app.databinding.LayoutExploreGroupItemBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/18/2023
 */
class ExploreGroupAdapter :
    ListAdapter<GroupData, ExploreGroupAdapter.ViewHolder>(ExploreGroupDiffUtil) {

    class ViewHolder(private val binding: LayoutExploreGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupData) = with(binding) {
            textGroupName.text = data.groupName
            Glide.with(root.context).load(data.groupImageUrl).into(imageGroup)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutExploreGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ExploreGroupDiffUtil : ItemCallback<GroupData>() {
        override fun areItemsTheSame(
            oldItem: GroupData,
            newItem: GroupData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GroupData,
            newItem: GroupData
        ): Boolean {
            return oldItem == newItem
        }

    }
}