package com.base.app.ui.main.fragment.reel

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.models.ExoPlayerItem
import com.base.app.data.models.Video
import com.base.app.databinding.FragmentReelBinding
import com.base.app.ui.main.MainViewModel


class ReelFragment : BaseFragment<FragmentReelBinding>(), ReelAdapter.OnVideoPreparedListener {
    private lateinit var adapter: ReelAdapter
    private val videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<ReelViewModel>()

    companion object {
        fun newInstance(): ReelFragment {
            return ReelFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_reel
    }

    override fun initView() {
        adapter = ReelAdapter(videos, requireContext(), this, mainViewModel)
        binding.reelViewPager.adapter = adapter
        binding.reelViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun initListener() {

    }

    override fun observerLiveData() {
        mainViewModel.reelClick.observe(this@ReelFragment) {
            if (it) {
                viewModel.getVideos()
            }
        }
        viewModel.videoListResponse.observe(this@ReelFragment) {
            mainViewModel.setReelClick(false)
            videos.addAll(it)
            Log.d("CheckHeee", it.size.toString())
            adapter.notifyDataSetChanged()
        }
    }


    override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
        exoPlayerItems.add(exoPlayerItem)
    }

    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.reelViewPager.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
//        val index = exoPlayerItems.indexOfFirst { it.position == binding.reelViewPager.currentItem }
//        if (index != -1) {
//            val player = exoPlayerItems[index].exoPlayer
//            player.playWhenReady = true
//            player.play()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}