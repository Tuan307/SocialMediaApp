package com.base.app.ui.chat

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.EMPTY_STRING
import com.base.app.data.models.NotificationData
import com.base.app.data.models.PushNotification
import com.base.app.databinding.ActivityDetailChatBinding
import com.base.app.ui.video_call.MainActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailChatActivity : BaseActivity<ActivityDetailChatBinding>() {
    private var chatId: String = ""
    private var chatName: String = ""
    private var url: String = ""
    private var idToken = ""
    private val viewModel by viewModels<ChatViewModel>()
    private lateinit var adapter: DetailChatAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_detail_chat
    }

    override fun initView() {
        val intent = intent
        chatId = intent.getStringExtra("id") ?: EMPTY_STRING
        chatName = intent.getStringExtra("name") ?: EMPTY_STRING
        url = intent.getStringExtra("url") ?: EMPTY_STRING
        viewModel.getPrivateChat(chatId, viewModel.firebaseUser?.uid.toString())
        viewModel.getReceiverToken(chatId)
        binding.apply {
            txtUserName.text = chatName
            Glide.with(this@DetailChatActivity).load(url).into(imgAvatar)
            rcvDetailChat.layoutManager = LinearLayoutManager(this@DetailChatActivity)
            rcvDetailChat.setHasFixedSize(true)
            adapter = DetailChatAdapter(viewModel.firebaseUser?.uid.toString())
            rcvDetailChat.adapter = adapter
        }
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
            imgSend.setOnClickListener {
                val message = edtInputText.text.toString()
                if (message.isNotEmpty()) {
                    viewModel.sendMessage(message, chatId)
                } else {
                    showToast(
                        this@DetailChatActivity,
                        resources.getString(R.string.strPleaseInputMessage)
                    )
                }
                if (idToken != "") {
                    val notification = PushNotification(
                        NotificationData("Message", message, EMPTY_STRING),
                        idToken
                    )
                    viewModel.sendNotification(notification)
                }
                edtInputText.text.clear()
            }
            edtInputText.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    imgSend.visibility = View.VISIBLE
                } else {
                    imgSend.visibility = View.GONE
                }
            }
            imgVideoCall.setOnClickListener {
                startActivity(Intent(this@DetailChatActivity, MainActivity::class.java))
            }
        }
    }

    override fun observerLiveData() {
        viewModel.sendChatResponse.observe(this@DetailChatActivity) {
            if (!it) {
                showToast(
                    this@DetailChatActivity,
                    resources.getString(R.string.str_error)
                )
            }
        }
        viewModel.chatListResponse.observe(this@DetailChatActivity) {
            adapter.submitList(it.toList())
            if (it.size > 0) {
                binding.rcvDetailChat.smoothScrollToPosition(it.size - 1)
            }
        }
        viewModel.tokenResponse.observe(this@DetailChatActivity) {
            idToken = it
        }
    }

}