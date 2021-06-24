package com.kyaw.springrevolution.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.R;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    private final ArrayList<Post> posts;

    private MediaController controller;
    private final Context context;
    private boolean is_playing = false;
    private Activity activity;

    public DetailAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    public DetailAdapter(ArrayList<Post> posts, Context context,Activity activity) {
        this.posts = posts;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.detail_item, parent, false);
        return new DetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.MyViewHolder holder, int position) {
        controller = new MediaController(context);
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        controller.setAnchorView(holder.videoView);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        //play video
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_playing) {
                    holder.play.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                    holder.videoView.pause();
                } else {
                    holder.play.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_pause_24));
                    holder.videoView.start();
                }
                is_playing = !is_playing;
            }
        });

        //set fullscreen
        holder.full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreen(holder.videoView);
            }
        });

        if (posts.get(position).getVideo_url().length() > 0) {
            holder.duration.setText(String.valueOf(holder.videoView.getDuration()));
            Uri uri = Uri.parse(posts.get(position).getVideo_url());
            System.out.println("url: "+uri.toString());
            holder.videoView.setVideoURI(uri);
            holder.videoView.setMediaController(controller);
            holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    switch (extra){
                        case MediaPlayer.MEDIA_ERROR_IO:
                            holder.videoView.requestFocus();
                            holder.videoView.start();
                    }
                    return true;
                }
            });
            holder.videoView.requestFocus();
        } else {
            holder.container.setVisibility(View.GONE);
        }
        holder.desc.setText(posts.get(position).getDescription());
    }

    private void fullScreen(VideoView videoView){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public ImageView play,full_screen;
        public RelativeLayout container;
        public TextView desc,duration;
        public ProgressBar progressBar,duration_progress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            play = itemView.findViewById(R.id.play_button);
            progressBar = itemView.findViewById(R.id.loading);
            container = itemView.findViewById(R.id.videoView_container);
            desc = itemView.findViewById(R.id.detail_description);
            duration = itemView.findViewById(R.id.duration_text);
            duration_progress = itemView.findViewById(R.id.duration_bar);
            full_screen = itemView.findViewById(R.id.full_screen);
        }
    }
}
