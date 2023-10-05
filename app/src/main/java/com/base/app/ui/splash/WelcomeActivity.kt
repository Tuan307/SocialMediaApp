package com.base.app.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import androidx.activity.viewModels
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityWelcomeBinding
import com.base.app.ui.login.LoginActivity
import com.base.app.ui.main.MainActivity
import com.base.app.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    private val viewModel by viewModels<WelcomeViewModel>()
    override fun getContentLayout(): Int {
        return R.layout.activity_welcome
    }

    override fun initView() {
        binding.apply {
            txtStartRegister.text = Html.fromHtml(getString(R.string.str_start_register))
        }
        viewModel.checkUser()
    }

    override fun initListener() {
        //viewModel.getUserProfile()
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                finish()
            }
            txtStartRegister.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }

    override fun observerLiveData() = with(viewModel) {
        checkUserResponse.observe(this@WelcomeActivity) {
            if (it) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    finish()
                }, 1000)
            }
        }
    }

}