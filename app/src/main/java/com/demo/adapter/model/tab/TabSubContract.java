package com.demo.adapter.model.tab;

import android.support.v7.widget.RecyclerView;

import com.demo.adapter.base.BasePresenter;
import com.demo.adapter.base.BaseView;

public class TabSubContract {

    interface View extends BaseView {

        void refreshComplete();
    }

    interface Presenter extends BasePresenter {

        void initMineChooseLayout(TabSubFragment fragment, RecyclerView recycler);

        void loadMineChooseData(TabSubFragment fragment, boolean isRefresh);
    }
}