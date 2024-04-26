package com.base.app.ui.story.content_story

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.story.StoryContentModel
import com.base.app.databinding.ItemDetailHomePostBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

/**
 * @author tuanpham
 * @since 4/27/2024
 */
class StoryContentAdapter(private val listener: (id: Int) -> Unit) :
    ListAdapter<StoryContentModel, StoryContentAdapter.ViewHolder>(StoryContentDiffUtil) {

    inner class ViewHolder(val binding: ItemDetailHomePostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: StoryContentModel, lengthSize: Int) = with(binding) {
            textPosition.text = "${absoluteAdapterPosition + 1} / $lengthSize"
            if (absoluteAdapterPosition != lengthSize - 1) {
                imageDetail.visibility = View.VISIBLE
                cardView.visibility = View.VISIBLE
                buttonAddMoreContent.visibility = View.GONE
                CoroutineScope(Dispatchers.IO).launch {
                    val url = URL(data.contentUrl)
                    try {
                        val image =
                            BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        withContext(Dispatchers.Main) {
                            imageDetail.setImageBitmap(image)
                        }
                    } catch (e: IOException) {
                        println(e)
                    }
                }
            } else {
                cardView.visibility = View.GONE
                imageDetail.visibility = View.GONE
                buttonAddMoreContent.visibility = View.VISIBLE
                buttonAddMoreContent.setOnClickListener {
                    listener(data.id ?: -1)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDetailHomePostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), currentList.size)
    }

    private object StoryContentDiffUtil : ItemCallback<StoryContentModel>() {
        override fun areItemsTheSame(
            oldItem: StoryContentModel,
            newItem: StoryContentModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StoryContentModel,
            newItem: StoryContentModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}