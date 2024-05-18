package com.base.app.ui.ecommerce_shop.home_male_clothe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.base.app.R

class ShopHomeMaleClothFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_home_male_cloth, container, false)
    }

    companion object {
        fun newInstance() = ShopHomeMaleClothFragment()
    }
}