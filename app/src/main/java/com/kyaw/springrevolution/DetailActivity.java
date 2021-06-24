package com.kyaw.springrevolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.MediaController;

import com.google.android.material.appbar.MaterialToolbar;
import com.kyaw.springrevolution.Adapter.DetailAdapter;
import com.kyaw.springrevolution.Adapter.GridViewAdapter;
import com.kyaw.springrevolution.Adapter.MyCustomAdapter;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.Utils.Constants;
import com.kyaw.springrevolution.Utils.LoadData;
import com.kyaw.springrevolution.Utils.dataCallBack;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements dataCallBack {
    private RecyclerView recyclerView;
    private GridViewAdapter adapter;
    private GridView gridView;
    private MaterialToolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        gridView = findViewById(R.id.detail_gv);
        toolbar = findViewById(R.id.toolbar);

        String date = getIntent().getStringExtra("date");
        toolbar.setTitle(date);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String url = getIntent().getStringExtra("url");
        LoadData loadData = new LoadData(this,this::onReceived, url);
        loadData.execute();
    }

    @Override
    public void onReceived(ArrayList<Post> posts) {
        adapter = new GridViewAdapter(this,posts);
        gridView.setAdapter(adapter);
    }
}