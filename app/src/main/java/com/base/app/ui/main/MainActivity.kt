package com.base.app.ui.main

import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.base.fragment.PagerFragmentAdapter
import com.base.app.databinding.ActivityMainBinding
import com.base.app.ui.main.fragment.home.HomeFragment
import com.base.app.ui.main.fragment.notification.NotificationFragment
import com.base.app.ui.main.fragment.profile.MyProfileFragment
import com.base.app.ui.main.fragment.reel.ReelFragment
import com.base.app.ui.main.fragment.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<ActivityMainBinding>(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val homeFragment = HomeFragment.newInstance()
    private val myProFileFragment = MyProfileFragment.newInstance()
    private val searchFragment = SearchFragment.newInstance()
    private val reelFragment = ReelFragment.newInstance()
    private val notificationFragment = NotificationFragment.newInstance()
    private val viewModel by viewModels<MainViewModel>()
    lateinit var mPagerAdapter: PagerFragmentAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        viewModel.saveId()
        binding.apply {
            bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        }
        initPagerFragment()
    }

    private fun initPagerFragment() {
        mPagerAdapter = PagerFragmentAdapter(supportFragmentManager)
        //add all fragment in main
        mPagerAdapter.addFragment(homeFragment)
        mPagerAdapter.addFragment(searchFragment)
        mPagerAdapter.addFragment(reelFragment)
        mPagerAdapter.addFragment(notificationFragment)
        mPagerAdapter.addFragment(myProFileFragment)
        binding.mainViewPager.adapter = mPagerAdapter
        binding.mainViewPager.offscreenPageLimit = mPagerAdapter.count
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        viewModel.currentIndex.observe(this@MainActivity, Observer {
            binding.mainViewPager.currentItem = it
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                if (isDoubleClick() && binding.mainViewPager.currentItem == 0) {
                    viewModel.setRefresh(true)
                } else {
                    binding.mainViewPager.currentItem = 0
                }
                return true
            }
            R.id.navigation_feature -> {
                binding.mainViewPager.currentItem = 1
                return true
            }
            R.id.navigation_reel -> {
                viewModel.setReelClick()
                binding.mainViewPager.currentItem = 2
                return true
            }
            R.id.navigation_notify -> {
                binding.mainViewPager.currentItem = 3
                return true
            }
            R.id.navigation_user -> {
                if (dataManager.getString("id") == null) {
                    viewModel.saveId()
                }
                viewModel.setSomething(false)
                binding.mainViewPager.currentItem = 4
                return true
            }
        }
        return false
    }

}