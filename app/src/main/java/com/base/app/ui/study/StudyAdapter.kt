package com.base.app.ui.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.databinding.StudyItemBinding

class StudyAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<StudyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: StudyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.txtStudy.text = data
            binding.txtStudy1.text = "$data 1"
            binding.txtStudy2.text = "$data 2"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StudyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}