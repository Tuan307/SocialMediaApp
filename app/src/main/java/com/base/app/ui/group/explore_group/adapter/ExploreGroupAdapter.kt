package com.base.app.ui.group.explore_group.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.LayoutExploreGroupItemBinding
import com.base.app.ui.group.explore_group.ExploreGroupViewModel
import com.base.app.ui.group.explore_group.viewdata.GroupDataViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/18/2023
 */
class ExploreGroupAdapter(
    private val viewModel: ExploreGroupViewModel,
    private val listener: OnExploreGroupClick
) :
    ListAdapter<GroupDataViewData, ExploreGroupAdapter.ViewHolder>(ExploreGroupDiffUtil) {

    class ViewHolder(private val binding: LayoutExploreGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: GroupDataViewData,
            viewModel: ExploreGroupViewModel,
            listener: OnExploreGroupClick
        ) =
            with(binding) {
                textGroupName.text = data.groupName
                textGroupPrivacy.text = if (data.groupPrivacy == "private") {
                    "Nhóm riêng tư"
                } else {
                    "Nhóm công khai"
                }
                if (data.hasJoined) {
                    if (data.groupPrivacy == "private") {
                        buttonJoinGroup.text = "Đã gửi yêu cầu"
                    } else {
                        buttonJoinGroup.text = "Đã tham gia"
                    }
                }
                Glide.with(root.context).load(data.groupImageUrl).into(imageGroup)
                data.id.let { viewModel.getAllGroupMemberInformation(it, textGroupMember) }
                buttonJoinGroup.setOnClickListener {
                    listener.onJoinRequest(data)
                }
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
        holder.bind(getItem(position), viewModel, listener)
    }

    object ExploreGroupDiffUtil : ItemCallback<GroupDataViewData>() {
        override fun areItemsTheSame(
            oldItem: GroupDataViewData,
            newItem: GroupDataViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GroupDataViewData,
            newItem: GroupDataViewData
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnExploreGroupClick {
        fun onJoinRequest(data: GroupDataViewData)
    }
}