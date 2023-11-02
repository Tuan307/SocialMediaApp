package com.base.app.ui.main.fragment.explore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.ItemExploreBinding
import com.base.app.ui.main.fragment.explore.viewdata.ExploreItemViewData
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 10/28/2023
 */
class ItemExploreAdapter(
    private val list: List<ExploreItemViewData>,
    private val listener: OnItemExploreClick
) :
    RecyclerView.Adapter<ItemExploreAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ExploreItemViewData, listener: OnItemExploreClick) = with(binding) {
            when (data.type) {
                0 -> {
                    textNumber.visibility = View.GONE
                    cardGroupAndUser.visibility = View.VISIBLE
                    cardDestination.visibility = View.GONE
                    textName.text = data.name
                    Glide.with(root.context).load(data.image).into(imageExplore)
                    imageExplore.setOnClickListener {
                        listener.onCLickFriend(data)
                    }
                }
                1 -> {
                    textNumber.visibility = View.GONE
                    cardGroupAndUser.visibility = View.VISIBLE
                    cardDestination.visibility = View.GONE
                    textName.text = data.name
                    Glide.with(root.context).load(data.image).into(imageExplore)
                    imageExplore.setOnClickListener {
                        listener.onCLickGroup(data)
                    }
                }
                2 -> {
                    textNumber.visibility = View.GONE
                    cardGroupAndUser.visibility = View.GONE
                    cardDestination.visibility = View.VISIBLE
                    textNameDestination.text = data.name
                    Glide.with(root.context).load(data.image).into(imageExploreDestination)
                    imageExploreDestination.setOnClickListener {
                        listener.onCLickDestination(data)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExploreBinding.inflate(
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

    interface OnItemExploreClick {
        fun onCLickFriend(data: ExploreItemViewData)
        fun onCLickGroup(data: ExploreItemViewData)
        fun onCLickDestination(data: ExploreItemViewData)
    }
}