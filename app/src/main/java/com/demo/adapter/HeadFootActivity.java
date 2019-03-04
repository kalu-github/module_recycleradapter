package com.demo.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 头尾
 * created by kalu on 2018/8/22 13:59
 */
public final class HeadFootActivity extends AppCompatActivity {

    private final ArrayList<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_foot);

        mDatas.add("");

        final RecyclerView recycler = findViewById(R.id.section_recycler);
        final BaseCommonAdapter<String> adapter = new BaseCommonAdapter<String>() {

            @Override
            protected int initItemResId() {
                return R.layout.layout_item_simple;
            }

            @Override
            protected List<String> onData() {
                return mDatas;
            }

            @Override
            protected void onNext(RecyclerHolder holder, String model, int position) {
                holder.setText(R.id.simple_text, String.valueOf(position));
            }
        };

        recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recycler.setAdapter(adapter);

        findViewById(R.id.section_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View head = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_item_head, null);
                adapter.addHead(head);
            }
        });

        findViewById(R.id.section_foot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View foot = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_item_foot, null);
                adapter.addFoot(foot);
            }
        });
    }
}
