package com.demo.adapter.model.tab;

import android.os.Bundle;
import com.demo.adapter.R;
import com.demo.adapter.widget.tab.TextTabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class StickyActivity2 extends AppCompatActivity {

    private TextTabLayout tab;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        tab = (TextTabLayout) findViewById(R.id.mine_subscribe_tab);
        pager = (ViewPager) findViewById(R.id.mine_subscribe_pager);

        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tab.setViewPager(pager);
    }
}
