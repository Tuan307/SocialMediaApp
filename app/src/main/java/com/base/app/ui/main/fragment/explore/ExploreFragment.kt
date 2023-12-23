package com.base.app.ui.main.fragment.explore

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.FragmentExploreBinding
import com.base.app.ui.group.explore_group.ExploreGroupViewModel
import com.base.app.ui.main.fragment.explore.adapter.ExploreAdapter
import com.base.app.ui.main.fragment.explore.viewdata.ExploreItemViewData
import com.base.app.ui.main.fragment.explore.viewdata.ExploreViewData
import com.base.app.ui.main.fragment.explore.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentExploreBinding? = null
    val binding: FragmentExploreBinding
        get() = _binding!!
    private val exploreList: ArrayList<ExploreViewData> = arrayListOf()
    private val viewModel by viewModels<ExploreViewModel>()
    private val groupViewModel by viewModels<ExploreGroupViewModel>()
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var saveShare: AppPreferencesHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        exploreAdapter = ExploreAdapter()
        saveShare = AppPreferencesHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (saveShare.getString("lat") != null && saveShare.getString("lng") != null) {
            val lat = saveShare.getString("lat").toDouble()
            val lng = saveShare.getString("lng").toDouble()
            viewModel.getAllNearByUsers(lat, lng, 6)
        }
        with(binding) {
            exploreRefresh.setOnRefreshListener(this@ExploreFragment)
            listOfExplore.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = exploreAdapter
            }
        }
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() = with(viewModel) {
        nearByUserResponse.observe(viewLifecycleOwner) {
            exploreList.clear()
            exploreList.add(
                ExploreViewData(
                    "near_by",
                    "Người dùng gần bạn",
                    it.map { data ->
                        ExploreItemViewData(
                            id = data.userId.toString(),
                            name = data.userName.toString(),
                            image = data.imageUrl.toString(),
                            type = 0,
                            userName = data.userName.toString(),
                            groupMember = null
                        )
                    }
                )
            )
            exploreAdapter.submitList(exploreList.toList())
            groupViewModel.getAllGroups(20, 1)
        }
        groupViewModel.getAllGroupsResponse.observe(viewLifecycleOwner) {
            val list = arrayListOf<ExploreItemViewData>()
            for (i in it.indices) {
                if (i == 5) {
                    break
                }
                list.add(
                    ExploreItemViewData(
                        id = it[i].id.toString(),
                        name = it[i].groupName,
                        image = it[i].groupImageUrl,
                        type = 1,
                        userName = "",
                        groupMember = null
                    )
                )
            }
            exploreList.add(
                ExploreViewData(
                    "group",
                    "Nhóm du lịch",
                    list.toList()
                )
            )
            exploreAdapter.submitList(exploreList.toList())
            viewModel.getRecommendCities()
        }
        recommendCityResponse.observe(viewLifecycleOwner) {
            val list = it.map { data ->
                ExploreItemViewData(
                    id = data.city_id.toString(),
                    name = data.city_name.toString(),
                    image = data.image_url?.get(0)?.imageUrl ?: "",
                    type = 2,
                    userName = "",
                    groupMember = null
                )
            }
            exploreList.add(
                ExploreViewData(
                    "city",
                    "Địa điểm du lịch",
                    list.toList()
                )
            )
            exploreAdapter.submitList(exploreList.toList())
        }
    }

    private fun loadRefreshData() {
        val lat = saveShare.getString("lat").toDouble()
        val lng = saveShare.getString("lng").toDouble()
        viewModel.getAllNearByUsers(lat, lng, 6)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.exploreRefresh.isRefreshing = false
        }, 1500)
    }

    override fun onRefresh() {
        loadRefreshData()
    }

    companion object {
        fun newInstance() = ExploreFragment()
    }
}