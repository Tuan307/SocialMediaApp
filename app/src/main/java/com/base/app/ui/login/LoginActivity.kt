package com.base.app.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityLoginBinding
import com.base.app.ui.main.MainActivity
import com.base.app.ui.register.RegisterActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()
    override fun getContentLayout(): Int = R.layout.activity_login

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@LoginActivity)
        binding.tvRegister.text =
            HtmlCompat.fromHtml(
                resources.getString(R.string.str_start_register),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        binding.edtInputAccount.setInfoEdt(R.drawable.ic_user)
        binding.edtInputPassword.setInfoEdt(R.drawable.ic_password)
        binding.edtInputAccount.setText("tuanprokt44@gmail.com")
        binding.edtInputPassword.setText("123456")
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            tvForgot.setOnClickListener {
                viewModel.forgetPassword(edtInputAccount.getText().toString())
            }
            btnLogin.setOnClickListener {
                if (edtInputAccount.getText()?.trim()?.isEmpty() == true) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.str_please_enter_your_email),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (edtInputPassword.getText()?.trim()?.isEmpty() == true) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.str_please_enter_password),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val account = edtInputAccount.getText().toString()
                    val password = edtInputPassword.getText().toString()
                    viewModel.login(account, password)
                }
            }
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            getLoginResponse.observe(this@LoginActivity) {
                if (it) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.str_authe),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            getSendResetPasswordResponse.observe(this@LoginActivity) {
                if (it) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.str_done),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.str_no_email_client_installed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}