package com.demo.adapter.adapter;

import com.demo.adapter.R;
import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.Status;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class EmptyAdapter extends BaseCommonAdapter<Status, RecyclerHolder> {

    public EmptyAdapter(int dataSize) {
        super(DataServer.getSampleData(dataSize), R.layout.activity_empty_item);
    }

    @Override
    protected void onNext(RecyclerHolder helper, Status item, int position) {

        helper.setText(R.id.empty_text, "我是第 => " + position + " <= 个孩子");
    }
}