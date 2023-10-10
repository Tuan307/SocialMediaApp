package com.base.app.ui.group

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.fragment.PagerFragmentAdapter
import com.base.app.data.models.group.ScreenTitle
import com.base.app.databinding.ActivityGroupBinding
import com.base.app.ui.group.for_you.GroupForYouFragment
import com.base.app.ui.group.your_group.GroupYourGroupFragment

class GroupActivity : AppCompatActivity(), GroupScreenListAdapter.OnGroupScreenTitleClick {
    private lateinit var binding: ActivityGroupBinding
    private var screenList = arrayListOf<ScreenTitle>()
    private lateinit var screenAdapter: GroupScreenListAdapter
    private lateinit var mPagerAdapter: PagerFragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group)
        setContentView(binding.root)

        screenList.add(ScreenTitle(getString(R.string.text_group_for_you), true))
        screenList.add(ScreenTitle(getString(R.string.text_group_your), false))
        screenList.add(ScreenTitle("Khám phá", false))
        screenAdapter = GroupScreenListAdapter(this@GroupActivity)
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            listOfScreens.apply {
                layoutManager =
                    LinearLayoutManager(this@GroupActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = screenAdapter
            }
            screenAdapter.submitList(screenList.toList())
        }
        initViewPagerFragment()
    }

    private fun initViewPagerFragment() = with(binding) {
        mPagerAdapter = PagerFragmentAdapter(supportFragmentManager)
        //add all fragment in main
        mPagerAdapter.addFragment(GroupForYouFragment.newInstance())
        mPagerAdapter.addFragment(GroupYourGroupFragment.newInstance())
        groupFragmentContainer.adapter = mPagerAdapter
        groupFragmentContainer.offscreenPageLimit = mPagerAdapter.count
        groupFragmentContainer.currentItem = 0
    }

    override fun onClick(title: String) {
        val list = screenList.mapIndexed { index, data ->
            if (title == data.title) {
                ScreenTitle(
                    title = data.title,
                    isCheck = !data.isCheck
                )
            } else {
                ScreenTitle(
                    title = data.title,
                    isCheck = false
                )
            }
        }
        screenList.clear()
        screenList.addAll(list)
        screenAdapter.submitList(list.toList())
        when (title) {
            getString(R.string.text_group_for_you) -> {
                binding.groupFragmentContainer.currentItem = 0
            }
            getString(R.string.text_group_your) -> {
                binding.groupFragmentContainer.currentItem = 1
            }
            "Khám phá" -> {
                Toast.makeText(this@GroupActivity, "3", Toast.LENGTH_SHORT).show()
            }
        }
    }
}