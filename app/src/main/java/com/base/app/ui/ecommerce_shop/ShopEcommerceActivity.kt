package com.base.app.ui.ecommerce_shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.base.app.R
import com.base.app.databinding.ActivityShopEcommerceBinding

class ShopEcommerceActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityShopEcommerceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_ecommerce)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_shop_container) as NavHostFragment
        navController = navHostFragment.findNavController()
    }
}