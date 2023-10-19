package com.base.app.ui.group.your_group

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.databinding.FragmentGroupYourGroupBinding
import com.base.app.ui.group.detail_group.GroupDetailActivity
import com.base.app.ui.group.your_group.adapter.GroupYourGroupAdapter
import com.base.app.ui.group.your_group.viewdata.GroupViewData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupYourGroupFragment : Fragment(), GroupYourGroupAdapter.OnShowAllGroup,
    SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentGroupYourGroupBinding? = null
    private val binding get() = _binding!!
    private lateinit var yourGroupAdapter: GroupYourGroupAdapter
    private val viewModel by viewModels<GroupYourGroupViewModel>()
    private val dataList = arrayListOf<GroupViewData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupYourGroupBinding.inflate(inflater, container, false)
        yourGroupAdapter = GroupYourGroupAdapter(this@GroupYourGroupFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGroup(0)
        with(binding) {
            swipeRefreshGroup.setOnRefreshListener(this@GroupYourGroupFragment)
            listOfYourGroup.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = yourGroupAdapter
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        ownGroupListResponse.observe(viewLifecycleOwner) {
            dataList.clear()
            dataList.addAll(it)
            viewModel.getGroup(1)
        }
        joinedGroupListResponse.observe(viewLifecycleOwner) {
            dataList.addAll(it)
            yourGroupAdapter.submitList(dataList.toList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): GroupYourGroupFragment {
            return GroupYourGroupFragment()
        }
    }

    override fun onShowAllList(type: String) {
        val intent = Intent(requireActivity(), GroupAllYourGroupActivity::class.java)
        if (type == "Nhóm của bạn") {
            intent.putExtra("type", 0)
        } else {
            intent.putExtra("type", 1)
        }
        startActivity(intent)
    }

    override fun onShowDetailGroup(groupId: Long,groupName:String) {
        val intent = Intent(requireActivity(), GroupDetailActivity::class.java)
        intent.putExtra("groupId", groupId)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
    }

    override fun onRefresh() {
        dataList.clear()
        viewModel.getGroup(0)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeRefreshGroup.isRefreshing = false
        }, 1500)
    }
}