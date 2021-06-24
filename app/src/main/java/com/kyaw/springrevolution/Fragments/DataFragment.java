package com.kyaw.springrevolution.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.kyaw.springrevolution.Adapter.MyCustomAdapter;
import com.kyaw.springrevolution.Interface.DataLoadingListener;
import com.kyaw.springrevolution.Interface.RecyclerViewScrollListener;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.R;
import com.kyaw.springrevolution.Utils.LoadData;

import java.util.ArrayList;

public class DataFragment extends Fragment implements DataLoadingListener {
    private ArrayList<ArrayList<Post>> posts = new ArrayList<>();
    private MyCustomAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout dataLoadingContainer;
    private LinearProgressIndicator dataLoading;
    private RecyclerViewScrollListener tabLayoutListener = null;
    private CoordinatorLayout snackBarContainer;

    public DataFragment() {
    }

    public DataFragment(RecyclerViewScrollListener tabLayoutListener) {
        this.tabLayoutListener = tabLayoutListener;
    }

    @Override
    public void onActivityCreated(@Nullable  Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.tabLayoutListener = (RecyclerViewScrollListener) getActivity();
        LoadData loadData = new LoadData(this::onFinished);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment, container, false);
        recyclerView = view.findViewById(R.id.data_recycler_rv);
        snackBarContainer = view.findViewById(R.id.snackbar_container);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        LoadData loadData = new LoadData(this::onFinished);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData.execute();
            }
        });


        //retrieve data from adapter
        ArrayList<Post> post = (ArrayList<Post>) getArguments().getSerializable("data");

        if (post.size() > 0){
            refreshLayout.setRefreshing(false);
        }

        adapter = new MyCustomAdapter(post, getContext());
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (tabLayoutListener != null) {
                    tabLayoutListener.onScroll(dy);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showSnackBar(View view) {
        Snackbar snackbar = Snackbar.make(view,
                "Hello",
                BaseTransientBottomBar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    @Override
    public void onFinished() {/*
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_to_top);
        recyclerView.startAnimation(animation);
        dataLoadingContainer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);*/
    }
}
