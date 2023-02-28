package com.base.app.ui.main.fragment.reel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.common.CommonUtils
import com.base.app.data.models.Video
import com.base.app.databinding.ReelItemBinding
import com.base.app.ui.main.MainViewModel

class ReelAdapter(
    private val list: ArrayList<Video>,
    private val context: Context,
    val mainViewModel: MainViewModel,
    val viewModel: ReelViewModel,
    val listener: onVideoClick
) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ReelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setVideoPath(data: Video) {
            var mediaPlayer: MediaPlayer? = null
            mainViewModel.getUser(
                context,
                binding.imgAvatar,
                binding.txtUserName,
            )
            viewModel.isLikeVideo(data.videoId, binding.imgHeart)
            viewModel.getVideoLikeCount(data.videoId, binding.txtLikeCount)
            viewModel.getVideoCommentCount(data.videoId, binding.txtCommentCount)
            binding.txtDescription.text = data.description
            binding.reelVideoPlayer.setVideoURI(Uri.parse(data.url))
            binding.videoProgress.visibility = View.VISIBLE
            binding.reelVideoPlayer.setOnPreparedListener {
                it.start()
                mediaPlayer = it
                binding.videoProgress.visibility = View.GONE
            }
            binding.reelVideoPlayer.setOnCompletionListener {
                it.start()
                mediaPlayer = it
            }

            binding.imgHeart.setOnClickListener {
                listener.likeVideo(data.videoId, binding.imgHeart.tag.toString())
            }
            binding.imgComment.setOnClickListener {
                listener.commentVideo(data.videoId)
            }
            binding.reelConstraint.setOnClickListener {
                if (!CommonUtils.isDoubleClick()) {
                    if (binding.reelVideoPlayer.isPlaying) {
                        binding.imgPlay.visibility = View.VISIBLE
                        binding.reelVideoPlayer.pause()
                        if (mediaPlayer != null) {
                            mediaPlayer?.pause()
                        }
                    } else {
                        binding.imgPlay.visibility = View.GONE
                        if (mediaPlayer != null) {
                            mediaPlayer?.start()
                        }
                    }
                } else {
                    listener.doubleClickLikePost(data.videoId, binding.imgHeart.tag.toString())
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.setVideoPath(model)

    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface onVideoClick {
        fun likeVideo(videoId: String, status: String)
        fun doubleClickLikePost(postId: String, status: String)
        fun commentVideo(videoId: String)
    }
}