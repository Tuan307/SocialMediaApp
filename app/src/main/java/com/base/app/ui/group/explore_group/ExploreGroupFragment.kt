package com.base.app.ui.group.explore_group

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.databinding.FragmentExploreGroupBinding
import com.base.app.ui.group.explore_group.adapter.ExploreGroupAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreGroupFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentExploreGroupBinding? = null
    val binding: FragmentExploreGroupBinding
        get() = _binding!!
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var exploreGroupAdapter: ExploreGroupAdapter
    private val viewModel by viewModels<ExploreGroupViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreGroupBinding.inflate(inflater, container, false)
        exploreGroupAdapter = ExploreGroupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllGroups(10, 1)
        with(binding) {
            listOfGroups.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = exploreGroupAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listOfGroups.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getAllGroups(10, page)
                    }
                }
            }
            listOfGroups.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        getAllGroupsResponse.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                exploreGroupAdapter.submitList(it.toList())
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
        viewModel.getAllGroups(10, 1)
        endlessRecyclerViewScrollListener.resetState()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeExploreGroup.isRefreshing = false
        }, 1500)
    }
}