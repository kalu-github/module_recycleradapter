package lib.kalu.adapter;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 侧滑, 加载更多
 * created by kalu on 2017/5/26 14:52
 */
public abstract class BaseLoadSwipeAdapter<T> extends BaseLoadAdapter<T> {

    private static final int NO_TOGGLE_VIEW = 0;
    protected int mToggleViewId = NO_TOGGLE_VIEW;
    protected ItemTouchHelper mItemTouchHelper;
    protected boolean itemSwipeEnabled = false;

    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;

    /*********************************************************/

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        int viewType = holder.getItemViewType();

        if (mItemTouchHelper != null && viewType != RecyclerHolder.LOAD_VIEW && viewType != RecyclerHolder.HEAD_VIEW
                && viewType != RecyclerHolder.EMPTY_VIEW && viewType != RecyclerHolder.FOOT_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = holder.getView(mToggleViewId);
                if (toggleView != null) {

                    int id = holder.itemView.getId();
                    toggleView.setTag(id, holder);
                    toggleView.setOnTouchListener(mOnToggleViewTouchListener);
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

    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
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

    protected abstract void onSwipeRemove(RecyclerView.ViewHolder holder, int position);

    protected abstract void onSwipeEnd(RecyclerView.ViewHolder holder, boolean isRemove, int position);

    protected abstract void onSwipeStart(RecyclerView.ViewHolder holder, int position);

    protected abstract void onSwipeMove(RecyclerView.ViewHolder holder, Canvas canvas, float moveX, float moveY, boolean isCurrentlyActive, boolean isSwipeLeft);
}