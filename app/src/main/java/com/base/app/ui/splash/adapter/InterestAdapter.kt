package com.base.app.ui.splash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.interest.InterestModel
import com.base.app.databinding.ItemInterestBinding

/**
 * @author tuanpham
 * @since 11/5/2023
 */
class InterestAdapter(
    private val listener: OnInterestItemClick
) : ListAdapter<InterestModel, InterestAdapter.ViewHolder>(InterestDiffUtil) {
    inner class ViewHolder(private val binding: ItemInterestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, listener: OnInterestItemClick) = with(binding) {
            val model = getItem(position)
            textInterest.text = model.name
            root.setOnClickListener {
                listener.onInterestClick(model, position)
            }
            if (model.isChosen) {
                constraintItemInterest.background =
                    ContextCompat.getDrawable(root.context, R.drawable.bg_item_interest)
                textInterest.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_check_24,
                    0,
                    0,
                    0
                )
                textInterest.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.colorWhite
                    )
                )
            } else {
                constraintItemInterest.background =
                    ContextCompat.getDrawable(root.context, R.drawable.bg_item_interest_unchecked)
                textInterest.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_add_products,
                    0,
                    0,
                    0
                )
                textInterest.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.colorBlack
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemInterestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, listener)
    }

    private object InterestDiffUtil : ItemCallback<InterestModel>() {
        override fun areItemsTheSame(oldItem: InterestModel, newItem: InterestModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InterestModel, newItem: InterestModel): Boolean {
            return oldItem == newItem
        }
    }

    interface OnInterestItemClick {
        fun onInterestClick(data: InterestModel, position: Int)
    }
}