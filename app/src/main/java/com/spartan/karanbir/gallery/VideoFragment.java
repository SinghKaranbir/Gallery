package com.spartan.karanbir.gallery;


import android.media.MediaPlayer;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

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
    private ScalableVideoView scalableVideoView;

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
        scalableVideoView = (ScalableVideoView) view.findViewById(R.id.texture_view);
        try {
            scalableVideoView.setDataSource(getActivity(),Uri.parse(mVideoUrl));
            scalableVideoView.setScalableType(ScalableType.CENTER_CROP);
            scalableVideoView.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    scalableVideoView.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }
}
