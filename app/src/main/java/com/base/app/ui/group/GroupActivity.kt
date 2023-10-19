package com.base.app.ui.group

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.fragment.PagerFragmentAdapter
import com.base.app.data.models.group.ScreenTitle
import com.base.app.databinding.ActivityGroupBinding
import com.base.app.ui.group.add_group.AddGroupActivity
import com.base.app.ui.group.explore_group.ExploreGroupFragment
import com.base.app.ui.group.for_you.GroupForYouFragment
import com.base.app.ui.group.your_group.GroupYourGroupFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        screenList.add(ScreenTitle(getString(R.string.text_group_explore), false))
        screenAdapter = GroupScreenListAdapter(this@GroupActivity)
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            imageAddGroup.setOnClickListener {
                startActivity(Intent(this@GroupActivity, AddGroupActivity::class.java))
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
        mPagerAdapter.addFragment(ExploreGroupFragment.newInstance())
        groupFragmentContainer.adapter = mPagerAdapter
        groupFragmentContainer.offscreenPageLimit = mPagerAdapter.count
        groupFragmentContainer.currentItem = 0
    }

    override fun onClick(title: String) {
        var position = 0
        val list = screenList.mapIndexed { index, data ->
            if (title == data.title) {
                position = index
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
        binding.listOfScreens.smoothScrollToPosition(position)
        when (title) {
            getString(R.string.text_group_for_you) -> {
                binding.groupFragmentContainer.currentItem = 0
            }
            getString(R.string.text_group_your) -> {
                binding.groupFragmentContainer.currentItem = 1
            }
            getString(R.string.text_group_explore) -> {
                binding.groupFragmentContainer.currentItem = 2
            }
        }
    }
}