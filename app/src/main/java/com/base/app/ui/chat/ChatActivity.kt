package com.base.app.ui.chat

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.chat.RecentChatModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.ActivityChatBinding
import com.base.app.ui.chat.adapter.ChatAdapter
import com.base.app.ui.chat.adapter.ChatFollowerAdapter
import com.base.app.ui.chat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(), ChatAdapter.OnItemClick,
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModels<ChatViewModel>()
    private lateinit var recentChatAdapter: ChatAdapter
    private lateinit var followerAdapter: ChatFollowerAdapter
    private lateinit var prettyTime: PrettyTime

    override fun getContentLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@ChatActivity)
        prettyTime = PrettyTime(Locale.getDefault())
        viewModel.getChatFollowList()
        viewModel.getRecentChatUserList()
        with(binding) {
            edtSearch.setOnClickListener {
                startActivity(Intent(this@ChatActivity, SearchChatFriendActivity::class.java))
            }
            swipeChat.setOnRefreshListener(this@ChatActivity)
            recentChatAdapter = ChatAdapter(this@ChatActivity, this@ChatActivity)
            followerAdapter = ChatFollowerAdapter(::navigateToDetailChat)
            listOfFollower.apply {
                layoutManager =
                    LinearLayoutManager(this@ChatActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = followerAdapter
            }
            rcvChat.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = recentChatAdapter
            }
        }
    }

    private fun navigateToDetailChat(data: DatingUser) {
        val intent = Intent(this@ChatActivity, DetailChatActivity::class.java)
        intent.putExtra("chatId", data.userId)
        intent.putExtra("chatName", data.userName)
        intent.putExtra("url", data.imageUrl)
        startActivity(intent)
    }

    override fun initListener() {
        with(binding) {
            imgBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun observerLiveData() = with(viewModel) {
        recentChatResponse.observe(this@ChatActivity) {
            val list = it.map { data ->
                RecentChatModel(
                    targetId = data.targetId,
                    targetName = data.targetName,
                    createdAt = prettyTime.format(data.createdAt?.let { it1 -> Date(it1.toLong()) }),
                    targetImage = data.targetImage,
                    lastMessage = "Tin nhắn: ${data.lastMessage}",
                )
            }
            recentChatAdapter.differ.submitList(list)
        }

        chatListUserResponse.observe(this@ChatActivity) {
            val list = it.data?.map { data ->
                DatingUser(
                    userId = data.targetId?.userId,
                    userName = data.targetId?.userName,
                    lastOnline = prettyTime.format(data.targetId?.lastOnline?.let { it1 ->
                        Date(
                            it1.toLong()
                        )
                    }),
                    imageUrl = data.targetId?.imageUrl,
                    fullName = null,
                    bio = null,
                    email = null,
                    latitude = null,
                    longitude = null,
                    hasChosen = null,
                    isBlock = null,
                    userInterestProfiles = null
                )
            }.orEmpty()
            followerAdapter.submitList(list.toList())
        }
    }

    override fun onItemCLick(id: String, name: String, url: String) {
        val intent = Intent(this@ChatActivity, DetailChatActivity::class.java)
        intent.putExtra("chatId", id)
        intent.putExtra("chatName", name)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    override fun onRefresh() {
        followerAdapter.submitList(null)
        viewModel.getRecentChatUserList()
        viewModel.getChatFollowList()
        binding.edtSearch.text = null
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeChat.isRefreshing = false
        }, 1000)
    }
}