package lib.kalu.adapter;

import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 侧滑
 * created by kalu on 2017/5/26 14:52
 */
public abstract class BaseCommonSwipeAdapter<T> extends BaseCommonAdapter<T> {

    private static final int NO_TOGGLE_VIEW = 0;
    protected int mToggleViewId = NO_TOGGLE_VIEW;
    protected ItemTouchHelper mItemTouchHelper;
    protected boolean itemSwipeEnabled = false;

    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;

    /*********************************************************/

    public BaseCommonSwipeAdapter(List<T> data, @LayoutRes int layoutResId) {
        super(data, layoutResId);
    }

    /*********************************************************/

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        int viewType = holder.getItemViewType();

        if (mItemTouchHelper != null && viewType != RecyclerHolder.LOAD_VIEW && viewType != RecyclerHolder.HEAD_VIEW
                && viewType != RecyclerHolder.NULL_VIEW && viewType != RecyclerHolder.FOOT_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                View toggleView = holder.getView(mToggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(RecyclerHolder.HOLDER_ID_TAG, holder);
                    toggleView.setOnTouchListener(mOnToggleViewTouchListener);
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

    public void enableSwipeItem() {
        itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return itemSwipeEnabled;
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - getHeadCount();
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
        getData().remove(pos);
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