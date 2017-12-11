package lib.kalu.adapter.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import lib.kalu.adapter.BaseLoadSwipeDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;

/**
 * description: 拖拽
 * created by kalu on 2017/5/26 12:05
 */
public class DragCallback extends ItemTouchHelper.Callback {

    public static final int DRAG_ID_TAG = DragCallback.class.hashCode();

    private BaseLoadSwipeDragAdapter mAdapter;

    // 拖拽方向
    private int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

    /**
     * 1.左右(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     * 2.上下(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
     * 3.上下左右(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     */
    public void setDragFlags(int dragFlags) {
        this.dragFlags = dragFlags;
    }

    public DragCallback(BaseLoadSwipeDragAdapter adapter) {
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
            viewHolder.itemView.setTag(DRAG_ID_TAG, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }

        if (viewHolder.itemView.getTag(DRAG_ID_TAG) != null
                && (Boolean) viewHolder.itemView.getTag(DRAG_ID_TAG)) {
            mAdapter.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(DRAG_ID_TAG, false);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        boolean result = isViewCreateByAdapter(viewHolder);
        return makeMovementFlags(result ? ACTION_STATE_IDLE : dragFlags, ACTION_STATE_IDLE);
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
