package com.demo.adapter.entity;

import lib.kalu.adapter.model.MultModel;

public class NewMulitItem implements MultModel {

    // dangqian
    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getMultType() {
        return itemType;
    }
}
