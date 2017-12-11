package lib.kalu.adapter.manager

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet

/**
 * description: fix-bug-IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter的解决方案
 * created by kalu on 2017/3/24 15:05
 */
class CrashStaggeredGridLayoutManager : StaggeredGridLayoutManager {

    private val TAG = CrashStaggeredGridLayoutManager::class.java.simpleName

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation) {}

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            //   LogUtil.e(TAG, e.getMessage(), e);
        }

    }
}
