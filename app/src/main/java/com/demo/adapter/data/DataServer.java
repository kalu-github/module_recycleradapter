package com.demo.adapter.data;

import com.demo.adapter.entity.MultipleItem;
import com.demo.adapter.entity.MyTab;
import com.demo.adapter.entity.Status;
import com.demo.adapter.entity.Video;

import java.util.ArrayList;
import java.util.List;

public class DataServer {

    private DataServer() {
    }

    public static List<Status> getSampleData(int lenth) {
        List<Status> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Status status = new Status();
            status.setUserName("" + i);
            status.setCreatedAt("04/05/" + i);
            status.setRetweet(i % 2 == 0);
            status.setUserAvatar("");
            status.setText("" + i);
            list.add(status);
        }
        return list;
    }

    public static List<MyTab> getSampleData() {
        List<MyTab> list = new ArrayList<>();
        list.add(new MyTab(true, "TAB_1", true));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(true, "TAB_2", false));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(true, "TAB_3", false));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(true, "TAB_4", false));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(true, "TAB_5", false));
        list.add(new MyTab(new Video("", "")));
        list.add(new MyTab(new Video("", "")));
        return list;
    }

    public static List<MultipleItem> getMultipleItemData() {
        List<MultipleItem> list = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, ""));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN));
        }

        return list;
    }
}