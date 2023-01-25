package com.base.app.ui.main.fragment.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.User
import com.base.app.databinding.SearchItemBinding
import com.bumptech.glide.Glide

class SearchAdapter(
    private val lists: ArrayList<User>,
    val context: Context,
    val iCallBack: ICallBack
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            Glide.with(context).load(data.imageurl).into(binding.imgAvatar)
            binding.txtName.text = data.fullname
            binding.txtUserName.text = data.username
            binding.searchConstrainView.setOnClickListener {
                data.id?.let { it1 -> iCallBack.itemClick(it1) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    interface ICallBack {
        fun itemClick(id: String)
    }
}