package com.base.app.ui.group.explore_group

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.databinding.FragmentExploreGroupBinding
import com.base.app.ui.group.detail_group.GroupDetailActivity
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
        with(binding) {
            emptyView.textEmpty.text = resources.getString(R.string.text_empty_view_group)
            edtSearch.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    viewModel.searchGroup(text.toString(), 20, 1)
                }
            }
            binding.swipeExploreGroup.setOnRefreshListener(this@ExploreGroupFragment)
            listOfGroups.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = exploreGroupAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listOfGroups.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.searchGroup(edtSearch.text.toString(), 20, page)
                    }
                }
            }
            listOfGroups.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        searchAllGroupsResponse.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                exploreGroupAdapter.submitList(null)
                binding.emptyView.linearEmptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.linearEmptyView.visibility = View.GONE
                listOfGroups.clear()
                listOfGroups.addAll(it)
                exploreGroupAdapter.submitList(listOfGroups.toList())
            }
        }
        searchMoreAllGroupsResponse.observe(viewLifecycleOwner) {
            listOfGroups.addAll(it)
            exploreGroupAdapter.submitList(listOfGroups.toList())
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

                Toast.makeText(
                    requireContext(),
                    getString(R.string.str_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        removeRequestGroupsResponse.observe(viewLifecycleOwner) {
            if (it.data == true) {
                onReload()
            }
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

                Toast.makeText(
                    requireContext(),
                    getString(R.string.str_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
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
        onReload()
    }

    private fun onReload() {
        listOfGroups.clear()
        exploreGroupAdapter.submitList(listOfGroups.toList())
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
                    data.groupOwner?.userId.toString(),
                    "request",
                    viewModel.firebaseUser?.uid.toString(),
                )
            )
        } else {
            viewModel.joinGroup(data.id)
        }
    }

    override fun onRemoveRequest(data: GroupDataViewData) {
        viewModel.removeGroupRequest(data.id)
    }

    override fun onClickDetail(data: GroupDataViewData) {
        val intent = Intent(requireActivity(), GroupDetailActivity::class.java)
        intent.putExtra("groupId", data.id)
        intent.putExtra("groupName", data.groupName)
        startActivity(intent)
    }
}