package com.demo.adapter;

import android.os.Bundle;

import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.MySection;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.BaseCommonSectionAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

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
        BaseCommonSectionAdapter sectionAdapter = new BaseCommonSectionAdapter() {
            @Override
            protected int onView() {
                return R.layout.activity_section_item;
            }

            @Override
            protected int onHead() {
                return R.layout.activity_section_head;
            }

            @NonNull
            @Override
            protected List onData() {
                return mData;
            }

            @Override
            protected void onNext(RecyclerHolder holder, Object model, int position) {

            }
        };
        mRecyclerView.setAdapter(sectionAdapter);
    }
}