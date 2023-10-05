package com.base.app.ui.main.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.LayoutPostInPostItemBinding
import com.bumptech.glide.Glide


class PostInPostAdapter(
    val context: Context,
    val list: List<ImagesList>?,
    val listener: OnImageCLick
) : RecyclerView.Adapter<PostInPostAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: LayoutPostInPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ImagesList, position: Int) {
            if (data.imageUrl != null) {
                Glide.with(context).load(data.imageUrl)
                    .into(binding.imgPost)
            }
            binding.imgPost.setOnClickListener {
                listener.onImageClick(position)
            }
            binding.textPosition.text = "${position + 1} / ${(list?.size ?: 0)}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostInPostAdapter.ViewHolder {
        return ViewHolder(
            LayoutPostInPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostInPostAdapter.ViewHolder, position: Int) {
        return holder.bind(list?.get(position) ?: ImagesList(0L, ""), position)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    interface OnImageCLick {
        fun onImageClick(position: Int)
    }
}