package com.base.app.ui.main.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.common.EMPTY_STRING
import com.base.app.databinding.LayoutPostInPostItemBinding
import com.bumptech.glide.Glide

class PostInPostAdapter(
    val context: Context,
    val list: ArrayList<String?>?
) : RecyclerView.Adapter<PostInPostAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: LayoutPostInPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            Glide.with(context).load(data).into(binding.imgPost)
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
        return holder.bind(list?.get(position) ?: EMPTY_STRING)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}