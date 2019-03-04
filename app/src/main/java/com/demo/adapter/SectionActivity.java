package com.demo.adapter;

import android.os.Bundle;
import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.MySection;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SectionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<MySection> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mData = DataServer.getSampleData();
//        LoadSectionAdapter sectionAdapter = new LoadSectionAdapter(mData, R.layout.activity_section_item, R.layout.activity_section_head);
//        mRecyclerView.setAdapter(sectionAdapter);
    }
}