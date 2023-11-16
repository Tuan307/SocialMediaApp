package com.base.app.ui.options

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.LayoutItemOptionShortcutBinding
import com.base.app.ui.explore_city.ExploreCityActivity
import com.base.app.ui.group.GroupActivity
import com.base.app.ui.main.fragment.search.SearchForFriendActivity

/**
 * @author tuanpham
 * @since 11/17/2023
 */
class OptionsAdapter(
    private val list: ArrayList<OptionViewData>,
) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    class ViewHolder(val binding: LayoutItemOptionShortcutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OptionViewData) = with(binding) {
            textShortcutTitle.text = data.name
            imageIcon.setImageResource(data.icon)
            root.setOnClickListener {
                when (data.name) {
                    "Nhóm du lịch" -> {
                        val intent = Intent(root.context, GroupActivity::class.java)
                        root.context.startActivity(intent)
                    }
                    "Tìm bạn bè" -> {
                        val intent = Intent(root.context, SearchForFriendActivity::class.java)
                        root.context.startActivity(intent)
                    }
                    "Khám phá du lịch" -> {
                        val intent = Intent(root.context, ExploreCityActivity::class.java)
                        root.context.startActivity(intent)
                    }
                    "Chế độ sáng/tối" -> {
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemOptionShortcutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}