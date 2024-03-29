package lib.kalu.recyclerview.adapter;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import lib.kalu.recyclerview.viewholder.RecyclerHolder;

/**
 * description: 侧滑, 拖拽, 加载更多
 * created by kalu on 2017/5/26 14:52
 */
public abstract class BaseLoadSwipeDragAdapter<T> extends BaseLoadAdapter<T> {

    private static final int NO_TOGGLE_VIEW = 0;
    protected int mToggleViewId = NO_TOGGLE_VIEW;
    protected ItemTouchHelper mItemTouchHelper;
    protected boolean itemDragEnabled = false;
    protected boolean itemSwipeEnabled = false;
    protected boolean mDragOnLongPress = true;

    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;

    /*********************************************************/

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        int viewType = holder.getItemViewType();

        if (mItemTouchHelper != null && itemDragEnabled && viewType != RecyclerHolder.LOAD_VIEW && viewType != RecyclerHolder.HEAD_VIEW
                && viewType != RecyclerHolder.EMPTY_VIEW && viewType != RecyclerHolder.FOOT_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = holder.getView(mToggleViewId);
                if (toggleView != null) {

                    int id = holder.itemView.getId();
                    toggleView.setTag(id, holder);
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener);
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener);
                    }
                }
            } else {

                int id = holder.itemView.getId();
                holder.itemView.setTag(id, holder);
                holder.itemView.setOnLongClickListener(mOnToggleViewLongClickListener);
            }
        }
    }

    public void setToggleViewId(int toggleViewId) {
        mToggleViewId = toggleViewId;
    }

    public void setToggleDragOnLongPress(boolean longPress) {
        mDragOnLongPress = longPress;
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null;
            mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemTouchHelper != null && itemDragEnabled) {

                        int id = v.getId();
                        mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(id));
                    }
                    return true;
                }
            };
        } else {
            mOnToggleViewTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN
                            && !mDragOnLongPress) {
                        if (mItemTouchHelper != null && itemDragEnabled) {

                            int id = v.getId();
                            mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(id));
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            mOnToggleViewLongClickListener = null;
        }
    }

    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper) {
        enableDragItem(itemTouchHelper, NO_TOGGLE_VIEW, true);
    }

    public void enableDragItem(@NonNull ItemTouchHelper itemTouchHelper, int toggleViewId, boolean dragOnLongPress) {
        itemDragEnabled = true;
        mItemTouchHelper = itemTouchHelper;
        setToggleViewId(toggleViewId);
        setToggleDragOnLongPress(dragOnLongPress);
    }

    public void disableDragItem() {
        itemDragEnabled = false;
        mItemTouchHelper = null;
    }

    public boolean isItemDraggable() {
        return itemDragEnabled;
    }

    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
    }

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (!itemDragEnabled) return;
        onDragStart(viewHolder, getViewHolderPosition(viewHolder));
    }

    public void onItemDragMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);

        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(onData(), i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(onData(), i, i - 1);
            }
        }
        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());

        if (!itemDragEnabled) return;
        onDragMove(source, target, from, to);
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (!itemDragEnabled) return;
        onDragEnd(viewHolder, getViewHolderPosition(viewHolder));
    }

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (!itemSwipeEnabled) return;
        onSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
    }

    public void onItemSwipeEnd(RecyclerView.ViewHolder viewHolder) {
        if (!itemSwipeEnabled) return;

        int position = getViewHolderPosition(viewHolder);
        onSwipeEnd(viewHolder, position == -1, position);
    }

    public void onSwipeRemove(RecyclerView.ViewHolder viewHolder) {
        int pos = getViewHolderPosition(viewHolder);
        onData().remove(pos);
        notifyItemRemoved(viewHolder.getAdapterPosition());

        if (!itemSwipeEnabled) return;
        onSwipeRemove(viewHolder, getViewHolderPosition(viewHolder));
    }

    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (!itemSwipeEnabled) return;
        onSwipeMove(viewHolder, canvas, dX, dY, isCurrentlyActive, dX > 0);
    }

    /*********************************************************************************************/

    protected abstract void onDragStart(RecyclerView.ViewHolder viewHolder, int position);

    protected abstract void onDragMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target, int fromPosition, int toPosition);

    protected abstract void onDragEnd(RecyclerView.ViewHolder viewHolder, int position);

    protected abstract void onSwipeRemove(RecyclerView.ViewHolder holder, int position);

    protected abstract void onSwipeEnd(RecyclerView.ViewHolder holder, boolean isRemove, int position);

    protected abstract void onSwipeStart(RecyclerView.ViewHolder holder, int position);

    protected abstract void onSwipeMove(RecyclerView.ViewHolder holder, Canvas canvas, float moveX, float moveY, boolean isCurrentlyActive, boolean isSwipeLeft);
}