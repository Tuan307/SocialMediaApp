package com.base.app.ui.chat

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.ActivitySearchChatFriendBinding
import com.base.app.ui.chat.adapter.SearchChatFriendAdapter
import com.base.app.ui.chat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchChatFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchChatFriendBinding
    private lateinit var searchChatFriendAdapter: SearchChatFriendAdapter
    private val viewModel by viewModels<ChatViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_chat_friend)
        setContentView(binding.root)
        initView()
        observeData()
    }

    private fun initView() = with(binding) {
        searchChatFriendAdapter = SearchChatFriendAdapter(::onItemCLick)
        imageBack.setOnClickListener {
            finish()
        }
        inputSearch.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != "" && !text.isNullOrEmpty()) {
                viewModel.searchUser(text.toString(), 100, 1)
            }
        }
        listOfChatFriend.apply {
            layoutManager = LinearLayoutManager(this@SearchChatFriendActivity)
            adapter = searchChatFriendAdapter
        }
    }

    private fun observeData() = with(viewModel) {
        searchUserResponse.observe(this@SearchChatFriendActivity) {
            searchChatFriendAdapter.submitList(it.toList())
        }
    }

    private fun onItemCLick(data: DatingUser) {
        val intent = Intent(this@SearchChatFriendActivity, DetailChatActivity::class.java)
        intent.putExtra("chatId", data.userId)
        intent.putExtra("chatName", data.userName)
        intent.putExtra("url", data.imageUrl)
        startActivity(intent)
        finish()
    }
}