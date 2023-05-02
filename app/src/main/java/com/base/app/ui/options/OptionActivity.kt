package com.base.app.ui.options

import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import androidx.activity.viewModels
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.base.dialogs.ConfirmDialog
import com.base.app.databinding.ActivityOptionBinding
import com.base.app.ui.chat_bot.ChatBotActivity
import com.base.app.ui.splash.WelcomeActivity
import com.bumptech.glide.Glide

class OptionActivity : BaseActivity<ActivityOptionBinding>(), ConfirmDialog.ConfirmCallback {
    private val viewModel by viewModels<OptionsViewModel>()
    override fun getContentLayout(): Int = R.layout.activity_option

    override fun initView() {
        viewModel.getInformation()
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
            txtSettings.setOnClickListener {
                startActivity(Intent(this@OptionActivity, ChatBotActivity::class.java))
            }
            btnLogOut.setOnClickListener {
                showLogoutDialog()
            }
            darkModeSwitch.setOnCheckedChangeListener { _, p1 ->
                if (p1) {
                    showToast(this@OptionActivity, "Yes")
                } else {
                    showToast(this@OptionActivity, "No")

                }
            }
        }
    }

    private fun showLogoutDialog() {
        val dialog = ConfirmDialog(
            this@OptionActivity,
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
            getUserInformation.observe(this@OptionActivity) {
                binding.txtUserName.text = it?.username.toString()
                Glide.with(this@OptionActivity).load(it?.imageurl).into(binding.imgAvatar)
            }
            logOutResponse.observe(this@OptionActivity) {
                if (it) {
                    startActivity(Intent(this@OptionActivity, WelcomeActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }

    override fun negativeAction() {

    }

    override fun positiveAction() {
        viewModel.logOut()
    }

}