package com.demo.adapter.base;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * description 基类Fragment
 * created by kalu on 2016/10/23 13:09
 */
public abstract class BaseFragment extends Fragment {

    private final String TAG = BaseFragment.class.getSimpleName();

    public View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        if (rootView == null) rootView = inflater.inflate(initView(), null);

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) parent.removeView(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    protected abstract int initView();

    protected abstract void initData();
}