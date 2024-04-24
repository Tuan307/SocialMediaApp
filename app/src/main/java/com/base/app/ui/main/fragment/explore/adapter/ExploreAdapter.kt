package com.base.app.ui.main.fragment.explore.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.LayoutItemExploreBinding
import com.base.app.ui.explore_city.ExploreCityActivity
import com.base.app.ui.explore_city.detail_city.DetailExploreActivity
import com.base.app.ui.group.GroupActivity
import com.base.app.ui.group.detail_group.GroupDetailActivity
import com.base.app.ui.main.fragment.explore.viewdata.ExploreItemViewData
import com.base.app.ui.main.fragment.explore.viewdata.ExploreViewData
import com.base.app.ui.profile.AllExploreActivity
import com.base.app.ui.profile.ProfileActivity

/**
 * @author tuanpham
 * @since 10/28/2023
 */
class ExploreAdapter :
    ListAdapter<ExploreViewData, ExploreAdapter.ViewHolder>(ExploreAdapterDiffUtil) {
    class ViewHolder(private val binding: LayoutItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var itemAdapter: ItemExploreAdapter
        fun bind(data: ExploreViewData) = with(binding) {
            itemAdapter = ItemExploreAdapter(data.exploreItems,
                object : ItemExploreAdapter.OnItemExploreClick {
                    override fun onCLickFriend(data: ExploreItemViewData) {
                        val intent = Intent(root.context, ProfileActivity::class.java)
                        intent.putExtra("userId", data.id)
                        root.context.startActivity(intent)
                    }

                    override fun onCLickGroup(data: ExploreItemViewData) {
                        val intent = Intent(root.context, GroupDetailActivity::class.java)
                        intent.putExtra("groupId", data.id.toLong())
                        intent.putExtra("groupName", data.name)
                        root.context.startActivity(intent)
                    }

                    override fun onCLickDestination(data: ExploreItemViewData) {
                        val intent = Intent(root.context, DetailExploreActivity::class.java)
                        intent.putExtra("id", data.id)
                        intent.putExtra("url", data.url)
                        root.context.startActivity(intent)
                    }
                })
            textExploreTitle.text = data.title
            imageViewAll.setOnClickListener {
                when (data.id) {
                    "near_by" -> {
                        val intent = Intent(root.context, AllExploreActivity::class.java)
                        intent.putExtra("type", data.id)
                        root.context.startActivity(intent)
                    }
                    "group" -> {
                        val intent = Intent(root.context, GroupActivity::class.java)
                        root.context.startActivity(intent)
                    }
                    else -> {
                        val intent = Intent(root.context, ExploreCityActivity::class.java)
                        root.context.startActivity(intent)
                    }
                }
            }
            listOfExploreItem.apply {
                layoutManager =
                    LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = itemAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemExploreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object ExploreAdapterDiffUtil : ItemCallback<ExploreViewData>() {
        override fun areItemsTheSame(oldItem: ExploreViewData, newItem: ExploreViewData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExploreViewData,
            newItem: ExploreViewData
        ): Boolean {
            return oldItem == newItem
        }
    }
}