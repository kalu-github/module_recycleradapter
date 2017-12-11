package lib.kalu.adapter.callback

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

import lib.kalu.adapter.BaseLoadSwipeDragAdapter
import lib.kalu.adapter.holder.RecyclerHolder

import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE

/**
 * description: 拖拽
 * created by kalu on 2017/5/26 12:05
 */
class DragCallback(private val mAdapter: BaseLoadSwipeDragAdapter<*, *>) : ItemTouchHelper.Callback() {

    // 拖拽方向
    private var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    /**
     * 1.左右(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     * 2.上下(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
     * 3.上下左右(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     */
    fun setDragFlags(dragFlags: Int) {
        this.dragFlags = dragFlags
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return mAdapter.isItemSwipeEnable
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemDragStart(viewHolder)
            viewHolder!!.itemView.setTag(DRAG_ID_TAG, true)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (isViewCreateByAdapter(viewHolder)) {
            return
        }

        if (viewHolder.itemView.getTag(DRAG_ID_TAG) != null && viewHolder.itemView.getTag(DRAG_ID_TAG) as Boolean) {
            mAdapter.onItemDragEnd(viewHolder)
            viewHolder.itemView.setTag(DRAG_ID_TAG, false)
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        val result = isViewCreateByAdapter(viewHolder)
        return ItemTouchHelper.Callback.makeMovementFlags(if (result) ACTION_STATE_IDLE else dragFlags, ACTION_STATE_IDLE)
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return if (source.itemViewType != target.itemViewType) {
            false
        } else {
            true
        }
    }

    override fun onMoved(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y)
        mAdapter.onItemDragMove(source, target)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (!isViewCreateByAdapter(viewHolder)) {
            mAdapter.onSwipeRemove(viewHolder)
        }
    }

    private fun isViewCreateByAdapter(viewHolder: RecyclerView.ViewHolder?): Boolean {

        if (null == viewHolder) return false

        when (viewHolder.itemViewType) {
            RecyclerHolder.HEAD_VIEW, RecyclerHolder.LOAD_VIEW, RecyclerHolder.FOOT_VIEW, RecyclerHolder.NULL_VIEW -> return true
            else -> return false
        }
    }

    companion object {

        val DRAG_ID_TAG = DragCallback::class.java.hashCode()
    }
}
