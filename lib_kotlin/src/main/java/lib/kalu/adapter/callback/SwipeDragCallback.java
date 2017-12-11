package lib.kalu.adapter.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import lib.kalu.adapter.BaseCommonSwipeDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 侧滑, 拖拽
 * created by kalu on 2017/5/26 14:16
 */
public class SwipeDragCallback extends ItemTouchHelper.Callback {

    BaseCommonSwipeDragAdapter mAdapter;

    float mMoveThreshold = 0.1f;
    float mSwipeThreshold = 0.7f;

    int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

    // ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    int mSwipeMoveFlags = ItemTouchHelper.END;

    public SwipeDragCallback(BaseCommonSwipeDragAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isItemSwipeEnable();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemDragStart(viewHolder);
            viewHolder.itemView.setTag(DragCallback.DRAG_ID_TAG, true);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeStart(viewHolder);
            viewHolder.itemView.setTag(SwipeCallback.SWIPE_ID_TAG, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }

        if (viewHolder.itemView.getTag(DragCallback.DRAG_ID_TAG) != null
                && (Boolean) viewHolder.itemView.getTag(DragCallback.DRAG_ID_TAG)) {
            mAdapter.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(DragCallback.DRAG_ID_TAG, false);
        }
        if (viewHolder.itemView.getTag(SwipeCallback.SWIPE_ID_TAG) != null
                && (Boolean) viewHolder.itemView.getTag(SwipeCallback.SWIPE_ID_TAG)) {
            mAdapter.onItemSwipeEnd(viewHolder);
            viewHolder.itemView.setTag(SwipeCallback.SWIPE_ID_TAG, false);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (isViewCreateByAdapter(viewHolder)) {
            return makeMovementFlags(0, 0);
        }

        return makeMovementFlags(mDragMoveFlags, mSwipeMoveFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);
        mAdapter.onItemDragMove(source, target);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (!isViewCreateByAdapter(viewHolder)) {
            mAdapter.onSwipeRemove(viewHolder);
        }
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    public void setSwipeThreshold(float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }

    public void setMoveThreshold(float moveThreshold) {
        mMoveThreshold = moveThreshold;
    }

    public void setDragFlags(int dragMoveFlags) {
        mDragMoveFlags = dragMoveFlags;
    }

    public void setSwipeFlags(int swipeMoveFlags) {
        mSwipeMoveFlags = swipeMoveFlags;
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {
            View itemView = viewHolder.itemView;

            c.save();
            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + dX, itemView.getBottom());
                c.translate(itemView.getLeft(), itemView.getTop());
            } else {
                c.clipRect(itemView.getRight() + dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                c.translate(itemView.getRight() + dX, itemView.getTop());
            }

            mAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
            c.restore();

        }
    }

    private boolean isViewCreateByAdapter(RecyclerView.ViewHolder viewHolder) {
        if (null == viewHolder) return false;

        switch (viewHolder.getItemViewType()) {
            case RecyclerHolder.HEAD_VIEW:
            case RecyclerHolder.LOAD_VIEW:
            case RecyclerHolder.FOOT_VIEW:
            case RecyclerHolder.NULL_VIEW:
                return true;
            default:
                return false;
        }
    }
}
