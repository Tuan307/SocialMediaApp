package com.base.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.base.app.R
import com.base.app.data.models.interest.InterestModel
import com.base.app.data.models.interest.request.AddUserInterestRequest
import com.base.app.data.models.interest.request.InterestModelRequest
import com.base.app.databinding.ActivityChooseInterestBinding
import com.base.app.ui.main.MainActivity
import com.base.app.ui.splash.adapter.InterestAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseInterestActivity : AppCompatActivity(), InterestAdapter.OnInterestItemClick {
    private lateinit var binding: ActivityChooseInterestBinding
    private lateinit var interestAdapter: InterestAdapter
    private val viewModel by viewModels<ChooseInterestViewModel>()
    val list = arrayListOf<InterestModel>()
    private var from = "start"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ChooseInterestActivity,
            R.layout.activity_choose_interest
        )
        setContentView(binding.root)
        val intent = intent
        from = intent.getStringExtra("from").toString()
        if (from == "update") {
            viewModel.getUpdateInterests()
        } else {
            viewModel.getAllInterests()
        }
        initView()
        initListener()
        observeData()
    }

    private fun initListener() = with(binding) {
        textDone.setOnClickListener {
            val checkedList = list.filter { it.isChosen }
            if (checkedList.isEmpty()) {
                Toast.makeText(
                    this@ChooseInterestActivity,
                    "Bạn phải chọn mục yêu thích",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (from == "update") {
                    viewModel.updateUserInterest(
                        AddUserInterestRequest(
                            userId = viewModel.firebaseUser?.uid.toString(),
                            checkedList.map { data ->
                                InterestModelRequest(
                                    id = data.id,
                                    name = data.name
                                )
                            }
                        )
                    )
                } else {
                    viewModel.saveUserInterest(
                        AddUserInterestRequest(
                            userId = viewModel.firebaseUser?.uid.toString(),
                            checkedList.map { data ->
                                InterestModelRequest(
                                    id = data.id,
                                    name = data.name
                                )
                            }
                        )
                    )
                }
            }
        }
    }

    private fun initView() = with(binding) {
        interestAdapter = InterestAdapter(this@ChooseInterestActivity)
        listOfInterests.apply {
            layoutManager = setUpFlexBoxLayout()
            setHasFixedSize(true)
            adapter = interestAdapter
        }
    }

    private fun observeData() = with(viewModel) {
        interestResponse.observe(this@ChooseInterestActivity) {
            list.clear()
            list.addAll(it)
            interestAdapter.submitList(list)
        }

        updateAllInterest.observe(this@ChooseInterestActivity) {
            list.clear()
            list.addAll(it.data?.map { data ->
                InterestModel(
                    id = data.id ?: 0,
                    name = data.name.toString(),
                    isChosen = data.hasChosen ?: false
                )
            }.orEmpty())
            interestAdapter.submitList(list.toList())
        }

        addInterestResponse.observe(this@ChooseInterestActivity) {
            if (it) {
                if (from == "update") {
                    finish()
                } else {
                    startActivity(Intent(this@ChooseInterestActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(
                    this@ChooseInterestActivity,
                    "Có lỗi xảy ra, vui lòng thử lại",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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