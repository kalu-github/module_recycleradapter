package lib.kalu.adapter;

import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.listener.OnDragChangeListener;
import lib.kalu.adapter.listener.OnSwipeChangeListener;

/**
 * description: 侧滑, 拖拽
 * created by kalu on 2017/5/26 14:52
 */
public abstract class BaseCommonSwipeDragAdapter<T, K extends RecyclerHolder> extends BaseCommonAdapter<T, K> {

    private static final int NO_TOGGLE_VIEW = 0;
    protected int mToggleViewId = NO_TOGGLE_VIEW;
    protected ItemTouchHelper mItemTouchHelper;
    protected boolean itemDragEnabled = false;
    protected boolean itemSwipeEnabled = false;
    protected OnDragChangeListener mOnItemDragListener;
    protected OnSwipeChangeListener mOnItemSwipeListener;
    protected boolean mDragOnLongPress = true;

    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;

    /*********************************************************/

    public BaseCommonSwipeDragAdapter(List<T> data, @LayoutRes int layoutResId) {
        super(data, layoutResId);
    }

    /*********************************************************/

    @Override
    public void onBindViewHolder(K holder, int positions) {
        super.onBindViewHolder(holder, positions);
        int viewType = holder.getItemViewType();

        if (mItemTouchHelper != null && itemDragEnabled && viewType != RecyclerHolder.LOAD_VIEW && viewType != RecyclerHolder.HEAD_VIEW
                && viewType != RecyclerHolder.NULL_VIEW && viewType != RecyclerHolder.FOOT_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = holder.getView(mToggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(RecyclerHolder.HOLDER_ID_TAG, holder);
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener);
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener);
                    }
                }
            } else {
                holder.itemView.setTag(RecyclerHolder.HOLDER_ID_TAG, holder);
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
                        mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(RecyclerHolder.HOLDER_ID_TAG));
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
                            mItemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(RecyclerHolder.HOLDER_ID_TAG));
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

    public void setOnItemDragListener(OnDragChangeListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - getHeadCount();
    }

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemDragMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);

        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(getData(), i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(getData(), i, i - 1);
            }
        }
        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());

        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onDragMove(source, target, from, to);
        }
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener.onDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void setOnItemSwipeListener(OnSwipeChangeListener listener) {
        mOnItemSwipeListener = listener;
    }

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwipeEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {

            int position = getViewHolderPosition(viewHolder);
            mOnItemSwipeListener.onSwipeEnd(viewHolder, position == -1, position);
        }
    }

    public void onSwipeRemove(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onSwipeRemove(viewHolder, getViewHolderPosition(viewHolder));
        }

        int pos = getViewHolderPosition(viewHolder);

        getData().remove(pos);
        notifyItemRemoved(viewHolder.getAdapterPosition());
    }

    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener.onSwipeMove(canvas, viewHolder, dX, dY, isCurrentlyActive, dX > 0);
        }
    }
}