package com.base.app.ui.ecommerce_shop.home_electronic_device

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.app.R
import com.base.app.ui.ecommerce_shop.home_all_product.ShopHomeAllProductFragment

class ShopHomeElectronicDeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_home_electronic_device, container, false)
    }

    companion object {
        fun newInstance() = ShopHomeElectronicDeviceFragment()
    }
}