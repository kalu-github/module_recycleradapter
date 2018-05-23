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
public abstract class BaseLoadAdapter<T> extends BaseCommonAdapter<T> {

    // 加载数据数据完毕了
    private boolean isLoadOver;
    private boolean isLoading;

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
                final View load = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(loadResId, parent, false);
                holder = createSimpleHolder(load);
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

        // 设置滑动监听
        if (parent instanceof RecyclerView && null == parent.getTag()) {

            parent.setTag(true);
            final RecyclerView temp = (RecyclerView) parent;
            final RecyclerView.LayoutManager manager = temp.getLayoutManager();
//
//                    if (!temp.canScrollVertically(-1)) {
//                        isLoadOver = true;
//                        onLoad(holder, isLoadOver);
//                        break;
//                    }

            final int[] rangeY = {0};

            temp.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    rangeY[0] -= dy;

                    if (manager instanceof GridLayoutManager) {
                        final int firstPosition = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
                        final int firstItemHeight = manager.findViewByPosition(firstPosition).getHeight();
                        onRoll(recyclerView, firstItemHeight, Math.abs(rangeY[0]), recyclerView.getScrollState(), firstPosition);
                    } else if (manager instanceof LinearLayoutManager) {
                        final int firstPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                        final int firstItemHeight = manager.findViewByPosition(firstPosition).getHeight();
                        onRoll(recyclerView, firstItemHeight, Math.abs(rangeY[0]), recyclerView.getScrollState(), firstPosition);
                    } else {
//                        StaggeredGridLayoutManager temp = (StaggeredGridLayoutManager) manager;
//                        final int positions[] = new int[1];
//                        temp.findFirstVisibleItemPositions(positions);
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (isLoading || newState != 0) return;

                    // 没有真正滑动到底部, 但是最后一个item可见
                    if (temp.canScrollVertically(1)) {
                        // 网格布局
                        if (manager instanceof GridLayoutManager) {
                            GridLayoutManager temp = (GridLayoutManager) manager;
                            final int itemCount = temp.getItemCount();
                            final int lastVisibleItemPositionReal = temp.findLastVisibleItemPosition() + 1;
                            if (itemCount != lastVisibleItemPositionReal) return;

                            onLoad(holder, isLoadOver);
                            isLoading = true;
                        }
                        // 线性布局
                        else if (manager instanceof LinearLayoutManager) {
                            LinearLayoutManager temp = (LinearLayoutManager) manager;
                            final int itemCount = temp.getItemCount();
                            final int lastVisibleItemPositionReal = temp.findLastVisibleItemPosition() + 1;
                            if (itemCount != lastVisibleItemPositionReal) return;

                            onLoad(holder, isLoadOver);
                            isLoading = true;
                        }
                        // 瀑布流布局
                        else {
                            StaggeredGridLayoutManager temp = (StaggeredGridLayoutManager) manager;
                            final int positions[] = new int[1];
                            temp.findLastVisibleItemPositions(positions);
                            final int itemCount = temp.getItemCount();
                            final int lastVisibleItemPositionReal = positions[0] + 1;
                            if (itemCount != lastVisibleItemPositionReal) return;

                            onLoad(holder, isLoadOver);
                            isLoading = true;
                        }
                    }
                    // 真正滑动到底部
                    else {
                        onLoad(holder, isLoadOver);
                        isLoading = true;
                    }
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case RecyclerHolder.HEAD_VIEW:
            case RecyclerHolder.NULL_VIEW:
            case RecyclerHolder.FOOT_VIEW:
            case RecyclerHolder.LOAD_VIEW:
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
        isLoading = false;

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyItemRangeChanged(manager.getItemCount(), getItemCount());
    }

    /**
     * 加载完成
     */
    public void loadCompleteNotifyDataSetChanged(RecyclerView recycler) {
        isLoadOver = false;
        isLoading = false;

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
        isLoading = false;

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;
        notifyItemRangeChanged(manager.getItemCount(), getItemCount());
    }

    /**********************************       抽象方法API     **************************************/

    /**
     * 加载更多
     */
    protected abstract void onLoad(RecyclerHolder holder, boolean isOver);
}