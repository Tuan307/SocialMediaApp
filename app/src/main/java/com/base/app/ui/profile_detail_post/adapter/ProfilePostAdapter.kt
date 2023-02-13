package com.base.app.ui.profile_detail_post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.PostItem
import com.base.app.databinding.LayoutHomeAdapterBinding

class ProfilePostAdapterDetail(
    val context: Context,
    val list: ArrayList<PostItem>,
) : RecyclerView.Adapter<ProfilePostAdapterDetail.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutHomeAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PostItem){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            LayoutHomeAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }


}