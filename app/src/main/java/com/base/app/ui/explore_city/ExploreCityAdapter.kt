package com.base.app.ui.explore_city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.city.CityModel
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.LayoutItemExploreCityBinding
import com.base.app.ui.main.fragment.home.PostInPostAdapter

/**
 * @author tuanpham
 * @since 11/9/2023
 */
class ExploreCityAdapter(private val listener: OnExploreCityClick) :
    ListAdapter<CityModel, ExploreCityAdapter.ViewHolder>(ExploreCityDiffUtil) {
    class ViewHolder(private val binding: LayoutItemExploreCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CityModel, listener: OnExploreCityClick) = with(binding) {
            root.setOnClickListener {
                listener.onCityClick(data)
            }
            textDescription.text = data.description
            textName.text = data.cityName
            val postInPostAdapter =
                PostInPostAdapter(
                    binding.root.context,
                    data.cityImages.orEmpty().map { it1 ->
                        ImagesList(
                            id = it1.id,
                            imageUrl = it1.imageUrl
                        )
                    },
                    object : PostInPostAdapter.OnImageCLick {
                        override fun onImageClick(position: Int) {
                        }
                    })
            listImagePost.apply {
                adapter = postInPostAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemExploreCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    private object ExploreCityDiffUtil : ItemCallback<CityModel>() {
        override fun areItemsTheSame(oldItem: CityModel, newItem: CityModel): Boolean {
            return oldItem.cityId == newItem.cityId
        }

        override fun areContentsTheSame(oldItem: CityModel, newItem: CityModel): Boolean {
            return oldItem == newItem
        }
    }

    interface OnExploreCityClick {
        fun onCityClick(data: CityModel)
    }
}