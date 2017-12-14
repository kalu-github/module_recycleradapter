package com.demo.adapter.adapter;

import com.demo.adapter.R;
import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.Status;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class HeadFootAdapter extends BaseCommonAdapter<Status> {

    public HeadFootAdapter(int dataSize) {
        super(DataServer.getSampleData(dataSize), R.layout.activity_headfoot_item);
    }

    @Override
    protected void onNext(RecyclerHolder helper, Status item, int position) {
        helper.setText(R.id.headfoot_text, "我是第 => " + position + " <= 个孩子");
    }
}
