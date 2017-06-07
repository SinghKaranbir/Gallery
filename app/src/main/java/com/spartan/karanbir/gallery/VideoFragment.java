package com.spartan.karanbir.gallery;


import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author karanbir.
 * @since 5/20/17.
 */

public class VideoFragment extends Fragment implements SimpleExoPlayer.VideoListener {

    private String mVideoUrl;

    private SimpleExoPlayer mPlayer;
    @BindView(R.id.video_view)
    TextureView mTextureView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    private float mWidth, mHeight;

    public static VideoFragment newInstance(String videoUrl) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("videoUrl", videoUrl);
        videoFragment.setArguments(args);
        return videoFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoUrl = getArguments().getString("videoUrl");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        ButterKnife.bind(this,view);
        mTextureView.post(new Runnable() {
            @Override
            public void run() {
                mWidth = mTextureView.getWidth();
                mHeight = mTextureView.getHeight();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(currentWindow, playbackPosition);
        }
        mPlayer.setVideoTextureView(mTextureView);
        HttpProxyCacheServer proxy = GalleryApp.getProxy(getActivity());
        String proxyUrl = proxy.getProxyUrl(mVideoUrl);
        MediaSource mediaSource = buildMediaSource(proxyUrl);
        LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
        mPlayer.prepare(loopingSource, true, false);
        mPlayer.setVideoListener(this);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private MediaSource buildMediaSource(String url) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        String userAgent = Util.getUserAgent(getActivity(), "Gallery");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent, bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        return new ExtractorMediaSource(Uri.parse(url), dataSourceFactory, extractorsFactory, null, null);
    }


    @Override
    public void onVideoSizeChanged(int width, int height, int i2, float v) {
        Matrix matrix = getMatrix(width, height);
        mTextureView.setTransform(matrix);
    }

    @Override
    public void onRenderedFirstFrame() {

    }


    private Matrix getMatrix(float videoWidth, float videoHeight) {
        Matrix matrix = new Matrix();

        float sx = mWidth / videoWidth;
        float sy = mHeight / videoHeight;
        float maxScale = Math.max(sx, sy);
        sx = maxScale / sx;
        sy = maxScale / sy;
        float px = mWidth / 2;
        float py = mHeight / 2;

        matrix.setScale(sx, sy, px, py);
        return matrix;

    }


}
