package com.demo.adapter.data;

import com.demo.adapter.entity.MySection;
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

    public static List<MySection> getSampleData() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "TAB_1", true));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(true, "TAB_2", true));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(true, "TAB_3", true));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(true, "TAB_4", true));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(true, "TAB_5", true));
        list.add(new MySection(new Video("", "")));
        list.add(new MySection(new Video("", "")));
        return list;
    }
}