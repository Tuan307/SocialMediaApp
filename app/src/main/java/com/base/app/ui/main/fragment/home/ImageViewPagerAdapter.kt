package com.base.app.ui.main.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.PostItemAdapterBinding
import com.bumptech.glide.Glide

class ImageViewPagerAdapter(
    val context: Context,
    var dataSet: List<String?>
) : RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PostItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            Glide.with(context).load(item).into(binding.imgPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PostItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataSet[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}