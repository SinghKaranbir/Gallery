package com.spartan.karanbir.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * @author karanbir.
 * @since 5/20/17.
 */

public class VideoFragment extends Fragment {

    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private String mVideoUrl;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private int currentWindow;
    private boolean playWhenReady = true;

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
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.video_view);
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
            // a factory to create an AdaptiveVideoTrackSelection
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            // let the factory create a player instance with default components
            mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
            mPlayerView.setPlayer(mPlayer);
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(currentWindow, 0);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(mVideoUrl));
        mPlayer.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }
}
