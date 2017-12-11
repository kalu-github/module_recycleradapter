package com.demo.adapter.widget.pull;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.adapter.R;
import com.demo.adapter.util.DeviceUtil;
import com.demo.adapter.widget.arrow.ArrowView;
import com.demo.adapter.widget.progress.ProgressLoading;

/**
 * description: 通用上下拉动并触发相应事件的ViewGroup。
 * created by kalu on 2017/1/17 11:30
 */
public class PullRefreshLoadLayout extends ViewGroup {

    private final String TAG = PullRefreshLoadLayout.class.getSimpleName();

    private final float DECELERATE_INTERPOLATION_FACTOR = 2f; // 滑动阻力因子

    private boolean pullUpEnabled;  // 是否启用上拉功能
    private boolean pullDownEnabled;// 是否启用下拉功能

    private boolean isLoadEnd;

    private int currentState; // 视图当前状态
    private OnPullRefreshLoadChangeListener onPullRefreshLoadChangeListener; // 滑动回调监听
    private OnPullRefreshLoadListener onPullRefreshLoadListener; // 滑动回调监听
    private OnPullRefreshChangeListener onPullRefreshChangeListener; // 滑动回调监听
    private OnPullRefreshListener onPullRefreshListener; // 滑动回调监听

    private View sub;  // 触发滑动手势的目标View
    private View pullHead; // 滑动头部
    private View pullFoot; // 滑动尾部

    private ArrowView pullArrowDown;   // 下拉状态指示器(箭头)
    private ProgressLoading pullProgressUp;  // 上拉加载进度条(圆形)
    private ProgressLoading pullProgressDown;// 下拉加载进度条(圆形)

    private TextView pullHeadText; // 下拉状态文本指示
    private TextView pullFootText;   // 上拉状态文本指示

    private Scroller scroller;  // 用于平滑滑动的Scroller对象
    private int effectiveRange;    // 使拉动回调生效(触发)的滑动距离
    private int ignoreRange;    // 可以忽略的拉动距离(超过此滑动距离，将不再允许parent view拦截触摸事件)
    private int pullColor; // 拉动部分背景(color|drawable)

    /**********************************************************************************************/

    public PullRefreshLoadLayout(Context context) {
        this(context, null, 0);
    }

    public PullRefreshLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PullRefreshLoadLayout);
        try {
            pullDownEnabled = array.getBoolean(R.styleable.PullRefreshLoadLayout_prll_pull_enable_down, false);
            pullUpEnabled = array.getBoolean(R.styleable.PullRefreshLoadLayout_prll_pull_enable_up, false);
            pullColor = array.getColor(R.styleable.PullRefreshLoadLayout_prll_pull_color, Color.WHITE);
            effectiveRange = (int) array.getDimension(R.styleable.PullRefreshLoadLayout_prll_pull_effective_range, DeviceUtil.dp2px(50));
            ignoreRange = (int) array.getDimension(R.styleable.PullRefreshLoadLayout_prll_pull_ignore_range, DeviceUtil.dp2px(50));
        } finally {
            array.recycle();
        }

        scroller = new Scroller(context);
    }

    /**********************************************************************************************/

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 下拉刷新
        if (pullDownEnabled) {

            pullHead = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_pull_header, null);
            pullHead.setBackgroundColor(pullColor);
            pullArrowDown = (ArrowView) pullHead.findViewById(R.id.smart_ui_iv_pull_down_arrow);
            pullProgressDown = (ProgressLoading) pullHead.findViewById(R.id.smart_ui_iv_pull_down_loading);
            pullHeadText = (TextView) pullHead.findViewById(R.id.smart_ui_tv_pull_down_des);
            this.addView(pullHead, 0);
        }

        // 上拉加载
        if (pullUpEnabled) {
            pullFoot = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_pull_footer, null);
            pullFoot.setBackgroundColor(pullColor);
            pullProgressUp = (ProgressLoading) pullFoot.findViewById(R.id.smart_ui_iv_pull_up_loading);
            pullFootText = (TextView) pullFoot.findViewById(R.id.smart_ui_tv_pull_up_des);
            this.addView(pullFoot, getChildCount());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (sub == null) {
            sub = getChildAt(pullDownEnabled ? 1 : 0);
        }

        if (sub == null) return;

        for (int i = 0; i < getChildCount(); i++) {

            int widthTemp = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int widthMeasureSpecTemp = MeasureSpec.makeMeasureSpec(widthTemp, MeasureSpec.EXACTLY);

            int heightTemp = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
            int heightMeasureSpecTemp = MeasureSpec.makeMeasureSpec(heightTemp, MeasureSpec.EXACTLY);

            getChildAt(i).measure(widthMeasureSpecTemp, heightMeasureSpecTemp);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == pullHead) { // 头视图隐藏在顶端
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            } else if (child == pullFoot) { // 尾视图隐藏在末端
                child.layout(0, sub.getMeasuredHeight(), child.getMeasuredWidth(), sub.getMeasuredHeight() + child.getMeasuredHeight());
            } else {
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
    }

    /**********************************************************************************************/

    private void rotateArrow() {

        if (null != pullArrowDown) {

            float offset = (pullArrowDown.getRotation() + 180) % 180;

            ObjectAnimator arrowAnimator = ObjectAnimator.ofFloat(pullArrowDown, "rotation",
                    pullArrowDown.getRotation(), pullArrowDown.getRotation() + 180 - offset);
            arrowAnimator.setDuration(400);
            arrowAnimator.start();

            arrowAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    pullArrowDown.clearAnimation();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    pullArrowDown.clearAnimation();
                }

                @Override
                public void onAnimationPause(Animator animation) {
                    pullArrowDown.clearAnimation();
                }
            });
        }
    }

    private float mLastMoveY;

    private float downX;
    private float downY;
    private float rangeX;
    private float rangeY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        // 当前ViewGroup是否拦截触摸事件
        boolean intercept = false;
        int y = (int) event.getY();
        // 判断触摸事件类型
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 不拦截ACTION_DOWN，因为当ACTION_DOWN被拦截，后续所有触摸事件都会被拦截
                intercept = false;

                downX = event.getX();
                downY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                rangeX = event.getX() - downX;
                rangeY = event.getY() - downY;

                if (sub == null || y == mLastMoveY || rangeY < rangeX) break;

                // 当前操作是否是下拉刷新
                // boolean isUp = (rangeY < 0);
                boolean isDown = (rangeY > 0);
                intercept = isDown ? canPullDown() : canPullUp();
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }

        mLastMoveY = y;
        return intercept;
    }

    /**
     * onInterceptTouchEvent return ture
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float y = event.getY();
                float scrollRange = mLastMoveY - y;
                mLastMoveY = y;
                updataScroll(scrollRange);
                getParent().requestDisallowInterceptTouchEvent(true);

                if (null != onPullRefreshLoadChangeListener && !isAutoRefresh) {
                    onPullRefreshLoadChangeListener.onPullChange(true, Math.abs(getScrollY()));
                }

                if (null != onPullRefreshChangeListener && !isAutoRefresh) {
                    onPullRefreshChangeListener.onPullChange(true, Math.abs(getScrollY()));
                }

                break;
            case MotionEvent.ACTION_UP:
                onStopScroll();
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (null == scroller) return;

        postInvalidate();
        if (!scroller.computeScrollOffset()) {
            return;
        }

        int currY = scroller.getCurrY();
        scrollTo(0, currY);

        if (null != onPullRefreshLoadChangeListener && !isAutoRefresh) {
            onPullRefreshLoadChangeListener.onPullChange(false, Math.abs(currY));
        }
        if (null != onPullRefreshChangeListener && !isAutoRefresh) {
            onPullRefreshChangeListener.onPullChange(false, Math.abs(currY));
        }
    }

    private void updataScroll(float scrollY) {
        // LogUtil.e(TAG, "updataScroll ==> scrollY = " + scrollY);

        // 下拉刷新
        if (scrollY < 0) {

            if (getScrollY() > 0) {

                // 此判断意味是在进行上拉操作的过程中，进行的下拉操作(可能代表用户视图取消此次上拉)
                if (getScrollY() < effectiveRange) {
                    if (pullDownEnabled && currentState != Pullable.PULL_UP_START) {
                        updateState(Pullable.PULL_UP_START);
                    }

                    if (Math.abs(scrollY) > getScrollY())
                        scrollY = -getScrollY();
                }
            } else {
                if (!pullDownEnabled)
                    return;
                if (currentState > Pullable.PULL_DOWN_RELEASEABLE)
                    return;

                if (Math.abs(getScrollY()) >= effectiveRange) { // 当下拉已经达到有效距离，则为滑动添加阻力
                    scrollY /= DECELERATE_INTERPOLATION_FACTOR;
                    if (currentState != Pullable.PULL_DOWN_RELEASEABLE)
                        updateState(Pullable.PULL_DOWN_RELEASEABLE);
                } else {

                    if (currentState != Pullable.PULL_DOWN_START) {
                        updateState(Pullable.PULL_DOWN_START);
                    }
                }
            }
        }

        // 上拉加载
        if (scrollY > 0) {

            if (getScrollY() < 0) {

                // 此判断意味是在进行下拉操作的过程中，进行的上拉操作(可能代表用户视图取消此次下拉)
                if (Math.abs(getScrollY()) < effectiveRange) {
                    if (pullDownEnabled && currentState != Pullable.PULL_DOWN_START) {
                        updateState(Pullable.PULL_DOWN_START);
                    }

                    if (scrollY > Math.abs(getScrollY()))
                        scrollY = -getScrollY();
                }
            } else {

                if (!pullUpEnabled)
                    return;
                if (currentState < Pullable.PULL_UP_START && currentState != Pullable.PULL_NORMAL)
                    return;


                // 当下拉已经达到有效距离，则为滑动添加阻力
                if (getScrollY() > effectiveRange) {
                    scrollY /= DECELERATE_INTERPOLATION_FACTOR;
                    if (currentState != Pullable.PULL_UP_RELEASEABLE)
                        updateState(Pullable.PULL_UP_RELEASEABLE);
                } else {

                    if (currentState != Pullable.PULL_UP_START) {
                        updateState(Pullable.PULL_UP_START);
                    }
                }
            }
        }

        scrollY /= DECELERATE_INTERPOLATION_FACTOR;
        scrollBy(0, (int) scrollY);
    }

    private void onStopScroll() {

        final int scrollY = getScrollY();

        // 有效拉动行为
        if (Math.abs(scrollY) >= effectiveRange) {

            // 有效下拉行为
            if (pullDownEnabled && scrollY < 0) {

                scroller.startScroll(0, scrollY, 0, -(scrollY + effectiveRange));
                updateState(Pullable.PULL_DOWN_RELEASE);
            }
            // 有效上拉行为
            if (pullUpEnabled && scrollY > 0) {

                updateState(Pullable.PULL_UP_RELEASE);
                scroller.startScroll(0, scrollY, 0, -(scrollY + effectiveRange));
            }

            // TODO: 2017/1/17
            if (isLoadEnd) {
                updateState(Pullable.PULL_NORMAL);
            }
        }
        // 无效拉动行为
        else {
            updateState(Pullable.PULL_NORMAL);
        }
    }

    private void updateState(int state) {
        // LogUtil.e(TAG, "updateState ==> state = " + state);

        switch (state) {
            case Pullable.PULL_NORMAL:

                final int scrollY = getScrollY();
                scroller.startScroll(0, scrollY, 0, -scrollY);
                break;
            case Pullable.PULL_DOWN_START:

                if (null != pullHeadText) {
                    pullHeadText.setText(R.string.txt_pull_refresh_normal);
                }

                if (null != pullProgressDown) {
                    pullProgressDown.setVisibility(View.GONE);
                }

                if (null != pullArrowDown) {
                    pullArrowDown.setRotation(0);
                    pullArrowDown.setVisibility(View.VISIBLE);
                }

                if (currentState == Pullable.PULL_NORMAL) break;
                rotateArrow();
                break;
            case Pullable.PULL_DOWN_RELEASEABLE:
                rotateArrow();

                if (null != pullHeadText) {
                    pullHeadText.setText(R.string.txt_pull_refresh_release);
                }
                break;
            case Pullable.PULL_DOWN_RELEASE:

                if (null != pullHeadText) {
                    pullHeadText.setText(R.string.txt_pull_refresh_loading);
                }

                if (null != pullProgressDown) {
                    pullProgressDown.setVisibility(View.VISIBLE);
                }

                if (null != pullArrowDown) {
                    pullArrowDown.setRotation(0);
                    pullArrowDown.setVisibility(View.GONE);
                }

                if (onPullRefreshLoadChangeListener != null && !isAutoRefresh) {
                    this.isLoadEnd = false;
                    onPullRefreshLoadChangeListener.onPullDown();
                }

                if (onPullRefreshLoadListener != null && !isAutoRefresh) {
                    this.isLoadEnd = false;
                    onPullRefreshLoadListener.onPullDown();
                }

                if (onPullRefreshChangeListener != null && !isAutoRefresh) {
                    this.isLoadEnd = false;
                    onPullRefreshChangeListener.onPullDown();
                }

                if (onPullRefreshListener != null && !isAutoRefresh) {
                    this.isLoadEnd = false;
                    onPullRefreshListener.onPullDown();
                }

                break;
            case Pullable.PULL_UP_START:

                if (null != pullProgressUp) {
                    pullProgressUp.setVisibility(View.GONE);
                }

                if (null != pullFootText && pullUpEnabled) {
                    pullFootText.setText(R.string.txt_pull_load_normal);
                }
                break;
            case Pullable.PULL_UP_RELEASEABLE:
                if (null != pullFootText) {
                    pullFootText.setText(R.string.txt_pull_load_release);
                }
                break;
            case Pullable.PULL_UP_RELEASE:

                if (null != pullProgressUp) {
                    pullProgressUp.setVisibility(View.VISIBLE);
                }

                if (null != pullFootText) {
                    pullFootText.setText(R.string.txt_pull_load_loading);
                }

                if (onPullRefreshLoadChangeListener != null) {

                    if (isLoadEnd) {
                        Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        onPullRefreshLoadChangeListener.onPullUp();
                    }
                }

                if (onPullRefreshLoadListener != null) {

                    if (isLoadEnd) {
                        Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        onPullRefreshLoadListener.onPullUp();
                    }
                }

                break;
        }

        currentState = state;
    }

    /**********************************************************************************************/

    /**
     * 上拉加载, 下拉刷新监听
     */
    public void setOnPullRefreshLoadChangeListener(OnPullRefreshLoadChangeListener onPullRefreshLoadChangeListener) {
        this.onPullRefreshLoadChangeListener = onPullRefreshLoadChangeListener;
    }

    /**
     * 上拉加载, 下拉刷新监听
     */
    public void setOnPullRefreshLoadListener(OnPullRefreshLoadListener onPullRefreshLoadListener) {
        this.onPullRefreshLoadListener = onPullRefreshLoadListener;
    }

    /**
     * 下拉刷新监听
     */
    public void setOnPullRefreshChangeListener(OnPullRefreshChangeListener onPullRefreshChangeListener) {
        this.onPullRefreshChangeListener = onPullRefreshChangeListener;
    }

    /**
     * 下拉刷新监听
     */
    public void setOnPullRefreshListener(OnPullRefreshListener onPullRefreshListener) {
        this.onPullRefreshListener = onPullRefreshListener;
    }

    /**********************************************************************************************/

    public void stopPull() {

        if (null != pullHeadText) {
            pullHeadText.setText(R.string.txt_pull_refresh_success);
        }

        if (null != pullFootText) {
            pullFootText.setText(R.string.txt_pull_load_success);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateState(Pullable.PULL_NORMAL);
            }
        }, 400);
    }

    public void setHeadBackgroundColor(int color) {

        if (null == pullHead) return;
        pullHead.setBackgroundColor(color);
    }

    public boolean canPullUp() {
        if (sub == null || !(sub instanceof Pullable)) return false;
        return ((Pullable) sub).canLoad(this, sub);
    }

    public boolean canPullDown() {

        if (sub == null || !(sub instanceof Pullable)) return false;
        return ((Pullable) sub).canRefresh(this, sub);
    }

    private final ValueAnimator animator = new ValueAnimator();
    private final int end = -DeviceUtil.dp2px(80);

    private boolean isAutoRefresh = false;

    public void autoRefresh() {

        if (null == animator || animator.isRunning() || animator.isStarted()) return;

        isAutoRefresh = true;
        animator.setFloatValues(0, end);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = (float) animation.getAnimatedValue();
                if (temp == end) {
                    onStopScroll();
                    getParent().requestDisallowInterceptTouchEvent(false);
                    clearAnimation();
                } else {
                    updataScroll(temp - getScrollY());
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        });
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                isAutoRefresh = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAutoRefresh = false;
            }

            @Override
            public void onAnimationPause(Animator animation) {
                isAutoRefresh = false;
            }
        });
    }
}