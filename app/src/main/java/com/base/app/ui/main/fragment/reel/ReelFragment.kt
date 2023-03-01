package com.base.app.ui.main.fragment.reel

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.models.ExoPlayerItem
import com.base.app.data.models.Video
import com.base.app.databinding.FragmentReelBinding
import com.base.app.ui.comment.video_comment.CustomBottomSheetFragment
import com.base.app.ui.main.MainViewModel


class ReelFragment : BaseFragment<FragmentReelBinding>(),
    //, ReelAdapter.OnVideoPreparedListener,
    ReelAdapter.onVideoClick {
    private lateinit var adapter: ReelAdapter
    private val videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<ReelViewModel>()
    var currentPosition = 0
    private var isClick = false

    companion object {
        fun newInstance(): ReelFragment {
            return ReelFragment()
        }

    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_reel
    }

    override fun initView() {
        adapter =
            ReelAdapter(videos, requireContext(), mainViewModel, viewModel, this@ReelFragment)
        binding.reelViewPager.adapter = adapter
    }

    override fun initListener() {

    }

    override fun observerLiveData() {
        mainViewModel.reelClick.observe(this@ReelFragment) {
            if (it != 0) {
                viewModel.getVideos()
            }
        }
        viewModel.videoListResponse.observe(this@ReelFragment) {
            videos.clear()
            videos.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }


    override fun onPause() {
        super.onPause()
        videos.clear()
        adapter.notifyDataSetChanged()
        viewModel.clearAllData()
    }


    override fun likeVideo(videoId: String, status: String) {
        viewModel.likeVideo(videoId, status)
    }

    override fun doubleClickLikePost(postId: String, status: String) {
        if (status == "like") {
            viewModel.likeVideo(postId, status)
        }
    }

    override fun commentVideo(videoId: String) {
        val bundle = Bundle()
        bundle.putString("videoId", videoId)
        val fragment = CustomBottomSheetFragment.newInstance()
        fragment.arguments = bundle
        fragment.show(childFragmentManager, "ActionBottomDialog")
    }

}