package com.demo.adapter.widget.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.adapter.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * description:
 * created by kalu on 2017/1/13 11:48
 */
public class TextTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private final String TAG = TextTabLayout.class.getSimpleName();

    private ViewPager mViewPager;
    private ArrayList<String> mTitles;
    private LinearLayout tabContainer;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private int mTabCount;
    private final Rect mIndicatorRect = new Rect(); // 用于绘制显示器
    private final Rect mTabRect = new Rect(); // 用于实现滚动居中
    private final GradientDrawable mIndicatorDrawable = new GradientDrawable();

    private final Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mTrianglePath = new Path();

    private static final int INDICATOR_STYLE_NORMAL = 0; // 常规
    private static final int INDICATOR_STYLE_TRIANGLE = 1; // 三角形
    private static final int INDICATOR_STYLE_BLOCK = 2; // 背景色块
    private int mIndicatorStyle = INDICATOR_STYLE_NORMAL;

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private int mIndicatorGravity;
    private boolean mIndicatorWidthEqualTitle;

    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextsize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    private int mLastScrollX;
    private boolean mSnapOnTabClick;

    /**********************************************************************************************/

    public TextTabLayout(Context context) {
        this(context, null, 0);
    }

    public TextTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        tabContainer = new LinearLayout(context);
        addView(tabContainer);

        TypedArray customArray = context.obtainStyledAttributes(attrs, R.styleable.TextTabLayout);
        mIndicatorStyle = customArray.getInt(R.styleable.TextTabLayout_ttl_indicator_style, INDICATOR_STYLE_NORMAL);
        mIndicatorColor = customArray.getColor(R.styleable.TextTabLayout_ttl_indicator_color, Color.parseColor(mIndicatorStyle == INDICATOR_STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_height,
                dp2px(mIndicatorStyle == INDICATOR_STYLE_TRIANGLE ? 4 : (mIndicatorStyle == INDICATOR_STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_width, dp2px(mIndicatorStyle == INDICATOR_STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_corner_radius, dp2px(mIndicatorStyle == INDICATOR_STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_margin_left, dp2px(2));
        mIndicatorMarginTop = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_margin_top, dp2px(mIndicatorStyle == INDICATOR_STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_margin_right, dp2px(0));
        mIndicatorMarginBottom = customArray.getDimension(R.styleable.TextTabLayout_ttl_indicator_margin_bottom, dp2px(mIndicatorStyle == INDICATOR_STYLE_BLOCK ? 7 : 0));
        mIndicatorGravity = customArray.getInt(R.styleable.TextTabLayout_ttl_indicator_gravity, Gravity.BOTTOM);
        mIndicatorWidthEqualTitle = customArray.getBoolean(R.styleable.TextTabLayout_ttl_indicator_width_equal_title, false);
        mUnderlineColor = customArray.getColor(R.styleable.TextTabLayout_ttl_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = customArray.getDimension(R.styleable.TextTabLayout_ttl_underline_height, dp2px(0));
        mUnderlineGravity = customArray.getInt(R.styleable.TextTabLayout_ttl_underline_gravity, Gravity.BOTTOM);
        mDividerColor = customArray.getColor(R.styleable.TextTabLayout_ttl_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = customArray.getDimension(R.styleable.TextTabLayout_ttl_divider_width, dp2px(0));
        mDividerPadding = customArray.getDimension(R.styleable.TextTabLayout_ttl_divider_padding, dp2px(12));
        mTextsize = customArray.getDimension(R.styleable.TextTabLayout_ttl_text_size, sp2px(14));
        mTextSelectColor = customArray.getColor(R.styleable.TextTabLayout_ttl_text_color_select, Color.parseColor("#ffffff"));
        mTextUnselectColor = customArray.getColor(R.styleable.TextTabLayout_ttl_text_color_normal, Color.parseColor("#AAffffff"));
        mTextBold = customArray.getInt(R.styleable.TextTabLayout_ttl_text_bold, TEXT_BOLD_NONE);
        mTextAllCaps = customArray.getBoolean(R.styleable.TextTabLayout_ttl_text_capital_letter, false);
        mTabSpaceEqual = customArray.getBoolean(R.styleable.TextTabLayout_ttl_tab_space_equal, false);
        mTabWidth = customArray.getDimension(R.styleable.TextTabLayout_ttl_tab_width, dp2px(-1));
        mTabPadding = customArray.getDimension(R.styleable.TextTabLayout_ttl_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(20));
        customArray.recycle();
    }

    /**********************************************************************************************/


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = tabContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // TODO: 2017/1/13 修改
        // 最下面实线
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                // canvas.drawRect(paddingLeft, height - mUnderlineHeight, tabContainer.getWidth() + paddingLeft, height, mRectPaint);
                canvas.drawRect(0, height - mUnderlineHeight, getWidth(), height, mRectPaint);
            } else {
                // canvas.drawRect(paddingLeft, 0, tabContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
                canvas.drawRect(0, 0, getWidth(), mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line

        calcIndicatorRect();

        // 画指示器
        switch (mIndicatorStyle) {

            // 三角形
            case INDICATOR_STYLE_TRIANGLE:

                if (mIndicatorHeight <= 0) break;

                mTrianglePaint.setColor(mIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - 2 * mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
                break;
            // 背景色块
            case INDICATOR_STYLE_BLOCK:

                boolean temp2 = (mIndicatorHeight <= 0);

                if (temp2) {
                    mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
                    break;
                } else {
                    if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                        mIndicatorCornerRadius = mIndicatorHeight / 2;
                    }

                    int left2 = paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left;
                    int right2 = (int) mIndicatorMarginTop;
                    int top2 = (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight);
                    int bottom2 = (int) (mIndicatorMarginTop + mIndicatorHeight);
                    mIndicatorDrawable.setBounds(left2, right2, top2, bottom2);

                    mIndicatorDrawable.setColor(mIndicatorColor);
                    mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                    mIndicatorDrawable.draw(canvas);
                }
                break;
            // 横线
            default:

                if (mIndicatorHeight <= 0) break;
                mIndicatorDrawable.setColor(mIndicatorColor);

                boolean temp3 = mIndicatorGravity == Gravity.BOTTOM;

                int left3 = paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left;
                int right3 = paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight;
                int top3 = temp3 ? (height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom) : (int) mIndicatorMarginTop;
                int bottom3 = temp3 ? (height - (int) mIndicatorMarginBottom) : (int) mIndicatorHeight + (int) mIndicatorMarginTop;
                mIndicatorDrawable.setBounds(left3, top3, right3, bottom3);

                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);

                break;
        }
    }

    /**
     * 关联ViewPager
     */
    public void setViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        this.mViewPager = vp;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    public void setViewPager(ViewPager vp, String[] titles) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        if (titles.length != vp.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }

        this.mViewPager = vp;
        mTitles = new ArrayList<>();
        Collections.addAll(mTitles, titles);

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) {
        if (vp == null) {
            throw new IllegalStateException("ViewPager can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        this.mViewPager = vp;
        this.mViewPager.setAdapter(new InnerPagerAdapter(fa.getSupportFragmentManager(), fragments, titles));

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        tabContainer.removeAllViews();
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();
        TextView tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = new TextView(getContext());
            tabView.setGravity(Gravity.CENTER);
            CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(i) : mTitles.get(i);
            addTab(i, pageTitle.toString(), tabView);
        }

        updateTabStyles();
    }

    public void addNewTab(String title) {
        TextView tabView = new TextView(getContext());
        tabView.setGravity(Gravity.CENTER);
        if (mTitles != null) {
            mTitles.add(title);
        }

        CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(mTabCount) : mTitles.get(mTabCount);
        addTab(mTabCount, pageTitle.toString(), tabView);
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();

        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title, View tabView) {
        TextView tv_tab_title = (TextView) tabView;
        if (tv_tab_title != null) {
            if (title != null) {

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                }

                tv_tab_title.setText(title);
            }
        }

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = tabContainer.indexOfChild(v);
                if (position != -1) {
                    if (mViewPager.getCurrentItem() != position) {
                        if (mSnapOnTabClick) {
                            mViewPager.setCurrentItem(position, false);
                        } else {
                            mViewPager.setCurrentItem(position);
                        }

                        if (mListener != null) {
                            mListener.onTabSelect(position);
                        }
                    } else {
                        if (mListener != null) {
                            mListener.onTabReselect(position);
                        }
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }

        tabContainer.addView(tabView, position, lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = tabContainer.getChildAt(i);
//            v.setPadding((int) mTabPadding, v.getPaddingTop(), (int) mTabPadding, v.getPaddingBottom());
            TextView tv_tab_title = (TextView) v;
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);
                tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
                if (mTextAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position:当前View的位置
         * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
         */
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }

        int offset = (int) (mCurrentPositionOffset * tabContainer.getChildAt(mCurrentTab).getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = tabContainer.getChildAt(mCurrentTab).getLeft() + offset;

        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0);
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = tabContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView;

            if (tab_title != null) {
                tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tab_title.getPaint().setFakeBoldText(isSelect);
                }
            }
        }
    }

    private float margin;

    private void calcIndicatorRect() {
        View currentTabView = tabContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == INDICATOR_STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            TextView tab_title = (TextView) currentTabView;
            mTextPaint.setTextSize(mTextsize);
            float textWidth = mTextPaint.measureText(tab_title.getText().toString());
            margin = (right - left - textWidth) / 2;
        }

        if (this.mCurrentTab < mTabCount - 1) {
            View nextTabView = tabContainer.getChildAt(this.mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + mCurrentPositionOffset * (nextTabLeft - left);
            right = right + mCurrentPositionOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == INDICATOR_STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                TextView next_tab_title = (TextView) nextTabView;
                mTextPaint.setTextSize(mTextsize);
                float nextTextWidth = mTextPaint.measureText(next_tab_title.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                margin = margin + mCurrentPositionOffset * (nextMargin - margin);
            }
        }

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == INDICATOR_STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (int) (left + margin - 1);
            mIndicatorRect.right = (int) (right - margin - 1);
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            if (this.mCurrentTab < mTabCount - 1) {
                View nextTab = tabContainer.getChildAt(this.mCurrentTab + 1);
                indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
            }

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

    public void setCurrentTab(int currentTab) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab);
    }

    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab, smoothScroll);
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setTabPadding(float tabPadding) {
        this.mTabPadding = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        this.mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
        invalidate();
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        this.mUnderlineHeight = dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        this.mDividerWidth = dp2px(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        this.mDividerPadding = dp2px(dividerPadding);
        invalidate();
    }

    public void setTextsize(float textsize) {
        this.mTextsize = sp2px(textsize);
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public void setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
    }


    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    public float getTextsize() {
        return mTextsize;
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public TextView getTitleView(int tab) {
        View tabView = tabContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView;
        return tv_tab_title;
    }

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**********************************************************************************************/

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && tabContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
