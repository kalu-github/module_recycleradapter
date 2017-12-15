package com.demo.adapter.adapter;

import com.demo.adapter.entity.MySection;

import java.util.List;

import lib.kalu.adapter.BaseCommonSectionAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class LoadSectionAdapter extends BaseCommonSectionAdapter<MySection> {

    public LoadSectionAdapter(List data, int layoutResId, int sectionResId) {
        super(data, layoutResId, sectionResId);
    }

    @Override
    protected void onNext(RecyclerHolder helper, MySection item, int position) {
        // helper.setText(R.id.tv, item.getTabName());
    }

    @Override
    protected void onSection(RecyclerHolder holder, int position) {

    }
}
