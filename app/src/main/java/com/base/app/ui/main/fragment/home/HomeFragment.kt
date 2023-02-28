package com.base.app.ui.main.fragment.home

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.PostItem
import com.base.app.databinding.FragmentHome2Binding
import com.base.app.ui.add_video_post.AddVideoActivity
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.main.MainActivity
import com.base.app.ui.main.MainViewModel
import java.io.File

class HomeFragment : BaseFragment<FragmentHome2Binding>(),
    PostAdapter.IPostCallBack, SwipeRefreshLayout.OnRefreshListener {
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
    private val homeViewModel by activityViewModels<MainViewModel>()

    override fun getContentLayout(): Int {
        return R.layout.fragment_home2
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@HomeFragment)
        initRecyclerView()
        binding.homeRefresh.setOnRefreshListener(this@HomeFragment)
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
            startActivity(Intent(requireContext(), AddVideoActivity::class.java))
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
            deletePostResponse.observe(this@HomeFragment) {
                if (it) {
                    showToast(requireContext(), resources.getString(R.string.str_success))
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                } else {
                    showToast(requireContext(), resources.getString(R.string.str_error))
                }
            }
        }
        homeViewModel.apply {
            doubleClick.observe(this@HomeFragment) {
                if (it) {
                    binding.rcvHome.smoothScrollToPosition(0)
                    setRefresh(false)
                }
            }
        }
    }

    override fun clickPost(postId: String, publisherId: String) {
        val intent = Intent(context, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun likePost(postId: String, status: String, publisherId: String) {
        viewModel.likePost(postId, status, publisherId)
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


    override fun doubleClickLikePost(postId: String, status: String, publisherId: String) {
        if (status == "like") {
            viewModel.likePost(postId, status, publisherId)
        }
    }

    override fun downloadImage(fileName: String, postId: String) {
        downloadImageByUri(fileName, postId)
    }

    override fun editImage(postId: String, view: View) {
//        val alertDialog = AlertDialog.Builder(requireContext())
//        alertDialog.setTitle("Edit Post")
//
//        val editText = EditText(requireContext())
//        val layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        editText.layoutParams = layoutParams
//        alertDialog.setView(editText)
        //getText(postid, editText)
        //TODO need to be fixed later

        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.post_item)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

            }
            true
        }
        popupMenu.show()
    }

    override fun deleteImage(postId: String) {
        viewModel.deletePost(postId)
    }

    private fun downloadImageByUri(fileName: String, postImage: String) {
        try {
            val downloadManager: DownloadManager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(postImage)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + fileName + ".jpg"
                )
            downloadManager.enqueue(request)
            showToast(
                requireContext(),
                resources.getString(R.string.str_success),
            )
        } catch (e: Exception) {
            showToast(
                requireContext(),
                resources.getString(R.string.str_error),
            )
        }
    }

    override fun onRefresh() {
        viewModel.getLastKey()
        viewModel.getData()
        viewModel.isLoading.postValue(false)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.homeRefresh.isRefreshing = false
        }, 1500)
    }
}