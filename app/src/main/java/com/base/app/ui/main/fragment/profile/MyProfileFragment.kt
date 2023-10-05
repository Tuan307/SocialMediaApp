package com.base.app.ui.main.fragment.profile

import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.models.response.post.ImagesList
import com.base.app.data.models.response.post.PostContent
import com.base.app.databinding.FragmentMyProfileBinding
import com.base.app.ui.add_post.PostActivity
import com.base.app.ui.add_video_post.AddVideoActivity
import com.base.app.ui.edit_profile.EditProfileActivity
import com.base.app.ui.follow.FollowerActivity
import com.base.app.ui.main.MainViewModel
import com.base.app.ui.main.fragment.profile.adapter.ProfilePostAdapter
import com.base.app.ui.options.OptionActivity
import com.base.app.ui.profile_detail_post.PostDetailActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(), ProfilePostAdapter.iCallBack {

    private val viewModel by viewModels<ProfileViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var profileAdapter: ProfilePostAdapter
    private var list = ArrayList<PostContent>()
    private var idKey: String = ""

    companion object {
        fun newInstance(): MyProfileFragment {
            return MyProfileFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_my_profile
    }

    override fun initView() {
        //registerObserverLoadingEvent(viewModel,this@MyProfileFragment)
        binding.rcvProfile.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.rcvProfile.setHasFixedSize(true)
        profileAdapter = ProfilePostAdapter(requireContext(), list, this)
        binding.rcvProfile.adapter = profileAdapter
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
                        startActivity(Intent(requireContext(), PostActivity::class.java))
                    }
                }
                true
            }
            popupMenu.show()
        }
        binding.imgOptions.setOnClickListener {
            startActivity(Intent(requireContext(), OptionActivity::class.java))
        }
        binding.btnFollowProfile.setOnClickListener {
            if (binding.btnFollowProfile.text.toString().lowercase() == "follow") {
                viewModel.followUser(true, idKey)
                viewModel.addNotifications(idKey)
            } else {
                viewModel.followUser(false, idKey)
            }
        }
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
                        viewModel.getProfilePost(idKey, 50, 1)
                    }
                    1 -> {
                        if (list.isNotEmpty()) {
                            list.clear()
                        }
                        viewModel.getSavePostKey(idKey)
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
        mainViewModel.apply {
            something.observe(this@MyProfileFragment) {
                viewModel.getKey(it)
            }
        }
        viewModel.apply {
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

            getUserResponse.observe(this@MyProfileFragment) {
                if (it != null) {
                    binding.apply {
                        txtUserName.text = it.username
                        txtBio.text = it.bio
                        txtFullName.text = it.fullname
                        Glide.with(requireContext()).load(it.imageurl).into(imgAvatar)
                    }
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
            getFollowerNumber.observe(this@MyProfileFragment) {
                binding.txtFollowerNumber.text = it.toString()
            }
            getFollowingNumber.observe(this@MyProfileFragment) {
                binding.txtFollowingNumber.text = it.toString()
            }
            responseMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToast(requireContext(), it)
                }
            }
            getProfilePost.observe(this@MyProfileFragment) {
                list.clear()
                list.addAll(it)
                binding.txtPostNumber.text = list.size.toString()
                profileAdapter.notifyDataSetChanged()
            }
            getKeyList.observe(this@MyProfileFragment) {
                viewModel.getSavePost(it)
            }
            getSavePost.observe(this@MyProfileFragment) {
                if (list.isNotEmpty()) {
                    list.clear()
                }
                list.addAll(it.map { data ->
                    PostContent(
                        postId = data.postid,
                        description = data.postid,
                        imagesList = arrayListOf(ImagesList(null, data.postimage)),
                        checkInTimestamp = "123456789",
                        checkInAddress = null,
                        checkInLatitude = 20.0,
                        checkInLongitude = 20.0,
                        type = "image",
                        videoUrl = null,
                        postUserId = null
                    )
                })
                profileAdapter.notifyDataSetChanged()
            }
            followResponse.observe(this@MyProfileFragment) {
                if (it) {
                    binding.btnFollowProfile.text = resources.getString(R.string.un_follow)
                } else {
                    binding.btnFollowProfile.text = resources.getString(R.string.follow)
                }
            }
            getKey.observe(this@MyProfileFragment) {
                idKey = it ?: viewModel.firebaseUser?.uid.toString()
                viewModel.isFollowing(idKey)
                viewModel.setId(idKey)
                //viewModel.getUserInformation(idKey)
                viewModel.getRemoteUserInformation(idKey)
                viewModel.getFollowing(idKey)
                viewModel.getFollower(idKey)
                viewModel.getProfilePost(idKey, 50, 1)
            }
        }
    }

    override fun onCLick(position: Int) {
        val intent = Intent(requireContext(), PostDetailActivity::class.java)
        intent.putExtra("idKey", idKey)
        intent.putExtra("imagePosition", position)
        startActivity(intent)
    }

}