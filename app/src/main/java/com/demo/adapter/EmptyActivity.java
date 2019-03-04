package com.demo.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.adapter.adapter.EmptyAdapter;
import com.demo.adapter.data.DataServer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EmptyAdapter mEmptyAdapter;
    private View nullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        mRecyclerView = findViewById(R.id.empty_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        nullView = getLayoutInflater().inflate(R.layout.activity_empty_null, (ViewGroup) mRecyclerView.getParent(), false);
        nullView.findViewById(R.id.empty_text).setClickable(false);
        nullView.findViewById(R.id.empty_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TextView) nullView.findViewById(R.id.empty_text)).setText("加载数据");
                nullView.findViewById(R.id.empty_circle).setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mEmptyAdapter.clearAddData(DataServer.getSampleData(10));
                    }
                }, 2000);
            }
        });

        mEmptyAdapter = new EmptyAdapter();
        mEmptyAdapter.setNullView(nullView);
        mRecyclerView.setAdapter(mEmptyAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nullView.findViewById(R.id.empty_text).setClickable(true);
                ((TextView) nullView.findViewById(R.id.empty_text)).setText("加载失败, 点击重试");
                nullView.findViewById(R.id.empty_circle).setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }
}
