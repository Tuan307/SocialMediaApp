package com.base.app.ui.ecommerce_shop.home_all_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.shop_ecommerce.ShopEcommerceModel
import com.base.app.databinding.FragmentShopHomeAllProductBinding
import com.base.app.ui.ecommerce_shop.home.adapter.ShopProductAdapter

class ShopHomeAllProductFragment : Fragment() {
    private lateinit var binding: FragmentShopHomeAllProductBinding
    private lateinit var productAdapter: ShopProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopHomeAllProductBinding.inflate(inflater, container, false)
        productAdapter = ShopProductAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listUser =
            arrayListOf(
                ShopEcommerceModel(1, R.drawable.astrosplash, "San pham 1", null, null, null, null),
                ShopEcommerceModel(2, R.drawable.astrosplash, "San pham 2", null, null, null, null),
                ShopEcommerceModel(3, R.drawable.astrosplash, "San pham 3", null, null, null, null),
                ShopEcommerceModel(4, R.drawable.astrosplash, "San pham 4", null, null, null, null),
                ShopEcommerceModel(5, R.drawable.astrosplash, "San pham 5", null, null, null, null),
            )
        with(binding) {
            listOfPosts.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdapter
            }
        }
        productAdapter.submitList(listUser.toList())
    }

    companion object {
        fun newInstance() = ShopHomeAllProductFragment()
    }
}