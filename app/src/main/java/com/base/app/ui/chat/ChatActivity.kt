package com.base.app.ui.chat

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(), ChatAdapter.OnItemClick {

    private val viewModel by viewModels<ChatViewModel>()
    private lateinit var adapter: ChatAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        viewModel.getFollower()
        binding.apply {
            rcvChat.layoutManager = LinearLayoutManager(this@ChatActivity)
            rcvChat.setHasFixedSize(true)
            adapter = ChatAdapter(this@ChatActivity, this@ChatActivity)
            rcvChat.adapter = adapter
        }
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun observerLiveData() {
        viewModel.getFollowerResponse.observe(this@ChatActivity) {
            viewModel.getUserView(it)
        }
        viewModel.getUserResponse.observe(this@ChatActivity) {
            adapter.differ.submitList(it)
        }
    }

    override fun onItemCLick(id: String, name: String, url: String) {
        val intent = Intent(this@ChatActivity, DetailChatActivity::class.java)
        intent.putExtra("chatId", id)
        intent.putExtra("chatName", name)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}