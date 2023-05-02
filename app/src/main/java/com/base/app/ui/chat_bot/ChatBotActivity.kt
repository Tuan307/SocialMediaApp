package com.base.app.ui.chat_bot

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.CommonUtils.hideSoftKeyboard
import com.base.app.data.models.response.Message
import com.base.app.databinding.ActivityChatBotBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatBotActivity : BaseActivity<ActivityChatBotBinding>() {

    private var chatList: ArrayList<Message> = ArrayList()
    private val viewModel by viewModels<ChatBotViewModel>()
    private lateinit var adapter: ChatBotAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_chat_bot
    }

    override fun initView() {
        chatList = ArrayList()
        binding.apply {
            toolbar.tvTitle.text = "Chat Bot"
            toolbar.imgBack.setOnClickListener {
                finish()
            }
            chatList.add(Message("assistant", "Mời bạn nhập câu hỏi muốn hỏi"))
            adapter = ChatBotAdapter(chatList)
            rcvMessages.layoutManager = LinearLayoutManager(this@ChatBotActivity)
            rcvMessages.setHasFixedSize(true)
            rcvMessages.adapter = adapter
        }

    }

    override fun initListener() {
        binding.apply {
            btnSend.setOnClickListener {
                val message = edtMessage.text.toString()
                if (message.isNullOrEmpty()) {
                    showToast(this@ChatBotActivity, "Null")
                } else {
                    chatList.add(
                        Message(
                            role = "user",
                            content = message
                        )
                    )
                    adapter.notifyDataSetChanged()
                    viewModel.sendChatRequest(
                        Message(
                            role = "user",
                            content = message
                        )
                    )
                    edtMessage.text.clear()
                    hideSoftKeyboard()
                }
            }
        }
    }

    override fun observerLiveData() {
        viewModel.chatResponse.observe(this@ChatBotActivity) {
            chatList.add(it)
            adapter.notifyDataSetChanged()
        }
    }

}