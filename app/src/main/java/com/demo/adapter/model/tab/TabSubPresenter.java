package com.demo.adapter.model.tab;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.adapter.City;
import com.demo.adapter.CityUtil;
import com.demo.adapter.DensityUtil;
import com.demo.adapter.R;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.decoration.TabDecoration;
import lib.kalu.adapter.holder.RecyclerHolder;

public class TabSubPresenter implements TabSubContract.Presenter {

    BaseCommonAdapter<City, RecyclerHolder> mAdapter;
    List<City> dataList = new ArrayList<>();

    @Override
    public void initMineChooseLayout(final TabSubFragment fragment, RecyclerView recycler) {
        if (null == recycler || null == fragment) return;

        //模拟数据
        dataList.addAll(CityUtil.getCityList());
        dataList.addAll(CityUtil.getCityList());

        LinearLayoutManager manager = new LinearLayoutManager(fragment.getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        TabDecoration decoration = new TabDecoration() {

            @Override
            public boolean hasHead() {
                return true;
            }

            @Override
            public int loadTabHeight() {
                return DensityUtil.dip2px(fragment.getContext(), 40);
            }

            @Override
            public String loadTabName(int position) {
                if (dataList.size() > position) {
                    return dataList.get(position).getProvince();
                }
                return null;
            }

            @Override
            public View loadTabView(int position) {
                //获取自定定义的组View
                if (dataList.size() > position) {
                    View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.city_group, null, false);
                    ((TextView) view.findViewById(R.id.tv)).setText("tab");
                    ((ImageView) view.findViewById(R.id.iv)).setImageResource(dataList.get(position).getIcon());
                    return view;
                } else {
                    return null;
                }
            }
        };

        recycler.addItemDecoration(decoration);
        mAdapter = new BaseCommonAdapter<City, RecyclerHolder>(dataList, R.layout.activity_sticky_1_item) {
            @Override
            protected void onNext(RecyclerHolder holder, City result, int position) {
                int i = position % 5 + 1;
                if (i == 1) {
                    holder.setImageResource(R.id.iv_city, R.mipmap.ic_launcher);
                } else if (i == 2) {
                    holder.setImageResource(R.id.iv_city, R.mipmap.ic_launcher);
                } else if (i == 3) {
                    holder.setImageResource(R.id.iv_city, R.mipmap.ic_launcher);
                } else if (i == 4) {
                    holder.setImageResource(R.id.iv_city, R.mipmap.ic_launcher);
                } else {
                    holder.setImageResource(R.id.iv_city, R.mipmap.ic_launcher);
                }
                holder.setText(R.id.tv_city, position + "");
            }
        };

        TextView textView = new TextView(fragment.getActivity().getApplicationContext());
        textView.setText("HEAD");
        textView.setBackgroundColor(Color.GRAY);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(fragment.getContext(), 250));
        textView.setLayoutParams(layoutParams);

        mAdapter.addHeadView(textView);

        recycler.setAdapter(mAdapter);
    }

    @Override
    public void loadMineChooseData(final TabSubFragment fragment, final boolean isRefresh) {

        dataList.clear();

        //模拟数据
        dataList.addAll(CityUtil.getCityList());
        dataList.addAll(CityUtil.getCityList());

        mAdapter.notifyDataSetChanged();

        if (isRefresh) {
            fragment.refreshComplete();
        }
    }

    /*****************************************/

    @Override
    public void start() {
    }
}
