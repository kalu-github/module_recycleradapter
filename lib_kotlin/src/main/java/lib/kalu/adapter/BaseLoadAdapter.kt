package lib.kalu.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import lib.kalu.adapter.holder.RecyclerHolder

/**
 * description: 加载更多
 * created by kalu on 2017/5/26 14:37
 */
abstract class BaseLoadAdapter<T, K : RecyclerHolder> : BaseCommonAdapter<T, K> {

    // 是否正在加载更多
    // private boolean isLoading;
    // 加载数据数据完毕了
    private var isLoadOver: Boolean = false

    @LayoutRes
    private var loadResId = -1

    /***********************************     构造器API        */

    /**
     * 分类型, 不对外暴露API
     */
    internal constructor(data: MutableList<T>?, @LayoutRes loadResId: Int) : super(data) {
        this.loadResId = loadResId
    }

    /**
     * 普通, 对外暴露API
     */
    constructor(data: MutableList<T>?, @LayoutRes itemResId: Int, @LayoutRes loadResId: Int) : super(data, itemResId) {
        this.loadResId = loadResId
    }

    /***********************************       重写API        */

    override fun getItemCount(): Int {
        return data!!.size + nullCount + headCount + footCount + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (nullCount == 1) {
            when (position) {
                0 -> return if (headCount != 0) RecyclerHolder.HEAD_VIEW else RecyclerHolder.NULL_VIEW
                1 -> return if (headCount != 0) RecyclerHolder.NULL_VIEW else RecyclerHolder.FOOT_VIEW
                2 -> return RecyclerHolder.FOOT_VIEW
                else -> return RecyclerHolder.NULL_VIEW
            }
        }
        val numHeaders = headCount
        if (position < numHeaders) {
            return RecyclerHolder.HEAD_VIEW
        } else {
            var adjPosition = position - numHeaders
            val adapterCount = data!!.size
            if (adjPosition < adapterCount) {
                return getItemModelType(adjPosition)
            } else {
                adjPosition = adjPosition - adapterCount
                val numFooters = footCount
                return if (adjPosition < numFooters) RecyclerHolder.FOOT_VIEW else RecyclerHolder.LOAD_VIEW
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {

        // 设置滑动监听
        if (parent is RecyclerView && null == parent.getTag()) {

            parent.setTag(true)
            val positions = IntArray(1)
            val manager = parent.layoutManager

            parent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState != 0) return

                    // 没有真正滑动到底部, 但是最后一个item可见
                    if (parent.canScrollVertically(1)) {
                        // 网格布局
                        if (manager is GridLayoutManager) {
                            val itemCount = manager.itemCount
                            val lastVisibleItemPositionReal = manager.findLastVisibleItemPosition() + 1
                            if (itemCount != lastVisibleItemPositionReal) return
                            onLoad()
                        } else if (manager is LinearLayoutManager) {
                            val itemCount = manager.itemCount
                            val lastVisibleItemPositionReal = manager.findLastVisibleItemPosition() + 1
                            if (itemCount != lastVisibleItemPositionReal) return
                            onLoad()
                        } else {
                            val temp = manager as StaggeredGridLayoutManager
                            temp.findLastVisibleItemPositions(positions)
                            val itemCount = temp.itemCount
                            val lastVisibleItemPositionReal = positions[0] + 1
                            if (itemCount != lastVisibleItemPositionReal) return
                            onLoad()
                        }// 瀑布流布局
                        // 线性布局
                    } else {
                        onLoad()
                    }// 真正滑动到底部
                }
            })
        }

        val holder: K
        when (viewType) {
            RecyclerHolder.LOAD_VIEW -> {
                val inflate = LayoutInflater.from(parent.context.applicationContext).inflate(loadResId, parent, false)
                holder = createSimpleHolder(inflate)
            }
            RecyclerHolder.HEAD_VIEW -> holder = createSimpleHolder(headLayout)
            RecyclerHolder.NULL_VIEW -> holder = createSimpleHolder(mEmptyLayout)
            RecyclerHolder.FOOT_VIEW -> holder = createSimpleHolder(footLayout)
            else -> holder = createModelHolder(parent, viewType)
        }
        return holder
    }

    override fun onViewAttachedToWindow(holder: K?) {
        super.onViewAttachedToWindow(holder)

        val type = holder!!.itemViewType
        val isModel = type == RecyclerHolder.NULL_VIEW || type == RecyclerHolder.HEAD_VIEW || type == RecyclerHolder.FOOT_VIEW || type == RecyclerHolder.LOAD_VIEW
        setModelStyle(holder, isModel)
    }

    override fun isModelType(type: Int): Boolean {
        return super.isModelType(type) && type != RecyclerHolder.LOAD_VIEW
    }

    override fun onBindViewHolder(holder: K, position: Int) {

        when (holder.itemViewType) {
            RecyclerHolder.HEAD_VIEW, RecyclerHolder.NULL_VIEW, RecyclerHolder.FOOT_VIEW, RecyclerHolder.LOAD_VIEW -> {
            }
            else -> onNext(holder, data!![holder.layoutPosition - headCount], position)
        }
    }

    override fun clearAddData(data: MutableList<T>?) {
        data!!.clear()
        mLastPosition = -1
        data?.addAll(data)
        notifyDataSetChanged()
    }

    /***********************************       方法API        */

    /**
     * 加载结束
     */
    fun loadOverNotifyDataSetChanged(recycler: RecyclerView?) {
        isLoadOver = true

        if (null == recycler) return
        val manager = recycler.layoutManager ?: return
        notifyItemRangeChanged(manager.itemCount, itemCount)
    }

    /**
     * 加载完成
     */
    fun loadCompleteNotifyDataSetChanged(recycler: RecyclerView?) {
        //        isLoading = false;

        if (null == recycler) return
        val manager = recycler.layoutManager ?: return
        notifyItemRangeChanged(manager.itemCount, itemCount)
    }

    /**
     * 重置加载更多标记
     */
    fun loadResetNotifyDataSetChanged(recycler: RecyclerView?) {
        //        isLoading = false;
        isLoadOver = false

        if (null == recycler) return
        val manager = recycler.layoutManager ?: return
        notifyItemRangeChanged(manager.itemCount, itemCount)
    }

    /**********************************       抽象方法API      */

    /**
     * 加载更多
     */
    protected abstract fun onLoad()
}