package com.kyaw.springrevolution.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kyaw.springrevolution.Fragments.DataFragment;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.Utils.Constants;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ArrayList<ArrayList<Post>> posts;
    int totalTabs;


    public MyFragmentPagerAdapter(Context context, int totalTabs, @NonNull FragmentManager fm, ArrayList<ArrayList<Post>> posts) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
        this.posts = posts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        System.out.println("fragment position: "  + position);
        //DataFragment fragment = new DataFragment(Constants.getPostsListInMonth(posts),position);
        DataFragment fragment = newInstance(posts.get(position));
        return fragment;
    }

    public static DataFragment newInstance(ArrayList<Post> post){
        DataFragment fragment = new DataFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", post);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
