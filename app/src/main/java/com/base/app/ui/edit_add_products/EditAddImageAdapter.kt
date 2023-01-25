//package com.base.app.ui.edit_add_products
//
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.RecyclerView
//import com.base.app.R
//import com.base.app.databinding.ItemAddImageBinding
//import com.base.app.databinding.LayoutRefundImageItemBinding
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.Target
//import com.base.app.common.AUTO_DELETE
//import com.base.app.common.MANUAL_DELETE
//
//
//class EditAddImageAdapter(
//    var listNews: MutableList<Any?>?,
//    val iRefundImageCallBack: IRefundImageCallBack
//) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private val TYPE_IMAGE = 0
//    private val TYPE_ADD = 1
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == TYPE_IMAGE) {
//            val binding = DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                R.layout.layout_refund_image_item,
//                parent,
//                false
//            ) as ViewDataBinding
//            ImageViewHolder(binding as LayoutRefundImageItemBinding)
//        } else {
//            val binding = DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                R.layout.item_add_image,
//                parent,
//                false
//            ) as ViewDataBinding
//            NewViewHolder(binding as ItemAddImageBinding)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return listNews?.size ?: 0
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val obj = listNews?.get(position)
//        when (TYPE_IMAGE) {
//            getItemViewType(position) -> {
//                (holder as ImageViewHolder).onFill(obj as Uri, position)
//            }
//            else -> {
//                (holder as NewViewHolder).fillData()
//            }
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when {
//            position != (listNews?.size ?: 0) - 1 -> {
//                TYPE_IMAGE
//            }
//            else -> {
//                TYPE_ADD
//            }
//        }
//    }
//
//    inner class ImageViewHolder(val binding: LayoutRefundImageItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun onFill(data: Uri?, position: Int) {
//            if (data.toString().contains("http")) {
//                Glide.with(binding.root.context)
//                    .load(data)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            iRefundImageCallBack.removeItem(
//                                listNews?.indexOf(data) ?: 0,
//                                AUTO_DELETE
//                            )
//                            return true
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            dataSource: DataSource?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            return false
//                        }
//                    })
//                    .into(binding.imvThumb)
//            } else {
//                binding.imvThumb.setImageURI(data)
//            }
//            binding.imvRemove.setOnClickListener {
//                iRefundImageCallBack.removeItem(listNews?.indexOf(data) ?: 0, MANUAL_DELETE)
//            }
//            binding.root.setOnClickListener {
//                iRefundImageCallBack.onItemClick()
//            }
//        }
//    }
//
//    inner class NewViewHolder(val binding: ItemAddImageBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun fillData() {
//            binding.root.setOnClickListener {
//                iRefundImageCallBack.addImage()
//            }
//        }
//    }
//
//    interface IRefundImageCallBack {
//        fun removeItem(position: Int, type: String)
//        fun onItemClick()
//        fun addImage()
//    }
//}
//
