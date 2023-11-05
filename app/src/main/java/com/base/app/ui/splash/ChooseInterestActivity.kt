package com.base.app.ui.splash

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.base.app.R
import com.base.app.data.models.interest.InterestModel
import com.base.app.databinding.ActivityChooseInterestBinding
import com.base.app.ui.splash.adapter.InterestAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseInterestActivity : AppCompatActivity(), InterestAdapter.OnInterestItemClick {
    private lateinit var binding: ActivityChooseInterestBinding
    private lateinit var interestAdapter: InterestAdapter
    private val viewModel by viewModels<WelcomeViewModel>()
    val list = arrayListOf<InterestModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ChooseInterestActivity,
            R.layout.activity_choose_interest
        )
        setContentView(binding.root)
        initView()
        initListener()
        observeData()
    }

    private fun initListener() = with(binding) {
        textDone.setOnClickListener {
        }
    }

    private fun initView() = with(binding) {
        interestAdapter = InterestAdapter(this@ChooseInterestActivity)
        listOfInterests.apply {
            layoutManager = setUpFlexBoxLayout()
            setHasFixedSize(true)
            adapter = interestAdapter
        }
        interestAdapter.submitList(list)
    }

    private fun observeData() = with(viewModel) {

    }

    private fun setUpFlexBoxLayout(): FlexboxLayoutManager {
        val layoutManager = FlexboxLayoutManager(this@ChooseInterestActivity)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        return layoutManager
    }

    override fun onInterestClick(data: InterestModel, position: Int) {
        list[position].isChosen = !list[position].isChosen
        interestAdapter.submitList(list.toList())
        interestAdapter.notifyDataSetChanged()
    }
}