package com.demo.adapter.adapter;

import android.graphics.Canvas;

import com.demo.adapter.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.BaseCommonSwipeDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public abstract class ItemDragAdapter extends BaseCommonSwipeDragAdapter<String> {

    @Override
    protected int onView() {
        return R.layout.item_draggable_view;
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

    //
    @Override
    protected void onDragStart(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onDragMove(RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target, int fromPosition, int toPosition) {

    }

    @Override
    protected void onDragEnd(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onSwipeRemove(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    protected void onSwipeEnd(RecyclerView.ViewHolder holder, boolean isRemove, int position) {
    }

    @Override
    protected void onSwipeStart(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    protected void onSwipeMove(RecyclerView.ViewHolder holder, Canvas canvas, float moveX, float moveY, boolean isCurrentlyActive, boolean isSwipeLeft) {
        canvas.drawColor(ContextCompat.getColor(holder.itemView.getContext().getApplicationContext(), R.color.color_light_blue));
    }
}
