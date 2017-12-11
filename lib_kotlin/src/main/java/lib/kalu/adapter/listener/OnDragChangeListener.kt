package lib.kalu.adapter.listener

import android.support.v7.widget.RecyclerView

/**
 * description: 拖拽监听
 * created by kalu on 2017/5/26 15:14
 */
interface OnDragChangeListener {

    fun onDragStart(viewHolder: RecyclerView.ViewHolder, position: Int)

    fun onDragMove(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder, fromPosition: Int, toPosition: Int)

    fun onDragEnd(viewHolder: RecyclerView.ViewHolder, position: Int)
}