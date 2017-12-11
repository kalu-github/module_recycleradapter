package lib.kalu.adapter.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import lib.kalu.adapter.BaseCommonAdapter

/**
 * description: 利用分割线实现悬浮, 配合BaseCommonAdapter使用
 * created by kalu on 2017/6/14 13:48
 */
abstract class TabDecoration : RecyclerView.ItemDecoration() {

    private var tabHeight = 0

    init {
        tabHeight = loadTabHeight()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        if (tabHeight == 0) return

        val position = parent.getChildAdapterPosition(view)
        if (position == (if (hasHead()) 1 else 0)) {
            outRect.top = tabHeight
        }

        val groupId = loadTabName(position)
        if (TextUtils.isEmpty(groupId)) return

        //只有是同一组的第一个才显示悬浮栏
        if (position >= (if (hasHead()) 2 else 1)) {

            val preGroupId = loadTabName(position - 1)
            if (!TextUtils.isEmpty(preGroupId) && preGroupId != groupId) {
                outRect.top = tabHeight
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        if (tabHeight == 0) return

        // 孩子总个数
        val itemCount = state!!.itemCount
        // 屏幕显示布局个数(布局复用)
        val layoutCount = parent.childCount

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        var preGroupName: String?
        var currentGroupName: String? = null

        for (i in 0 until layoutCount) {

            // 1.当前遍历临时View
            val sub = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(sub)

            preGroupName = currentGroupName
            currentGroupName = loadTabName(position)

            if (TextUtils.isEmpty(currentGroupName) || TextUtils.equals(currentGroupName, preGroupName))
                continue

            val viewBottom = sub.bottom
            var top = Math.max(tabHeight, sub.top)//top 决定当前顶部第一个悬浮Group的位置
            if (position + 1 < itemCount) {
                //获取下个GroupName
                val nextGroupName = loadTabName(position + 1)
                //下一组的第一个View接近头部
                if (currentGroupName != nextGroupName && viewBottom < top) {
                    top = viewBottom
                }
            }

            //根据position获取View
            val tab = loadTabView(position) ?: return

            // 1.填充TAB内容
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, tabHeight)
            tab.layoutParams = layoutParams
            tab.isDrawingCacheEnabled = true
            tab.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            // 2.绘制TAB布局
            tab.layout(0, 0, right, tabHeight)
            tab.buildDrawingCache()
            val bitmap = tab.drawingCache

            if (hasHead() && position == 0) {
                var headBottom = 0
                val adapter = parent.adapter
                if (null != adapter && adapter is BaseCommonAdapter<*, *>) {
                    val headerLayout = adapter.headLayout
                    if (null != headerLayout) {
                        headBottom = headerLayout.bottom
                    }
                }
                c.drawBitmap(bitmap, left.toFloat(), (top - tabHeight + headBottom).toFloat(), null)
            } else {
                c.drawBitmap(bitmap, left.toFloat(), (top - tabHeight).toFloat(), null)
            }
        }
    }

    /** */

    open fun hasHead(): Boolean {
        return false
    }

    /**
     * Tab高度
     *
     * @return
     */
    abstract fun loadTabHeight(): Int

    /**
     * Tab名字
     *
     * @param position
     * @return
     */
    abstract fun loadTabName(position: Int): String

    /**
     * Tab布局文件
     *
     * @param position
     * @return
     */
    abstract fun loadTabView(position: Int): View?
}