package com.demo.adapter.entity;

import com.demo.adapter.adapter.TransAdapter;

import lib.kalu.recyclerview.model.MultModel;
import lib.kalu.recyclerview.model.TransModel;

public class Level1Item extends TransModel<Person> implements MultModel {
    public String title;
    public String subTitle;

    public Level1Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    @Override
    public int getMultType() {
        return TransAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}