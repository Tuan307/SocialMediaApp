package com.base.app.ui.group.your_group

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.databinding.ActivityGroupAllYourGroupBinding
import com.base.app.ui.group.your_group.adapter.GroupAllYourGroupAdapter
import com.base.app.ui.group.your_group.viewdata.GroupBodyViewData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class GroupAllYourGroupActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var allYourGroupAdapter: GroupAllYourGroupAdapter
    private var type = 0
    private val viewModel by viewModels<GroupAllYourGroupViewModel>()
    private val dataList: ArrayList<GroupBodyViewData> = arrayListOf()
    private lateinit var binding: ActivityGroupAllYourGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_all_your_group)
        setContentView(binding.root)
        val intent = intent
        type = intent.getIntExtra("type", 0)
        viewModel.getGroup(type, 1)
        allYourGroupAdapter = GroupAllYourGroupAdapter()
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            if (type == 0) {
                textTitle.text = "Nhóm của bạn"
            } else {
                textTitle.text = "Nhóm bạn đã tham gia"
            }
            swipeRefreshAllGroup.setOnRefreshListener(this@GroupAllYourGroupActivity)
            listOfGroup.apply {
                layoutManager = LinearLayoutManager(this@GroupAllYourGroupActivity)
                adapter = allYourGroupAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listOfGroup.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getGroup(type, page)
                    }
                }
            }
            listOfGroup.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        ownGroupListResponse.observe(this@GroupAllYourGroupActivity) {
            dataList.clear()
            dataList.addAll(it)
            allYourGroupAdapter.submitList(null)
            allYourGroupAdapter.submitList(dataList.toList())
        }
        ownGroupListLoadMoreResponse.observe(this@GroupAllYourGroupActivity) {
            dataList.addAll(it)
            allYourGroupAdapter.submitList(dataList.toList())
        }
        joinedGroupListResponse.observe(this@GroupAllYourGroupActivity) {
            dataList.clear()
            dataList.addAll(it)
            allYourGroupAdapter.submitList(null)
            allYourGroupAdapter.submitList(dataList.toList())
        }
        joinedGroupListLoadMoreResponse.observe(this@GroupAllYourGroupActivity) {
            dataList.addAll(it)
            allYourGroupAdapter.submitList(dataList.toList())
        }
    }

    override fun onRefresh() {
        dataList.clear()
        viewModel.getGroup(type, 1)
        endlessRecyclerViewScrollListener.resetState()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.swipeRefreshAllGroup.isRefreshing = false
            }, 1500
        )
    }
}