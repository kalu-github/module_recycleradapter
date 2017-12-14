package com.demo.adapter.adapter;

import com.demo.adapter.R;

import java.util.List;

import lib.kalu.adapter.BaseCommonSwipeDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class ItemDragAdapter extends BaseCommonSwipeDragAdapter<String> {
    public ItemDragAdapter(List data) {
        super(data, R.layout.item_draggable_view);
    }

    @Override
    protected void onNext(RecyclerHolder helper, String item, int position) {
        switch (helper.getLayoutPosition() %
                3) {
            case 0:
                helper.setImageResource(R.id.iv_head, R.mipmap.ic_launcher);
                break;
            case 1:
                helper.setImageResource(R.id.iv_head, R.mipmap.ic_launcher);
                break;
            case 2:
                helper.setImageResource(R.id.iv_head, R.mipmap.ic_launcher);
                break;
        }
        helper.setText(R.id.tv, item);
    }
}
