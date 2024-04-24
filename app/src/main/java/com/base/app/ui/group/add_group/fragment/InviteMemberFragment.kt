package com.base.app.ui.group.add_group.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.data.models.user.User
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.databinding.FragmentInviteMemberBinding
import com.base.app.ui.group.add_group.InviteMemberViewModel
import com.base.app.ui.group.add_group.adapter.InviteMemberAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InviteMemberFragment : Fragment(), InviteMemberAdapter.OnInvite {
    private lateinit var binding: FragmentInviteMemberBinding
    private val viewModel by viewModels<InviteMemberViewModel>()
    private lateinit var inviteMemberAdapter: InviteMemberAdapter
    private val args: InviteMemberFragmentArgs by navArgs()
    private var groupId = ""
    private var groupName = ""
    private var inviteList: ArrayList<User> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteMemberBinding.inflate(inflater, container, false)
        inviteMemberAdapter = InviteMemberAdapter(this@InviteMemberFragment)
        groupId = args.groupId
        groupName = args.groupName
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFollowing()
        with(binding) {
            imageBack.setOnClickListener {
                findNavController().navigateUp()
            }
            textDone.setOnClickListener {
                requireActivity().finish()
            }
            listInviteMember.apply {
                layoutManager = LinearLayoutManager(requireContext())
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
        followingResponse.observe(viewLifecycleOwner) {
            getUserView(it)
        }
        userResponse.observe(viewLifecycleOwner) {
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
        searchUserResponse.observe(viewLifecycleOwner) {
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
        inviteUserResponse.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.status.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInviteClick(position: Int) {
        if(inviteList.isNotEmpty()){
            val user = inviteList[position]
            viewModel.inviteMember(
                CreateGroupInvitationRequest(
                    groupId.toLong(),
                    Calendar.getInstance().time.time.toString(),
                    "Mời bạn tham gia nhóm $groupName",
                    user.userId.toString(),
                    "invite",
                    viewModel.firebaseUser?.uid.toString(),
                )
            )
            val check = inviteList[position].hasChosen
            inviteList[position].hasChosen = !check!!
            inviteMemberAdapter.submitList(inviteList.toList())
        }
    }
}