package com.base.app.ui.story.content_story

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.story.StoryContentModel
import com.base.app.databinding.ActivityStoryContentBinding
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryContentActivity : BaseActivity<ActivityStoryContentBinding>() {
    private val viewModel by viewModels<StoryContentViewModel>()
    private lateinit var storyContentAdapter: StoryContentAdapter
    private val listOfStoryContent = arrayListOf<StoryContentModel>()
    private var imageUri: ArrayList<Uri> = arrayListOf()
    private var imagePath: ArrayList<String> = arrayListOf()
    private val showImageList = ArrayList<Uri>()
    private var storyFolderId = 0
    private val imagePickerLauncher = registerImagePicker {
        imagesPicker.clear()
        imagesPicker.addAll(it)
        handleImagePicker(imagesPicker)
    }

    private fun handleImagePicker(data: List<Image>) {
        if (data.isEmpty()) {
            showToast(this@StoryContentActivity, "Ban chua chon anh nao")
        } else {
            imageUri.clear()
            imagePath.clear()
            showImageList.clear()
            for (i in data.indices) {
                showImageList.add(data[i].uri)
                imageUri.add(data[i].uri)
                imagePath.add(getFileExtension(data[i].uri).toString())
            }
            viewModel.uploadStoryContent(storyFolderId, imageUri, imagePath)
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_story_content
    }

    override fun initView() {
        val intent = intent
        storyFolderId = intent.getIntExtra("id", 0)
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
        registerObserverLoadingEvent(viewModel, this@StoryContentActivity)
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        with(viewModel) {
            addStoryContentResponse.observe(this@StoryContentActivity) {
                if (it) {
                    finish()
                }
            }
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

    private fun handleAddStoryContent() {
        Toast.makeText(this@StoryContentActivity, "Yesa", Toast.LENGTH_SHORT).show()
        imagePickerLauncher.launch(createConfig(false, 10))
    }

}