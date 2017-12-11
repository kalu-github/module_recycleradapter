package com.demo.adapter.entity;

import lib.kalu.adapter.model.TabModel;

public class MyTab extends TabModel<Video> {
    private boolean isMore;

    public MyTab(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MyTab(Video t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
