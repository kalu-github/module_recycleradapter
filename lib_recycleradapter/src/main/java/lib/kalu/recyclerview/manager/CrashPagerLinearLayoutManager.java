package lib.kalu.recyclerview.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * description: 垂直水平翻页
 * created by kalu on 2018/6/5 14:27
 */
public final class CrashPagerLinearLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {

    private final PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
    private int mDrift;//位移，用来判断移动方向

    public CrashPagerLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        }
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        view.addOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        view.removeOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View viewIdle = mPagerSnapHelper.findSnapView(this);
                int positionIdle = getPosition(viewIdle);
              //  Log.e("onScrollStateChanged", "count = " + getChildCount() + ", positionIdle = " + positionIdle);
                if (mOnPagerChangeListener == null || getChildCount() != 1) break;
                mOnPagerChangeListener.onPageSelect(positionIdle, positionIdle == 0, positionIdle == (getItemCount() - 1));
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                View viewDrag = mPagerSnapHelper.findSnapView(this);
                int positionDrag = getPosition(viewDrag);
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                View viewSettling = mPagerSnapHelper.findSnapView(this);
                int positionSettling = getPosition(viewSettling);
                break;

        }
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        if (null == mOnPagerChangeListener) return;
        mOnPagerChangeListener.onPageFinish();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        if (null == mOnPagerChangeListener) return;
        mOnPagerChangeListener.onPageDetach(mDrift >= 0, getPosition(view));
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    /************************************************************/

    private OnPagerChangeListener mOnPagerChangeListener;

    public interface OnPagerChangeListener {

        void onPageDetach(boolean isNext, int position);

        void onPageSelect(int position, boolean isTop, boolean isBottom);

        void onPageFinish();
    }

    public void setOnPagerChangeListener(OnPagerChangeListener listener) {
        this.mOnPagerChangeListener = listener;
    }
}
