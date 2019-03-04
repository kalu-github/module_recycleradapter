package com.demo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.adapter.model.tab.StickyActivity2;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.manager.CrashGridLayoutManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> list = Arrays.asList("点击事件", "加载更多", "头部尾部", "多种布局", "空布局", "悬浮菜单", "TabMore", "Tab", "分组", "侧滑拖动", "分组折叠");
        final List<Class<? extends AppCompatActivity>> clazzs = Arrays.asList(ClickActivity.class, LoadmoreActivity.class, HeadFootActivity.class, MulitActivity.class, EmptyActivity.class, FloatActivity.class, StickyActivity2.class, StickyActivity1.class, SectionActivity.class, DragSwipeActivity.class, TransActivity.class);

        BaseCommonAdapter<String> adapter = new BaseCommonAdapter<String>() {

            @Override
            protected int onView() {
                return R.layout.activity_main_item;
            }

            @NonNull
            @Override
            protected List<String> onData() {
                return list;
            }

            @Override
            protected void onNext(RecyclerHolder holder, String model, final int position) {

                holder.setText(R.id.main_text, model);
                holder.getView(R.id.main_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), clazzs.get(position)));
                    }
                });
            }
        };

        RecyclerView view = findViewById(R.id.list);
        view.setLayoutManager(new CrashGridLayoutManager(getApplicationContext(), 2));
        view.setAdapter(adapter);
    }
}
