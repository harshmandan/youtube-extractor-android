package com.mbytessolution.youtube_extractor_example;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mbytessolution.youtube_extractor.Interfaces.YoutubeExtractorCallback;
import com.mbytessolution.youtube_extractor.Models.VideoStream;

import com.mbytessolution.youtube_extractor.YoutubeExtractor;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PlayerView videoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private ProgressBar progress_bar;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoPlayerView = (PlayerView) findViewById(R.id.video_player_view);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        YoutubeExtractor youtubeExtractor = new YoutubeExtractor();
        youtubeExtractor.Extract("https://www.youtube.com/watch?v=Z2F67Ji6Jos", "StreamExtractor", new YoutubeExtractorCallback() {
            @Override
            public void onSuccess(List<VideoStream> videoStreamList) {
                if (videoStreamList.size() <= 0) {
                    Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (videoStreamList.size() > 1) {
                        playVideos(videoStreamList.get(1).getUrl());
                    }
                    else  {
                        playVideos(videoStreamList.get(0).getUrl());
                    }


                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, ""+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    private void playVideos(String url) {
         DataSource.Factory mediaDataSourceFactory;
         MediaSource hlsMediaSource = null;

            TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                    DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                    DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                    DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS).createDefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(adaptiveTrackSelection), loadControl);
            videoPlayerView.setShutterBackgroundColor(Color.TRANSPARENT);
            videoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(true);
            videoPlayerView.requestFocus();

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        if (url.startsWith("https://manifest.googlevideo.com/api/manifest/dash/expire/") && !url.endsWith(".m3u8")) {
            DataSource.Factory dataSourceFactory =
                    new DefaultHttpDataSourceFactory(com.google.android.exoplayer2.util.Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);
            MediaSource mediaSource = new DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            simpleExoPlayer.prepare(mediaSource, false, false);

        } else if (url.endsWith(".m3u8")) {
            mediaDataSourceFactory = new DefaultDataSourceFactory(this, com.google.android.exoplayer2.util.Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);
            MediaSource mediaSource = new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory("My Agent"))
                    .setAllowChunklessPreparation(true)
                    .createMediaSource(Uri.parse(url));
            simpleExoPlayer.prepare(mediaSource, false, false);
            hlsMediaSource = mediaSource;
        } else {
            mediaDataSourceFactory = new DefaultDataSourceFactory(this, com.google.android.exoplayer2.util.Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);

                MediaSource mediaSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(url));
                simpleExoPlayer.prepare(mediaSource, false, false);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (simpleExoPlayer!= null)
        simpleExoPlayer.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer!= null)
        simpleExoPlayer.release();
    }


}