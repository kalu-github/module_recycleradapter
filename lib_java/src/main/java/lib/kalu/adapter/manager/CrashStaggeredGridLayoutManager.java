package lib.kalu.adapter.manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * description: fix-bug-IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter的解决方案
 * created by kalu on 2017/3/24 15:05
 */
public class CrashStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private final String TAG = CrashStaggeredGridLayoutManager.class.getSimpleName();

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
         //   LogUtil.e(TAG, e.getMessage(), e);
        }
    }
}
