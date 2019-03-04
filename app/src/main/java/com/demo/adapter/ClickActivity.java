package com.demo.adapter;

import android.os.Bundle;
import android.util.Log;

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
            protected void onNext(RecyclerHolder holder, String model, int position) {
                Log.e("kalu", "position = " + position);
            }

            @Override
            protected void onRoll(boolean roll, boolean loadmore) {
                Log.e("kalu", "roll = " + roll + ", loadmore = " + loadmore);
            }
        };

        RecyclerView view = findViewById(R.id.list);
        view.addItemDecoration(new SpaceDecoration(10f));
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        view.setAdapter(adapter);
    }
}
