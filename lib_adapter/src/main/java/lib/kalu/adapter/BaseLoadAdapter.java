package lib.kalu.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 加载更多
 * created by kalu on 2017/5/26 14:37
 */
public abstract class BaseLoadAdapter<T> extends BaseCommonAdapter<T> {

    // 加载数据数据完毕了
    private boolean over;

    // 分页页码
    private int page = 1;

    /***********************************       重写API       **************************************/


    @Override
    public int getItemCount() {
        if (null == onData() || onData().size() == 0) {
            return super.getItemCount();
        } else {
            return super.getItemCount() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {

        // 没有数据
        if (null == onData() || onData().size() == 0) {
            return RecyclerHolder.EMPTY_VIEW;
        }
        // 有数据
        else {
            int numHead = getHeadViewCount();
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
                    int numFoot = getFootViewCount();
                    return realPosition < numFoot ? RecyclerHolder.FOOT_VIEW : RecyclerHolder.LOAD_VIEW;
                }
            }
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 分页
        if (viewType == RecyclerHolder.LOAD_VIEW) {

            View inflate = LayoutInflater.from(parent.getContext()).inflate(onLoad(), parent, false);
            RecyclerHolder holder = new RecyclerHolder(parent, inflate);

            // 事件绑定
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
            onHolder(layoutManager, holder, viewType);

            return holder;
        }
        // 默认
        else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        int itemViewType = holder.getItemViewType();
        if (itemViewType == RecyclerHolder.LOAD_VIEW) {
            // Log.e("loadmoreac", "onBindViewHolder => over" + isLoadOver);
            onLoad(holder, over, page);
        } else {
            // Log.e("loadmoreac", "onholder = "+position);
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    protected boolean isItem(int itemType) {
        return super.isItem(itemType) && itemType != RecyclerHolder.LOAD_VIEW;
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

//    private final void forceLoad(RecyclerView recycler, RecyclerView.LayoutManager manager, boolean isRefresh) {
//
//        if (null == recycler)
//            return;
//
//        if (null == manager)
//            return;
//
//        final RecyclerView.Adapter adapter = recycler.getAdapter();
//        if (null == adapter || adapter.getItemCount() == 0)
//            return;
//
//        final int itemViewType = adapter.getItemViewType(adapter.getItemCount() - 1);
//        if (itemViewType != RecyclerHolder.LOAD_VIEW)
//            return;
//
//        final View view = manager.findViewByPosition(adapter.getItemCount() - 1);
//        if (null == view)
//            return;
//
//        final RecyclerView.ViewHolder childViewHolder = recycler.getChildViewHolder(view);
//        if (null == childViewHolder || !(childViewHolder instanceof RecyclerHolder))
//            return;
//
//        onLoad((RecyclerHolder) childViewHolder, isLoadOver, isRefresh);
//    }

    /***********************************       方法API       **************************************/

    /**
     * 加载结束
     *
     * @param over
     */
    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isOver() {
        return this.over;
    }

    /**
     * 分页页码：减
     *
     * @param num
     */
    public void setPageMinus(int num) {

        if (page <= 1)
            return;
        page -= num;
    }

    /**
     * 分页页码：加
     *
     * @param num
     */
    public void setPageAdd(int num) {
        page += num;
    }

    /**
     * 分页页码：重置归1
     */
    public void setPageReset() {
        page = 1;
        over = false;
    }

    /**
     * 刷新
     *
     * @param refresh
     */
    public void setRefresh(boolean refresh) {

        if (refresh) {
            setPageReset();
        } else {
            setPageAdd(1);
        }
    }

    /**
     * 获取页码
     *
     * @return
     */
    public int getPage() {
        return page;
    }

    public void setLoadingText(RecyclerHolder holder, String str, int viewId) {

        if (TextUtils.isEmpty(str))
            return;

        ViewParent parent = holder.itemView.getParent();
        if (null == parent)
            return;

        RecyclerView view = (RecyclerView) parent;
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

        ViewParent parent = holder.itemView.getParent();
        if (null == parent)
            return;

        RecyclerView view = (RecyclerView) parent;
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
    int onLoad();

    /**
     * 加载更多
     */
    protected abstract void onLoad(RecyclerHolder holder, boolean over, int page);
}