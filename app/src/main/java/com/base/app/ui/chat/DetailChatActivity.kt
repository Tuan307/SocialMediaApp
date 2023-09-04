package com.base.app.ui.chat

import androidx.navigation.fragment.NavHostFragment
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.EMPTY_STRING
import com.base.app.databinding.ActivityDetailChatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailChatActivity : BaseActivity<ActivityDetailChatBinding>() {
    private var chatId: String = ""
    private var chatName: String = ""
    private var url: String = ""

    override fun getContentLayout(): Int {
        return R.layout.activity_detail_chat
    }

    override fun initView() {
        val intent = intent
        chatId = intent.getStringExtra("chatId") ?: EMPTY_STRING
        chatName = intent.getStringExtra("chatName") ?: EMPTY_STRING
        url = intent.getStringExtra("url") ?: EMPTY_STRING

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.chat_nav)
        navGraph.setStartDestination(R.id.detailChatFragment)
        navController.setGraph(navGraph, intent.extras)
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
    }

}