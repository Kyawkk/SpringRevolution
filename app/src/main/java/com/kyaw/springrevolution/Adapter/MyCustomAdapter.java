package com.kyaw.springrevolution.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.kyaw.springrevolution.DetailActivity;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder> {
    int lastPosition = 0;
    String webUrl = "https://cms.myanmarwitness.info/wp-json/v1/post?posts_per_page=300&date[0]=";
    private ArrayList<Post> posts;
    private Context context;

    public MyCustomAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomAdapter.MyViewHolder holder, int position) {
        holder.desc.setText(posts.get(position).getDescription());
        holder.date.setText(posts.get(position).getHuman_date());
        setAnimation(holder.container, position);
        String url = posts.get(position).getDisplay_image();
        Picasso.get().load(url).into(holder.display);

        //item click action
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            String link;

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                try {
                    link = webUrl.concat(posts.get(position).getHuman_date());
                } catch (NullPointerException exception) {
                    exception.getCause();
                }
                intent.putExtra("date", posts.get(position).getHuman_date());
                intent.putExtra("url", link);
                context.startActivity(intent);
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            lastPosition = position;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_content);
            viewToAnimate.startAnimation(animation);
        }
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView display;
        public TextView desc, date;
        public MaterialCardView cardView;
        public LinearLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            display = itemView.findViewById(R.id.display_image);
            desc = itemView.findViewById(R.id.description);
            cardView = itemView.findViewById(R.id.item_card);
            date = itemView.findViewById(R.id.human_date);
            container = itemView.findViewById(R.id.layout_container);
        }
    }
}
