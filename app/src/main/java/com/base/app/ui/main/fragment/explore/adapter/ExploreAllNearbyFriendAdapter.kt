package com.base.app.ui.main.fragment.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.ItemExploreAllNearbyFriendBinding
import com.base.app.ui.main.fragment.explore.viewdata.ExploreItemViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 12/23/2023
 */
class ExploreAllNearbyFriendAdapter(private val listener: (ExploreItemViewData) -> Unit) :
    ListAdapter<ExploreItemViewData, ExploreAllNearbyFriendAdapter.ViewHolder>(
        ExploreAllNearbyFriendDiffUtil
    ) {

    inner class ViewHolder(private val binding: ItemExploreAllNearbyFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ExploreItemViewData) = with(binding) {
            textFriendName.text = data.name
            textFriendUserName.text = data.userName
            Glide.with(root.context).load(data.image).into(imageFriendAvatar)
            root.setOnClickListener {
                listener(data)
            }
        }
    }

    private object ExploreAllNearbyFriendDiffUtil : ItemCallback<ExploreItemViewData>() {
        override fun areItemsTheSame(
            oldItem: ExploreItemViewData,
            newItem: ExploreItemViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExploreItemViewData,
            newItem: ExploreItemViewData
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExploreAllNearbyFriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}