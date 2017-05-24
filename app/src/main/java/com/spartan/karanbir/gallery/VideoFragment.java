package com.spartan.karanbir.gallery;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danikula.videocache.HttpProxyCacheServer;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

/**
 * @author karanbir.
 * @since 5/20/17.
 */

public class VideoFragment extends Fragment {

    private String mVideoUrl;
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
            HttpProxyCacheServer proxy = GalleryApp.getProxy(getActivity());
            String proxyUrl = proxy.getProxyUrl(mVideoUrl);
            scalableVideoView.setDataSource(proxyUrl);
            scalableVideoView.setScalableType(ScalableType.CENTER_CROP);
            scalableVideoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    scalableVideoView.start();
                    mp.setLooping(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }
}
