package lib.kalu.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 加载更多
 * created by kalu on 2017/5/26 14:37
 */
public abstract class BaseLoadAdapter<T> extends BaseCommonAdapter<T> {

    // 加载数据数据完毕了
    private boolean isLoadOver;
    private boolean isListener;

    private @LayoutRes
    int loadResId = -1;

    /***********************************     构造器API       **************************************/

    /**
     * 分类型, 不对外暴露API
     */
    BaseLoadAdapter(@Nullable List<T> data, @LayoutRes int loadResId) {
        super(data);
        this.loadResId = loadResId;
    }

    /**
     * 普通, 对外暴露API
     */
    public BaseLoadAdapter(@Nullable List<T> data, @LayoutRes int itemResId, @LayoutRes int loadResId) {
        super(data, itemResId);
        this.loadResId = loadResId;
    }

    /***********************************       重写API       **************************************/

    @Override
    public int getItemCount() {

        final List<T> list = getData();
        if (null == list || list.size() == 0) {
            return list.size() + getNullCount() + getHeadCount() + getFootCount();
        } else {
            return list.size() + getNullCount() + getHeadCount() + getFootCount() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {

        // 没有数据
        if (null == getData() || getData().size() == 0) {
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
                int numModel = getData().size();
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
                final View view = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(loadResId, parent, false);
                holder = createSimpleHolder(view);
                break;
            case RecyclerHolder.HEAD_VIEW:
                holder = createSimpleHolder(mHeaderLayout);
                break;
            case RecyclerHolder.NULL_VIEW:
                holder = createSimpleHolder(mEmptyLayout);
                break;
            case RecyclerHolder.FOOT_VIEW:
                holder = createSimpleHolder(mFooterLayout);
                break;
            default:
                holder = createModelHolder(parent, viewType);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case RecyclerHolder.HEAD_VIEW:
            case RecyclerHolder.NULL_VIEW:
            case RecyclerHolder.FOOT_VIEW:
                break;
            case RecyclerHolder.LOAD_VIEW:
                onLoad(holder, isLoadOver, false);
               // Log.e("onBindViewHolder", "onLoad ==> isLoadOver = " + isLoadOver);
                break;
            default:
                final int headCount = getHeadCount();
                int realPosition = holder.getLayoutPosition() - headCount;
                onNext(holder, getData().get(realPosition), realPosition);
                break;
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
        getData().clear();
        mLastPosition = -1;
        if (null != data) {
            getData().addAll(data);
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
    public void loadOverDataSetChanged(RecyclerView recycler) {
        isLoadOver = true;

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyDataSetChanged();
        forceLoad(recycler, manager, false);
    }

    public void loadSuccDataSetChanged(RecyclerView recycler) {
        isLoadOver = false;

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyDataSetChanged();
    }

    /**
     * 重置加载更多标记
     */
    public void loadResetDataSetChanged(RecyclerView recycler) {
        isLoadOver = false;

        if (null == recycler)
            return;

        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager)
            return;

        notifyDataSetChanged();
        forceLoad(recycler, manager, true);
    }

    public void setLoadingText(RecyclerView recycler, String str, int viewId) {

        if (null == recycler)
            return;

        if (TextUtils.isEmpty(str))
            return;

        final RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        if (null == layoutManager)
            return;

        final RecyclerView.Adapter adapter = recycler.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
            return;

        final int itemViewType = adapter.getItemViewType(adapter.getItemCount() - 1);
        if (itemViewType != RecyclerHolder.LOAD_VIEW)
            return;

        final View view = layoutManager.findViewByPosition(adapter.getItemCount() - 1);
        if (null == view)
            return;

        final View text = view.findViewById(viewId);
        if (null == text || !(text instanceof TextView))
            return;

        ((TextView) text).setText(str);
    }

    public void setLoadingVisable(RecyclerView recycler, int visibility, int viewId) {

        if (null == recycler)
            return;

        final RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        if (null == layoutManager)
            return;

        final RecyclerView.Adapter adapter = recycler.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
            return;

        final int itemViewType = adapter.getItemViewType(adapter.getItemCount() - 1);
        if (itemViewType != RecyclerHolder.LOAD_VIEW)
            return;

        final View view = layoutManager.findViewByPosition(adapter.getItemCount() - 1);
        if (null == view)
            return;

        final View bar = view.findViewById(viewId);
        if (null == bar)
            return;

        bar.setVisibility(visibility);
    }

    /**********************************       抽象方法API     **************************************/

    /**
     * 加载更多
     */
    protected abstract void onLoad(RecyclerHolder holder, boolean isOver, boolean isRefresh);
}