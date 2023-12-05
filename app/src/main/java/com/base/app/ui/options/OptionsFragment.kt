package com.base.app.ui.options

import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.dialogs.ConfirmDialog
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.FragmentOptionsBinding
import com.base.app.ui.profile.ProfileActivity
import com.base.app.ui.splash.WelcomeActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionsFragment : BaseFragment<FragmentOptionsBinding>(), ConfirmDialog.ConfirmCallback {
    private val viewModel by viewModels<OptionsViewModel>()
    private lateinit var optionsAdapter: OptionsAdapter
    private lateinit var saveShare: AppPreferencesHelper
    override fun getContentLayout(): Int = R.layout.fragment_options

    override fun initView() {
        saveShare = AppPreferencesHelper(requireContext())
        val optionsList = arrayListOf<OptionViewData>()
        optionsList.add(OptionViewData("Nhóm du lịch", R.drawable.icon_travel_group))
        optionsList.add(OptionViewData("Tìm bạn bè", R.drawable.icon_find_friend))
        optionsList.add(OptionViewData("Khám phá du lịch", R.drawable.ic_explore_location))
        optionsList.add(OptionViewData("Chế độ sáng/tối", R.drawable.icon_dark_mode_setting))
        optionsAdapter = OptionsAdapter(optionsList)
        viewModel.getRemoteUserInformation(viewModel.firebaseUser?.uid.toString())
        with(binding) {
            listOfShortcut.apply {
                isNestedScrollingEnabled = false
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = optionsAdapter
            }
        }
    }

    override fun initListener() {
        with(binding) {
            btnLogOut.setOnClickListener {
                showLogoutDialog()
            }
            buttonMyProfile.setOnClickListener {
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                intent.putExtra("userId", viewModel.firebaseUser?.uid.toString())
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFollow(viewModel.firebaseUser?.uid.toString(), "follow")
        viewModel.getFollow(viewModel.firebaseUser?.uid.toString(), "follower")
    }

    private fun showLogoutDialog() {
        val dialog = ConfirmDialog(
            requireContext(),
            this,
            resources.getString(R.string.str_logout_dialog),
            null,
            "Yes",
            "No"
        )
        dialog.show()
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun observerLiveData() {
        viewModel.apply {
            getFollowerResponse.observe(this@OptionsFragment) {
                binding.textNumberFollow.text = it.data?.size.toString()
            }
            getFollowingResponse.observe(this@OptionsFragment) {
                binding.textNumberFollowing.text = it.data?.size.toString()
            }
            userRemoteResponse.observe(this@OptionsFragment) {
                binding.txtUserName.text = it?.userName.toString()
                saveShare.save("user_name", it?.userName)
                saveShare.save("user_image", it?.imageUrl)
                Glide.with(requireContext()).load(it?.imageUrl).into(binding.imgAvatar)
            }
            logOutResponse.observe(viewLifecycleOwner) {
                if (it) {
                    startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                    requireActivity().finishAffinity()
                }
            }
        }
    }

    override fun negativeAction() {

    }

    override fun positiveAction() {
        viewModel.logOut()
    }

    companion object {
        fun newInstance() = OptionsFragment()
    }

}