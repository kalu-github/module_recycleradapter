package lib.kalu.adapter.listener;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * description: 侧滑监听
 * created by kalu on 2017/5/26 15:14
 */
public interface OnSwipeChangeListener {

    /**
     * 侧滑删除
     */
    void onSwipeRemove(RecyclerView.ViewHolder viewHolder, int position);

    /**
     * 侧滑结束
     */
    void onSwipeEnd(RecyclerView.ViewHolder viewHolder, boolean isRemove, int position);

    /**
     * 侧滑开始
     */
    void onSwipeStart(RecyclerView.ViewHolder viewHolder, int position);

    /**
     * 侧滑移动
     */
    void onSwipeMove(Canvas canvas, RecyclerView.ViewHolder viewHolder, float moveX, float moveY, boolean isCurrentlyActive, boolean isSwipeLeft);
}
