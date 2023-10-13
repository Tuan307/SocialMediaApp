package com.base.app.ui.group.detail_group.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.databinding.LayoutDetailGroupInformationBinding
import com.base.app.databinding.LayoutHomeAdapterBinding
import com.base.app.ui.group.detail_group.viewdata.DetailGroupInformationViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupPostViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/13/2023
 */
class DetailGroupAdapter :
    ListAdapter<DetailGroupViewData, RecyclerView.ViewHolder>(DetailGroupViewData.DetailGroupDiffUtil) {

    class InformationViewHolder(val binding: LayoutDetailGroupInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DetailGroupInformationViewData) = with(binding) {
            Glide.with(root.context).load(data.imageUrl).into(imageGroupBanner)
            Glide.with(root.context).load(data.userImageUrl).into(imageAvatar)
            textGroupName.text = data.groupName
            textGroupPrivacy.text = if (data.groupPrivacy == "private") {
                "Riêng tư"
            } else {
                "Công khai"
            }
            textMemberNumber.text = data.groupMemberNumber
            buttonJoinGroup.text = if (data.hasJoined) {
                "Đã tham gia"
            } else {
                "Tham gia"
            }
        }
    }

    class GroupPostViewHolder(val binding: LayoutHomeAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DetailGroupPostViewData) = with(binding) {
            listImagePost.visibility = View.GONE
            imageVideoThumbnail.visibility = View.VISIBLE
            Glide.with(root.context).load(data.imagesList?.get(0)).into(imageVideoThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.layout_home_adapter -> {
                GroupPostViewHolder(
                    LayoutHomeAdapterBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            R.layout.layout_detail_group_information -> {
                InformationViewHolder(
                    LayoutDetailGroupInformationBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InformationViewHolder -> {
                holder.bind(getItem(position) as DetailGroupInformationViewData)
            }
            is GroupPostViewHolder -> {
                holder.bind(getItem(position) as DetailGroupPostViewData)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutRes
    }

}