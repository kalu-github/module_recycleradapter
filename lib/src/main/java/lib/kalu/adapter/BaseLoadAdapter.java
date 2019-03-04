package lib.kalu.adapter;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 加载更多
 * created by kalu on 2017/5/26 14:37
 */
public abstract class BaseLoadAdapter<T> extends BaseCommonAdapter<T> {

    // 加载数据数据完毕了
    private boolean isLoadOver;

    /***********************************       重写API       **************************************/

    @Override
    public int getItemCount() {

        final List<T> list = onData();
        if (null == list || list.size() == 0) {
            return list.size() + getNullCount() + getHeadCount() + getFootCount();
        } else {
            return list.size() + getNullCount() + getHeadCount() + getFootCount() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {

        // 没有数据
        if (null == onData() || onData().size() == 0) {
            return RecyclerHolder.NULL_VIEW;
        }
        // 有数据
        else {
            int numHead = getHeadCount();
            if (position < numHead) {
                return RecyclerHolder.HEAD_VIEW;
            } else {

                // 需要传递的索引位置
                int realPosition = position - numHead;
                int numModel = onData().size();
                if (realPosition < numModel) {
                    return getItemModelType(realPosition);
                } else {
                    realPosition = realPosition - numModel;
                    int numFoot = getFootCount();
                    return realPosition < numFoot ? RecyclerHolder.FOOT_VIEW : RecyclerHolder.LOAD_VIEW;
                }
            }
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final RecyclerHolder holder;
        switch (viewType) {
            case RecyclerHolder.LOAD_VIEW:
                final View view = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(onFoot(), parent, false);
                holder = createSimpleHolder(parent, view);
                break;
            case RecyclerHolder.HEAD_VIEW:
                holder = createSimpleHolder(parent, mHeaderLayout);
                break;
            case RecyclerHolder.NULL_VIEW:
                holder = createSimpleHolder(parent, mEmptyLayout);
                break;
            case RecyclerHolder.FOOT_VIEW:
                holder = createSimpleHolder(parent, mFooterLayout);
                break;
            default:
                holder = createModelHolder(parent, viewType);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        int itemViewType = holder.getItemViewType();
        if (itemViewType == RecyclerHolder.LOAD_VIEW) {
            // Log.e("loadmoreac", "onLoad = "+position);
            onLoad(holder, isLoadOver, false);
        } else {
            // Log.e("loadmoreac", "onholder = "+position);
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);

        int type = holder.getItemViewType();
        boolean isModel = (type == RecyclerHolder.NULL_VIEW || type == RecyclerHolder.HEAD_VIEW || type == RecyclerHolder.FOOT_VIEW || type == RecyclerHolder.LOAD_VIEW);
        setModelStyle(holder, isModel);
    }

    @Override
    protected boolean isModelType(int type) {
        return super.isModelType(type) && (type != RecyclerHolder.LOAD_VIEW);
    }

    @Override
    public void clearInsertData(@Nullable List<T> data) {
        onData().clear();
        mLastPosition = -1;
        if (null != data) {
            onData().addAll(data);
        }
        notifyDataSetChanged();
    }

    private final void forceLoad(RecyclerView recycler, RecyclerView.LayoutManager manager, boolean isRefresh) {

        if (null == recycler)
            return;

        if (null == manager)
            return;

        final RecyclerView.Adapter adapter = recycler.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
            return;

        final int itemViewType = adapter.getItemViewType(adapter.getItemCount() - 1);
        if (itemViewType != RecyclerHolder.LOAD_VIEW)
            return;

        final View view = manager.findViewByPosition(adapter.getItemCount() - 1);
        if (null == view)
            return;

        final RecyclerView.ViewHolder childViewHolder = recycler.getChildViewHolder(view);
        if (null == childViewHolder || !(childViewHolder instanceof RecyclerHolder))
            return;

        onLoad((RecyclerHolder) childViewHolder, isLoadOver, isRefresh);
    }

    /***********************************       方法API       **************************************/

    /**
     * 加载结束
     */
    public void loadOverDataSetChanged(RecyclerHolder holder) {
        isLoadOver = true;

        RecyclerView view = (RecyclerView) holder.getParent();
        if (null == view)
            return;
        // 强制停止RecyclerView滑动
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));

        if (null == view) return;
        final RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (null == manager) return;
        notifyDataSetChanged();
        forceLoad(view, manager, false);
    }

    public void loadSuccDataSetChanged(RecyclerHolder holder) {
        isLoadOver = false;

        RecyclerView view = (RecyclerView) holder.getParent();
        if (null == view)
            return;

        if (null == view) return;
        final RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (null == manager) return;
        notifyDataSetChanged();
    }

    /**
     * 重置加载更多标记
     */
    public void loadResetDataSetChanged(RecyclerHolder holder) {
        isLoadOver = false;

        RecyclerView view = (RecyclerView) holder.getParent();
        if (null == view)
            return;

        final RecyclerView.LayoutManager manager = view.getLayoutManager();
        if (null == manager)
            return;

        notifyDataSetChanged();
        forceLoad(view, manager, true);
    }

    public void setLoadingText(RecyclerHolder holder, String str, int viewId) {

        if (TextUtils.isEmpty(str))
            return;

        RecyclerView view = (RecyclerView) holder.getParent();
        if (null == view)
            return;

        final RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (null == layoutManager)
            return;

        final RecyclerView.Adapter adapter = view.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
            return;

        final int itemViewType = adapter.getItemViewType(adapter.getItemCount() - 1);
        if (itemViewType != RecyclerHolder.LOAD_VIEW)
            return;

        final View item = layoutManager.findViewByPosition(adapter.getItemCount() - 1);
        if (null == item)
            return;

        final View text = item.findViewById(viewId);
        if (null == text || !(text instanceof TextView))
            return;

        ((TextView) text).setText(str);
    }

    public void setLoadingVisable(RecyclerHolder holder, int visibility, int viewId) {

        RecyclerView view = (RecyclerView) holder.getParent();
        if (null == view)
            return;

        final RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (null == layoutManager)
            return;

        final RecyclerView.Adapter adapter = view.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
            return;

        final int itemViewType = adapter.getItemViewType(adapter.getItemCount() - 1);
        if (itemViewType != RecyclerHolder.LOAD_VIEW)
            return;

        final View item = layoutManager.findViewByPosition(adapter.getItemCount() - 1);
        if (null == item)
            return;

        final View bar = item.findViewById(viewId);
        if (null == bar)
            return;

        bar.setVisibility(visibility);
    }

    /**********************************       抽象方法API     **************************************/

    protected abstract @LayoutRes
    int onFoot();

    /**
     * 加载更多
     */
    protected abstract void onLoad(RecyclerHolder holder, boolean isOver, boolean isRefresh);
}