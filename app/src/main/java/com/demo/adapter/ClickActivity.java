package com.demo.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.decoration.SpaceDecoration;
import lib.kalu.adapter.holder.RecyclerHolder;

public class ClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }

        final int[] i = {1};

        BaseCommonAdapter<String> adapter = new BaseCommonAdapter<String>() {

            @Override
            protected int onView() {
                return R.layout.activity_click_item;
            }

            @NonNull
            @Override
            protected List<String> onData() {
                return list;
            }

            @Override
            protected void onHolder(final RecyclerView.LayoutManager manager, final RecyclerHolder holder, int type) {

                Log.e("holder", "onHolder = i = "+ i[0]);
                ++i[0];

                holder.getView(R.id.click_text1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = manager.getPosition(holder.itemView);
                        Log.e("holder", "position = " + position);
                    }
                });
            }

            @Override
            protected void onNext(RecyclerHolder holder, String model, int position) {
                Log.e("kalu", "position = " + position);
            }

            @Override
            protected void onScroll(int horizontalOffset, int verticalOffset) {
                super.onScroll(horizontalOffset, verticalOffset);
            }
        };

        RecyclerView view = findViewById(R.id.list);
        view.addItemDecoration(new SpaceDecoration(10f));
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(adapter);
    }
}
