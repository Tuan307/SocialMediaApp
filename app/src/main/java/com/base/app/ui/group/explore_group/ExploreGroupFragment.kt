package com.base.app.ui.group.explore_group

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.databinding.FragmentExploreGroupBinding
import com.base.app.ui.group.explore_group.adapter.ExploreGroupAdapter
import com.base.app.ui.group.explore_group.viewdata.GroupDataViewData
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ExploreGroupFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    ExploreGroupAdapter.OnExploreGroupClick {
    private var _binding: FragmentExploreGroupBinding? = null
    val binding: FragmentExploreGroupBinding
        get() = _binding!!
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var exploreGroupAdapter: ExploreGroupAdapter
    private val listOfGroups: ArrayList<GroupDataViewData> = arrayListOf()
    private val viewModel by viewModels<ExploreGroupViewModel>()
    private val chooseGroup: GroupDataViewData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreGroupBinding.inflate(inflater, container, false)
        exploreGroupAdapter = ExploreGroupAdapter(viewModel, this@ExploreGroupFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllGroups(20, 1)
        with(binding) {
            binding.swipeExploreGroup.setOnRefreshListener(this@ExploreGroupFragment)
            listOfGroups.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = exploreGroupAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listOfGroups.layoutManager as GridLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getAllGroups(20, page)
                    }
                }
            }
            listOfGroups.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        getAllGroupsResponse.observe(viewLifecycleOwner) {
            listOfGroups.clear()
            listOfGroups.addAll(it)
            exploreGroupAdapter.submitList(it.toList())
        }
        joinGroupsResponse.observe(viewLifecycleOwner) {
            if (it.data == null) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${it.status?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var index = 0
                for (i in 0 until listOfGroups.size) {
                    if (listOfGroups[i].id == it.data.groupModelId.id) {
                        index = i
                        break
                    }
                }
                listOfGroups[index].hasJoined = true
                exploreGroupAdapter.submitList(listOfGroups.toList())
                exploreGroupAdapter.notifyDataSetChanged()
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.str_success),
                Toast.LENGTH_SHORT
            ).show()
        }
        requestJoinGroupsResponse.observe(viewLifecycleOwner) {
            if (it.data == null) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${it.status.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var index = 0
                for (i in 0 until listOfGroups.size) {
                    if (listOfGroups[i].id == it.data.groupId.id) {
                        index = i
                        break
                    }
                }
                listOfGroups[index].hasJoined = true
                exploreGroupAdapter.submitList(listOfGroups.toList())
                exploreGroupAdapter.notifyDataSetChanged()
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.str_success),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ExploreGroupFragment()
    }

    override fun onRefresh() {
        viewModel.getAllGroups(20, 1)
        listOfGroups.clear()
        endlessRecyclerViewScrollListener.resetState()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeExploreGroup.isRefreshing = false
        }, 1500)
    }

    override fun onJoinRequest(data: GroupDataViewData) {
        if (data.groupPrivacy == "private") {
            viewModel.requestJoinGroup(
                CreateGroupInvitationRequest(
                    data.id,
                    Calendar.getInstance().time.time.toString(),
                    "Đã gửi yêu cầu tham gia nhóm",
                    viewModel.firebaseUser?.uid.toString(),
                    "request",
                    ""
                )
            )
        } else {
            viewModel.joinGroup(data.id)
        }
    }
}