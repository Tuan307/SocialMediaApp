package com.base.app.ui.main.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.base.app.R
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.ActivityDetailHomePostBinding
import com.base.app.ui.main.fragment.home.adapter.DetailHomePostAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailHomePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHomePostBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var detailHomePostAdapter: DetailHomePostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_home_post)
        setContentView(binding.root)
        val intent = intent
        val bundle = intent.extras
        val itemPosition = bundle?.getInt("postPosition")
        val list = bundle?.getParcelableArrayList<ImagesList>("postList")
        detailHomePostAdapter = DetailHomePostAdapter(list?.toList() ?: emptyList())

        binding.viewPagerImageDetail.apply {
            adapter = detailHomePostAdapter
            offscreenPageLimit = list?.size ?: 0
            if (itemPosition != null) {
                currentItem = itemPosition
            }
        }

        binding.imageClose.setOnClickListener {
            finish()
        }
    }

}