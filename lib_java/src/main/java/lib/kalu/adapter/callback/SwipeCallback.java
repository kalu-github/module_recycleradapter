package lib.kalu.adapter.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import lib.kalu.adapter.BaseLoadSwipeDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;

/**
 * description: 侧滑
 * created by kalu on 2017/5/26 13:34
 */
public class SwipeCallback extends ItemTouchHelper.Callback {

    public static final int SWIPE_ID_TAG = SwipeCallback.class.hashCode();

    private BaseLoadSwipeDragAdapter mAdapter;

    private int moveFlags = ItemTouchHelper.START;

    /**
     * 1.右侧滑动(ItemTouchHelper.START)
     * 2.左侧滑动(ItemTouchHelper.END)
     * 3.两边滑动(ItemTouchHelper.START | ItemTouchHelper.END)
     */
    public void setMoveFlags(int moveFlags) {
        this.moveFlags = moveFlags;
    }

    public SwipeCallback(BaseLoadSwipeDragAdapter adapter) {
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeStart(viewHolder);
            viewHolder.itemView.setTag(SWIPE_ID_TAG, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }

//        if (viewHolder.itemView.getTag(SWIPE_ID_TAG) != null
//                && (Boolean) viewHolder.itemView.getTag(SWIPE_ID_TAG)) {
//            mAdapter.onSwipeRemove(viewHolder);
//            viewHolder.itemView.setTag(SWIPE_ID_TAG, false);
//        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        boolean result = isViewCreateByAdapter(viewHolder);
        return makeMovementFlags(ACTION_STATE_IDLE, result ? 0 : moveFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

        boolean result = source.getItemViewType() != target.getItemViewType();
        return !result;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);
        mAdapter.onItemDragMove(source, target);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (!isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeEnd(viewHolder);
        }
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
