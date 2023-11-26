package com.base.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.response.post.PostContent
import com.base.app.databinding.ActivityProfileBinding
import com.base.app.ui.edit_profile.EditProfileActivity
import com.base.app.ui.follow.FollowerActivity
import com.base.app.ui.main.fragment.profile.ProfileViewModel
import com.base.app.ui.main.fragment.profile.adapter.ProfilePostAdapter
import com.base.app.ui.profile_detail_post.PostDetailActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), ProfilePostAdapter.iCallBack {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private var userId = ""
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var profileAdapter: ProfilePostAdapter
    private var postList = ArrayList<PostContent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ProfileActivity, R.layout.activity_profile)
        setContentView(binding.root)
        val intent = intent
        userId = intent.getStringExtra("userId").toString()
        viewModel.getRemoteUserInformation(userId)
        viewModel.getProfilePost(userId, 20, 1)
        viewModel.isFollowing(userId)
        viewModel.getFollowing(userId)
        viewModel.getFollower(userId)
        with(binding) {
            imgBackProfile.setOnClickListener {
                finish()
            }
            rcvProfile.layoutManager =
                GridLayoutManager(this@ProfileActivity, 3)
            rcvProfile.setHasFixedSize(true)
            profileAdapter =
                ProfilePostAdapter(this@ProfileActivity, postList, this@ProfileActivity)
            binding.rcvProfile.adapter = profileAdapter
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                binding.rcvProfile.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getProfilePost(userId, 20, page)
                    }
                }
            }
            binding.rcvProfile.addOnScrollListener(endlessRecyclerViewScrollListener)
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
                if (binding.btnFollowProfile.text.toString().lowercase() == "follow") {
                    viewModel.followUser(true, userId)
                    viewModel.addNotifications(userId)
                } else {
                    viewModel.followUser(false, userId)
                }
            }
        }
        observeData()
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
        followResponse.observe(this@ProfileActivity) {
            if (it) {
                binding.btnFollowProfile.text = resources.getString(R.string.un_follow)
            } else {
                binding.btnFollowProfile.text = resources.getString(R.string.follow)
            }
        }
        getFollowerNumber.observe(this@ProfileActivity) {
            binding.txtFollowerNumber.text = it.toString()
        }
        getFollowingNumber.observe(this@ProfileActivity) {
            binding.txtFollowingNumber.text = it.toString()
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
    }

    override fun onCLick(position: Int) {
        val intent = Intent(this@ProfileActivity, PostDetailActivity::class.java)
        intent.putExtra("idKey", userId)
        intent.putExtra("imagePosition", position)
        startActivity(intent)
    }
}