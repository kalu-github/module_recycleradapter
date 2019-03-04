package lib.kalu.adapter.callback;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.BaseCommonDragAdapter;
import lib.kalu.adapter.BaseLoadDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;

/**
 * description: 拖拽
 * created by kalu on 2017/5/26 12:05
 */
public class DragCallback extends ItemTouchHelper.Callback {

    public static final int DRAG_ID_TAG = DragCallback.class.hashCode();

    private BaseCommonAdapter mAdapter;

    // 拖拽方向
    private int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

    public DragCallback(BaseCommonAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 1.左右(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     * 2.上下(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
     * 3.上下左右(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     */
    public void setDragFlags(int dragFlags) {
        this.dragFlags = dragFlags;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (null == mAdapter) return;

        if (null == viewHolder || actionState != ItemTouchHelper.ACTION_STATE_DRAG || isViewCreateByAdapter(viewHolder))
            return;

        if (mAdapter instanceof BaseCommonDragAdapter) {
            BaseCommonDragAdapter temp = (BaseCommonDragAdapter) mAdapter;
            temp.onItemDragStart(viewHolder);
            viewHolder.itemView.setTag(DRAG_ID_TAG, true);
        } else if (mAdapter instanceof BaseLoadDragAdapter) {
            BaseLoadDragAdapter temp = (BaseLoadDragAdapter) mAdapter;
            temp.onItemDragStart(viewHolder);
            viewHolder.itemView.setTag(DRAG_ID_TAG, true);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (null == mAdapter) return;

        if (null == viewHolder || isViewCreateByAdapter(viewHolder)) return;

        if (null == viewHolder.itemView.getTag(DRAG_ID_TAG) ||
                !(Boolean) viewHolder.itemView.getTag(DRAG_ID_TAG)) return;

        if (mAdapter instanceof BaseCommonDragAdapter) {
            BaseCommonDragAdapter temp = (BaseCommonDragAdapter) mAdapter;
            temp.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(DRAG_ID_TAG, false);
        } else if (mAdapter instanceof BaseLoadDragAdapter) {
            BaseLoadDragAdapter temp = (BaseLoadDragAdapter) mAdapter;
            temp.onItemDragEnd(viewHolder);
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
        return source.getItemViewType() == target.getItemViewType();
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);

        if (null == mAdapter) return;

        if (mAdapter instanceof BaseCommonDragAdapter) {
            BaseCommonDragAdapter temp = (BaseCommonDragAdapter) mAdapter;
            temp.onItemDragMove(source, target);
        } else if (mAdapter instanceof BaseLoadDragAdapter) {
            BaseLoadDragAdapter temp = (BaseLoadDragAdapter) mAdapter;
            temp.onItemDragMove(source, target);
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
