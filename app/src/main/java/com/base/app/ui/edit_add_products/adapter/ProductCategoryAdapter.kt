//package com.base.app.ui.edit_add_products.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import androidx.databinding.DataBindingUtil
//import com.base.app.R
//import com.base.app.data.models.response.product.Category
//import com.base.app.databinding.ItemSpinnerReasonBinding
//
//class ProductCategoryAdapter(context: Context, data: List<Category>) :
//    ArrayAdapter<Category>(context, 0, data) {
//
//    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
//        return this.createView(position, parent)
//    }
//
//    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
//        return this.createView(position, parent)
//    }
//
//    private fun createView(position: Int, parent: ViewGroup): View {
//        val info = getItem(position)
//
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding = DataBindingUtil.inflate(
//            layoutInflater,
//            R.layout.item_spinner_reason,
//            parent,
//            false
//        ) as ItemSpinnerReasonBinding
//        binding.tvReason.text = info?.name
//        return binding.root
//    }
//}