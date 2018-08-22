package lib.kalu.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private boolean isOver = false;

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (null == msg || null == msg.obj || !(msg.obj instanceof RecyclerView))
                return true;

            final RecyclerView recycler = (RecyclerView) msg.obj;
            switch (msg.what) {
                case 1:
                    loadOverDataSetChanged(recycler);
                    msg.obj = null;
                    msg.recycle();
                    break;
                case 2:
                    loadSuccDataSetChanged(recycler);
                    msg.obj = null;
                    msg.recycle();
                    break;
                case 3:
                    loadResetDataSetChanged(recycler);
                    msg.obj = null;
                    msg.recycle();
                    break;
            }
            return true;
        }
    });

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
                final View view = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(initLoadResId(), parent, false);
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
                onLoad(holder, isOver);
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

    private final void forceLoad(RecyclerView recycler, RecyclerView.LayoutManager manager, boolean isOver) {

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

        onLoad((RecyclerHolder) childViewHolder, isOver);
    }

    /***********************************       方法API       **************************************/

    /**
     * 加载结束
     */
    public final void loadOverDataSetChanged(final RecyclerView recycler) {

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;

        isOver = true;
        if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (recycler.isComputingLayout() == false)) {
            notifyDataSetChanged();
            forceLoad(recycler, manager, true);
            // Log.e("kalu", "loadOverDataSetChanged ==> 静止状态");
        } else {
            final Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = recycler;
            mHandler.sendMessageDelayed(obtain, 2000);
            // Log.e("kalu", "loadOverDataSetChanged ==> 滑动状态, 延迟2s刷新");
        }
    }

    public final void loadSuccDataSetChanged(final RecyclerView recycler) {

        if (null == recycler) return;
        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager) return;

        isOver = false;
        if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (recycler.isComputingLayout() == false)) {
            notifyDataSetChanged();
            // Log.e("kalu", "loadSuccDataSetChanged ==> 静止状态");
        } else {
            final Message obtain = Message.obtain();
            obtain.what = 2;
            obtain.obj = recycler;
            mHandler.sendMessageDelayed(obtain, 2000);
            // Log.e("kalu", "loadSuccDataSetChanged ==> 滑动状态, 延迟2s刷新");
        }
    }

    /**
     * 重置加载更多标记
     */
    public final void loadResetDataSetChanged(final RecyclerView recycler) {

        if (null == recycler)
            return;

        final RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (null == manager)
            return;

        isOver = false;
        if (recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (recycler.isComputingLayout() == false)) {
            notifyDataSetChanged();
            forceLoad(recycler, manager, false);
            // Log.e("kalu", "loadResetDataSetChanged ==> 静止状态");
        } else {
            final Message obtain = Message.obtain();
            obtain.what = 3;
            obtain.obj = recycler;
            mHandler.sendMessageDelayed(obtain, 2000);
            // Log.e("kalu", "loadResetDataSetChanged ==> 滑动状态, 延迟2s刷新");
        }
    }

    /**********************************       抽象方法API     **************************************/

    protected abstract @LayoutRes
    int initLoadResId();

    protected abstract void onLoad(RecyclerHolder holder, boolean isOver);
}