package com.base.app.ui.group.invite_member

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.user.User
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.databinding.ActivityInviteMemberBinding
import com.base.app.ui.group.add_group.InviteMemberViewModel
import com.base.app.ui.group.add_group.adapter.InviteMemberAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InviteMemberActivity : AppCompatActivity(), InviteMemberAdapter.OnInvite {
    private lateinit var binding: ActivityInviteMemberBinding
    private val viewModel by viewModels<InviteMemberViewModel>()
    private lateinit var inviteMemberAdapter: InviteMemberAdapter
    private var groupId = 0.toLong()
    private var groupName = ""
    private var inviteList: ArrayList<User> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_member)
        setContentView(binding.root)
        inviteMemberAdapter = InviteMemberAdapter(this@InviteMemberActivity)
        viewModel.getFollowing()
        val intent = intent
        groupId = intent.getStringExtra("groupId").toString().toLong()
        groupName = intent.getStringExtra("groupName").toString()
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            listInviteMember.apply {
                layoutManager = LinearLayoutManager(this@InviteMemberActivity)
                adapter = inviteMemberAdapter
            }
            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.searchUser(p0.toString(), 100, 1)
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        followingResponse.observe(this@InviteMemberActivity) {
            getUserView(it)
        }
        userResponse.observe(this@InviteMemberActivity) {
            inviteList.clear()
            inviteList.addAll(it.map { data ->
                User(
                    userId = data.id.toString(),
                    userName = data.username.toString(),
                    fullName = data.fullname.toString(),
                    imageUrl = data.imageurl.toString(),
                    bio = data.bio,
                    email = "",
                    latitude = 0.0,
                    longitude = 0.0,
                    hasChosen = data.hasInvite,
                    lastOnline = "",
                    isBlock = false,
                    userInterestProfiles = null
                )
            })
            inviteMemberAdapter.submitList(it.map { data ->
                User(
                    userId = data.id.toString(),
                    userName = data.username.toString(),
                    fullName = data.fullname.toString(),
                    imageUrl = data.imageurl.toString(),
                    bio = data.bio,
                    email = "",
                    latitude = 0.0,
                    longitude = 0.0,
                    hasChosen = data.hasInvite,
                    lastOnline = "",
                    isBlock = false,
                    userInterestProfiles = null
                )
            })
        }
        searchUserResponse.observe(this@InviteMemberActivity) {
            inviteList.clear()
            inviteList.addAll(it.map { data ->
                User(
                    userId = data.userId.toString(),
                    userName = data.userName.toString(),
                    fullName = data.fullName.toString(),
                    imageUrl = data.imageUrl.toString(),
                    bio = data.bio,
                    email = data.email,
                    latitude = data.latitude ?: 0.0,
                    longitude = data.longitude ?:0.0,
                    hasChosen = data.hasChosen ?: false,
                    lastOnline = data.lastOnline,
                    isBlock = data.isBlock,
                    userInterestProfiles = null
                )
            })
            inviteMemberAdapter.submitList(it.toList())
        }
        inviteUserResponse.observe(this@InviteMemberActivity) {
            Toast.makeText(this@InviteMemberActivity, it.status.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInviteClick(position: Int) {
        Toast.makeText(this@InviteMemberActivity, position.toString(), Toast.LENGTH_SHORT).show()
        val user = inviteList[position]
        val check = inviteList[position].hasChosen
        if (check != null) {
            if (!check) {
                viewModel.inviteMember(
                    CreateGroupInvitationRequest(
                        groupId,
                        Calendar.getInstance().time.time.toString(),
                        "Mời bạn tham gia nhóm $groupName",
                        user.userId.toString(),
                        "invite",
                        viewModel.firebaseUser?.uid.toString(),
                    )
                )
                inviteList[position].hasChosen = true
                inviteMemberAdapter.submitList(inviteList.toList())
                inviteMemberAdapter.notifyDataSetChanged()
            }
        }

    }
}