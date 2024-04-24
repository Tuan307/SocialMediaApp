package com.base.app.base.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerFragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val fragmentList: MutableList<Fragment> =
        ArrayList()
    private val fragmentTitleList: MutableList<String> =
        ArrayList()


    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        fragmentTitleList.add(fragment.javaClass.simpleName)
    }

    fun addFragment(
        fragment: Fragment,
        title: String
    ) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}