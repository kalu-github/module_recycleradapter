package com.demo.adapter.widget.pull;

import android.view.View;

/**
 * description: 监听
 * created by kalu on 2017/1/13 18:01
 */
public interface Pullable {

    int PULL_NORMAL = 1;    // 默认状态
    int PULL_DOWN_START = 2; // 下拉中
    int PULL_DOWN_RELEASEABLE = 3; // 可释放下拉
    int PULL_DOWN_RELEASE = 4;     // 已释放下拉
    int PULL_UP_START = 5;    // 上拉中
    int PULL_UP_RELEASEABLE = 6;  // 可释放上拉
    int PULL_UP_RELEASE = 7;     // 已释放上拉

    /**
     * 是否可以上拉加载
     *
     * @param parent
     * @param child
     * @return
     */
    boolean canLoad(PullRefreshLoadLayout parent, View child);

    /**
     * 是否可以上拉刷新
     *
     * @param parent
     * @param child
     * @return
     */
    boolean canRefresh(PullRefreshLoadLayout parent, View child);

    /**
     * 是否滚动到顶部
     *
     * @return
     */
    // boolean isScrollTop();

    /**
     * 强制滚动到顶部
     */
    // void scrollToTop();
}
