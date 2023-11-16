package com.base.app.ui.options

import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.base.app.R
import com.base.app.base.dialogs.ConfirmDialog
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.recycleview_utils.GridSpacingItemDecoration
import com.base.app.databinding.FragmentOptionsBinding
import com.base.app.ui.splash.WelcomeActivity
import com.bumptech.glide.Glide

class OptionsFragment : BaseFragment<FragmentOptionsBinding>(), ConfirmDialog.ConfirmCallback {
    private val viewModel by viewModels<OptionsViewModel>()
    private lateinit var optionsAdapter: OptionsAdapter
    override fun getContentLayout(): Int = R.layout.fragment_options

    override fun initView() {
        val optionsList = arrayListOf<OptionViewData>()
        optionsList.add(OptionViewData("Nhóm du lịch", R.drawable.group_people))
        optionsList.add(OptionViewData("Tìm bạn bè", R.drawable.ic_find_user))
        optionsList.add(OptionViewData("Khám phá du lịch", R.drawable.ic_explore_location))
        optionsList.add(OptionViewData("Chế độ sáng/tối", R.drawable.icon_dark_mode_setting))
        optionsAdapter = OptionsAdapter(optionsList)
        viewModel.getInformation()
        with(binding) {
            listOfShortcut.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = optionsAdapter
                addItemDecoration(GridSpacingItemDecoration(2, 30, false, 0))
            }
        }
    }

    override fun initListener() {
        binding.apply {
            btnLogOut.setOnClickListener {
                showLogoutDialog()
            }
        }
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
            getUserInformation.observe(viewLifecycleOwner) {
                binding.txtUserName.text = it?.username.toString()
                Glide.with(requireContext()).load(it?.imageurl).into(binding.imgAvatar)
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