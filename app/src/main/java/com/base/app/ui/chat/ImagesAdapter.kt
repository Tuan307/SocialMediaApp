package com.base.app.ui.chat

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.databinding.ItemImageChooseBinding
import com.bumptech.glide.Glide
import java.io.File

/**
 * @author tuanpham
 * @since 8/18/2023
 */
class ImagesAdapter(private val list: ArrayList<String>, private val listener: ChooseImage) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemImageChooseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String, listener: ChooseImage) = with(binding) {
            Glide.with(root.context).load(File(data)).placeholder(R.mipmap.ic_launcher)
                .into(imageChat)
            imageChat.setOnClickListener {
                listener.onChooseImage(Uri.fromFile(File(data)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageChooseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ChooseImage {
        fun onChooseImage(url: Uri)
    }
}