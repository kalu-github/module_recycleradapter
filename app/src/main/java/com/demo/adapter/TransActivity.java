package com.demo.adapter;

import android.os.Bundle;

import com.demo.adapter.adapter.TransAdapter;
import com.demo.adapter.entity.Level0Item;
import com.demo.adapter.entity.Level1Item;
import com.demo.adapter.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.model.MultModel;

/**
 * description: 展开合并
 * created by kalu on 2017/12/8 9:07
 */
public class TransActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TransAdapter adapter;
    ArrayList<MultModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_item_use);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        list = generateData();
        adapter = new TransAdapter() {

            @NonNull
            @Override
            protected List<MultModel> onData() {
                return list;
            }
        };

        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == TransAdapter.TYPE_PERSON ? 1 : manager.getSpanCount();
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
        adapter.expandAll();
    }

    private ArrayList<MultModel> generateData() {
        int lv0Count = 3;
        int lv1Count = 3;
        int personCount = 3;

        String[] nameList = {"三级", "三级", "三级", "三级", "三级"};
        Random random = new Random();

        ArrayList<MultModel> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            Level0Item lv0 = new Level0Item("一级", "二级");
            for (int j = 0; j < lv1Count; j++) {
                Level1Item lv1 = new Level1Item("二级", "三级");
                for (int k = 0; k < personCount; k++) {
                    lv1.addModel(new Person(nameList[k], random.nextInt(40)));
                }
                lv0.addModel(lv1);
            }
            res.add(lv0);
        }
        return res;
    }
}
