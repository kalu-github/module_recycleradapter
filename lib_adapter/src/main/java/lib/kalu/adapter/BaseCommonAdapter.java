package lib.kalu.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;

import lib.kalu.adapter.animation.AlphaInAnimation;
import lib.kalu.adapter.animation.BaseAnimation;
import lib.kalu.adapter.animation.ScaleInAnimation;
import lib.kalu.adapter.animation.SlideInBottomAnimation;
import lib.kalu.adapter.animation.SlideInLeftAnimation;
import lib.kalu.adapter.animation.SlideInRightAnimation;
import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.TransModel;

/**
 * description: 没有加载更多
 * created by kalu on 2017/5/26 14:22
 */
public abstract class BaseCommonAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

//    private final RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();

    protected static final String TAG = BaseCommonAdapter.class.getSimpleName();
    protected int mLastPosition = -1;
    // 布局
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private FrameLayout mEmptyLayout;
    // 动画
    private BaseAnimation mAnimation = null;

    /***********************************       方法API       **************************************/

    protected int onMerge(int position) {
        return 1;
    }

    public T getModel(@IntRange(from = 0) int position) {
        return position < onData().size() ? onData().get(position) : null;
    }

    protected int getItemModelType(int position) {
        return super.getItemViewType(position);
    }

    protected RecyclerHolder createHolder(@NonNull ViewGroup parent, @LayoutRes int resource, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(resource, parent, false);
        return new RecyclerHolder(parent, inflate);
    }

    /***********************************       重写API       **************************************/

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int dataCount = onData().size();
        int emptyViewCount = getEmptyViewCount();
        int headCount = getHeadViewCount();
        int footCount = getFootViewCount();
        return dataCount + emptyViewCount + headCount + footCount;
    }

    @Override
    public int getItemViewType(int position) {

        // 没有数据
        if (null == onData() || onData().size() == 0) {
            return RecyclerHolder.EMPTY_VIEW;
        }
        // 有数据
        else {
            int numHead = getHeadViewCount();
            if (position < numHead) {
                return RecyclerHolder.HEAD_VIEW;
            } else {
                // 需要传递的索引位置
                int realPosition = position - numHead;
                int numModel = onData().size();
                return realPosition < numModel ? getItemModelType(realPosition) : RecyclerHolder.FOOT_VIEW;
            }
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 添加缓存池
//        RecyclerView recyclerView = (RecyclerView) parent;
//        if (null == recyclerView.getRecycledViewPool()) {
//            Log.e("BaseCommonAdapter", "onCreateViewHolder => 添加缓存池");
//            recyclerView.setRecycledViewPool(pool);
//        }

        // 空布局
        if (viewType == RecyclerHolder.EMPTY_VIEW) {
            RecyclerHolder holder = new RecyclerHolder(parent, mEmptyLayout);
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
            onHolder(layoutManager, holder, viewType);
            return holder;
        }
        // 头
        else if (viewType == RecyclerHolder.HEAD_VIEW) {
            RecyclerHolder holder = new RecyclerHolder(parent, mHeaderLayout);
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
            onHolder(layoutManager, holder, viewType);
            return holder;
        }
        // 脚
        else if (viewType == RecyclerHolder.FOOT_VIEW) {
            RecyclerHolder holder = new RecyclerHolder(parent, mFooterLayout);
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
            onHolder(layoutManager, holder, viewType);
            return holder;
        }
        // 孩子
        else {
            RecyclerHolder holder = createHolder(parent, onView(), viewType);
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
            onHolder(layoutManager, holder, viewType);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        // 赋值
        if (null != holder && holder.isItem()) {
            int realPosition = holder.getBindingAdapterPosition() - getHeadViewCount();
            onNext(holder, onData().get(realPosition), position);
        }
    }

    /**
     * 回收itemview
     *
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // Log.e("basecommonadapter", "onViewDetachedFromWindow =>");
    }

    /**
     * 复用itemview
     *
     * @param holder Holder of the view being attached
     */
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (null != holder) {
            int itemType = holder.getItemViewType();
            boolean isItem = isItem(itemType);
            if (isItem) {
                if (null != mAnimation) {
                    boolean isOnce = mAnimation.isOnce();
                    int bindingAdapterPosition = holder.getBindingAdapterPosition();
                    if (!isOnce || bindingAdapterPosition > mLastPosition) {
                        long duration = mAnimation.getDuration();
                        Interpolator interpolator = mAnimation.getInterpolator();
                        Animator[] animators = mAnimation.getAnimators(holder.itemView);
                        for (Animator anim : animators) {
                            anim.setDuration(duration);
                            if (null != interpolator) {
                                anim.setInterpolator(interpolator);
                            }
                            anim.start();
                        }
                        mLastPosition = bindingAdapterPosition;
                    }
                }
            } else {
                try {
                    StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                    layoutParams.setFullSpan(true);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        // Log.e("basecommonadapter", "onDetachedFromRecyclerView =>");
        if (null != recyclerView) {
            // Log.e("basecommonadapter", "onDetachedFromRecyclerView => 移除滑动监听");
            recyclerView.clearOnScrollListeners();
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // Log.e("basecommonadapter", "onAttachedToRecyclerView =>");

        if (null == recyclerView)
            return;

        // 滑动监听
        // Log.e("basecommonadapter", "onAttachedToRecyclerView => 添加滑动监听");
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // Log.e("basecommonadapter", "onScrollStateChanged => newState = " + newState);
                onState(newState != 0);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (null == recyclerView)
                    return;

                // 如果是垂直滑动，获取垂直滑动距离
                int verticalOffset = recyclerView.computeVerticalScrollOffset();
                // 如果是水平滑动，获取水平滑动距离
                int horizontalOffset = recyclerView.computeHorizontalScrollOffset();

                // Log.e("basecommonadapter", "onScrolled => horizontalOffset = " + horizontalOffset + ", verticalOffset = " + verticalOffset);
                onScroll(horizontalOffset, verticalOffset);
            }
        });

        // 网格布局
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    boolean isValid = isItem(type);
                    return isValid ? onMerge(position - getHeadViewCount()) : gridManager.getSpanCount();
                }
            });
        }
    }

    /***********************************       重写API       **************************************/

    public final <T extends BaseAnimation> void setLoadAnimation(@NonNull T animation) {
        this.mAnimation = animation;
    }

    public final void setLoadAnimation(@BaseAnimation.BaseAnimationType int animationType) {
        switch (animationType) {
            case BaseAnimation.ALPHAIN:
                setLoadAnimation(new AlphaInAnimation());
                break;
            case BaseAnimation.SCALEIN:
                setLoadAnimation(new ScaleInAnimation());
                break;
            case BaseAnimation.SLIDEIN_BOTTOM:
                setLoadAnimation(new SlideInBottomAnimation());
                break;
            case BaseAnimation.SLIDEIN_LEFT:
                setLoadAnimation(new SlideInLeftAnimation());
                break;
            case BaseAnimation.SLIDEIN_RIGHT:
                setLoadAnimation(new SlideInRightAnimation());
                break;
        }
    }

    /***********************************       展开API       **************************************/

    public void expand(@IntRange(from = 0) int position, boolean animate) {
        position -= getHeadViewCount();

        final T model = getModel(position);
        if (null == model || !(model instanceof TransModel)) return;

        TransModel trans = (TransModel) model;
        if (trans.isExpanded()) return;

        final List tempList = trans.getModelList();
        if (null == tempList || tempList.size() == 0) return;

        // 需要展开
        trans.setExpanded(true);
        final int tempSize = tempList.size();
        final int tempBegin = position + 1;
        onData().addAll(tempBegin, tempList);

        if (animate) {
            notifyItemRangeInserted(tempBegin, tempSize);
        } else {
            notifyDataSetChanged();
        }
    }

    public void expand(@IntRange(from = 0) int position) {
        expand(position, true);
    }

    public void expandAll() {
        for (int i = onData().size() - 1; i >= 0 + getHeadViewCount(); i--) {
            expand(i, true);
        }
    }

    /***********************************       折叠API       **************************************/

    public void collapse(@IntRange(from = 0) int position, boolean animate) {
        position -= getHeadViewCount();

        final T model = getModel(position);
        if (null == model || !(model instanceof TransModel)) return;

        TransModel trans = (TransModel) model;
        if (!trans.isExpanded()) return;

        final List<T> tempList = trans.getModelList();
        if (null == tempList || tempList.size() == 0) return;

        // 需要折叠
        trans.setExpanded(false);
        final int tempSize = tempList.size();
        final int tempBegin = position + 1;
        for (int i = 0; i < tempSize; i++) {
            onData().remove(tempBegin);
        }

        if (animate) {
            notifyItemRangeRemoved(tempBegin, tempSize);
        } else {
            notifyDataSetChanged();
        }
    }

    public void collapse(@IntRange(from = 0) int position) {
        collapse(position, true);
    }

    public void collapseAll() {
        for (int i = onData().size() - 1; i >= 0 + getHeadViewCount(); i--) {
            collapse(i, true);
        }
    }

    /***********************************       索引API       **************************************/

    public int getParentPosition(@NonNull T item) {

        if (null == item || null == onData() || onData().isEmpty()) return -1;

        int position = onData().indexOf(item);
        if (position == -1) return -1;

        int level = (item instanceof TransModel) ? ((TransModel) item).getLevel() : Integer.MAX_VALUE;

        if (level == 0) return position;
        if (level == -1) return -1;

        for (int i = position; i >= 0; i--) {
            T temp = onData().get(i);
            if (!(temp instanceof TransModel)) continue;
            TransModel expandable = (TransModel) temp;
            if (expandable.getLevel() >= 0 && expandable.getLevel() < level) return i;
        }
        return -1;
    }

    public View getViewPosition(RecyclerView recyclerView, int position, @IdRes int viewId) {

        if (recyclerView == null) return null;

        RecyclerHolder viewHolder = (RecyclerHolder) recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolder == null) return null;

        return viewHolder.getView(viewId);
    }

    /***********************************       头部API       **************************************/

    private int getHeadPosition() {
        return getHeadViewCount() == 1 ? -1 : 0;
    }

    public int getHeadViewCount() {
        return (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) ? 0 : 1;
    }

    public View getHead(int position) {

        if (null == mHeaderLayout) {
            return null;
        } else {
            int childCount = mHeaderLayout.getChildCount();
            if (childCount == 0) {
                return null;
            } else if (childCount == 1) {
                return mHeaderLayout.getChildAt(0);
            } else if (childCount - 1 < position) {
                return null;
            } else {
                return mHeaderLayout.getChildAt(position);
            }
        }
    }

    public void addHead(View header) {
        addHead(header, -1);
    }

    public void addHead(Context context, @LayoutRes int layoutResId) {

        View head = LayoutInflater.from(context).inflate(layoutResId, null, false);
        addHead(head, -1);
    }

    public void addHead(View header, int index) {
        addHead(header, index, LinearLayout.VERTICAL);
    }

    public void addHead(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            int position = getHeadPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
    }

    public void changeHead(View header) {
        changeHead(header, 0, LinearLayout.VERTICAL);
    }

    public void changeHead(View header, int index) {
        changeHead(header, index, LinearLayout.VERTICAL);
    }

    public void changeHead(View header, int index, int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            addHead(header, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(header, index);
        }
    }

    public void removeHead(View header) {
        if (getHeadViewCount() == 0) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            int position = getHeadPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllHead() {
        if (getHeadViewCount() == 0) return;

        mHeaderLayout.removeAllViews();
        int position = getHeadPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /***********************************       尾部API       **************************************/

    private int getFootPosition() {

        final int footCount = getFootViewCount();
        if (footCount != 1) return -1;

        final int headCount = getHeadViewCount();
        if (headCount == 1) {
            return getHeadViewCount() + onData().size();
        }
        return onData().size();
    }

    public int getFootViewCount() {
        return (mFooterLayout == null || mFooterLayout.getChildCount() == 0) ? 0 : 1;
    }

    public View getFoot(int position) {

        if (null == mFooterLayout) {
            return null;
        } else {
            int childCount = mFooterLayout.getChildCount();
            if (childCount == 0) {
                return null;
            } else if (childCount == 1) {
                return mFooterLayout.getChildAt(0);
            } else if (childCount - 1 < position) {
                return null;
            } else {
                return mFooterLayout.getChildAt(position);
            }
        }
    }

    public void addFoot(View footer) {
        addFoot(footer, -1, LinearLayout.VERTICAL);
    }

    public void addFoot(View footer, int index) {
        addFoot(footer, index, LinearLayout.VERTICAL);
    }

    public void addFoot(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFootPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
    }

    public void changeFoot(View header) {
        changeFoot(header, 0, LinearLayout.VERTICAL);
    }

    public void changeFoot(View header, int index) {
        changeFoot(header, index, LinearLayout.VERTICAL);
    }

    public void changeFoot(View header, int index, int orientation) {
        if (mFooterLayout == null || mFooterLayout.getChildCount() <= index) {
            addFoot(header, index, orientation);
        } else {
            mFooterLayout.removeViewAt(index);
            mFooterLayout.addView(header, index);
        }
    }

    public void removeFoot(View footer) {
        if (getFootViewCount() == 0) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFootPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllFoot() {
        if (getFootViewCount() == 0) return;

        mFooterLayout.removeAllViews();
        int position = getFootPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /***********************************       空布局API      **************************************/

    public int getEmptyViewCount() {
        if (onData().size() == 0 && null != mEmptyLayout) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setEmptyView(@NonNull Context context, @LayoutRes int layoutResId) {
        View inflate = LayoutInflater.from(context).inflate(layoutResId, null, false);
        setEmptyView(inflate);
    }

    public View getEmptyView() {
        return mEmptyLayout;
    }

    public void setEmptyView(@NonNull View view) {

        if (null == mEmptyLayout) {
            mEmptyLayout = new FrameLayout(view.getContext());
        }

        RecyclerView.LayoutParams layoutParams;
        if (null == mEmptyLayout.getLayoutParams()) {
            layoutParams = new RecyclerView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        } else {
            layoutParams = (RecyclerView.LayoutParams) mEmptyLayout.getLayoutParams();
        }
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mEmptyLayout.setLayoutParams(layoutParams);
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(view);

        int nullCount = getEmptyViewCount();
        if (nullCount != 1)
            return;

        int position = 0;
        if (getHeadViewCount() != 0) {
            position++;
        }
        notifyItemInserted(position);
    }

    public void clearEmptyView() {

        if (null == mEmptyLayout) return;
        mEmptyLayout.removeAllViews();
        mEmptyLayout.setVisibility(View.GONE);
    }

    public void setEmptyText(int viewId, @StringRes int strid) {

        if (null == mEmptyLayout)
            return;

        final View text = mEmptyLayout.findViewById(viewId);
        if (null == text || !(text instanceof TextView))
            return;

        ((TextView) text).setText(strid);
    }

    public void setEmptyImage(int viewId, @DrawableRes int strid) {

        if (null == mEmptyLayout)
            return;

        final View text = mEmptyLayout.findViewById(viewId);
        if (null == text || !(text instanceof ImageView))
            return;

        ((ImageView) text).setImageResource(strid);
    }

//    public void setNullVisibility(int viewId, int visibility) {
//
//        if (null == mEmptyLayout)
//            return;
//
//        final View text = mEmptyLayout.findViewById(viewId);
//        if (null == text)
//            return;
//
//        text.setVisibility(visibility);
//    }

    /***********************************       数据API      **************************************/

    public void clearInsertData(@Nullable List<T> data) {

        onData().clear();
        mLastPosition = -1;
        onData().addAll(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull T data) {
        onData().add(data);
        notifyItemInserted(onData().size() + getHeadViewCount());
        notifyDataSetChanged();
    }

    public void addData(@NonNull Collection<? extends T> newData) {
        onData().addAll(newData);
        notifyItemRangeInserted(onData().size() - newData.size() + getHeadViewCount(), newData.size());
        notifyDataSetChanged();
    }

    public void remove(@IntRange(from = 0) int position) {
        onData().remove(position);
        int internalPosition = position + getHeadViewCount();
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition, onData().size() - internalPosition);
    }

    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        onData().set(index, data);
        notifyItemChanged(index + getHeadViewCount());
    }

    /**********************************       抽象方法API     **************************************/

    protected abstract @LayoutRes
    int onView();

    protected abstract @NonNull
    List<T> onData();

    protected abstract void onNext(RecyclerHolder holder, T model, int position);

    /**
     * 滑动距离
     *
     * @param horizontalOffset 水平位移
     * @param verticalOffset   垂直位移
     */
    protected void onScroll(int horizontalOffset, int verticalOffset) {
    }

    protected boolean isItem(int itemType) {
        return itemType != RecyclerHolder.HEAD_VIEW && itemType != RecyclerHolder.FOOT_VIEW && itemType != RecyclerHolder.EMPTY_VIEW;
    }

    /**
     * 滑动状态
     *
     * @param isScroll 是否正在滑动
     */
    protected void onState(boolean isScroll) {
    }

    protected void onHolder(RecyclerView.LayoutManager manager, RecyclerHolder holder, int type) {
    }

    protected int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getBindingAdapterPosition() - getHeadViewCount();
    }

    /***********************************       动画API       **************************************/

    @IntDef({BaseAnimation.ALPHAIN, BaseAnimation.SCALEIN, BaseAnimation.SLIDEIN_BOTTOM, BaseAnimation.SLIDEIN_LEFT, BaseAnimation.SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }
}