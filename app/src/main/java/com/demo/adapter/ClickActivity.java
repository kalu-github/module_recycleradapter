package com.demo.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 点击事件
 * created by kalu on 2018/8/22 13:14
 */
public final class ClickActivity extends AppCompatActivity {

    private final ArrayList<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);

        for (int i = 0; i < 6; i++) {
            mDatas.add(String.valueOf(i));
        }

        final RecyclerView recycler = findViewById(R.id.click_recycler);

        final BaseCommonAdapter<String> adapter = new BaseCommonAdapter<String>() {
            @Override
            protected int initItemResId() {
                return R.layout.layout_item_click;
            }

            @Override
            protected List<String> onData() {
                return mDatas;
            }

            @Override
            protected void onNext(RecyclerHolder holder, String model, final int position) {

                holder.setText(R.id.click_text, "我是第" + position + "个孩子");

                holder.setOnClickListener(R.id.click_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "点击文字, position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                holder.setOnClickListener(R.id.click_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "点击图片, position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                holder.setOnClickListener(R.id.click_ll, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "点击根布局, position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);
    }
}
