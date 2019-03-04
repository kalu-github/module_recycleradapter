package com.demo.adapter.adapter;

import com.demo.adapter.R;
import com.demo.adapter.data.DataServer;
import com.demo.adapter.entity.Status;

import java.util.List;

import androidx.annotation.NonNull;
import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class EmptyAdapter extends BaseCommonAdapter<Status> {

    @Override
    protected int onView() {
        return R.layout.activity_empty_item;
    }

    @NonNull
    @Override
    protected List<Status> onData() {
        return DataServer.getSampleData(0);
    }

    @Override
    protected void onNext(RecyclerHolder helper, Status item, int position) {

        helper.setText(R.id.empty_text, "我是第 => " + position + " <= 个孩子");
    }
}