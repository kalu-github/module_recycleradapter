package com.demo.adapter.adapter;

import com.demo.adapter.entity.MyTab;

import java.util.List;

import lib.kalu.adapter.BaseCommonTabAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class LoadTabAdapter extends BaseCommonTabAdapter<MyTab, RecyclerHolder> {

    public LoadTabAdapter(int layoutResId, int sectionResId, List data) {
        super(data, layoutResId, sectionResId);
    }

    @Override
    protected void onNext(RecyclerHolder helper, MyTab item, int position) {
       // helper.setText(R.id.tv, item.getTabName());
    }

    @Override
    protected void onSection(int position) {

    }
}
