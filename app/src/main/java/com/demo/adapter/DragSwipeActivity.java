package com.demo.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import com.demo.adapter.adapter.ItemDragAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.callback.ItemSwipeDragHelperCallback;

public class DragSwipeActivity extends AppCompatActivity {
    private static final String TAG = DragSwipeActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<String> mData;
    private ItemDragAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemSwipeDragHelperCallback mItemDragAndSwipeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_use);
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mData = generateData(50);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);

        mAdapter = new ItemDragAdapter() {
            @NonNull
            @Override
            protected List<String> onData() {
                return mData;
            }
        };
        mItemDragAndSwipeCallback = new ItemSwipeDragHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

//        mItemDragAndSwipeCallback.setSwipeFlags(ItemTouchHelper.START);
        mItemDragAndSwipeCallback.setSwipeFlags(ItemTouchHelper.START);
        mItemDragAndSwipeCallback.setDragFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
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