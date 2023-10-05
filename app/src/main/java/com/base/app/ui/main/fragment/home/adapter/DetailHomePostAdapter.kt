package com.base.app.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.ItemDetailHomePostBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 9/22/2023
 */
class DetailHomePostAdapter(private val list: List<ImagesList>) :
    RecyclerView.Adapter<DetailHomePostAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemDetailHomePostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ImagesList, position: Int) = with(binding) {
            Glide.with(root.context).load(data.imageUrl.toString())
                .into(binding.imageDetail)
            textPosition.text = "${position + 1} / ${list?.size}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDetailHomePostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}