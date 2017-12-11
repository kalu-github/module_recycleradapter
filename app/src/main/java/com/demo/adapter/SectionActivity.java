package com.demo.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.demo.adapter.adapter.LoadTabAdapter;
import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.MyTab;

import java.util.List;

public class SectionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<MyTab> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mData = DataServer.getSampleData();
        LoadTabAdapter sectionAdapter = new LoadTabAdapter(R.layout.activity_section_item, R.layout.activity_section_head, mData);
        mRecyclerView.setAdapter(sectionAdapter);
    }
}