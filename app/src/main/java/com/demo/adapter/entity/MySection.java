package com.demo.adapter.entity;

import lib.kalu.adapter.model.SectionModel;

public class MySection implements SectionModel {
    private boolean isMore;

    public MySection(boolean isHeader, String header, boolean isMroe) {
        this.isMore = isMroe;
    }

    public MySection(Video t) {
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }

    @Override
    public boolean isSection() {
        return isMore;
    }

    @Override
    public String getSection() {
        return "哈哈";
    }
}
