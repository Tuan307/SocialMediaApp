package com.base.app.ui.main.fragment.profile

import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.models.PostItem
import com.base.app.databinding.FragmentMyProfileBinding
import com.base.app.ui.edit_profile.EditProfileActivity
import com.base.app.ui.main.MainViewModel
import com.base.app.ui.main.fragment.profile.adapter.ProfilePostAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout


class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {

    private val viewModel by viewModels<ProfileViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var profileAdapter: ProfilePostAdapter
    private var list = ArrayList<PostItem>()
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
        binding.rcvProfile.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.rcvProfile.setHasFixedSize(true)
        profileAdapter = ProfilePostAdapter(requireContext(), list)
        binding.rcvProfile.adapter = profileAdapter
        viewModel.getKey(true)
    }

    override fun initListener() {
        binding.btnFollowProfile.setOnClickListener {
            if (binding.btnFollowProfile.text.toString().lowercase() == "follow") {
                viewModel.followUser(true, idKey)
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
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))

        }
        binding.tabLayoutProfile.getTabAt(0)?.setIcon(R.drawable.ic_grid)
        binding.tabLayoutProfile.getTabAt(1)?.setIcon(R.drawable.ic_bookmark_border)

        binding.tabLayoutProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        viewModel.getProfilePost(idKey)
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
                list.addAll(it)
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
                if (it != null) {
                    idKey = it
                } else {
                    idKey = viewModel.firebaseUser?.uid.toString()
                }
                viewModel.isFollowing(idKey)
                viewModel.setId(idKey)
                viewModel.getUserInformation(idKey)
                viewModel.getFollowing(idKey)
                viewModel.getFollower(idKey)
                viewModel.getProfilePost(idKey)
            }
        }
    }

}