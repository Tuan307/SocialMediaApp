package com.base.app.ui.main.fragment.reel

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.ExoPlayerItem
import com.base.app.data.models.Video
import com.base.app.databinding.ReelItemBinding
import com.base.app.ui.main.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ReelAdapter(
    private val list: ArrayList<Video>,
    private val context: Context,
    var videoPreparedListener: OnVideoPreparedListener,
    val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ReelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var exoPlayer: ExoPlayer
        lateinit var mediaSource: MediaSource
        fun setVideoPath(url: String) {
            mainViewModel.getUser(
                context,
                binding.imgAvatar,
                binding.txtUserName,
                binding.txtDescription
            )
            binding.imgPlay.tag = "play"
            binding.imgPlay.visibility = View.INVISIBLE
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playWhenReady) {
                        binding.imgPlay.tag = "play"
                        binding.imgPlay.visibility = View.INVISIBLE
                    }
                }
            })
            binding.reelVideoPlayer.player = exoPlayer
            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            val dataSourceFactory = DefaultDataSource.Factory(context)
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            if (absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }
            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
            binding.reelConstraint.setOnClickListener {
                if (binding.imgPlay.tag.equals("play")) {
                    exoPlayer.pause()
                    exoPlayer.playWhenReady = false
                    binding.imgPlay.tag = "stop"
                    binding.imgPlay.visibility = View.VISIBLE
                } else {
                    exoPlayer.playWhenReady = true
                    exoPlayer.play()
                    binding.imgPlay.tag = "play"
                    binding.imgPlay.visibility = View.INVISIBLE
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
        holder.setVideoPath(model.url)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}