package com.base.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.ActivityAllExploreBinding
import com.base.app.ui.main.fragment.explore.adapter.ItemExploreAdapter
import com.base.app.ui.main.fragment.explore.viewdata.ExploreItemViewData
import com.base.app.ui.main.fragment.explore.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllExploreActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    ItemExploreAdapter.OnItemExploreClick {
    private lateinit var binding: ActivityAllExploreBinding
    private lateinit var userAdapter: ItemExploreAdapter
    private val viewModel by viewModels<ExploreViewModel>()
    private val userList: ArrayList<ExploreItemViewData> = arrayListOf()
    private var type: String = ""
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private lateinit var saveShare: AppPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@AllExploreActivity, R.layout.activity_all_explore)
        setContentView(binding.root)
        saveShare = AppPreferencesHelper(this@AllExploreActivity)
        val intent = intent
        type = intent.getStringExtra("type").toString()
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            swipeExploreAll.setOnRefreshListener(this@AllExploreActivity)
            if (type == "near_by") {
                lat = saveShare.getString("lat").toDouble()
                lng = saveShare.getString("lng").toDouble()
                viewModel.getAllNearByUsers(lat, lng, 100)
                userAdapter = ItemExploreAdapter(userList, this@AllExploreActivity)
                listOfGroups.apply {
                    layoutManager = GridLayoutManager(this@AllExploreActivity, 2)
                    adapter = userAdapter
                }
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        nearByUserResponse.observe(this@AllExploreActivity) {
            userList.clear()
            userList.addAll(it.map { data ->
                ExploreItemViewData(
                    id = data.userId.toString(),
                    name = data.userName.toString(),
                    image = data.imageUrl.toString(),
                    type = 0,
                    groupMember = null
                )
            })
            userAdapter.notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        viewModel.getAllNearByUsers(lat, lng, 100)
        userList.clear()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeExploreAll.isRefreshing = false
        }, 1500)
    }

    override fun onCLickFriend(data: ExploreItemViewData) {
        val intent = Intent(this@AllExploreActivity, ProfileActivity::class.java)
        intent.putExtra("userId", data.id)
        startActivity(intent)
    }

    override fun onCLickGroup(data: ExploreItemViewData) {

    }

    override fun onCLickDestination(data: ExploreItemViewData) {
    }

}