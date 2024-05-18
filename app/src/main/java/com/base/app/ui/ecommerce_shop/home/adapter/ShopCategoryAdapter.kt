package com.base.app.ui.ecommerce_shop.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.shop_ecommerce.ShopEcommerceCategoryItem
import com.base.app.databinding.LayoutItemEcommerceHomeCategoryBinding

/**
 * @author tuanpham
 * @since 5/14/2024
 */
class ShopCategoryAdapter :
    ListAdapter<ShopEcommerceCategoryItem, ShopCategoryAdapter.ViewHolder>(ShopCategoryItemDiffUtil) {

    class ViewHolder(private val binding: LayoutItemEcommerceHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ShopEcommerceCategoryItem) {
            with(binding) {
                imageCategory.setImageResource(data.imageUrl)
                textCategory.text = data.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemEcommerceHomeCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object ShopCategoryItemDiffUtil : ItemCallback<ShopEcommerceCategoryItem>() {
        override fun areItemsTheSame(
            oldItem: ShopEcommerceCategoryItem,
            newItem: ShopEcommerceCategoryItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShopEcommerceCategoryItem,
            newItem: ShopEcommerceCategoryItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}