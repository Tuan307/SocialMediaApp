package com.base.app.ui.study

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.databinding.StudyItemBinding

class StudyAdapter(
    private val context: Context,
    private val list: ArrayList<Person>,
    private val onClickDelete: clickDelete
) :
    RecyclerView.Adapter<StudyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: StudyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            val data = list[pos]
            binding.constraintStudy.setBackgroundColor(
                context.resources.getColor(R.color.colorWhite)
            )
            binding.txtStudy.text = data.name
            binding.txtStudy1.text = data.address
            binding.btnDelete.setOnClickListener {
                onClickDelete.deleteItem(pos)
            }

            binding.constraintStudy.setOnClickListener {
                onClickDelete.clickItem(pos)
                binding.constraintStudy.setBackgroundColor(
                    context.resources.getColor(R.color.color_blue)
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StudyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface clickDelete {
        fun deleteItem(item: Int)
        fun clickItem(item: Int)

    }
}