package lib.kalu.adapter.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.BaseCommonSwipeAdapter;
import lib.kalu.adapter.BaseLoadSwipeAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;

/**
 * description: 侧滑
 * created by kalu on 2017/5/26 13:34
 */
public class SwipeCallback extends ItemTouchHelper.Callback {

    public static final int SWIPE_ID_TAG = SwipeCallback.class.hashCode();

    private BaseCommonAdapter mAdapter;

    private int moveFlags = ItemTouchHelper.START;

    public SwipeCallback(BaseCommonSwipeAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 1.右侧滑动(ItemTouchHelper.START)
     * 2.左侧滑动(ItemTouchHelper.END)
     * 3.两边滑动(ItemTouchHelper.START | ItemTouchHelper.END)
     */
    public void setMoveFlags(int moveFlags) {
        this.moveFlags = moveFlags;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {

        if (null == mAdapter) return false;

        if (mAdapter instanceof BaseCommonSwipeAdapter) {
            BaseCommonSwipeAdapter temp = (BaseCommonSwipeAdapter) mAdapter;
            return temp.isItemSwipeEnable();
        } else if (mAdapter instanceof BaseLoadSwipeAdapter) {
            BaseLoadSwipeAdapter temp = (BaseLoadSwipeAdapter) mAdapter;
            return temp.isItemSwipeEnable();
        } else {
            return false;
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (null == mAdapter || null == viewHolder) return;
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE || isViewCreateByAdapter(viewHolder))
            return;

        if (mAdapter instanceof BaseCommonSwipeAdapter) {
            BaseCommonSwipeAdapter temp = (BaseCommonSwipeAdapter) mAdapter;
            temp.onItemSwipeStart(viewHolder);
            viewHolder.itemView.setTag(SWIPE_ID_TAG, true);
        } else if (mAdapter instanceof BaseLoadSwipeAdapter) {
            BaseLoadSwipeAdapter temp = (BaseLoadSwipeAdapter) mAdapter;
            temp.onItemSwipeStart(viewHolder);
            viewHolder.itemView.setTag(SWIPE_ID_TAG, true);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (null == mAdapter || null == viewHolder) return;
        if (isViewCreateByAdapter(viewHolder)) return;
        if (null == viewHolder.itemView.getTag(SwipeCallback.SWIPE_ID_TAG) ||
                !(Boolean) viewHolder.itemView.getTag(SwipeCallback.SWIPE_ID_TAG)) return;

        if (mAdapter instanceof BaseCommonSwipeAdapter) {
            BaseCommonSwipeAdapter temp = (BaseCommonSwipeAdapter) mAdapter;
            temp.onItemSwipeEnd(viewHolder);
            viewHolder.itemView.setTag(SwipeCallback.SWIPE_ID_TAG, false);
        } else if (mAdapter instanceof BaseLoadSwipeAdapter) {
            BaseLoadSwipeAdapter temp = (BaseLoadSwipeAdapter) mAdapter;
            temp.onItemSwipeEnd(viewHolder);
            viewHolder.itemView.setTag(SwipeCallback.SWIPE_ID_TAG, false);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        boolean result = isViewCreateByAdapter(viewHolder);
        return makeMovementFlags(ACTION_STATE_IDLE, result ? 0 : moveFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

        return source.getItemViewType() == target.getItemViewType();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (null == mAdapter || null == viewHolder) return;
        if (isViewCreateByAdapter(viewHolder)) return;

        if (mAdapter instanceof BaseCommonSwipeAdapter) {
            BaseCommonSwipeAdapter temp = (BaseCommonSwipeAdapter) mAdapter;
            temp.onSwipeRemove(viewHolder);
        } else if (mAdapter instanceof BaseLoadSwipeAdapter) {
            BaseLoadSwipeAdapter temp = (BaseLoadSwipeAdapter) mAdapter;
            temp.onSwipeRemove(viewHolder);
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

            if (null != mAdapter && null != viewHolder) {
                if (mAdapter instanceof BaseCommonSwipeAdapter) {
                    BaseCommonSwipeAdapter temp = (BaseCommonSwipeAdapter) mAdapter;
                    temp.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
                } else if (mAdapter instanceof BaseLoadSwipeAdapter) {
                    BaseLoadSwipeAdapter temp = (BaseLoadSwipeAdapter) mAdapter;
                    temp.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
                }
            }
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
