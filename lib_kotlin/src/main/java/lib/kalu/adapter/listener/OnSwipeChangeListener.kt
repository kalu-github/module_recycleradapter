package lib.kalu.adapter.listener

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView

/**
 * description: 侧滑监听
 * created by kalu on 2017/5/26 15:14
 */
interface OnSwipeChangeListener {

    /**
     * 侧滑删除
     */
    fun onSwipeRemove(viewHolder: RecyclerView.ViewHolder, position: Int)

    /**
     * 侧滑结束
     */
    fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder, isRemove: Boolean, position: Int)

    /**
     * 侧滑开始
     */
    fun onSwipeStart(viewHolder: RecyclerView.ViewHolder, position: Int)

    /**
     * 侧滑移动
     */
    fun onSwipeMove(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, moveX: Float, moveY: Float, isCurrentlyActive: Boolean, isSwipeLeft: Boolean)
}
