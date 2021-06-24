package com.kyaw.springrevolution.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.R;
import com.kyaw.springrevolution.Utils.PlayVideo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<Post> {
    boolean is_playing = false;
    TextView desc, duration, beginning;
    ImageView display_img;
    VideoView videoView;
    CircularProgressIndicator loading_bar;
    LinearLayout player_container, loading_bar_container;
    ProgressBar loading, timer;
    LinearLayout videoView_container, overlay;

    public GridViewAdapter(@NonNull Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.material_grid_item, parent, false);
        }
        Post post = getItem(position);
        TextView detail = view.findViewById(R.id.grid_description);
        ImageView cover = view.findViewById(R.id.cover_img);
        MaterialCardView cardView = view.findViewById(R.id.card);
        detail.setText(post.getDescription());
        Picasso.get().load(post.getDisplay_image()).into(cover);

        //show bottom sheet
        cardView.setOnClickListener(v -> showBottomSheetDialog(post));

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showBottomSheetDialog(Post post) {
        MediaController controller = new MediaController(getContext());
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());

        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog, null);

        desc = bottomSheetView.findViewById(R.id.bottom_sheet_desc);
        videoView = bottomSheetView.findViewById(R.id.videoView);
        overlay = bottomSheetView.findViewById(R.id.overlay);
        player_container = bottomSheetView.findViewById(R.id.player_item_container);
        timer = bottomSheetView.findViewById(R.id.duration_bar);
        display_img = bottomSheetView.findViewById(R.id.cover_img);
        videoView_container = bottomSheetView.findViewById(R.id.videoView_container);
        loading_bar_container = bottomSheetView.findViewById(R.id.loading_bar_container);
        loading_bar = bottomSheetView.findViewById(R.id.loading_bar);
        beginning = bottomSheetView.findViewById(R.id.start_duration);
        duration = bottomSheetView.findViewById(R.id.total_duration);

        display_img.setClipToOutline(true);

        showCoverImg(post);
        if (post.getVideo_url().length() > 0) {
            hideCoverImg();
        } else {
            showCoverImg(post);
        }

        //play video
        PlayVideo playVideo = new PlayVideo(post.getVideo_url(), videoView, timer, getContext(), loading_bar, loading_bar_container, videoView_container, player_container, duration, overlay,beginning);

        if (post.getVideo_url().length() != 0) {
            playVideo.execute();
        }
        desc.setText(post.getDescription());

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void hideCoverImg() {
        videoView.setVisibility(View.VISIBLE);
        display_img.setVisibility(View.GONE);
    }

    private void showCoverImg(Post post) {
        videoView.setVisibility(View.GONE);
        Picasso.get().load(post.getDisplay_image()).into(display_img);
    }

    private void updateTime(int second, TextView beginning) {
        Handler handler = new Handler();
        Runnable updater = null;
        updater = new Runnable() {
            @Override
            public void run() {
                beginning.setText(String.valueOf(second));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updater);
    }

}
