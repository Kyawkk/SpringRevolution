package com.kyaw.springrevolution;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.kyaw.springrevolution.Adapter.MyCustomAdapter;
import com.kyaw.springrevolution.Adapter.MyFragmentPagerAdapter;
import com.kyaw.springrevolution.Fragments.DataFragment;
import com.kyaw.springrevolution.Interface.RecyclerViewScrollListener;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.Network.CheckInternet;
import com.kyaw.springrevolution.Utils.Constants;
import com.kyaw.springrevolution.Utils.LoadData;
import com.kyaw.springrevolution.Utils.dataCallBack;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewScrollListener,dataCallBack {
    private static final int HEADER_HIDE_ANIM_DURATION = 400;
    RecyclerView recyclerView;
    LoadData loadData;
    private final ArrayList<Post> posts = new ArrayList<>();
    private MyCustomAdapter adapter;
    public TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout progressBarContainer;
    private LinearProgressIndicator loading_progress;
    private MaterialToolbar toolbar;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout appBarContainer;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager2);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbarLayout);
        loading_progress = findViewById(R.id.loading_progress);
        progressBarContainer = findViewById(R.id.loading_progress_container);
        appBarContainer = findViewById(R.id.appBarContainer);

        //setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getString(R.string.app_name));

        //tabLayout listener
        DataFragment fragment = new DataFragment(this::onScroll);

        loadData = new LoadData(this, this::onReceived, Constants.MAIN_URL,progressBarContainer,loading_progress);
        checkInternet();
    }

    private void checkInternet() {
        CheckInternet internet = new CheckInternet(this);
        if (internet.isInternetOn()) {
            getData();
        } else {
            showInternetNotAvailableDialog();
        }
    }

    private void showInternetNotAvailableDialog() {
        Dialog dialog = new Dialog(this,R.style.FullWidth_Dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.no_internet_connection_dialog, null);
        MaterialButton tryAgainBtn = view.findViewById(R.id.try_again);

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableInternet();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.show();
    }

    private void enableInternet() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    //get data from activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkInternet();
    }

    private void getData() {
        if (loadData != null) {
            loadData.execute();
        }
    }

    @Override
    public void onReceived(ArrayList<Post> posts) {
        tabLayout.setVisibility(View.VISIBLE);
        if (posts != null) {
            adapter = new MyCustomAdapter(posts, MainActivity.this);
        } else {
            Dialog dialog = new Dialog(this);
            dialog.setTitle("No Data found!");
            dialog.show();
        }
        adapter.notifyDataSetChanged();

        ArrayList<String> months = new ArrayList<>();
        months.addAll(Constants.getMonthList(posts));

        for (int i = 0; i < months.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(months.get(i)));
        }

        //viewpager
        final MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(this, tabLayout.getTabCount(), getSupportFragmentManager(), Constants.getPostsListInMonth(posts));
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onScroll(int dy) {
        /*if (dy<0){
            toolbar.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        }
        else {
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .alpha(0)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        }*/
    }
}