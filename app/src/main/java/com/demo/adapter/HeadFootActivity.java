package com.demo.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.demo.adapter.adapter.HeadFootAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.animation.BaseAnimation;

public class HeadFootActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HeadFootAdapter headFootAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_headfoot);
        mRecyclerView = findViewById(R.id.headfoot_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();

        View headerView = getLayoutInflater().inflate(R.layout.activity_headfoot_head, (ViewGroup) mRecyclerView.getParent(), false);
        headFootAdapter.addHead(headerView);
        headerView.findViewById(R.id.head_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View headerView = getLayoutInflater().inflate(R.layout.activity_headfoot_head, (ViewGroup) mRecyclerView.getParent(), false);
                headFootAdapter.addHead(headerView);
            }
        });


        View footerView = getLayoutInflater().inflate(R.layout.activity_headfoot_foot, (ViewGroup) mRecyclerView.getParent(), false);
        headFootAdapter.addFoot(footerView, 0);
        footerView.findViewById(R.id.foot_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View footerView = getLayoutInflater().inflate(R.layout.activity_headfoot_foot, (ViewGroup) mRecyclerView.getParent(), false);
                headFootAdapter.addFoot(footerView, 0);
            }
        });

        mRecyclerView.setAdapter(headFootAdapter);

    }

    private void initAdapter() {
        headFootAdapter = new HeadFootAdapter();
        headFootAdapter.setLoadAnimation(BaseAnimation.ALPHAIN);
        mRecyclerView.setAdapter(headFootAdapter);
    }
}