package com.demo.adapter.widget.pull;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * description: 下拉刷新 - RecyclerView
 * created by kalu on 2016/10/28 18:16
 */
public class PullRecyclerViewNoLoad extends RecyclerView implements Pullable {

    public final String TAG = PullRecyclerViewNoLoad.class.getSimpleName();

    public int firstVisiblePosition = -1;

    public PullRecyclerViewNoLoad(Context context) {
        this(context, null, 0);
    }

    public PullRecyclerViewNoLoad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerViewNoLoad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(Color.WHITE);
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
        return false;
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
}
