package com.spartan.karanbir.gallery


import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.video_fragment.*


/**
 * @author karanbir.
 * *
 * @since 5/20/17.
 */

class VideoFragment : Fragment(), SimpleExoPlayer.VideoListener {

    private var mVideoUrl: String? = null

    private var mPlayer: SimpleExoPlayer? = null

    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady = true

    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()

    // Store instance variables based on arguments passed
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVideoUrl = arguments.getString("videoUrl")
    }

    // Inflate the view for the fragment based on layout XML
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.video_fragment, container, false)
        val textureView = view.findViewById(R.id.texture_view) as TextureView
        textureView.post {
            mWidth = texture_view.width.toFloat()
            mHeight = texture_view.height.toFloat()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(activity),
                    DefaultTrackSelector(), DefaultLoadControl())
            mPlayer!!.playWhenReady = playWhenReady
            mPlayer!!.seekTo(currentWindow, playbackPosition)
        }
        mPlayer!!.setVideoTextureView(texture_view)
        val proxy = GalleryApp.getProxy(activity)
        val proxyUrl = proxy.getProxyUrl(mVideoUrl)
        val mediaSource = buildMediaSource(proxyUrl)
        val loopingSource = LoopingMediaSource(mediaSource)
        mPlayer!!.prepare(loopingSource, true, false)
        mPlayer!!.setVideoListener(this)
    }

    private fun releasePlayer() {
        if (mPlayer != null) {
            playbackPosition = mPlayer!!.currentPosition
            currentWindow = mPlayer!!.currentWindowIndex
            playWhenReady = mPlayer!!.playWhenReady
            mPlayer!!.release()
            mPlayer = null
        }
    }

    private fun buildMediaSource(url: String): MediaSource {
        val bandwidthMeter = DefaultBandwidthMeter()
        val userAgent = Util.getUserAgent(activity, "Gallery")
        val dataSourceFactory = DefaultDataSourceFactory(activity, userAgent, bandwidthMeter)
        val extractorsFactory = DefaultExtractorsFactory()
        return ExtractorMediaSource(Uri.parse(url), dataSourceFactory, extractorsFactory, null, null)
    }


    override fun onVideoSizeChanged(width: Int, height: Int, i2: Int, v: Float) {
        val matrix = getMatrix(width.toFloat(), height.toFloat())
        texture_view!!.setTransform(matrix)
    }

    override fun onRenderedFirstFrame() {

    }


    private fun getMatrix(videoWidth: Float, videoHeight: Float): Matrix {
        val matrix = Matrix()

        var sx = mWidth / videoWidth
        var sy = mHeight / videoHeight
        val maxScale = Math.max(sx, sy)
        sx = maxScale / sx
        sy = maxScale / sy
        val px = mWidth / 2
        val py = mHeight / 2

        matrix.setScale(sx, sy, px, py)
        return matrix

    }

    companion object {

        fun newInstance(videoUrl: String): VideoFragment {
            val videoFragment = VideoFragment()
            val args = Bundle()
            args.putString("videoUrl", videoUrl)
            videoFragment.arguments = args
            return videoFragment
        }
    }


}
