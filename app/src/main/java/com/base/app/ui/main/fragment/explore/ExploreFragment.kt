package com.base.app.ui.main.fragment.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.FragmentExploreBinding
import com.base.app.ui.main.fragment.explore.adapter.ExploreAdapter
import com.base.app.ui.main.fragment.explore.viewdata.ExploreItemViewData
import com.base.app.ui.main.fragment.explore.viewdata.ExploreViewData
import com.base.app.ui.main.fragment.explore.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    val binding: FragmentExploreBinding
        get() = _binding!!
    private val exploreList: ArrayList<ExploreViewData> = arrayListOf()
    private val viewModel by viewModels<ExploreViewModel>()
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
        val lat = saveShare.getString("lat").toDouble()
        val lng = saveShare.getString("lng").toDouble()
        viewModel.getAllNearByUsers(lat, lng, 6)
        with(binding) {
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
                            id = data.userId,
                            name = data.userName,
                            image = data.imageUrl,
                            type = 0,
                            groupMember = null
                        )
                    }
                )
            )
            exploreAdapter.submitList(exploreList.toList())
        }
    }

    companion object {
        fun newInstance() = ExploreFragment()
    }
}