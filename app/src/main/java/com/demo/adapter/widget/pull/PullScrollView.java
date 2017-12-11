package com.demo.adapter.widget.pull;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * description: 下拉刷新上拉加载 - Scrollview 嵌套 RecyclerView 及在Android 5.1版本滑动时 惯性消失问题
 * created by kalu on 2016/10/28 18:17
 */
public class PullScrollView extends ScrollView implements Pullable {

    private int downY;
    private int mTouchSlop;

    public PullScrollView(Context context) {
        this(context, null, 0);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean canRefresh(PullRefreshLoadLayout parent, View child) {
        // 是否滑到顶部
        return ViewCompat.canScrollVertically(this, 1) && child.getScrollY() == 0;
    }

    @Override
    public boolean canLoad(PullRefreshLoadLayout parent, View child) {
        // 是否滑到底部
        // return ViewCompat.canScrollVertically(this, -1);
        return false;
    }
}