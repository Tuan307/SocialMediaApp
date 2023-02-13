package com.base.app.ui.follow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.User
import com.base.app.databinding.FollowerItemBinding
import com.bumptech.glide.Glide

class FollowerAdapter(val context: Context, val list: ArrayList<User>, val iCallBack: ICallBack) :
    RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FollowerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            Glide.with(context).load(data.imageurl).into(binding.imgAvatar)
            binding.txtName.text = data.fullname
            binding.txtUserName.text = data.username
            binding.searchConstrainView.setOnClickListener {
                data.id?.let { it1 -> iCallBack.itemClick(it1) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerAdapter.ViewHolder {
        return ViewHolder(
            FollowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FollowerAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ICallBack {
        fun itemClick(id: String)
    }
}