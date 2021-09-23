package com.demo.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.recyclerview.adapter.BaseCommonAdapter;
import lib.kalu.recyclerview.itemdecoration.FloatDecoration;
import lib.kalu.recyclerview.viewholder.RecyclerHolder;
import lib.kalu.recyclerview.manager.CrashLinearLayoutManager;

public class FloatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }

        BaseCommonAdapter<String> adapter = new BaseCommonAdapter<String>() {

            @Override
            protected int onView() {
                return R.layout.activity_float_item;
            }

            @NonNull
            @Override
            protected List<String> onData() {
                return list;
            }

            @Override
            protected void onNext(RecyclerHolder holder, String model, final int position) {

                holder.setText(R.id.sticky_text, "第 ==> " + position + " <==个孩子");
            }
        };

        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_float_menu, null);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击", Toast.LENGTH_LONG).show();
            }
        });
        FloatDecoration floatDecoration = new FloatDecoration();
        floatDecoration.setDecoration(inflate);

        final RecyclerView view = findViewById(R.id.list);
        view.addItemDecoration(floatDecoration);
        view.setLayoutManager(new CrashLinearLayoutManager(getApplicationContext()));
        view.setAdapter(adapter);
    }
}
