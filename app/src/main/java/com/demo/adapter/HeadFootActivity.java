package com.demo.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.demo.adapter.adapter.HeadFootAdapter;

import lib.kalu.adapter.animation.BaseAnimation;

public class HeadFootActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HeadFootAdapter headFootAdapter;
    private static final int PAGE_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_headfoot);
        mRecyclerView = findViewById(R.id.headfoot_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();

        View headerView = getLayoutInflater().inflate(R.layout.activity_headfoot_head, (ViewGroup) mRecyclerView.getParent(), false);
        headFootAdapter.addHeadView(headerView);
        headerView.findViewById(R.id.head_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View headerView = getLayoutInflater().inflate(R.layout.activity_headfoot_head, (ViewGroup) mRecyclerView.getParent(), false);
                headFootAdapter.addHeadView(headerView);
            }
        });


        View footerView = getLayoutInflater().inflate(R.layout.activity_headfoot_foot, (ViewGroup) mRecyclerView.getParent(), false);
        headFootAdapter.addFootView(footerView, 0);
        footerView.findViewById(R.id.foot_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View footerView = getLayoutInflater().inflate(R.layout.activity_headfoot_foot, (ViewGroup) mRecyclerView.getParent(), false);
                headFootAdapter.addFootView(footerView, 0);
            }
        });

        mRecyclerView.setAdapter(headFootAdapter);

    }

    private void initAdapter() {
        headFootAdapter = new HeadFootAdapter(PAGE_SIZE);
        headFootAdapter.setLoadAnimation(BaseAnimation.ALPHAIN, 500, true);
        mRecyclerView.setAdapter(headFootAdapter);
    }
}