package lib.kalu.adapter.callback;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.BaseCommonSwipeDragAdapter;
import lib.kalu.adapter.BaseLoadSwipeDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 侧滑, 拖拽
 * created by kalu on 2017/5/26 14:16
 */
public class ItemSwipeDragHelperCallback extends ItemTouchHelper.Callback {

    float mMoveThreshold = 0.1f;
    float mSwipeThreshold = 0.7f;
    int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    // ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    int mSwipeMoveFlags = ItemTouchHelper.END;
    private BaseCommonAdapter mAdapter;

    public ItemSwipeDragHelperCallback(BaseCommonAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        if (null == mAdapter) return false;

        if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
            BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
            return temp.isItemSwipeEnable();
        } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
            BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
            return temp.isItemSwipeEnable();
        } else {
            return false;
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (null == mAdapter || null == viewHolder) return;

        // 拖拽
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                && !isViewCreateByAdapter(viewHolder)) {

            if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
                BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
                temp.onItemDragStart(viewHolder);

                int id = viewHolder.itemView.getId();
                viewHolder.itemView.setTag(id, true);
            } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
                BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
                temp.onItemDragStart(viewHolder);
                int id = viewHolder.itemView.getId();
                viewHolder.itemView.setTag(id, true);
            }
        }
        // 侧滑
        else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {

            if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
                BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
                temp.onItemSwipeStart(viewHolder);
                viewHolder.itemView.setTag(ItemSwipeHelperCallback.SWIPE_ID_TAG, true);
            } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
                BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
                temp.onItemSwipeStart(viewHolder);
                viewHolder.itemView.setTag(ItemSwipeHelperCallback.SWIPE_ID_TAG, true);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (null == mAdapter || null == viewHolder) return;
        if (isViewCreateByAdapter(viewHolder)) return;

        int id = viewHolder.itemView.getId();
        if (viewHolder.itemView.getTag(id) != null
                && (Boolean) viewHolder.itemView.getTag(id)) {
            if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
                BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
                temp.onItemDragEnd(viewHolder);
                viewHolder.itemView.setTag(id, false);
            } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
                BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
                temp.onItemDragEnd(viewHolder);
                viewHolder.itemView.setTag(id, false);
            }
        }
        if (viewHolder.itemView.getTag(ItemSwipeHelperCallback.SWIPE_ID_TAG) != null
                && (Boolean) viewHolder.itemView.getTag(ItemSwipeHelperCallback.SWIPE_ID_TAG)) {

            if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
                BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
                temp.onItemSwipeEnd(viewHolder);
                viewHolder.itemView.setTag(ItemSwipeHelperCallback.SWIPE_ID_TAG, false);
            } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
                BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
                temp.onItemSwipeEnd(viewHolder);
                viewHolder.itemView.setTag(ItemSwipeHelperCallback.SWIPE_ID_TAG, false);
            }
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
        return source.getItemViewType() == target.getItemViewType();
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);

        if (null == mAdapter) return;

        if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
            BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
            temp.onItemDragMove(source, target);
        } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
            BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
            temp.onItemDragMove(source, target);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (null == mAdapter || null == viewHolder || isViewCreateByAdapter(viewHolder)) return;

        if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
            BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
            temp.onSwipeRemove(viewHolder);
        } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
            BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
            temp.onSwipeRemove(viewHolder);
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

            if (null != mAdapter || null != viewHolder) {
                if (mAdapter instanceof BaseCommonSwipeDragAdapter) {
                    BaseCommonSwipeDragAdapter temp = (BaseCommonSwipeDragAdapter) mAdapter;
                    temp.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
                } else if (mAdapter instanceof BaseLoadSwipeDragAdapter) {
                    BaseLoadSwipeDragAdapter temp = (BaseLoadSwipeDragAdapter) mAdapter;
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
