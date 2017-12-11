package com.demo.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.demo.adapter.adapter.ItemDragAdapter;

import java.util.ArrayList;
import java.util.List;

import lib.kalu.adapter.callback.SwipeDragCallback;
import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.listener.OnDragChangeListener;
import lib.kalu.adapter.listener.OnSwipeChangeListener;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mData = generateData(50);
        OnDragChangeListener listener = new OnDragChangeListener() {
            @Override
            public void onDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                RecyclerHolder holder = ((RecyclerHolder) viewHolder);
                Log.e("kalu", "onItemDragStart ==> postion = " + pos);
            }

            @Override
            public void onDragMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target, int fromPosition, int toPosition) {
                Log.e("kalu", "onItemDragMoving ==> fromPosition = " + fromPosition + ", toPosition = " + toPosition);
            }

            @Override
            public void onDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                RecyclerHolder holder = ((RecyclerHolder) viewHolder);
                Log.e("kalu", "onItemDragEnd ==> postion = " + pos);
            }
        };
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        OnSwipeChangeListener onSwipeChangeListener = new OnSwipeChangeListener() {
            @Override
            public void onSwipeStart(RecyclerView.ViewHolder viewHolder, int position) {
                RecyclerHolder holder = ((RecyclerHolder) viewHolder);
            }

            @Override
            public void onSwipeMove(Canvas canvas, RecyclerView.ViewHolder viewHolder, float moveX, float moveY, boolean isCurrentlyActive, boolean isSwipeLeft) {
                canvas.drawColor(ContextCompat.getColor(DragSwipeActivity.this, R.color.color_light_blue));
            }

            @Override
            public void onSwipeEnd(RecyclerView.ViewHolder viewHolder, boolean isRemove, int position) {
                RecyclerHolder holder = ((RecyclerHolder) viewHolder);
            }

            @Override
            public void onSwipeRemove(RecyclerView.ViewHolder viewHolder, int position) {
            }
        };

        mAdapter = new ItemDragAdapter(mData);
        mItemDragAndSwipeCallback = new SwipeDragCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        //mItemDragAndSwipeCallback.setDragFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback.setSwipeFlags(ItemTouchHelper.START);
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onSwipeChangeListener);
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);
//        mRecyclerView.addItemDecoration(new GridItemDecoration(this ,R.drawable.list_divider));

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