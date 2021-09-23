package com.demo.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.Status;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.recyclerview.adapter.BaseCommonAdapter;
import lib.kalu.recyclerview.viewholder.RecyclerHolder;

public class EmptyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private View nullView;
    private ArrayList<Status> arrayList = new ArrayList<>();

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
                        List<Status> sampleData = DataServer.getSampleData(10);
                        arrayList.addAll(sampleData);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        BaseCommonAdapter mEmptyAdapter = new BaseCommonAdapter<Status>() {
            @Override
            protected int onView() {
                return R.layout.activity_empty_item;
            }

            @NonNull
            @Override
            protected List<Status> onData() {
                return arrayList;
            }

            @Override
            protected void onNext(RecyclerHolder helper, Status item, int position) {
                helper.setText(R.id.empty_text, "我是第 => " + position + " <= 个孩子");
            }
        };
        mEmptyAdapter.setEmptyView(nullView);
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
