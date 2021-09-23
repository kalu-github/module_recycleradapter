package lib.kalu.adapter.callback;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.BaseCommonDragAdapter;
import lib.kalu.adapter.BaseLoadDragAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;

/**
 * description: 拖拽
 * created by kalu on 2017/5/26 12:05
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    /**
     * 拖拽方向
     * <p>
     * 1.左右(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     * 2.上下(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
     * 3.上下左右(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
     */
    private int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;


    public void setDragFlags(int dragFlags) {
        this.dragFlags = dragFlags;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
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

        Log.e("dragcallback", "onSelectedChanged => step1");

        if (null == viewHolder)
            return;

        if (actionState != ItemTouchHelper.ACTION_STATE_DRAG)
            return;

        boolean viewCreateByAdapter = isViewCreateByAdapter(viewHolder);
        if (viewCreateByAdapter)
            return;

        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> bindingAdapter = viewHolder.getBindingAdapter();
        if (null == bindingAdapter)
            return;

        Log.e("dragcallback", "onSelectedChanged => step2");

        // type1
        if (bindingAdapter instanceof BaseCommonDragAdapter) {

            // 震动
            try {
                Context context = viewHolder.itemView.getContext();
                Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(50);
            } catch (Exception e) {

            }

            int id = viewHolder.itemView.getId();
            viewHolder.itemView.setTag(id, true);

            BaseCommonDragAdapter temp = (BaseCommonDragAdapter) bindingAdapter;
            temp.onItemDragStart(viewHolder);
        }
        // type2
        else if (bindingAdapter instanceof BaseLoadDragAdapter) {

            // 震动
            try {
                Context context = viewHolder.itemView.getContext();
                Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(50);
            } catch (Exception e) {

            }

            int id = viewHolder.itemView.getId();
            viewHolder.itemView.setTag(id, true);

            BaseLoadDragAdapter temp = (BaseLoadDragAdapter) bindingAdapter;
            temp.onItemDragStart(viewHolder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (null == viewHolder)
            return;

        boolean viewCreateByAdapter = isViewCreateByAdapter(viewHolder);
        if (viewCreateByAdapter)
            return;

        int tagid = viewHolder.itemView.getId();
        Object tag = viewHolder.itemView.getTag(tagid);
        if (null == tag || !(Boolean) tag)
            return;

        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> bindingAdapter = viewHolder.getBindingAdapter();
        if (null == bindingAdapter)
            return;

        // type1
        if (bindingAdapter instanceof BaseCommonDragAdapter) {

            int id = viewHolder.itemView.getId();
            viewHolder.itemView.setTag(id, false);

            BaseCommonDragAdapter temp = (BaseCommonDragAdapter) bindingAdapter;
            temp.onItemDragEnd(viewHolder);
        }
        // type2
        else if (bindingAdapter instanceof BaseLoadDragAdapter) {

            int id = viewHolder.itemView.getId();
            viewHolder.itemView.setTag(id, false);

            BaseLoadDragAdapter temp = (BaseLoadDragAdapter) bindingAdapter;
            temp.onItemDragEnd(viewHolder);
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

        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> bindingAdapter = source.getBindingAdapter();
        if (null == bindingAdapter)
            return;

        // type1
        if (bindingAdapter instanceof BaseCommonDragAdapter) {
            BaseCommonDragAdapter temp = (BaseCommonDragAdapter) bindingAdapter;
            temp.onItemDragMove(source, target);
        }
        // type2
        else if (bindingAdapter instanceof BaseLoadDragAdapter) {
            BaseLoadDragAdapter temp = (BaseLoadDragAdapter) bindingAdapter;
            temp.onItemDragMove(source, target);
        }
    }

    private boolean isViewCreateByAdapter(RecyclerView.ViewHolder viewHolder) {

        if (null == viewHolder)
            return false;

        switch (viewHolder.getItemViewType()) {
            case RecyclerHolder.HEAD_VIEW:
            case RecyclerHolder.LOAD_VIEW:
            case RecyclerHolder.FOOT_VIEW:
            case RecyclerHolder.EMPTY_VIEW:
                return true;
            default:
                return false;
        }
    }
}
