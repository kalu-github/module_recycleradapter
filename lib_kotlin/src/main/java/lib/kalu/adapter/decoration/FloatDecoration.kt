package lib.kalu.adapter.decoration

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * description: 顶部悬浮菜单 - 例如搜索菜单
 * created by kalu on 2017/6/14 13:48
 */
class FloatDecoration(private val tab: View) : RecyclerView.ItemDecoration() {

    private var mBitmap: Bitmap? = null
    private val height: Int

    init {

        /**
         * UNSPECIFIED(未指定),父控件对子控件不加任何束缚，子元素可以得到任意想要的大小
         * EXACTLY(完全)，父控件为子View指定确切大小，希望子View完全按照自己给定尺寸来处理
         * AT_MOST(至多)，父控件为子元素指定最大参考尺寸，希望子View的尺寸不要超过这个尺寸
         */
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        tab.measure(widthMeasureSpec, heightMeasureSpec)

        height = tab.measuredHeight
        Log.e("kalu", "tab = " + height)
    }//根据position获取View

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) {
            Log.e("kalu", "预留50")
            outRect.top = height
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        if (null == mBitmap) {

            val layoutParams = ViewGroup.LayoutParams(parent.width, height)
            tab.layoutParams = layoutParams
            tab.isDrawingCacheEnabled = false

            //            if (tab instanceof ViewGroup) {
            //                ViewGroup group = (ViewGroup) tab;
            //                for (int i = 0; i < group.getChildCount(); i++) {
            //                    final View child = group.getChildAt(i);
            //                    child.measure(widthMeasureSpec, heightMeasureSpec);
            //                }
            //            }

            // 2.绘制TAB布局
            val right = parent.width - parent.paddingRight
            tab.layout(0, 0, right, height)
            tab.buildDrawingCache()
            mBitmap = tab.drawingCache
        }

        c.drawBitmap(mBitmap!!, parent.paddingLeft.toFloat(), 0f, null)
    }
}