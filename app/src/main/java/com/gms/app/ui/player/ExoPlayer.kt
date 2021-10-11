package com.gms.app.ui.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.gms.app.R
import com.gms.app.databinding.ExoPlayerFragmentBinding
import com.gms.app.utils.Injector
import com.gms.app.utils.hideSystemUI
import com.gms.app.utils.toast
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExoPlayer : AppCompatActivity(), Player.EventListener {

    lateinit var binding: ExoPlayerFragmentBinding

    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0


    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }
    object Start {
        fun start(context: Context, url: String?) {
            val starter = Intent(context, ExoPlayer::class.java)
            starter.putExtra("url", url)
            context.startActivity(starter)
        }
    }


    override fun onStart() {
        super.onStart()
        this.hideSystemUI()
        initializePlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        binding = ExoPlayerFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        preparePlayer(
            intent.getStringExtra("url").toString(),
            intent.getStringExtra("type").toString()
        )
        binding.exoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return when (type) {
            "dash" -> {
                DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
            }
            "m3u8" -> {
                HlsMediaSource.Factory(DefaultHttpDataSourceFactory(dataSourceFactory.toString()))
                    .createMediaSource(uri)
            }
            else -> {
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
            }
        }
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)

    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            binding.progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            binding.progressBar.visibility = View.INVISIBLE
        else if (playbackState == Player.EVENT_PLAYER_ERROR )
            this.toast(this.resources.getString(R.string.error_general))
    }

}