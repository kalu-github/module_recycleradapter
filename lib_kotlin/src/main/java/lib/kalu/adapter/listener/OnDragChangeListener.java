package lib.kalu.adapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 * description: 拖拽监听
 * created by kalu on 2017/5/26 15:14
 */
public interface OnDragChangeListener {

    void onDragStart(RecyclerView.ViewHolder viewHolder, int position);

    void onDragMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target, int fromPosition, int toPosition);

    void onDragEnd(RecyclerView.ViewHolder viewHolder, int position);
}