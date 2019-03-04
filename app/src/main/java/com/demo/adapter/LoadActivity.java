package com.demo.adapter;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.BaseLoadAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 加载更多
 * created by kalu on 2018/8/22 13:15
 */
public final class LoadActivity extends AppCompatActivity {

    private final ArrayList<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        insertData();
        final RecyclerView recycler = findViewById(R.id.recycler_load);

        final BaseLoadAdapter<String> adapter = new BaseLoadAdapter<String>() {

            @Override
            protected int initLoadResId() {
                return R.layout.layout_item_load;
            }

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
                holder.setText(R.id.simple_text, "我是第" + position + "个孩子");
            }

            @Override
            protected void onLoad(RecyclerHolder holder, boolean isOver) {
                Log.e("kalu", "onLoad ==> isOver = " + isOver);

                holder.setText(R.id.load_text, isOver ? "没有更多了" : "加载更多...");
                holder.setVisible(R.id.load_bar, isOver ? View.INVISIBLE : View.VISIBLE);

                if (isOver || mDatas.size() > 5)
                    return;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SystemClock.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                insertData();

                                if (mDatas.size() > 5) {
                                    loadOverDataSetChanged(recycler);
                                } else {
                                    loadSuccDataSetChanged(recycler);
                                }
                            }
                        });
                    }
                }).start();
            }
        };

        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);

        final SwipeRefreshLayout swipe = findViewById(R.id.swipe_load);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();
                swipe.setRefreshing(false);
                adapter.loadResetDataSetChanged(recycler);
            }
        });
    }

    private final void insertData() {

        for (int i = 0; i < 5; i++) {
            mDatas.add(String.valueOf(i));
        }
    }

    private final void refreshData() {

        mDatas.clear();
        for (int i = 0; i < 5; i++) {
            mDatas.add(String.valueOf(i));
        }
    }
}
