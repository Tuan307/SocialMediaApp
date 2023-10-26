package com.base.app.ui.group.detail_group

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.group.response.RequestModel
import com.base.app.databinding.ActivityGroupAllRequestBinding
import com.base.app.ui.group.detail_group.adapter.GroupRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupAllRequestActivity : AppCompatActivity(), GroupRequestAdapter.OnGroupRequestItem {
    private lateinit var binding: ActivityGroupAllRequestBinding
    private lateinit var requestAdapter: GroupRequestAdapter
    private val viewModel by viewModels<GroupAllRequestViewModel>()
    private var groupId = 0.toLong()
    private var agreeGroupPosition = -1
    private var declineGroupPosition = -1
    private val listOfRequest: ArrayList<RequestModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@GroupAllRequestActivity,
            R.layout.activity_group_all_request
        )
        setContentView(binding.root)
        val intent = intent
        groupId = intent.getLongExtra("groupId", 0)
        viewModel.getAllGroupRequest(groupId)
        requestAdapter = GroupRequestAdapter(this@GroupAllRequestActivity)
        with(binding) {
            setSupportActionBar(toolbarVerifyGroupRequest)
            toolbarVerifyGroupRequest.setNavigationOnClickListener {
                finish()
            }
            listOfRequest.apply {
                layoutManager = LinearLayoutManager(this@GroupAllRequestActivity)
                adapter = requestAdapter
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        requestResponse.observe(this@GroupAllRequestActivity) {
            listOfRequest.clear()
            listOfRequest.addAll(it.data.orEmpty())
            requestAdapter.submitList(listOfRequest.toList())
        }
        agreeRequestResponse.observe(this@GroupAllRequestActivity) {
            if (it.data != null) {
                listOfRequest.removeAt(agreeGroupPosition)
                val list = arrayListOf<RequestModel>()
                list.addAll(listOfRequest)
                requestAdapter.submitList(list.toList())
            }
            Toast.makeText(this@GroupAllRequestActivity, it.status?.message, Toast.LENGTH_SHORT)
                .show()
        }
        removeRequestGroupsResponse.observe(this@GroupAllRequestActivity) {
            if (it.data == true) {
                listOfRequest.removeAt(declineGroupPosition)
                val list = arrayListOf<RequestModel>()
                list.addAll(listOfRequest)
                requestAdapter.submitList(list.toList())
            }
            Toast.makeText(this@GroupAllRequestActivity, it.status?.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onAgree(data: RequestModel, position: Int) {
        agreeGroupPosition = position
        data.groupId?.id?.let {
            data.fromInvitedUserId?.userId?.let { it1 ->
                viewModel.joinGroup(
                    it,
                    it1
                )
            }
        }
    }

    override fun onReject(data: RequestModel, position: Int) {
        declineGroupPosition = position
        data.groupId?.id?.let {
            data.fromInvitedUserId?.userId?.let { it1 ->
                viewModel.removeGroupRequest(
                    it,
                    it1
                )
            }
        }
    }
}