package com.demo.adapter;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.BaseLoadAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.manager.CrashLinearLayoutManager;

public class LoadmoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore);

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }

        BaseLoadAdapter<String> adapter = new BaseLoadAdapter<String>() {

            @Override
            protected int onLoad() {
                return R.layout.activity_loadmore_loading;
            }

            @Override
            protected int onView() {
                return R.layout.activity_loadmore_item;
            }

            @NonNull
            @Override
            protected List<String> onData() {
                return list;
            }

            @Override
            protected void onNext(RecyclerHolder holder, String model, int position) {
                holder.setText(R.id.loadmore_text, "第 ==> " + position + " <==个孩子");
            }

            @Override
            protected void onLoad(RecyclerHolder holder, boolean over, int page) {

                if (over)
                    return;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SystemClock.sleep(2000);
                        list.add(String.valueOf(list.size()));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.setText(R.id.loading_text, "-- 我也是有底线的 --");
                                holder.setVisible(R.id.loading_cycle, View.GONE);
                            }
                        });
                    }
                }).start();
            }
        };

        RecyclerView view = findViewById(R.id.list);
        view.setLayoutManager(new CrashLinearLayoutManager(getApplicationContext()));
        view.setAdapter(adapter);
    }
}
