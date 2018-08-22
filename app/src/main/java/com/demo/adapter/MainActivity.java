package com.demo.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public final class MainActivity extends AppCompatActivity {

    private final List<String> mDatas = Arrays.asList("点击事件", "下拉刷新, 上拉加载", "分组", "头部尾部", "多种布局", "空布局", "悬浮菜单", "TabMore", "Tab", "侧滑拖动", "分组折叠");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recycler = findViewById(R.id.main_recycler);

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
            protected void onNext(RecyclerHolder holder, String model, final int position) {

                holder.setText(R.id.simple_text, model);

                holder.setOnClickListener(R.id.simple_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Intent intent = new Intent();
                        switch (position) {
                            case 0:
                                intent.setClass(getApplicationContext(), ClickActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent.setClass(getApplicationContext(), LoadActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent.setClass(getApplicationContext(), SectionActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }
        };
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);
    }
}
