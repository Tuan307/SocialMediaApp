package com.base.app.ui.chat

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(), ChatAdapter.OnItemClick {

    private val viewModel by viewModels<ChatViewModel>()
    private lateinit var adapter: ChatAdapter
    private lateinit var prettyTime: PrettyTime

    override fun getContentLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@ChatActivity)
        prettyTime = PrettyTime(Locale.getDefault())
        viewModel.getChatList()
        with(binding) {
            rcvChat.layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = ChatAdapter(this@ChatActivity, this@ChatActivity)
            rcvChat.adapter = adapter
        }
    }

    override fun initListener() {
        with(binding) {
            imgBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun observerLiveData() = with(viewModel) {
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
            Log.d("CheckHere",list.toString())
            adapter.differ.submitList(list)
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