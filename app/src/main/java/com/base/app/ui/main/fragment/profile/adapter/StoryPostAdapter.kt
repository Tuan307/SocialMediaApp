package com.base.app.ui.main.fragment.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.story.StoryModel
import com.base.app.databinding.LayoutItemStoryBinding
import com.bumptech.glide.Glide


/**
 * @author tuanpham
 * @since 4/23/2024
 */
class StoryPostAdapter(
    private val onStoryClick: (Int) -> Unit
) : ListAdapter<StoryModel, StoryPostAdapter.ViewHolder>(StoryModelDiffUtil) {

    inner class ViewHolder(val binding: LayoutItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryModel) = with(binding) {
            if (absoluteAdapterPosition == currentList.size - 1) {
                textName.text = "Thêm mới +"
                imageStory.setImageResource(R.drawable.ic_add_chat_photo)
            } else {
                textName.text = data.name
                Glide.with(root.context).load(data.imageUrl).into(imageStory)
            }
            imageStory.setOnClickListener {
                onStoryClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object StoryModelDiffUtil : ItemCallback<StoryModel>() {
        override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
            return oldItem == newItem
        }

    }

}