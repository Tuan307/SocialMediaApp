package com.base.app.ui.main.fragment.profile.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.PostItem
import com.bumptech.glide.Glide

class ProfilePostAdapter(
    val context: Context,
    val list: ArrayList<PostItem>
) : RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>() {

    class ViewHolder(val binding: View) :
        RecyclerView.ViewHolder(binding) {
        val image: ImageView?

        init {
            image = itemView.findViewById(R.id.imgImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_post_item, parent, false)
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        val width = context.resources.displayMetrics.widthPixels
        val imageWidth = width / 3
        params.height = imageWidth
        view.layoutParams = params
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        if (holder.image != null) {
            Glide.with(context).load(data.postimage).into(holder.image)
            holder.image.setOnClickListener {
                // do something later
                Log.d("CheckAAA", data.postid.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}