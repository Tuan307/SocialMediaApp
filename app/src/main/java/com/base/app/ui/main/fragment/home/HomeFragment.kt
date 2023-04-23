package com.base.app.ui.main.fragment.home

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.EMPTY_STRING
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.NotificationData
import com.base.app.data.models.PostItem
import com.base.app.data.models.PushNotification
import com.base.app.databinding.FragmentHome2Binding
import com.base.app.ui.chat.ChatActivity
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.main.MainActivity
import com.base.app.ui.main.MainViewModel
import com.base.app.ui.main.fragment.home.bottom_sheet.AddPostBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHome2Binding>(),
    PostAdapter.IPostCallBack, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var key: String? = null
    private var lastKey: String? = null
    private var listAll: ArrayList<PostItem> = ArrayList()
    private var txtName = EMPTY_STRING
    private var imageUri: Uri? = null


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
        homeViewModel.getCurrentUserInformation()
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

    override fun initListener() = with(binding) {
        viewModel.getLastKey()
        viewModel.getData()
        imgDM.setOnClickListener {
            startActivity(Intent(requireContext(), ChatActivity::class.java))
        }
        imgAddPost.setOnClickListener {
            val fragment = AddPostBottomSheetFragment.newInstance()
            fragment.show(childFragmentManager, "ActionBottomDialog")
        }
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
            tokenResponse.observe(this@HomeFragment) {
                var message = txtName
                if (message == EMPTY_STRING) {
                    message = "Ai đó"
                }
                val notification = PushNotification(
                    to = it,
                    data = NotificationData(
                        "Thông Báo",
                        "$message đã thích ảnh của bạn",
                        EMPTY_STRING
                    )
                )
                viewModel.sendNotification(notification)
            }
        }
        homeViewModel.apply {
            doubleClick.observe(this@HomeFragment) {
                if (it) {
                    binding.rcvHome.smoothScrollToPosition(0)
                    setRefresh(false)
                }
            }
            userResponse.observe(this@HomeFragment) {
                txtName = it.username.toString()
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
        if (publisherId != viewModel.firebaseUser?.uid.toString()
            && status == "like"
        ) {
            viewModel.getReceiverToken(publisherId)
        }
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

    override fun sharePost(postId: Drawable) {
        // convert to bitmap
        val bitmap = postId.toBitmap()
        // get uri
        imageUri = getImageToShare(bitmap);
        val intent = Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        intent.type = "image/*";
        context?.startActivity(Intent.createChooser(intent, "Share Image"));
    }

    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val folder = File(context!!.filesDir, "images")
        var uri: Uri? = null
        try {
            folder.mkdir()
            val file = File(folder, "shared_images.jpg")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            uri = FileProvider.getUriForFile(context!!, "com.base.app.ui.main.fragment.home", file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return uri
    }

    override fun doubleClickLikePost(postId: String, status: String, publisherId: String) {
        if (status == "like") {
            viewModel.likePost(postId, status, publisherId)
            if (publisherId != viewModel.firebaseUser?.uid.toString()) {
                viewModel.getReceiverToken(publisherId)
            }
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