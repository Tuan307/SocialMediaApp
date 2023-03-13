package com.base.app.ui.chat

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.EMPTY_STRING
import com.base.app.databinding.ActivityDetailChatBinding
import com.bumptech.glide.Glide

class DetailChatActivity : BaseActivity<ActivityDetailChatBinding>() {
    private var chatId: String = ""
    private var chatName: String = ""
    private var url: String = ""
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
        viewModel.getPrivateChat(chatId)
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
                if (!message.isNullOrEmpty()) {
                    viewModel.sendMessage(message, chatId)
                } else {
                    showToast(
                        this@DetailChatActivity,
                        resources.getString(R.string.strPleaseInputMessage)
                    )
                }
                edtInputText.text.clear()
            }
        }
    }

    override fun observerLiveData() {
        viewModel.sendChatResponse.observe(this@DetailChatActivity) {
            if (it) {
                showToast(
                    this@DetailChatActivity,
                    resources.getString(R.string.str_success)
                )
            } else {
                showToast(
                    this@DetailChatActivity,
                    resources.getString(R.string.str_error)
                )
            }
        }
        viewModel.chatListResponse.observe(this@DetailChatActivity) {
            adapter.differ.submitList(it)
            if (it.size > 0) {
                binding.rcvDetailChat.smoothScrollToPosition(it.size - 1)
            }
        }
    }

}