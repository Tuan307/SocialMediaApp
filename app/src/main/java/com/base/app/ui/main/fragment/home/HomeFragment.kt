package com.base.app.ui.main.fragment.home

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.PostItem
import com.base.app.databinding.FragmentHome2Binding
import com.base.app.ui.comment.CommentActivity
import com.esafirm.imagepicker.features.registerImagePicker

class HomeFragment : BaseFragment<FragmentHome2Binding>(),
    PostAdapter.IPostCallBack {
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var key: String? = null
    private var lastKey: String? = null
    private var listAll: ArrayList<PostItem> = ArrayList()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var homeAdapter: PostAdapter
    private val imagePickerLauncher = registerImagePicker {
        imagesPicker.clear()
        imagesPicker.addAll(it)
        handleImagePicker()
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_home2
    }

    override fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            rcvHome.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            rcvHome.setHasFixedSize(true)
        }
    }

    override fun initListener() {
        binding.imgAdd.setOnClickListener {
            //imagePickerLauncher.launch(createConfig(true, 1))
        }
        viewModel.getLastKey()
        viewModel.getData()
    }

    override fun observerLiveData() {
        viewModel.apply {
            getListResponse.observe(this@HomeFragment) {
                key = it[it.size - 1].postid
                listAll.clear()
                listAll.addAll(it)
                homeAdapter = PostAdapter(requireContext(), this@HomeFragment, viewModel, listAll)
                binding.rcvHome.adapter = homeAdapter

                endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                    binding.rcvHome.layoutManager as LinearLayoutManager
                ) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        if (key != lastKey)
                            key?.let { it1 -> viewModel.getDataOnLoadMore(it1) }
                    }
                }
                binding.rcvHome.addOnScrollListener(endlessRecyclerViewScrollListener)

            }
            getListOnLoadMore.observe(this@HomeFragment) {
                listAll.addAll(it)
                key = it[it.size - 1].postid
                homeAdapter.notifyDataSetChanged()
            }
            getLastKey.observe(this@HomeFragment) {
                lastKey = it
            }
        }
    }

    override fun clickPost(postId: String, publisherId: String) {
        val intent = Intent(context, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun likePost(postId: String, status: String) {
        viewModel.likePost(postId, status)
    }

    override fun disLikePost(postId: String) {
    }

    override fun commentPost(postId: String, publisherId: String) {
        val intent = Intent(context, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun savePost(postId: String, status: String) {
        viewModel.savePost(postId, status)
    }

    override fun sharePost(postId: String) {
        //do later
    }

    private fun handleImagePicker() {
//        if (imagesPicker.size > 0) {
//            viewModel.createImage(this, imagesPicker)
//            val image = imagesPicker[0]
//            val file = File(image.path)
//            val requestFile = file
//                .asRequestBody("image/jpg".toMediaTypeOrNull())
//            val fileName =
//                CommonUtils.getRandomString() + System.currentTimeMillis() + file.name
//            body = MultipartBody.Part.createFormData(
//                "image",
//                fileName,
//                requestFile
//            )
//            viewModel.uploadAvatar(body)
//        }
    }

}