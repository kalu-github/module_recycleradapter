package lib.kalu.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;

/**
 * description: 加载更多
 * created by kalu on 2017/5/26 14:37
 */
public abstract class BaseLoadAdapter<T, K extends RecyclerHolder> extends BaseCommonAdapter<T, K> {

    // 加载数据数据完毕了
    private boolean isLoadOver;

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
        return getData().size() + getNullCount() + getHeadCount() + getFootCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getNullCount() == 1) {
            switch (position) {
                case 0:
                    return (getHeadCount() != 0) ? RecyclerHolder.HEAD_VIEW : RecyclerHolder.NULL_VIEW;
                case 1:
                    return (getHeadCount() != 0) ? RecyclerHolder.NULL_VIEW : RecyclerHolder.FOOT_VIEW;
                case 2:
                    return RecyclerHolder.FOOT_VIEW;
                default:
                    return RecyclerHolder.NULL_VIEW;
            }
        }
        int numHeaders = getHeadCount();
        if (position < numHeaders) {
            return RecyclerHolder.HEAD_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = getData().size();
            if (adjPosition < adapterCount) {
                return getItemModelType(adjPosition);
            } else {
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFootCount();
                return adjPosition < numFooters ? RecyclerHolder.FOOT_VIEW : RecyclerHolder.LOAD_VIEW;
            }
        }
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {

        final K holder;
        switch (viewType) {
            case RecyclerHolder.LOAD_VIEW:
                final View load = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(loadResId, parent, false);
                holder = createSimpleHolder(load);

                // 设置滑动监听
                if (parent instanceof RecyclerView && null == parent.getTag()) {

                    parent.setTag(true);
                    final int positions[] = new int[1];
                    final RecyclerView temp = (RecyclerView) parent;
                    final RecyclerView.LayoutManager manager = temp.getLayoutManager();

                    temp.addOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);

                            if (newState != 0) return;

                            // 没有真正滑动到底部, 但是最后一个item可见
                            if (temp.canScrollVertically(1)) {
                                // 网格布局
                                if (manager instanceof GridLayoutManager) {
                                    GridLayoutManager temp = (GridLayoutManager) manager;
                                    final int itemCount = temp.getItemCount();
                                    final int lastVisibleItemPositionReal = temp.findLastVisibleItemPosition() + 1;
                                    if (itemCount != lastVisibleItemPositionReal) return;

                                    onLoad(holder, isLoadOver);
                                }
                                // 线性布局
                                else if (manager instanceof LinearLayoutManager) {
                                    LinearLayoutManager temp = (LinearLayoutManager) manager;
                                    final int itemCount = temp.getItemCount();
                                    final int lastVisibleItemPositionReal = temp.findLastVisibleItemPosition() + 1;
                                    if (itemCount != lastVisibleItemPositionReal) return;

                                    onLoad(holder, isLoadOver);
                                }
                                // 瀑布流布局
                                else {
                                    StaggeredGridLayoutManager temp = (StaggeredGridLayoutManager) manager;
                                    temp.findLastVisibleItemPositions(positions);
                                    final int itemCount = temp.getItemCount();
                                    final int lastVisibleItemPositionReal = positions[0] + 1;
                                    if (itemCount != lastVisibleItemPositionReal) return;

                                    onLoad(holder, isLoadOver);
                                }
                            }
                            // 真正滑动到底部
                            else {
                                onLoad(holder, isLoadOver);
                            }
                        }
                    });
                }

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
    public void onViewAttachedToWindow(K holder) {
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
    public void onBindViewHolder(K holder, int position) {

        switch (holder.getItemViewType()) {
            case RecyclerHolder.HEAD_VIEW:
            case RecyclerHolder.NULL_VIEW:
            case RecyclerHolder.FOOT_VIEW:
            case RecyclerHolder.LOAD_VIEW:
                break;
            default:
                onNext(holder, getData().get(holder.getLayoutPosition() - getHeadCount()), position);
                break;
        }
    }

    @Override
    public void clearAddData(@Nullable List<T> data) {
        getData().clear();
        mLastPosition = -1;
        if (null != data) {
            getData().addAll(data);
        }
        notifyDataSetChanged();
    }

    /***********************************       方法API       **************************************/

    /**
     * 加载结束
     */
    public void loadOverNotifyDataSetChanged(RecyclerView recycler) {
        isLoadOver = true;

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyItemRangeChanged(manager.getItemCount(), getItemCount());
    }

    /**
     * 加载完成
     */
    public void loadCompleteNotifyDataSetChanged(RecyclerView recycler) {

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyItemRangeChanged(manager.getItemCount(), getItemCount());
    }

    /**
     * 重置加载更多标记
     */
    public void loadResetNotifyDataSetChanged(RecyclerView recycler) {
        isLoadOver = false;

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyItemRangeChanged(manager.getItemCount(), getItemCount());
    }

    /**********************************       抽象方法API     **************************************/

    /**
     * 加载更多
     */
    protected abstract void onLoad(K holder, boolean isOver);
}