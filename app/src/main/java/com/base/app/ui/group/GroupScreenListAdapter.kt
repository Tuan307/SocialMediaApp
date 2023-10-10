package com.base.app.ui.group

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.group.ScreenTitle
import com.base.app.databinding.ItemGroupScreenBinding

/**
 * @author tuanpham
 * @since 10/10/2023
 */
class GroupScreenListAdapter(
    private val listener: OnGroupScreenTitleClick
) : ListAdapter<ScreenTitle, GroupScreenListAdapter.ViewHolder>(
    ScreenTitle.ScreenTitleDiffUtil
) {

    inner class ViewHolder(val binding: ItemGroupScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(data: ScreenTitle, listener: OnGroupScreenTitleClick) = with(binding) {
            textScreenTitle.text = data.title
            if (data.isCheck) {
                textScreenTitle.backgroundTintList =
                    ColorStateList.valueOf(root.context.getColor(R.color.color_blue_faded))
            } else {
                textScreenTitle.backgroundTintList = null
            }
            textScreenTitle.setOnClickListener {
                if (!data.isCheck) {
                    listener.onClick(data.title)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGroupScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    interface OnGroupScreenTitleClick {
        fun onClick(title: String)
    }
}