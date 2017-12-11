package lib.kalu.adapter.callback

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View

import lib.kalu.adapter.BaseCommonSwipeDragAdapter
import lib.kalu.adapter.holder.RecyclerHolder

/**
 * description: 侧滑, 拖拽
 * created by kalu on 2017/5/26 14:16
 */
class SwipeDragCallback(internal var mAdapter: BaseCommonSwipeDragAdapter<*, *>) : ItemTouchHelper.Callback() {

    internal var mMoveThreshold = 0.1f
    internal var mSwipeThreshold = 0.7f

    internal var mDragMoveFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    // ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    internal var mSwipeMoveFlags = ItemTouchHelper.END

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return mAdapter.isItemSwipeEnable
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemDragStart(viewHolder)
            viewHolder!!.itemView.setTag(DragCallback.DRAG_ID_TAG, true)
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeStart(viewHolder)
            viewHolder!!.itemView.setTag(SwipeCallback.SWIPE_ID_TAG, true)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (isViewCreateByAdapter(viewHolder)) {
            return
        }

        if (viewHolder.itemView.getTag(DragCallback.DRAG_ID_TAG) != null && viewHolder.itemView.getTag(DragCallback.DRAG_ID_TAG) as Boolean) {
            mAdapter.onItemDragEnd(viewHolder)
            viewHolder.itemView.setTag(DragCallback.DRAG_ID_TAG, false)
        }
        if (viewHolder.itemView.getTag(SwipeCallback.SWIPE_ID_TAG) != null && viewHolder.itemView.getTag(SwipeCallback.SWIPE_ID_TAG) as Boolean) {
            mAdapter.onItemSwipeEnd(viewHolder)
            viewHolder.itemView.setTag(SwipeCallback.SWIPE_ID_TAG, false)
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (isViewCreateByAdapter(viewHolder)) {
            ItemTouchHelper.Callback.makeMovementFlags(0, 0)
        } else ItemTouchHelper.Callback.makeMovementFlags(mDragMoveFlags, mSwipeMoveFlags)

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

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return mMoveThreshold
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return mSwipeThreshold
    }

    fun setSwipeThreshold(swipeThreshold: Float) {
        mSwipeThreshold = swipeThreshold
    }

    fun setMoveThreshold(moveThreshold: Float) {
        mMoveThreshold = moveThreshold
    }

    fun setDragFlags(dragMoveFlags: Int) {
        mDragMoveFlags = dragMoveFlags
    }

    fun setSwipeFlags(swipeMoveFlags: Int) {
        mSwipeMoveFlags = swipeMoveFlags
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
}
