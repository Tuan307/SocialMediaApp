package com.base.app.ui.story.content_story

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.base.app.R
import com.base.app.data.models.story.StoryContentModel
import com.base.app.databinding.ActivityStoryContentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryContentBinding
    private val viewModel by viewModels<StoryContentViewModel>()
    private lateinit var storyContentAdapter: StoryContentAdapter
    private val listOfStoryContent = arrayListOf<StoryContentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@StoryContentActivity,
            R.layout.activity_story_content
        )
        setContentView(binding.root)
        val intent = intent
        viewModel.fetchStoryContent(intent.getIntExtra("id", 0))
        storyContentAdapter = StoryContentAdapter(::handleAddStoryContent)
        with(binding) {
            viewPagerImageDetail.apply {
                adapter = storyContentAdapter
            }
//            imageDownload.setOnClickListener {
//                Log.d(
//                    "CheckListItem",
//                    list?.get(binding.viewPagerImageDetail.currentItem)?.imageUrl.toString()
//                );
//                this@DetailHomePostActivity.downloadImageByUri(
//                    "ảnh được tải từ TravelGo",
//                    list?.get(binding.viewPagerImageDetail.currentItem)?.imageUrl.toString()
//                )
//            }
            viewPagerImageDetail.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    if (position == listOfStoryContent.size - 1) {
                        imageMore.visibility = View.GONE
                    } else {
                        imageMore.visibility = View.VISIBLE
                    }
                }
            })
            imageClose.setOnClickListener {
                finish()
            }
        }
        with(viewModel) {
            storyContentResponse.observe(this@StoryContentActivity) {
                if (it.isNotEmpty()) {
                    binding.viewPagerImageDetail.offscreenPageLimit = it.size
                }
                listOfStoryContent.clear()
                listOfStoryContent.addAll(it)
                listOfStoryContent.add(StoryContentModel(-1, "", null))
                storyContentAdapter.submitList(listOfStoryContent.toList())
            }
        }
    }

    private fun handleAddStoryContent(id: Int) {
        if (id != -1) {

        } else {
            Toast.makeText(this@StoryContentActivity, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}