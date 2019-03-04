package com.demo.adapter.widget.pull;

/**
 * description: 监听
 * created by kalu on 2017/1/13 18:01
 */
public interface OnPullRefreshLoadChangeListener {

    /**
     * @param isPull  是否正在下拉
     * @param scrollY 位移
     */
    void onPullChange(boolean isPull, float scrollY);

    void onPullDown();

    void onPullUp();
}
