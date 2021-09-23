package com.demo.adapter.entity;

import com.demo.adapter.adapter.TransAdapter;

import lib.kalu.recyclerview.model.MultModel;

public class Person implements MultModel {
    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public String name;
    public int age;

    @Override
    public int getMultType() {
        return TransAdapter.TYPE_PERSON;
    }
}