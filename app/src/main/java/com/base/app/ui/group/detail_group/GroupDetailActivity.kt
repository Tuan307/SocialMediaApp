package com.base.app.ui.group.detail_group

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.databinding.ActivityGroupDetailBinding
import com.base.app.ui.group.detail_group.adapter.DetailGroupAdapter
import com.base.app.ui.group.detail_group.viewdata.DetailGroupViewData

class GroupDetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityGroupDetailBinding
    private lateinit var detailAdapter: DetailGroupAdapter
    private val viewModel by viewModels<GroupDetailViewModel>()
    private val list: ArrayList<DetailGroupViewData> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_detail)
        setContentView(binding.root)
        detailAdapter = DetailGroupAdapter()
        viewModel.getGroupInformation()
        with(binding) {
            swipeDetailGroup.setOnRefreshListener(this@GroupDetailActivity)
            imageBack.setOnClickListener {
                finish()
            }
            listOfGroupPost.apply {
                layoutManager = LinearLayoutManager(this@GroupDetailActivity)
                adapter = detailAdapter
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        listInformationResponse.observe(this@GroupDetailActivity) {
            list.clear()
            list.addAll(it)
            detailAdapter.submitList(it.toList())
            viewModel.getGroupPost()
        }
        listGroupPostResponse.observe(this@GroupDetailActivity) {
            list.addAll(it)
            detailAdapter.submitList(it.toList())
        }
    }

    override fun onRefresh() {
        viewModel.getGroupInformation()
        //endlessRecyclerViewScrollListener.resetState()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeDetailGroup.isRefreshing = false
        }, 1200)
    }
}