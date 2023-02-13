package com.base.app.ui.follow

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.User
import com.base.app.databinding.ActivityFollowerBinding
import com.base.app.ui.follow.adapter.FollowerAdapter

class FollowerActivity : BaseActivity<ActivityFollowerBinding>(), FollowerAdapter.ICallBack {

    private val viewModel by viewModels<FollowerViewModel>()
    private var profileId: String = ""
    private var title: String = ""
    private lateinit var followerAdapter: FollowerAdapter
    private var list: ArrayList<String> = ArrayList()
    private var userList = ArrayList<User>()
    override fun getContentLayout(): Int = R.layout.activity_follower


    override fun initView() {
        val intent = intent
        profileId = intent.getStringExtra("id").toString()
        title = intent.getStringExtra("title").toString()
        binding.apply {
            rcvFollower.layoutManager = LinearLayoutManager(this@FollowerActivity)
            rcvFollower.setHasFixedSize(true)
            followerAdapter =
                FollowerAdapter(this@FollowerActivity, userList, this@FollowerActivity)
            rcvFollower.adapter = followerAdapter
        }
        when (title) {
            "following" -> {
                viewModel.getFollowing(profileId)
            }
            "followers" -> {
                viewModel.getFollower(profileId)
            }
        }
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUser(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun searchUser(s: String) {
        if (s != "") {
            viewModel.searchUser(s)
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            followerResponse.observe(this@FollowerActivity) {
                list.clear()
                list.addAll(it)
                Log.d("CheckLo", "$title abd $profileId")
                viewModel.getUserView(list)
            }
            followingResponse.observe(this@FollowerActivity) {
                list.clear()
                list.addAll(it)
                viewModel.getUserView(list)
            }
            userResponse.observe(this@FollowerActivity) {
                userList.clear()
                userList.addAll(it)
                followerAdapter.notifyDataSetChanged()
            }
            getUserResponse.observe(this@FollowerActivity) {
                userList.clear()
                userList.addAll(it)
                followerAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun itemClick(id: String) {
//        if (CustomApplication.dataManager.getString("id") != null) {
//            CustomApplication.dataManager.remove("id")
//        }
//        CustomApplication.dataManager.save("id", id)
//        val intent = Intent(this@FollowerActivity, MainActivity::class.java)
//        intent.putExtra("from", "follow")
//        startActivity(intent)
    }
}