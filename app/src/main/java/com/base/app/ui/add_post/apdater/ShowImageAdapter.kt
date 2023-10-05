package com.base.app.ui.add_post.map_apdater

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.ItemShowPostImageBinding

/**
 * @author tuanpham
 * @since 9/29/2023
 */
class ShowImageAdapter(
    private val list: ArrayList<Uri>,
    private val listener: ShowImageClickListener
) : RecyclerView.Adapter<ShowImageAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemShowPostImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) = with(binding) {
            val data = list[position]
            imageShow.setImageURI(data)
            imageCloseShowImage.setOnClickListener {
                listener.clickOnClose(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemShowPostImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ShowImageClickListener {
        fun clickOnClose(position: Int)
    }
}