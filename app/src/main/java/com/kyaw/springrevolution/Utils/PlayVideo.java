package com.kyaw.springrevolution.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kyaw.springrevolution.Model.Duration;

public class PlayVideo extends AsyncTask<Void,Void,Void> {
    String url;
    VideoView videoView;
    ProgressBar timer;
    Context context;
    TextView duration,beginning;
    LinearLayout container;
    CircularProgressIndicator loading_bar;
    LinearLayout videoViewContainer,player_container,overlay;

    public PlayVideo(String url, VideoView videoView, ProgressBar timer, Context context, CircularProgressIndicator loading_bar, LinearLayout container, LinearLayout videoViewContainer, LinearLayout player_container, TextView duration, LinearLayout overlay, TextView beginning){
        this.url = url;
        this.videoView = videoView;
        this.timer = timer;
        this.context = context;
        this.loading_bar = loading_bar;
        this.container = container;
        this.videoViewContainer = videoViewContainer;
        this.player_container = player_container;
        this.duration = duration;
        this.overlay = overlay;
        this.beginning = beginning;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        videoView.setVideoURI(Uri.parse(url));
        int originDuration = videoView.getDuration();



        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        System.out.println("progress: "+percent);
                        Duration time = Constants.getTime(videoView.getDuration());
                        container.setVisibility(View.VISIBLE);
                        loading_bar.setVisibilityAfterHide(View.GONE);
                        duration.setText(String.valueOf(time.getMinute() + ":" + time.getSecond()));
                        if (percent == 100){
                            overlay.animate().alpha(0).setDuration(400).start();
                            if (overlay.getAlpha() == 0){
                                overlay.setVisibility(View.GONE);
                            }
                            new CountDownTimer(videoView.getDuration(),1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    beginning.setText("00:"+millisUntilFinished/1000);
                                }

                                @Override
                                public void onFinish() {

                                }
                            }.start();
                            player_container.setVisibility(View.VISIBLE);
                            videoViewContainer.setVisibility(View.VISIBLE);
                            videoView.start();
                            container.setVisibility(View.GONE);
                            if (videoView.getDuration() == originDuration){
                                container.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected Void doInBackground(Void... voids) {
        videoView.setClipToOutline(true);
        videoView.requestFocus();
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
    }
}
