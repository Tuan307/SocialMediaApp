package com.base.app.ui.chat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.common.EMPTY_STRING
import com.base.app.data.models.NotificationData
import com.base.app.data.models.PushNotification
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.FragmentDetailChatBinding
import com.base.app.ui.video_call.MainActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashMap

@AndroidEntryPoint
class DetailChatFragment : Fragment(), ImagesAdapter.ChooseImage, DetailChatAdapter.OnMessageClick {
    private lateinit var binding: FragmentDetailChatBinding
    private var chatId: String = ""
    private var chatName: String = ""
    private var url: String = ""
    private var idToken = ""
    private val viewModel by viewModels<ChatViewModel>()
    private lateinit var adapter: DetailChatAdapter
    private var imagesList: ArrayList<String> = arrayListOf()
    private lateinit var imagesAdapter: ImagesAdapter
    private var isOpenImageList: Boolean = false
    private val args: DetailChatFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        observerLiveData()
    }

    fun initView() {
        chatId = args.chatId
        chatName = args.chatName
        url = args.url
        viewModel.getPrivateChat(chatId, viewModel.firebaseUser?.uid.toString())
        viewModel.getReceiverToken(chatId)
        with(binding) {
            txtUserName.text = chatName
            Glide.with(requireContext()).load(url).into(imgAvatar)
            rcvDetailChat.layoutManager = LinearLayoutManager(requireContext())
            rcvDetailChat.setHasFixedSize(true)
            adapter =
                DetailChatAdapter(viewModel.firebaseUser?.uid.toString(), this@DetailChatFragment)
            rcvDetailChat.adapter = adapter
        }
    }

    fun initListener() {
        with(binding) {
            imgBack.setOnClickListener {
                requireActivity().finish()
            }
            imgSend.setOnClickListener {
                val message = edtInputText.text.toString()
                if (message.isNotEmpty()) {
                    viewModel.sendMessage(message, chatId,chatName,url)
                } else {
                    showToast(
                        requireContext(),
                        resources.getString(R.string.strPleaseInputMessage)
                    )
                }
                if (idToken != "") {
                    val notification = PushNotification(
                        NotificationData("Message", message, EMPTY_STRING),
                        idToken
                    )
                    viewModel.sendNotification(notification)
                }
                edtInputText.text.clear()
            }
            edtInputText.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    imgSend.visibility = View.VISIBLE
                } else {
                    imgSend.visibility = View.GONE
                }
            }
            imgVideoCall.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
            imageAddImages.setOnClickListener {
                isOpenImageList = !isOpenImageList
                if (isOpenImageList) {
                    imageAddImages.setImageResource(R.drawable.ic_add_photo_checked)
                    listPhotos.visibility = View.VISIBLE
                } else {
                    imageAddImages.setImageResource(R.drawable.ic_add_chat_photo)
                    listPhotos.visibility = View.GONE
                }
                viewModel.getAllImagesFromGallery(requireContext())
            }
        }
    }

    fun observerLiveData() = with(viewModel) {
        isLoadingResponse.observe(viewLifecycleOwner) {
            binding.progressBarChat.isVisible = it
        }
        uploadImageMessageResponse.observe(viewLifecycleOwner) {
            if (it) {
                binding.imageAddImages.setImageResource(R.drawable.ic_add_chat_photo)
                binding.listPhotos.visibility = View.GONE
            } else {
                showToast(requireContext(), "Error")
            }
        }
        sendChatResponse.observe(viewLifecycleOwner) {
            if (!it) {
                showToast(
                    requireContext(),
                    resources.getString(R.string.str_error)
                )
            }
        }
        chatListResponse.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
            if (it.size > 0) {
                binding.rcvDetailChat.smoothScrollToPosition(it.size - 1)
            }
        }
        tokenResponse.observe(viewLifecycleOwner) {
            idToken = it
        }
        listOfImages.observe(viewLifecycleOwner) {
            imagesList.clear()
            imagesList.addAll(it)
            imagesAdapter = ImagesAdapter(imagesList, this@DetailChatFragment)
            if (imagesList.isEmpty()) {
                binding.listPhotos.isVisible = false
            }
            binding.listPhotos.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = imagesAdapter
            }
            imagesAdapter.notifyDataSetChanged()
        }
    }

    override fun onChooseImage(url: Uri) {
        viewModel.uploadImageMessage(url, chatId)
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onImageClick(data: String) {
        val action =
            DetailChatFragmentDirections.actionDetailChatFragmentToDetailChatImageFragment(chatUrl = data)
        findNavController().navigate(action)
    }
}