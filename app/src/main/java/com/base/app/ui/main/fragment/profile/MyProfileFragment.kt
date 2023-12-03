package com.base.app.ui.main.fragment.profile

import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.response.post.PostContent
import com.base.app.databinding.FragmentMyProfileBinding
import com.base.app.ui.add_post.PostActivity
import com.base.app.ui.add_video_post.AddVideoActivity
import com.base.app.ui.edit_profile.EditProfileActivity
import com.base.app.ui.follow.FollowerActivity
import com.base.app.ui.main.MainViewModel
import com.base.app.ui.main.fragment.profile.adapter.ProfilePostAdapter
import com.base.app.ui.profile_detail_post.PostDetailActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(), ProfilePostAdapter.iCallBack {

    private val viewModel by viewModels<ProfileViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var profileAdapter: ProfilePostAdapter
    private var postList = ArrayList<PostContent>()
    private var idKey: String = ""
    private var tabType: Int = 0

    companion object {
        fun newInstance(): MyProfileFragment {
            return MyProfileFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_my_profile
    }

    override fun onResume() {
        super.onResume()
        if (postList.isNotEmpty()) {
            postList.clear()
        }
        viewModel.getRemoteUserInformation(idKey)
        if (tabType == 0) {
            viewModel.getProfilePost(idKey, 20, 1)
        } else {
            viewModel.getSavePost(idKey, 20, 1)
        }

    }

    override fun initView() {
        viewModel.getProfilePost(idKey, 20, 1)
        binding.rcvProfile.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.rcvProfile.setHasFixedSize(true)
        profileAdapter = ProfilePostAdapter(requireContext(), postList, this)
        binding.rcvProfile.adapter = profileAdapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
            binding.rcvProfile.layoutManager as LinearLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (page > 1) {
                    if (tabType == 0) {
                        viewModel.getProfilePost(idKey, 20, page)
                    } else {
                        viewModel.getSavePost(idKey, 20, page)
                    }
                }
            }
        }
        binding.rcvProfile.addOnScrollListener(endlessRecyclerViewScrollListener)
        viewModel.getKey(true)
    }

    override fun initListener() {
        binding.imgAddImage.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.inflate(R.menu.add_item)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.postVideo -> {
                        startActivity(Intent(requireContext(), AddVideoActivity::class.java))
                    }
                    R.id.postImage -> {
                        val intent = Intent(requireContext(), PostActivity::class.java)
                        intent.putExtra("from", "home")
                        startActivity(intent)
                    }
                }
                true
            }
            popupMenu.show()
        }
//        binding.btnFollowProfile.setOnClickListener {
//            if (binding.btnFollowProfile.text.toString().lowercase() == "follow") {
//                viewModel.followUser(true, idKey)
//                viewModel.addNotifications(idKey)
//            } else {
//                viewModel.followUser(false, idKey)
//            }
//        }
        binding.imgBackProfile.setOnClickListener {
            dataManager.remove("id")
            mainViewModel.setSomething(false)
            mainViewModel.setCurrentIndex(1)
            //viewModel.key.postValue(viewModel.firebaseUser?.uid.toString())
        }
        binding.txtFollower.setOnClickListener {
            val intent = Intent(context, FollowerActivity::class.java)
            intent.putExtra("id", idKey)
            intent.putExtra("title", "followers")
            startActivity(intent)
        }
        binding.txtFollowing.setOnClickListener {
            val intent = Intent(context, FollowerActivity::class.java)
            intent.putExtra("id", idKey)
            intent.putExtra("title", "following")
            startActivity(intent)
        }
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        binding.tabLayoutProfile.getTabAt(0)?.setIcon(R.drawable.ic_grid)
        binding.tabLayoutProfile.getTabAt(1)?.setIcon(R.drawable.ic_bookmark_border)

        binding.tabLayoutProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        tabType = 0
                        endlessRecyclerViewScrollListener.resetState()
                        viewModel.getProfilePost(idKey, 50, 1)
                    }
                    1 -> {
                        tabType = 1
                        if (postList.isNotEmpty()) {
                            postList.clear()
                        }
                        endlessRecyclerViewScrollListener.resetState()
                        viewModel.getSavePost(idKey, 50, 1)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun observerLiveData() {
        with(mainViewModel) {
            something.observe(this@MyProfileFragment) {
                viewModel.getKey(it)
            }
        }
        with(viewModel) {
            statusId.observe(this@MyProfileFragment) {
                if (!it) {
                    binding.imgBackProfile.visibility = View.VISIBLE
                    binding.btnEditProfile.visibility = View.INVISIBLE
                    binding.btnFollowProfile.visibility = View.VISIBLE
                } else {
                    binding.btnFollowProfile.visibility = View.GONE
                    binding.imgBackProfile.visibility = View.GONE
                    binding.btnEditProfile.visibility = View.VISIBLE
                }

            }
            getUserRemoteResponse.observe(this@MyProfileFragment) {
                if (it != null) {
                    binding.apply {
                        txtUserName.text = it.userName
                        txtBio.text = it.bio
                        txtFullName.text = it.fullName
                        Glide.with(requireContext()).load(it.imageUrl).into(imgAvatar)
                    }
                }
            }
//            getFollowerNumber.observe(this@MyProfileFragment) {
//                binding.txtFollowerNumber.text = it.toString()
//            }
//            getFollowingNumber.observe(this@MyProfileFragment) {
//                binding.txtFollowingNumber.text = it.toString()
//            }
            responseMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToast(requireContext(), it)
                }
            }
            getProfilePost.observe(this@MyProfileFragment) {
                postList.clear()
                postList.addAll(it)
                binding.txtPostNumber.text = postList.size.toString()
                profileAdapter.notifyDataSetChanged()
            }
            getMoreProfilePost.observe(this@MyProfileFragment) {
                postList.addAll(it)
                binding.txtPostNumber.text = postList.size.toString()
                profileAdapter.notifyDataSetChanged()
            }
            getSavedPostResponse.observe(this@MyProfileFragment) {
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
            getMoreSavedPostResponse.observe(this@MyProfileFragment) {
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
//            followResponse.observe(this@MyProfileFragment) {
//                if (it) {
//                    binding.btnFollowProfile.text = resources.getString(R.string.un_follow)
//                } else {
//                    binding.btnFollowProfile.text = resources.getString(R.string.follow)
//                }
//            }
//            getKey.observe(this@MyProfileFragment) {
//                idKey = it ?: viewModel.firebaseUser?.uid.toString()
//                viewModel.isFollowing(idKey)
//                viewModel.setId(idKey)
//                viewModel.getRemoteUserInformation(idKey)
//                viewModel.getFollowing(idKey)
//                viewModel.getFollower(idKey)
//                if (tabType == 0) {
//                    viewModel.getProfilePost(idKey, 20, 1)
//                } else {
//                    viewModel.getSavePost(idKey, 20, 1)
//                }
//            }
        }
    }

    override fun onCLick(position: Int) {
        val intent = Intent(requireContext(), PostDetailActivity::class.java)
        intent.putExtra("idKey", idKey)
        intent.putExtra("imagePosition", position)
        startActivity(intent)

    }

}