package lib.kalu.adapter.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * description: fix-bug-IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter的解决方案
 * created by kalu on 2017/3/24 15:05
 */
public final class CrashStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private boolean isScrollEnabled = true;

    public CrashStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CrashStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
               Log.e("", e.getMessage(), e);
        }
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    /**********************************************************************************************/
}
