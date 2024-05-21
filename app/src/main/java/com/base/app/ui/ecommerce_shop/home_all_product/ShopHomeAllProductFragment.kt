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
import com.base.app.ui.utils.InfiniteScrollController

class ShopHomeAllProductFragment : Fragment() {
    private lateinit var binding: FragmentShopHomeAllProductBinding
    private lateinit var productAdapter: ShopProductAdapter
    private val listUser: ArrayList<ShopEcommerceModel> = arrayListOf()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var currentPage = 1
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
        listUser.addAll(
            arrayListOf(
                ShopEcommerceModel(
                    1,
                    R.drawable.astrosplash,
                    "San pham 1",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    2,
                    R.drawable.astrosplash,
                    "San pham 2",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    3,
                    R.drawable.astrosplash,
                    "San pham 3",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    4,
                    R.drawable.astrosplash,
                    "San pham 4",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    5,
                    R.drawable.astrosplash,
                    "San pham 5",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
            )
        )
        with(binding) {
            listOfPosts.apply {
                val linearLayoutManager = LinearLayoutManager(requireContext())
                layoutManager = linearLayoutManager
                adapter = productAdapter
                addOnScrollListener(object : InfiniteScrollController(linearLayoutManager) {
                    override fun loadMore() {
                        onLoadMore()
                    }

                    override fun isLoading(): Boolean {
                        return isLoading
                    }

                    override fun isLastPage(): Boolean {
                        return isLastPage
                    }

                })
            }
        }
        productAdapter.submitList(listUser.toList())
    }

    fun onLoadMore() {
        isLoading = true
        listUser.addAll(
            arrayListOf(
                ShopEcommerceModel(
                    6,
                    R.drawable.astrosplash,
                    "San pham 6",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    7,
                    R.drawable.astrosplash,
                    "San pham 7",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    8,
                    R.drawable.astrosplash,
                    "San pham 8",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    9,
                    R.drawable.astrosplash,
                    "San pham 9",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
                ShopEcommerceModel(
                    10,
                    R.drawable.astrosplash,
                    "San pham 10",
                    "San pham 1",
                    10000.0,
                    4.5,
                    350
                ),
            )
        )
        currentPage++
        productAdapter.submitList(listUser.toList())
        isLoading = false
        isLastPage = true
    }

    companion object {
        fun newInstance() = ShopHomeAllProductFragment()
    }
}