package com.base.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.common.EventObserver
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.request.FollowUserRequest
import com.base.app.data.models.response.post.PostContent
import com.base.app.data.models.story.StoryModel
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.ActivityProfileBinding
import com.base.app.ui.edit_profile.EditProfileActivity
import com.base.app.ui.follow.FollowerActivity
import com.base.app.ui.main.fragment.profile.ProfileViewModel
import com.base.app.ui.main.fragment.profile.adapter.ProfilePostAdapter
import com.base.app.ui.main.fragment.profile.adapter.StoryPostAdapter
import com.base.app.ui.profile_detail_post.PostDetailActivity
import com.base.app.ui.story.create_story_folder.CreateStoryFolderFragment
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), ProfilePostAdapter.iCallBack {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private var userId = ""
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var profileAdapter: ProfilePostAdapter
    private var postList = ArrayList<PostContent>()
    private lateinit var storyPostAdapter: StoryPostAdapter
    private var tabType: Int = 0
    private lateinit var saveShare: AppPreferencesHelper
    private var listOfStories = arrayListOf<StoryModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ProfileActivity, R.layout.activity_profile)
        setContentView(binding.root)
        saveShare = AppPreferencesHelper(this@ProfileActivity)
        val intent = intent
        userId = intent.getStringExtra("userId").toString()
        listOfStories.add(StoryModel(0, null, null, null))
        viewModel.fetchAllStoryFolders()
        viewModel.getRemoteUserInformation(userId)
        viewModel.getProfilePost(userId, 20, 1)
        viewModel.isFollowing(FollowUserRequest(userId, viewModel.firebaseUser?.uid.toString(), ""))
        viewModel.getFollow(userId, "follow")
        viewModel.getFollow(userId, "follower")
        storyPostAdapter = StoryPostAdapter(::handleOnStoryClick)
        with(binding) {
            listOfStory.apply {
                layoutManager =
                    LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = storyPostAdapter
            }
            imgBackProfile.setOnClickListener {
                finish()
            }
            rcvProfile.layoutManager =
                GridLayoutManager(this@ProfileActivity, 3)
            rcvProfile.setHasFixedSize(true)
            profileAdapter =
                ProfilePostAdapter(this@ProfileActivity, postList, this@ProfileActivity)
            rcvProfile.adapter = profileAdapter
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                binding.rcvProfile.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        if (tabType == 0) {
                            viewModel.getProfilePost(userId, 20, page)
                        } else {
                            viewModel.getSavePost(userId, 20, page)
                        }
                    }
                }
            }
            rcvProfile.addOnScrollListener(endlessRecyclerViewScrollListener)
            btnEditProfile.setOnClickListener {
                startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
            }
            txtFollowing.setOnClickListener {
                val intent1 = Intent(this@ProfileActivity, FollowerActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("title", "following")
                startActivity(intent1)
            }
            txtFollower.setOnClickListener {
                val intent2 = Intent(this@ProfileActivity, FollowerActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("title", "followers")
                startActivity(intent2)
            }
            btnFollowProfile.setOnClickListener {
                if (btnFollowProfile.text.toString().lowercase() == "follow") {
                    viewModel.followUser(
                        FollowUserRequest(
                            userId,
                            viewModel.firebaseUser?.uid.toString(),
                            Calendar.getInstance().time.time.toString()
                        )
                    )
                    val userName = saveShare.getString("user_name") ?: ""
                    viewModel.addNotifications(userId, userName)
                } else {
                    viewModel.unfollowUser(
                        FollowUserRequest(
                            userId,
                            viewModel.firebaseUser?.uid.toString(),
                            Calendar.getInstance().time.time.toString()
                        )
                    )
                }
            }
            tabLayoutProfile.getTabAt(0)?.setIcon(R.drawable.ic_grid)
            tabLayoutProfile.getTabAt(1)?.setIcon(R.drawable.ic_bookmark_border)
            tabLayoutProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            tabType = 0
                            endlessRecyclerViewScrollListener.resetState()
                            viewModel.getProfilePost(userId, 50, 1)
                        }

                        1 -> {
                            tabType = 1
                            if (postList.isNotEmpty()) {
                                postList.clear()
                            }
                            endlessRecyclerViewScrollListener.resetState()
                            viewModel.getSavePost(userId, 50, 1)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })
        }
        observeData()
    }

    private fun handleOnStoryClick(position: Int) {
        if (position == 0) {
            val fragment = CreateStoryFolderFragment.newInstance()
            fragment.show(supportFragmentManager, "CreateStoryBottomDialog")
        }
    }

    private fun observeData() = with(viewModel) {
        getUserRemoteResponse.observe(this@ProfileActivity) {
            if (it != null) {
                if (it.userId == viewModel.firebaseUser?.uid.toString()) {
                    binding.btnEditProfile.visibility = View.VISIBLE
                    binding.btnFollowProfile.visibility = View.GONE
                } else {
                    binding.btnEditProfile.visibility = View.GONE
                    binding.btnFollowProfile.visibility = View.VISIBLE
                }
                binding.apply {
                    txtUserName.text = it.userName
                    txtBio.text = it.bio
                    txtFullName.text = it.fullName
                    Glide.with(this@ProfileActivity).load(it.imageUrl).into(imgAvatar)
                }
            }
        }
        followUserResponse.observe(this@ProfileActivity) {
            viewModel.getFollow(userId, "follow")
            viewModel.getFollow(userId, "follower")
        }
        unfollowUserResponse.observe(this@ProfileActivity) {
            viewModel.getFollow(userId, "follow")
            viewModel.getFollow(userId, "follower")
        }
        isFollowingUserResponse.observe(this@ProfileActivity) {
            if (it.data == true) {
                binding.btnFollowProfile.text = resources.getString(R.string.un_follow)
            } else {
                binding.btnFollowProfile.text = resources.getString(R.string.follow)
            }
        }
        followUserResponse.observe(this@ProfileActivity) {
            if (it.status?.code == 200.toLong()) {
                binding.btnFollowProfile.text = resources.getString(R.string.un_follow)
            }
        }
        unfollowUserResponse.observe(this@ProfileActivity) {
            if (it.data == true) {
                binding.btnFollowProfile.text = resources.getString(R.string.follow)
            } else {
                Toast.makeText(this@ProfileActivity, it.status?.message, Toast.LENGTH_SHORT).show()
            }
        }
        getFollowerResponse.observe(this@ProfileActivity) {
            binding.txtFollowerNumber.text = it.data?.size.toString()
        }
        getFollowingResponse.observe(this@ProfileActivity) {
            binding.txtFollowingNumber.text = it.data?.size.toString()
        }
        getProfilePost.observe(this@ProfileActivity) {
            postList.clear()
            postList.addAll(it)
            binding.txtPostNumber.text = postList.size.toString()
            profileAdapter.notifyDataSetChanged()
        }
        getMoreProfilePost.observe(this@ProfileActivity) {
            postList.addAll(it)
            binding.txtPostNumber.text = postList.size.toString()
            profileAdapter.notifyDataSetChanged()
        }
        getSavedPostResponse.observe(this@ProfileActivity) {
            if (postList.isNotEmpty()) {
                postList.clear()
            }
            if (it.data != null) {
                postList.addAll(it.data.map { data ->
                    PostContent(
                        postId = data.post_saved_id?.postId,
                        description = data.post_saved_id?.description,
                        imagesList = data.post_saved_id?.imagesList,
                        checkInTimestamp = data.post_saved_id?.checkInTimestamp,
                        checkInAddress = data.post_saved_id?.checkInAddress,
                        checkInLatitude = data.post_saved_id?.checkInLatitude,
                        checkInLongitude = data.post_saved_id?.checkInLongitude,
                        type = data.post_saved_id?.type,
                        videoUrl = data.post_saved_id?.videoUrl,
                        postUserId = data.post_saved_id?.postUserId,
                        question = data.post_saved_id?.question
                    )
                })
            }
            profileAdapter.notifyDataSetChanged()
        }
        getMoreSavedPostResponse.observe(this@ProfileActivity) {
            if (it.data != null) {
                postList.addAll(it.data.map { data ->
                    PostContent(
                        postId = data.post_saved_id?.postId,
                        description = data.post_saved_id?.description,
                        imagesList = data.post_saved_id?.imagesList,
                        checkInTimestamp = data.post_saved_id?.checkInTimestamp,
                        checkInAddress = data.post_saved_id?.checkInAddress,
                        checkInLatitude = data.post_saved_id?.checkInLatitude,
                        checkInLongitude = data.post_saved_id?.checkInLongitude,
                        type = data.post_saved_id?.type,
                        videoUrl = data.post_saved_id?.videoUrl,
                        postUserId = data.post_saved_id?.postUserId,
                        question = data.post_saved_id?.question
                    )
                })
            }
            profileAdapter.notifyDataSetChanged()
        }

        addStorySuccessfully.observe(this@ProfileActivity, EventObserver {
            if (it) {
                listOfStories.clear()
                listOfStories.add(StoryModel(0, null, null, null))
                viewModel.fetchAllStoryFolders()
            }
        })

        storyFolderResponse.observe(this@ProfileActivity) {
            listOfStories.addAll(it)
            storyPostAdapter.submitList(listOfStories.toList())
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d("Check1", "Yeah")
        viewModel.getRemoteUserInformation(userId)
    }

    override fun onCLick(position: Int) {
        val intent = Intent(this@ProfileActivity, PostDetailActivity::class.java)
        intent.putExtra("idKey", userId)
        intent.putExtra("imagePosition", position)
        startActivity(intent)
    }
}