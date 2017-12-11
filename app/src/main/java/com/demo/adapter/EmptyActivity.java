package com.demo.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.demo.adapter.adapter.EmptyAdapter;
import com.demo.adapter.data.DataServer;

public class EmptyActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private EmptyAdapter mEmptyAdapter;
    private View notDataView;
    private View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        mRecyclerView = findViewById(R.id.empty_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notDataView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        errorView = getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        initAdapter();
        onRefresh();
    }

    private void initAdapter() {
        mEmptyAdapter = new EmptyAdapter(0);
        mRecyclerView.setAdapter(mEmptyAdapter);
    }

    @Override
    public void onClick(View v) {
        mError = true;
        mNoData = true;
        mEmptyAdapter.clearAddData(null);
        onRefresh();
    }

    private boolean mError = true;
    private boolean mNoData = true;

    private void onRefresh() {
        mEmptyAdapter.setNullView(getApplicationContext(), R.layout.activity_empty_null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mError) {
                    mEmptyAdapter.setNullView(errorView);
                    mError = false;
                } else {
                    if (mNoData) {
                        mEmptyAdapter.setNullView(notDataView);
                        mNoData = false;
                    } else {
                        mEmptyAdapter.clearAddData(DataServer.getSampleData(10));
                    }
                }
            }
        }, 1000);
    }
}
