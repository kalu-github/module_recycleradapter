package lib.kalu.recyclerview.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import lib.kalu.recyclerview.manager.CrashGridLayoutManager;
import lib.kalu.recyclerview.manager.CrashLinearLayoutManager;

/**
 * description: RecyclerView分隔线
 * created by kalu on 2017/6/1 18:11
 */
public final class SpaceDecoration2 extends RecyclerView.ItemDecoration {

    // 分隔线(高度, 宽度)
    private float mDividerSize;

    private Drawable mDividerDrawableV;//垂直方向绘制的Drawable
    private Drawable mDividerDrawableH;//水平方向绘制的Drawable
    /**
     * LinearLayoutManager中有效
     * VERTICAL 方向: padding 的是top 和 bottom
     * HORIZONTAL 方向: padding 的是left 和 right
     */
    private int mMarginStart = 0;
    private int mMarginEnd = 0;

    public SpaceDecoration2() {
        this(1);
    }

    public SpaceDecoration2(float dividerSize) {
        this(dividerSize, 1);
    }

    public SpaceDecoration2(Drawable drawable) {
        this(drawable, 1);
    }

    public SpaceDecoration2(float dividerSize, int dividerColor) {
        mDividerSize = dividerSize;
        mDividerDrawableV = new ColorDrawable(dividerColor);
        mDividerDrawableH = mDividerDrawableV;
    }

    public SpaceDecoration2(Drawable drawable, int dividerSize) {
        mDividerSize = dividerSize;
        mDividerDrawableV = drawable;
        mDividerDrawableH = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 布局管理器
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        // 孩子总个数
        int itemCount = layoutManager.getItemCount();
        if (itemCount <= 1) return;

        // 线性布局
        if (layoutManager instanceof CrashLinearLayoutManager) {
            CrashLinearLayoutManager linearLayoutManager = ((CrashLinearLayoutManager) layoutManager);
            final int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
            for (int i = 0; i < layoutManager.getChildCount(); i++) {
                final View view = layoutManager.findViewByPosition(firstItem + i);
                if (view != null) {
                    if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                        // 水平
                        drawDrawableV(c, view);
                    } else {
                        // 垂直
                        drawDrawableH(c, view);
                    }
                }
            }
        }
        // 网格布局
        else if (layoutManager instanceof CrashGridLayoutManager) {
            drawGrid(c, layoutManager);
        }
        // 流式布局(暂时不支持)
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 布局管理器
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        // 孩子总个数
        int itemCount = layoutManager.getItemCount();
        if (itemCount <= 1) return;

        final RecyclerView.LayoutParams layoutParam = (RecyclerView.LayoutParams) view.getLayoutParams();

        // 当前View的位置
        final int viewLayoutPosition = layoutParam.getViewLayoutPosition();

        // 线性布局
        if (layoutManager instanceof CrashLinearLayoutManager) {
            CrashLinearLayoutManager linearLayoutManager = ((CrashLinearLayoutManager) layoutManager);
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                //水平方向
                if (viewLayoutPosition == 0) {
                    //这里可以决定,第一个item的分割线
                    outRect.set(0, 0, (int) mDividerSize, 0);//默认只有右边有分割线, 你也可以把左边的分割线添加出来
                } else if (viewLayoutPosition == itemCount - 1) {
                    //这里可以决定, 最后一个item的分割线
                    outRect.set(0, 0, 0, 0);//默认, 最后一个item不需要分割线
                } else {
                    //中间的item,默认只有右边的分割线
                    outRect.set(0, 0, (int) mDividerSize, 0);
                }
            } else {
                //垂直方向
                if (viewLayoutPosition == 0) {
                    //这里可以决定,第一个item的分割线
                    outRect.set(0, 0, 0, (int) mDividerSize);//默认只有右边有分割线, 你也可以把左边的分割线添加出来
                } else if (viewLayoutPosition == itemCount - 1) {
                    //这里可以决定, 最后一个item的分割线
                    outRect.set(0, 0, 0, 0);//默认, 最后一个item不需要分割线
                } else {
                    //中间的item,默认只有右边的分割线
                    outRect.set(0, 0, 0, (int) mDividerSize);
                }
            }
        }
        // 网格布局
        else if (layoutManager instanceof CrashGridLayoutManager) {
            //请注意,这里的分割线 并不包括边框边上的分割线, 只处理不含边框的内部之间的分割线.
            offsetsOfGrid(outRect, ((CrashGridLayoutManager) layoutManager), viewLayoutPosition);
        }
        // 流式布局(暂时不支持)
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
        }
    }

    public void setDividerSize(float dividerSize) {
        mDividerSize = dividerSize;
    }

    public void setDividerDrawableV(Drawable dividerDrawableV) {
        mDividerDrawableV = dividerDrawableV;
    }

    public void setDividerDrawableH(Drawable dividerDrawableH) {
        mDividerDrawableH = dividerDrawableH;
    }

    public void setMarginStart(int marginStart) {
        mMarginStart = marginStart;
    }

    public void setMarginEnd(int marginEnd) {
        mMarginEnd = marginEnd;
    }


    //------------------------------------------私有方法---------------------------------

    /**
     * GridLayoutManager 布局, 计算每个Item, 应该留出的空间(用来绘制分割线的空间)
     */
    private void offsetsOfGrid(Rect outRect, CrashGridLayoutManager layoutManager, int viewLayoutPosition) {
        final int spanCount = layoutManager.getSpanCount();

        GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
        int spanSize = spanSizeLookup.getSpanSize(viewLayoutPosition);
        if (spanSize == 1)
            return;

        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            //垂直方向
//            if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)/*判断是否是最后一排*/) {
//                //最后一排的item, 不添加底部分割线,为了美观
//                outRect.set(0, 0, (int) mDividerSize, 0);
//            } else {
                if (isEndOfGrid(viewLayoutPosition, spanCount)/*判断是否是最靠右的一排*/) {
                    //最靠右的一排,不添加右边的分割线, 为了美观
                    outRect.set(0, 0, 0, (int) mDividerSize);
                } else {
                    outRect.set(0, 0, (int) mDividerSize, (int) mDividerSize);
                }
//            }
        } else {
            //水平方向
//            if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)) {
//                outRect.set(0, 0, 0, (int) mDividerSize);
//            } else {
                if (isEndOfGrid(viewLayoutPosition, spanCount)) {
                    outRect.set(0, 0, (int) mDividerSize, 0);
                } else {
                    outRect.set(0, 0, (int) mDividerSize, (int) mDividerSize);
                }
//            }
        }

    }

    /**
     * 判断 viewLayoutPosition 是否是一排的结束位置 (垂直水平通用)
     */
    private boolean isEndOfGrid(int viewLayoutPosition, int spanCount) {
        return viewLayoutPosition % spanCount == spanCount - 1;
    }

    /**
     * 判断 viewLayoutPosition 所在的位置,是否是最后一排(垂直水平通用)
     */
//    private boolean isLastOfGrid(int itemCount, int viewLayoutPosition, int spanCount) {
//        boolean result = false;
//        final double ceil = Math.ceil(((float) itemCount) / spanCount);
//        if (viewLayoutPosition >= ceil * spanCount - spanCount) {
//            result = true;
//        }
//        return result;
//    }

    private boolean isLastOfGrid(GridLayoutManager manager, int position) {

        int spanSize = manager.getSpanSizeLookup().getSpanSize(position);
        int spanCount = manager.getSpanCount();

        return (spanSize+1)%spanCount == 0;
    }

    private void drawGrid(Canvas c, RecyclerView.LayoutManager manager) {
        final GridLayoutManager layoutManager = (GridLayoutManager) manager;

        final int firstItem = layoutManager.findFirstVisibleItemPosition();
        for (int i = 0; i < layoutManager.getChildCount(); i++) {

            final View view = layoutManager.findViewByPosition(firstItem + i);
            if (null == view)
                continue;

            ///////////////////////////////////////

            // TODO: 2019/04/22
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanSize = spanSizeLookup.getSpanSize(i);
            if (spanSize != 1)
                continue;

            ///////////////////////////////////////

            final int spanCount = layoutManager.getSpanCount();
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            final int viewLayoutPosition = layoutParams.getViewLayoutPosition();//布局时当前View的位置

            if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
                //垂直方向
//                if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)/*判断是否是最后一排*/) {
//                    //最后一排的item, 不添加底部分割线,为了美观
//                    if (viewLayoutPosition != layoutManager.getItemCount() - 1) {
//                        //如果不是最后一个,过滤掉最后一个.最后一item不滑分割线
//                        drawDrawableV(c, view);
//                    }
//                } else {
                    if (isEndOfGrid(viewLayoutPosition, spanCount)/*判断是否是最靠右的一排*/) {
                        //最靠右的一排,不添加右边的分割线, 为了美观
                        drawDrawableH(c, view);

                        // TODO: 2019/04/22
                        drawDrawableV(c, view);
                    } else {
                        drawDrawableH(c, view);
                        drawDrawableV(c, view);
                    }
//                }
            } else {
//                //水平方向
//                if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)) {
//                    if (viewLayoutPosition != layoutManager.getItemCount() - 1) {
//                        //如果不是最后一个,过滤掉最后一个.最后一item不滑分割线
//                        drawDrawableH(c, view);
//                    }
//                } else {
                    if (isEndOfGrid(viewLayoutPosition, spanCount)) {
                        drawDrawableV(c, view);
                    } else {
                        drawDrawableH(c, view);
                        drawDrawableV(c, view);
                    }
//                }
            }
        }
    }

    /**
     * 绘制view对应垂直方向的分割线
     */
    private void drawDrawableV(Canvas c, View view) {

        final RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
        mDividerDrawableV.setBounds(
                view.getRight() + p.rightMargin,
                view.getTop() + mMarginStart,
                (int) (view.getRight() + p.rightMargin + mDividerSize),
                view.getBottom() - mMarginEnd);
        drawDrawable(c, mDividerDrawableV);
    }

    /**
     * 绘制view对应水平方向的分割线
     */
    private void drawDrawableH(Canvas c, View view) {
        final RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
        mDividerDrawableH.setBounds(
                view.getLeft() + mMarginStart,
                view.getBottom() + p.bottomMargin,
                view.getRight() - mMarginEnd,
                (int) (view.getBottom() + p.bottomMargin + mDividerSize));
        drawDrawable(c, mDividerDrawableH);
    }

    private void drawDrawable(Canvas c, Drawable drawable) {
        drawable.draw(c);
    }
}
