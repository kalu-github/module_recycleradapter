package com.demo.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.BaseCommonSectionAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.SectionModel;

/**
 * description: 分组
 * created by kalu on 2018/8/22 13:15
 */
public final class SectionActivity extends AppCompatActivity {

    private final ArrayList<SectionModel> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        insertData();

        final RecyclerView recycler = findViewById(R.id.section_recycler);
        final BaseCommonSectionAdapter<SectionModel> adapter = new BaseCommonSectionAdapter<SectionModel>() {
            @Override
            protected int initSectionResId() {
                return R.layout.layout_item_section;
            }

            @Override
            protected int initItemResId() {
                return R.layout.layout_item_simple;
            }

            @Override
            protected List<SectionModel> onData() {
                return mDatas;
            }

            @Override
            protected void onSection(RecyclerHolder holder, SectionModel model, int position) {
                Log.e("kalu", "onSection ==> position = " + position);
                holder.setText(R.id.section_text, model.getSection());
            }

            @Override
            protected void onNext(RecyclerHolder holder, SectionModel model, int position) {

                holder.setText(R.id.simple_text, String.valueOf(position));
            }
        };

        recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recycler.setAdapter(adapter);
    }

    private final void insertData() {

        for (int i = 0; i < 5; i++) {

            final int finalI = i;
            final SectionModel sectionModel1 = new SectionModel() {

                @Override
                public boolean isSection() {
                    return true;
                }

                @Override
                public String getSection() {
                    return "section" + finalI;
                }
            };
            mDatas.add(sectionModel1);

            for (int j = 0; j < 5; j++) {
                final SectionModel sectionModel2 = new SectionModel() {

                    @Override
                    public boolean isSection() {
                        return false;
                    }

                    @Override
                    public String getSection() {
                        return null;
                    }
                };
                mDatas.add(sectionModel2);
            }
        }
    }
}
