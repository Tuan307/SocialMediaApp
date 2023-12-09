package com.base.app.ui.explore_city

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.city.LocationModel
import com.base.app.databinding.ActivityExploreCityBinding
import com.base.app.ui.explore_city.detail_city.DetailExploreActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreCityActivity : BaseActivity<ActivityExploreCityBinding>(),
    ExploreCityAdapter.OnExploreCityClick {
    private val viewModel by viewModels<ExploreCityViewModel>()
    private lateinit var cityAdapter: ExploreCityAdapter

    override fun onCityClick(data: LocationModel) {
        val intent = Intent(this@ExploreCityActivity, DetailExploreActivity::class.java)
        intent.putExtra("id", data.cityId)
        intent.putExtra("url", data.url)
        startActivity(intent)
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_explore_city
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@ExploreCityActivity)
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            viewModel.getAllLocation()
            cityAdapter = ExploreCityAdapter(this@ExploreCityActivity)
            listOfCities.apply {
                layoutManager = LinearLayoutManager(this@ExploreCityActivity)
                adapter = cityAdapter
            }
        }
    }

    override fun initListener() {
        with(binding) {
            imageSearch.setOnClickListener {
                linearSearch.visibility = View.VISIBLE
                textTitle.visibility = View.GONE
                imageSearch.visibility = View.GONE
            }
            imageClose.setOnClickListener {
                linearSearch.visibility = View.GONE
                textTitle.visibility = View.VISIBLE
                imageSearch.visibility = View.VISIBLE
            }
            edtSearch.setOnEditorActionListener { text, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchForLocation(text.text.toString())
                }
                true
            }
        }
    }

    override fun observerLiveData() {
        with(viewModel) {
            allCityResponse.observe(this@ExploreCityActivity) {
                if (it.isEmpty()) {
                    binding.linearEmptyView.visibility = View.VISIBLE
                    binding.listOfCities.visibility = View.GONE
                } else {
                    binding.linearEmptyView.visibility = View.GONE
                    binding.listOfCities.visibility = View.VISIBLE
                    cityAdapter.submitList(it.toList())
                }
            }
            searchCityResponse.observe(this@ExploreCityActivity) {
                if (it.isEmpty()) {
                    binding.linearEmptyView.visibility = View.VISIBLE
                    binding.listOfCities.visibility = View.GONE
                } else {
                    binding.linearEmptyView.visibility = View.GONE
                    binding.listOfCities.visibility = View.VISIBLE
                    cityAdapter.submitList(it.toList())
                }
            }
        }
    }

}