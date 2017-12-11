package lib.kalu.adapter.callback

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View

import lib.kalu.adapter.BaseLoadSwipeDragAdapter
import lib.kalu.adapter.holder.RecyclerHolder

import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE

/**
 * description: 侧滑
 * created by kalu on 2017/5/26 13:34
 */
class SwipeCallback(private val mAdapter: BaseLoadSwipeDragAdapter<*, *>) : ItemTouchHelper.Callback() {

    private var moveFlags = ItemTouchHelper.START

    /**
     * 1.右侧滑动(ItemTouchHelper.START)
     * 2.左侧滑动(ItemTouchHelper.END)
     * 3.两边滑动(ItemTouchHelper.START | ItemTouchHelper.END)
     */
    fun setMoveFlags(moveFlags: Int) {
        this.moveFlags = moveFlags
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return mAdapter.isItemSwipeEnable
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeStart(viewHolder)
            viewHolder!!.itemView.setTag(SWIPE_ID_TAG, true)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (isViewCreateByAdapter(viewHolder)) {
            return
        }

        //        if (viewHolder.itemView.getTag(SWIPE_ID_TAG) != null
        //                && (Boolean) viewHolder.itemView.getTag(SWIPE_ID_TAG)) {
        //            mAdapter.onSwipeRemove(viewHolder);
        //            viewHolder.itemView.setTag(SWIPE_ID_TAG, false);
        //        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        val result = isViewCreateByAdapter(viewHolder)
        return ItemTouchHelper.Callback.makeMovementFlags(ACTION_STATE_IDLE, if (result) 0 else moveFlags)
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

        val result = source.itemViewType != target.itemViewType
        return !result
    }

    override fun onMoved(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y)
        mAdapter.onItemDragMove(source, target)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (!isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeEnd(viewHolder)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                 dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder)) {
            val itemView = viewHolder.itemView

            c.save()
            if (dX > 0) {
                c.clipRect(itemView.left.toFloat(), itemView.top.toFloat(),
                        itemView.left + dX, itemView.bottom.toFloat())
                c.translate(itemView.left.toFloat(), itemView.top.toFloat())
            } else {
                c.clipRect(itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat())
                c.translate(itemView.right + dX, itemView.top.toFloat())
            }

            mAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive)
            c.restore()
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

        val SWIPE_ID_TAG = SwipeCallback::class.java.hashCode()
    }
}
