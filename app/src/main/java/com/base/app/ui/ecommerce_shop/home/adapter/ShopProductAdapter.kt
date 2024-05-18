package com.base.app.ui.ecommerce_shop.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.shop_ecommerce.ShopEcommerceModel
import com.base.app.databinding.LayoutShopEcommerceProductItemBinding

/**
 * @author tuanpham
 * @since 5/7/2024
 */
class ShopProductAdapter :
    ListAdapter<ShopEcommerceModel, ShopProductAdapter.ViewHolder>(ShopProductDiffUtil) {

    class ViewHolder(private val binding: LayoutShopEcommerceProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ShopEcommerceModel) = with(binding) {
            data.imageUrl?.let { imageProduct.setImageResource(it) }
            textProductName.text = data.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutShopEcommerceProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object ShopProductDiffUtil : ItemCallback<ShopEcommerceModel>() {
        override fun areItemsTheSame(
            oldItem: ShopEcommerceModel,
            newItem: ShopEcommerceModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShopEcommerceModel,
            newItem: ShopEcommerceModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}