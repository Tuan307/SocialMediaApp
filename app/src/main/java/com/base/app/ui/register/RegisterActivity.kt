package com.base.app.ui.register

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityRegisterBinding
import com.base.app.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    private val viewModel by viewModels<RegisterViewModel>()

    override fun getContentLayout(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@RegisterActivity)
        binding.apply {
            txtLogin.text = HtmlCompat.fromHtml(
                resources.getString(R.string.str_start_login),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            edtInputEmail.setInfoEdt(R.drawable.ic_email)
            edtInputPassword.setInfoEdt(R.drawable.ic_lock)
            edtInputName.setInfoEdt(R.drawable.ic_account)
            edtInputUserName.setInfoEdt(R.drawable.ic_account)
        }
    }

    override fun initListener() {
        binding.apply {
            txtLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            imgBack.setOnClickListener {
                finish()
            }
            btnRegister.setOnClickListener {
                if (edtInputName.getText().isNullOrEmpty()) {
                    showToast(
                        this@RegisterActivity,
                        getString(R.string.str_input_first_name)
                    )
                } else if (edtInputUserName.getText().isNullOrEmpty()) {
                    showToast(
                        this@RegisterActivity,
                        getString(R.string.str_input_account)
                    )
                } else if (edtInputEmail.getText().isNullOrEmpty()) {
                    showToast(
                        this@RegisterActivity,
                        getString(R.string.str_input_email)
                    )
                } else if (edtInputPassword.getText().isNullOrEmpty()) {
                    showToast(
                        this@RegisterActivity,
                        getString(R.string.str_input_pass)
                    )
                } else {
                    val userName = edtInputUserName.getText().toString()
                    val fullName = edtInputName.getText().toString()
                    val email = edtInputEmail.getText().toString()
                    val passWord = edtInputPassword.getText().toString()
                    viewModel.register(userName, fullName, email, passWord)
                }
            }
        }
    }

    override fun observerLiveData() {
        with(viewModel) {
            saveToDBResponse.observe(this@RegisterActivity) {
                registerUser(it)
            }
            registerUserToDBResponse.observe(this@RegisterActivity) {
                if (it.data != null) {
                    showToast(this@RegisterActivity, getString(R.string.str_success))
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    showToast(this@RegisterActivity, it.status.message)
                }
            }
            errorResponse.observe(this@RegisterActivity) {
                if (!it) {
                    showToast(this@RegisterActivity, getString(R.string.str_error))
                }
            }
        }
    }

}