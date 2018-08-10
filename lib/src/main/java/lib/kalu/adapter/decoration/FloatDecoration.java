package lib.kalu.adapter.decoration;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * description: 顶部悬浮菜单 - 例如搜索菜单
 * created by kalu on 2017/6/14 13:48
 */
public final class FloatDecoration extends RecyclerView.ItemDecoration {

    private Bitmap mBitmap;
    private int height;
    private View tab;


    public void setDecoration(View tab) {
        //根据position获取View
        this.tab = tab;

        /**
         * UNSPECIFIED(未指定),父控件对子控件不加任何束缚，子元素可以得到任意想要的大小
         * EXACTLY(完全)，父控件为子View指定确切大小，希望子View完全按照自己给定尺寸来处理
         * AT_MOST(至多)，父控件为子元素指定最大参考尺寸，希望子View的尺寸不要超过这个尺寸
         */
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tab.measure(widthMeasureSpec, heightMeasureSpec);

        height = tab.getMeasuredHeight();
       // Log.e("kalu", "tab = " + height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
         //   Log.e("kalu", "预留50");
            outRect.top = height;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (null == mBitmap) {

            final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(parent.getWidth(), height);
            tab.setLayoutParams(layoutParams);
            tab.setDrawingCacheEnabled(false);

            // 2.绘制TAB布局
            final int right = parent.getWidth() - parent.getPaddingRight();
            tab.layout(0, 0, right, height);
            tab.buildDrawingCache();
            mBitmap = tab.getDrawingCache();
        }

        c.drawBitmap(mBitmap, parent.getPaddingLeft(), 0, null);
    }
}