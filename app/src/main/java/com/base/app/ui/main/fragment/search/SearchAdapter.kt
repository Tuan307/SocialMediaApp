package com.base.app.ui.main.fragment.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.SearchItemBinding
import com.bumptech.glide.Glide

class SearchAdapter(
    val iCallBack: ICallBack,
) : ListAdapter<DatingUser, SearchAdapter.ViewHolder>(SearchUserDiffUtil) {

    inner class ViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DatingUser) = with(binding) {
            Glide.with(root.context).load(data.imageUrl).into(binding.imgAvatar)
            binding.txtName.text = data.fullName
            binding.txtUserName.text = data.userName
            binding.searchConstrainView.setOnClickListener {
                data.userId.let { it1 -> iCallBack.itemClick(it1) }
            }
            binding.imgRemove.setOnClickListener {
                data.userId.let { it1 -> iCallBack.removeSearch(it1) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object SearchUserDiffUtil : ItemCallback<DatingUser>() {
        override fun areItemsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem == newItem
        }

    }

    interface ICallBack {
        fun itemClick(id: String)
        fun removeSearch(id: String)
    }
}