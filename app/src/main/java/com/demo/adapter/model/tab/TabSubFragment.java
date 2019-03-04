package com.demo.adapter.model.tab;

import android.os.Bundle;

import com.demo.adapter.R;
import com.demo.adapter.base.BaseFragment;
import com.demo.adapter.widget.pull.OnPullRefreshListener;
import com.demo.adapter.widget.pull.PullRefreshLoadLayout;

import androidx.recyclerview.widget.RecyclerView;

public class TabSubFragment extends BaseFragment implements TabSubContract.View {

    public static final String ARGUMENTS_ID = "arguments_id";

    private RecyclerView recycler;
    private PullRefreshLoadLayout pull;

    public static TabSubFragment newInstance(int tabType) {

        TabSubFragment fragment = new TabSubFragment();

        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(ARGUMENTS_ID, tabType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int initView() {
        return R.layout.activity_tab_sub_fragment;
    }

    @Override
    protected void initData() {

        recycler = (RecyclerView) rootView.findViewById(R.id.mine_subscribe_choose_recycler);
        pull = (PullRefreshLoadLayout) rootView.findViewById(R.id.mine_subscribe_choose_pull);

        final TabSubPresenter tabSubPresenter = new TabSubPresenter();
        tabSubPresenter.initMineChooseLayout(this, recycler);

        pull.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
                tabSubPresenter.loadMineChooseData(TabSubFragment.this, true);
            }
        });
    }

    @Override
    public void refreshComplete() {
        if (null != pull) {
            pull.stopPull();
        }
    }
}