package lib.kalu.adapter.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View

import lib.kalu.adapter.manager.CrashGridLayoutManager
import lib.kalu.adapter.manager.CrashLinearLayoutManager

/**
 * description: RecyclerView分隔线
 * created by kalu on 2017/6/1 18:11
 */
class SpaceDecoration : RecyclerView.ItemDecoration {

    // 分隔线(高度, 宽度)
    private var mDividerSize: Float = 0.toFloat()

    private var mDividerDrawableV: Drawable? = null//垂直方向绘制的Drawable
    private var mDividerDrawableH: Drawable? = null//水平方向绘制的Drawable
    /**
     * LinearLayoutManager中有效
     * VERTICAL 方向: padding 的是top 和 bottom
     * HORIZONTAL 方向: padding 的是left 和 right
     */
    private var mMarginStart = 0
    private var mMarginEnd = 0

    @JvmOverloads constructor(dividerSize: Int = 1, dividerColor: Int = 1) {
        mDividerSize = dividerSize.toFloat()
        mDividerDrawableV = ColorDrawable(dividerColor)
        mDividerDrawableH = mDividerDrawableV
    }

    @JvmOverloads constructor(drawable: Drawable, dividerSize: Int = 1) {
        mDividerSize = dividerSize.toFloat()
        mDividerDrawableV = drawable
        mDividerDrawableH = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)

        // 布局管理器
        val layoutManager = parent.layoutManager

        // 孩子总个数
        val itemCount = layoutManager.itemCount
        if (itemCount <= 1) return

        // 线性布局
        if (layoutManager is CrashLinearLayoutManager) {
            val firstItem = layoutManager.findFirstVisibleItemPosition()
            for (i in 0 until layoutManager.getChildCount()) {
                val view = layoutManager.findViewByPosition(firstItem + i)
                if (view != null) {
                    if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                        // 水平
                        drawDrawableV(c, view)
                    } else {
                        // 垂直
                        drawDrawableH(c, view)
                    }
                }
            }
        } else if (layoutManager is CrashGridLayoutManager) {
            drawGrid(c, layoutManager)
        } else if (layoutManager is StaggeredGridLayoutManager) {
        }// 流式布局(暂时不支持)
        // 网格布局
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        // 布局管理器
        val layoutManager = parent.layoutManager

        // 孩子总个数
        val itemCount = layoutManager.itemCount
        if (itemCount <= 1) return

        val layoutParam = view.layoutParams as RecyclerView.LayoutParams

        // 当前View的位置
        val viewLayoutPosition = layoutParam.viewLayoutPosition

        // 线性布局
        if (layoutManager is CrashLinearLayoutManager) {
            if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                //水平方向
                if (viewLayoutPosition == 0) {
                    //这里可以决定,第一个item的分割线
                    outRect.set(0, 0, mDividerSize.toInt(), 0)//默认只有右边有分割线, 你也可以把左边的分割线添加出来
                } else if (viewLayoutPosition == itemCount - 1) {
                    //这里可以决定, 最后一个item的分割线
                    outRect.set(0, 0, 0, 0)//默认, 最后一个item不需要分割线
                } else {
                    //中间的item,默认只有右边的分割线
                    outRect.set(0, 0, mDividerSize.toInt(), 0)
                }
            } else {
                //垂直方向
                if (viewLayoutPosition == 0) {
                    //这里可以决定,第一个item的分割线
                    outRect.set(0, 0, 0, mDividerSize.toInt())//默认只有右边有分割线, 你也可以把左边的分割线添加出来
                } else if (viewLayoutPosition == itemCount - 1) {
                    //这里可以决定, 最后一个item的分割线
                    outRect.set(0, 0, 0, 0)//默认, 最后一个item不需要分割线
                } else {
                    //中间的item,默认只有右边的分割线
                    outRect.set(0, 0, 0, mDividerSize.toInt())
                }
            }
        } else if (layoutManager is CrashGridLayoutManager) {
            //请注意,这里的分割线 并不包括边框边上的分割线, 只处理不含边框的内部之间的分割线.
            offsetsOfGrid(outRect, layoutManager, viewLayoutPosition)
        } else if (layoutManager is StaggeredGridLayoutManager) {
        }// 流式布局(暂时不支持)
        // 网格布局
    }

    fun setDividerSize(dividerSize: Float) {
        mDividerSize = dividerSize
    }

    fun setDividerDrawableV(dividerDrawableV: Drawable) {
        mDividerDrawableV = dividerDrawableV
    }

    fun setDividerDrawableH(dividerDrawableH: Drawable) {
        mDividerDrawableH = dividerDrawableH
    }

    fun setMarginStart(marginStart: Int) {
        mMarginStart = marginStart
    }

    fun setMarginEnd(marginEnd: Int) {
        mMarginEnd = marginEnd
    }


    //------------------------------------------私有方法---------------------------------

    /**
     * GridLayoutManager 布局, 计算每个Item, 应该留出的空间(用来绘制分割线的空间)
     */
    private fun offsetsOfGrid(outRect: Rect, layoutManager: CrashGridLayoutManager, viewLayoutPosition: Int) {
        val spanCount = layoutManager.spanCount

        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            //垂直方向
            if (isLastOfGrid(layoutManager.itemCount, viewLayoutPosition, spanCount)/*判断是否是最后一排*/) {
                //最后一排的item, 不添加底部分割线,为了美观
                outRect.set(0, 0, mDividerSize.toInt(), 0)
            } else {
                if (isEndOfGrid(viewLayoutPosition, spanCount)/*判断是否是最靠右的一排*/) {
                    //最靠右的一排,不添加右边的分割线, 为了美观
                    outRect.set(0, 0, 0, mDividerSize.toInt())
                } else {
                    outRect.set(0, 0, mDividerSize.toInt(), mDividerSize.toInt())
                }
            }
        } else {
            //水平方向
            if (isLastOfGrid(layoutManager.itemCount, viewLayoutPosition, spanCount)) {
                outRect.set(0, 0, 0, mDividerSize.toInt())
            } else {
                if (isEndOfGrid(viewLayoutPosition, spanCount)) {
                    outRect.set(0, 0, mDividerSize.toInt(), 0)
                } else {
                    outRect.set(0, 0, mDividerSize.toInt(), mDividerSize.toInt())
                }
            }
        }

    }

    /**
     * 判断 viewLayoutPosition 是否是一排的结束位置 (垂直水平通用)
     */
    private fun isEndOfGrid(viewLayoutPosition: Int, spanCount: Int): Boolean {
        return viewLayoutPosition % spanCount == spanCount - 1
    }

    /**
     * 判断 viewLayoutPosition 所在的位置,是否是最后一排(垂直水平通用)
     */
    private fun isLastOfGrid(itemCount: Int, viewLayoutPosition: Int, spanCount: Int): Boolean {
        var result = false
        val ceil = Math.ceil((itemCount.toFloat() / spanCount).toDouble())
        if (viewLayoutPosition >= ceil * spanCount - spanCount) {
            result = true
        }
        return result
    }

    private fun drawGrid(c: Canvas, manager: RecyclerView.LayoutManager) {
        val layoutManager = manager as GridLayoutManager

        val firstItem = layoutManager.findFirstVisibleItemPosition()
        for (i in 0 until layoutManager.childCount) {
            val view = layoutManager.findViewByPosition(firstItem + i)
            if (view != null) {
                val spanCount = layoutManager.spanCount
                val layoutParams = view.layoutParams as RecyclerView.LayoutParams
                val viewLayoutPosition = layoutParams.viewLayoutPosition//布局时当前View的位置

                if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                    //垂直方向
                    if (isLastOfGrid(layoutManager.itemCount, viewLayoutPosition, spanCount)/*判断是否是最后一排*/) {
                        //最后一排的item, 不添加底部分割线,为了美观
                        if (viewLayoutPosition != layoutManager.itemCount - 1) {
                            //如果不是最后一个,过滤掉最后一个.最后一item不滑分割线
                            drawDrawableV(c, view)
                        }
                    } else {
                        if (isEndOfGrid(viewLayoutPosition, spanCount)/*判断是否是最靠右的一排*/) {
                            //最靠右的一排,不添加右边的分割线, 为了美观
                            drawDrawableH(c, view)
                        } else {
                            drawDrawableH(c, view)
                            drawDrawableV(c, view)
                        }
                    }
                } else {
                    //水平方向
                    if (isLastOfGrid(layoutManager.itemCount, viewLayoutPosition, spanCount)) {
                        if (viewLayoutPosition != layoutManager.itemCount - 1) {
                            //如果不是最后一个,过滤掉最后一个.最后一item不滑分割线
                            drawDrawableH(c, view)
                        }
                    } else {
                        if (isEndOfGrid(viewLayoutPosition, spanCount)) {
                            drawDrawableV(c, view)
                        } else {
                            drawDrawableH(c, view)
                            drawDrawableV(c, view)
                        }
                    }
                }
            }
        }
    }

    /**
     * 绘制view对应垂直方向的分割线
     */
    private fun drawDrawableV(c: Canvas, view: View) {

        val p = view.layoutParams as RecyclerView.LayoutParams
        mDividerDrawableV!!.setBounds(
                view.right + p.rightMargin,
                view.top + mMarginStart,
                (view.right.toFloat() + p.rightMargin.toFloat() + mDividerSize).toInt(),
                view.bottom - mMarginEnd)
        drawDrawable(c, mDividerDrawableV!!)
    }

    /**
     * 绘制view对应水平方向的分割线
     */
    private fun drawDrawableH(c: Canvas, view: View) {
        val p = view.layoutParams as RecyclerView.LayoutParams
        mDividerDrawableH!!.setBounds(
                view.left + mMarginStart,
                view.bottom + p.bottomMargin,
                view.right - mMarginEnd,
                (view.bottom.toFloat() + p.bottomMargin.toFloat() + mDividerSize).toInt())
        drawDrawable(c, mDividerDrawableH!!)
    }

    private fun drawDrawable(c: Canvas, drawable: Drawable) {
        drawable.draw(c)
    }
}
