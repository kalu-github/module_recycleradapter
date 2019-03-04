package com.demo.adapter.widget.pull;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.demo.adapter.R;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * description: 下拉刷新上拉加载 - RecyclerView
 * created by kalu on 2016/10/28 18:16
 */
public class PullRecyclerView extends RecyclerView implements Pullable {

    public final String TAG = PullRecyclerView.class.getSimpleName();

    public int firstVisiblePosition, lastVisiblePosition = -1;

    public PullRecyclerView(Context context) {
        this(context, null, 0);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(getResources().getColor(R.color.background));
        setFocusableInTouchMode(true);
    }

    /**********************************************************************************************/

    private boolean canPullDown;
    private float mDownX, mDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float rangeX = Math.abs(ev.getX() - mDownX);
                float rangeY = Math.abs(ev.getY() - mDownY);
                canPullDown = rangeY > 50 && (rangeY > rangeX);
                break;
            case MotionEvent.ACTION_UP:
                canPullDown = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**********************************************************************************************/

    @Override
    public boolean canRefresh(PullRefreshLoadLayout parent, View child) {
//        Log.e(TAG, "canRefresh ==> ");

        if (!canPullDown) return false;

        LayoutManager layoutManager = getLayoutManager();
        if (null == layoutManager) return false;

        firstVisiblePosition = getFirstVisibleItemPosition();
        View view = layoutManager.findViewByPosition(firstVisiblePosition);

        // 没有数据时, 可以下拉刷新
        if (null == view) return true;

        int recyclerTop = getTop();
        int itemTop = view.getTop();
        int count = getAdapter().getItemCount();

        return (count == 0 || (null != view && itemTop == recyclerTop && firstVisiblePosition == 0)) ? true : false;
    }

    @Override
    public boolean canLoad(PullRefreshLoadLayout parent, View child) {
        Log.e(TAG, "canLoad ==> ");

        firstVisiblePosition = getFirstVisibleItemPosition();
        if (firstVisiblePosition < 0) {
            Log.e(TAG, "canLoad ==> firstVisiblePosition < 0");
            return false;
        }

        lastVisiblePosition = getLastVisibleItemPosition();
        // 只有一个数据时, 不可以上拉加载更多
        if (lastVisiblePosition < 0 || firstVisiblePosition == lastVisiblePosition) {
            Log.e(TAG, "canLoad ==> lastVisiblePosition < 0 || firstVisiblePosition == lastVisiblePosition");
            return false;
        }

        int count = getAdapter().getItemCount();

        // 没有item的时候也可以上拉加载, 滑到底部了
        return (count == 0 || (lastVisiblePosition == (count - 1) && getLayoutManager().findViewByPosition(count - 1).getBottom() <= getMeasuredHeight())) ? true : false;
    }

    /**
     * 获取顶部可见项的位置
     */
    private int getFirstVisibleItemPosition() {
        LayoutManager lm = getLayoutManager();
        int firstVisibleItemPosition = 0;
        if (lm instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager) lm).findFirstVisibleItemPosition();
        } else if (lm instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
        } else if (lm instanceof StaggeredGridLayoutManager) {
            int positions[] = new int[1];
            ((StaggeredGridLayoutManager) lm).findFirstVisibleItemPositions(positions);
            firstVisibleItemPosition = positions[0];
        }
        return firstVisibleItemPosition;
    }

    /**
     * 获取底部可见项的位置
     */
    private int getLastVisibleItemPosition() {
        LayoutManager lm = getLayoutManager();
        int lastVisibleItemPosition = 0;
        if (lm instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) lm).findLastVisibleItemPosition();
        } else if (lm instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) lm).findLastVisibleItemPosition();
        } else if (lm instanceof StaggeredGridLayoutManager) {
            int positions[] = new int[1];
            ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(positions);
            lastVisibleItemPosition = positions[0];
        }
        return lastVisibleItemPosition;
    }
}
