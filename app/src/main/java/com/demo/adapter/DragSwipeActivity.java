package com.demo.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.demo.adapter.adapter.ItemDragAdapter;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.callback.SwipeDragCallback;

public class DragSwipeActivity extends AppCompatActivity {
    private static final String TAG = DragSwipeActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<String> mData;
    private ItemDragAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private SwipeDragCallback mItemDragAndSwipeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_use);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mData = generateData(50);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);

        mAdapter = new ItemDragAdapter(mData);
        mItemDragAndSwipeCallback = new SwipeDragCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mItemDragAndSwipeCallback.setSwipeFlags(ItemTouchHelper.START);
        mAdapter.enableSwipeItem();
        mAdapter.enableDragItem(mItemTouchHelper);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<String> generateData(int size) {
        ArrayList<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add("item " + i);
        }
        return data;
    }
}