package com.base.app.ui.ecommerce_shop.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.shop_ecommerce.ShopEcommerceCategoryItem
import com.base.app.databinding.FragmentShopHomeBinding
import com.base.app.ui.ecommerce_shop.home.adapter.ShopCategoryAdapter
import com.base.app.ui.ecommerce_shop.home.adapter.TabLayoutViewPagerAdapter
import com.base.app.ui.ecommerce_shop.home_all_product.ShopHomeAllProductFragment
import com.base.app.ui.ecommerce_shop.home_electronic_device.ShopHomeElectronicDeviceFragment
import com.base.app.ui.ecommerce_shop.home_female_cloth.ShopHomeFemaleClothFragment
import com.base.app.ui.ecommerce_shop.home_male_clothe.ShopHomeMaleClothFragment
import com.google.android.material.tabs.TabLayoutMediator

class ShopHomeFragment : Fragment() {
    private var _binding: FragmentShopHomeBinding? = null
    private val binding: FragmentShopHomeBinding
        get() = _binding!!
    private lateinit var tabLayoutViewPagerAdapter: TabLayoutViewPagerAdapter
    private lateinit var categoryItemAdapter: ShopCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopHomeBinding.inflate(inflater, container, false)
        categoryItemAdapter = ShopCategoryAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfFragment = arrayListOf(
            ShopHomeAllProductFragment(),
            ShopHomeMaleClothFragment(),
            ShopHomeFemaleClothFragment(),
            ShopHomeElectronicDeviceFragment(),
        )
        val listOfTitle = arrayListOf(
            "Tất cả",
            "Quần áo nam",
            "Quần áo nữ",
            "Đồ dùng điện tử",
        )

        val listOfCategoryItem = arrayListOf(
            ShopEcommerceCategoryItem(1, R.drawable.ic_search, "Đơn hàng"),
            ShopEcommerceCategoryItem(1, R.drawable.ic_search, "Tin nhắn"),
            ShopEcommerceCategoryItem(1, R.drawable.ic_search, "Miễn phí vận chuyển"),
            ShopEcommerceCategoryItem(1, R.drawable.ic_search, "Hàng việt"),
        )

        tabLayoutViewPagerAdapter =
            TabLayoutViewPagerAdapter(listOfFragment, childFragmentManager, this.lifecycle)
        with(binding) {
            shopHomeViewPager.adapter = tabLayoutViewPagerAdapter
            shopHomeViewPager.offscreenPageLimit = listOfFragment.size
            TabLayoutMediator(tabLayoutHome, shopHomeViewPager) { tab, position ->
                tab.text = listOfTitle[position]
            }.attach()
            collapseShopHome.setContentScrimColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )

            listOfCategory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = categoryItemAdapter
            }

            categoryItemAdapter.submitList(listOfCategoryItem.toList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}