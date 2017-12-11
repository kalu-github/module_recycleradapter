package lib.kalu.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * description: 没有加载更多
 * created by kalu on 2017/5/26 14:22
 */
public abstract class BaseCommonAdapter<T, K extends RecyclerHolder> extends RecyclerView.Adapter<K> {

    protected static final String TAG = BaseCommonAdapter.class.getSimpleName();

    // 是否仅仅第一次加载显示动画
    private boolean isOpenAnimFirstOnly = true;
    // 显示动画
    private boolean isOpenAnim = false;
    // 动画显示时间
    private int mAnimTime = 300;

    protected int mLastPosition = -1;

    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    // 布局ID
    protected int mLayoutResId;
    // 数据集合
    private List<T> mModelList;
    private final Interpolator mInterpolator = new LinearInterpolator();

    protected LinearLayout mHeaderLayout, mFooterLayout;
    protected FrameLayout mEmptyLayout;

    /***********************************     构造器API       **************************************/

    /**
     * 分类型, 不对外暴露API
     */
    BaseCommonAdapter(@Nullable List<T> data) {
        this(data, 0);
    }

    /**
     * 普通, 对外暴露API
     */
    public BaseCommonAdapter(@Nullable List<T> data, @LayoutRes int layoutResId) {
        this.mModelList = data;
        this.mLayoutResId = layoutResId;
    }

    /***********************************       方法API       **************************************/

    protected int onMerge(int position) {
        return 1;
    }

    public T getModel(@IntRange(from = 0) int position) {
        return position < mModelList.size() ? mModelList.get(position) : null;
    }

    protected int getItemModelType(int position) {
        return super.getItemViewType(position);
    }

    protected K createModelHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(mLayoutResId, parent, false);
        return createSimpleHolder(itemView);
    }

    protected K createSimpleHolder(View view) {
        Class clazz = getClass();
        Class z = null;
        while (null == z && null != clazz) {
            z = createKClass(clazz);
            clazz = clazz.getSuperclass();
        }
        K k = createKModel(z, view);
        return null != k ? k : (K) new RecyclerHolder(view);
    }

    private Class createKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (RecyclerHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    private K createKModel(Class z, View view) {
        try {
            Constructor constructor;
            String buffer = Modifier.toString(z.getModifiers());
            String className = z.getName();
            if (className.contains("$") && !buffer.contains("static")) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                return (K) constructor.newInstance(view);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    protected void setModelStyle(RecyclerView.ViewHolder holder, boolean isModel) {

        if (isModel) {
            if (!isOpenAnim) return;
            if (!isOpenAnimFirstOnly || holder.getLayoutPosition() > mLastPosition) {
                for (Animator anim : mSelectAnimation.getAnimators(holder.itemView)) {
                    anim.setDuration(mAnimTime).start();
                    anim.setInterpolator(mInterpolator);
                }
                mLastPosition = holder.getLayoutPosition();
            }
        } else {

            if (null == holder || null == holder.itemView) return;

            final ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (null == layoutParams) return;

            final boolean isStaggeredGridLayoutManager = (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams);
            if (!isStaggeredGridLayoutManager) return;

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan(true);
        }
    }

    protected boolean isModelType(int type) {
        return type != RecyclerHolder.HEAD_VIEW && type != RecyclerHolder.FOOT_VIEW && type != RecyclerHolder.NULL_VIEW;
    }

    /***********************************       重写API       **************************************/

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mModelList.size() + getNullCount() + getHeadCount() + getFootCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (getNullCount() == 1) {
            boolean header = getHeadCount() != 0;
            switch (position) {
                case 0:
                    return header ? RecyclerHolder.HEAD_VIEW : RecyclerHolder.NULL_VIEW;
                case 1:
                    return header ? RecyclerHolder.NULL_VIEW : RecyclerHolder.FOOT_VIEW;
                case 2:
                    return RecyclerHolder.FOOT_VIEW;
                default:
                    return RecyclerHolder.NULL_VIEW;
            }
        }
        int numHeaders = getHeadCount();
        if (position < numHeaders) {
            return RecyclerHolder.HEAD_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = mModelList.size();
            if (adjPosition < adapterCount) {
                return getItemModelType(adjPosition);
            } else {
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFootCount();
                return adjPosition < numFooters ? RecyclerHolder.FOOT_VIEW : RecyclerHolder.LOAD_VIEW;
            }
        }
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K holder;
        switch (viewType) {
            case RecyclerHolder.NULL_VIEW:
                holder = createSimpleHolder(mEmptyLayout);
                break;
            case RecyclerHolder.HEAD_VIEW:
                holder = createSimpleHolder(mHeaderLayout);
                break;
            case RecyclerHolder.FOOT_VIEW:
                holder = createSimpleHolder(mFooterLayout);
                break;
            default:
                holder = createModelHolder(parent, viewType);
        }
        return holder;
    }

    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);

        int type = holder.getItemViewType();
        boolean isModel = isModelType(type);
        setModelStyle(holder, isModel);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (!(manager instanceof GridLayoutManager)) return;

        final GridLayoutManager gridManager = ((GridLayoutManager) manager);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int type = getItemViewType(position);
                final boolean modelType = isModelType(type);
                return modelType ? onMerge(position - getHeadCount()) : gridManager.getSpanCount();
            }
        });
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        switch (holder.getItemViewType()) {
            case RecyclerHolder.HEAD_VIEW:
            case RecyclerHolder.NULL_VIEW:
            case RecyclerHolder.FOOT_VIEW:
                break;
            default:
                onNext(holder, mModelList.get(holder.getLayoutPosition() - getHeadCount()), position);
                break;
        }
    }

    /***********************************       动画API       **************************************/

    @IntDef({BaseAnimation.ALPHAIN, BaseAnimation.SCALEIN, BaseAnimation.SLIDEIN_BOTTOM, BaseAnimation.SLIDEIN_LEFT, BaseAnimation.SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    public void openLoadAnimation(@AnimationType int animationType, int animTime, boolean isOpenAnimFirstOnly) {
        this.isOpenAnim = true;
        this.isOpenAnimFirstOnly = isOpenAnimFirstOnly;
        this.mAnimTime = animTime;

        switch (animationType) {
            case BaseAnimation.ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case BaseAnimation.SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case BaseAnimation.SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case BaseAnimation.SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case BaseAnimation.SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    /***********************************       展开API       **************************************/

    public void expand(@IntRange(from = 0) int position, boolean animate) {
        position -= getHeadCount();

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
        mModelList.addAll(tempBegin, tempList);

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
        for (int i = mModelList.size() - 1; i >= 0 + getHeadCount(); i--) {
            expand(i, true);
        }
    }

    /***********************************       折叠API       **************************************/

    public void collapse(@IntRange(from = 0) int position, boolean animate) {
        position -= getHeadCount();

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
            mModelList.remove(tempBegin);
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
        for (int i = mModelList.size() - 1; i >= 0 + getHeadCount(); i--) {
            collapse(i, true);
        }
    }

    /***********************************       索引API       **************************************/

    public int getParentPosition(@NonNull T item) {

        if (null == item || null == mModelList || mModelList.isEmpty()) return -1;

        int position = mModelList.indexOf(item);
        if (position == -1) return -1;

        int level = (item instanceof TransModel) ? ((TransModel) item).getLevel() : Integer.MAX_VALUE;

        if (level == 0) return position;
        if (level == -1) return -1;

        for (int i = position; i >= 0; i--) {
            T temp = mModelList.get(i);
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
        return getHeadCount() == 1 ? -1 : 0;
    }

    public int getHeadCount() {
        return (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) ? 0 : 1;
    }

    public LinearLayout getHeadLayout() {
        return mHeaderLayout;
    }

    public int addHeadView(View header) {
        return addHeadView(header, -1);
    }

    public int addHeadView(View header, int index) {
        return addHeadView(header, index, LinearLayout.VERTICAL);
    }

    public int addHeadView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new LayoutParams(WRAP_CONTENT, MATCH_PARENT));
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
        return index;
    }

    public int setHeadView(View header) {
        return setHeadView(header, 0, LinearLayout.VERTICAL);
    }

    public int setHeadView(View header, int index) {
        return setHeadView(header, index, LinearLayout.VERTICAL);
    }

    public int setHeadView(View header, int index, int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            return addHeadView(header, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(header, index);
            return index;
        }
    }

    public void removeHeadView(View header) {
        if (getHeadCount() == 0) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            int position = getHeadPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllHeadView() {
        if (getHeadCount() == 0) return;

        mHeaderLayout.removeAllViews();
        int position = getHeadPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /***********************************       尾部API       **************************************/

    private int getFootPosition() {

        final int footCount = getFootCount();
        if (footCount != 1) return -1;

        final int headCount = getHeadCount();
        if (headCount == 1) {
            return getHeadCount() + mModelList.size();
        }
        return mModelList.size();
    }

    public int getFootCount() {
        return (mFooterLayout == null || mFooterLayout.getChildCount() == 0) ? 0 : 1;
    }

    public LinearLayout getFootLayout() {
        return mFooterLayout;
    }

    public int addFootView(View footer) {
        return addFootView(footer, -1, LinearLayout.VERTICAL);
    }

    public int addFootView(View footer, int index) {
        return addFootView(footer, index, LinearLayout.VERTICAL);
    }

    public int addFootView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new LayoutParams(WRAP_CONTENT, MATCH_PARENT));
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
        return index;
    }

    public int setFootView(View header) {
        return setFootView(header, 0, LinearLayout.VERTICAL);
    }

    public int setFootView(View header, int index) {
        return setFootView(header, index, LinearLayout.VERTICAL);
    }

    public int setFootView(View header, int index, int orientation) {
        if (mFooterLayout == null || mFooterLayout.getChildCount() <= index) {
            return addFootView(header, index, orientation);
        } else {
            mFooterLayout.removeViewAt(index);
            mFooterLayout.addView(header, index);
            return index;
        }
    }

    public void removeFootView(View footer) {
        if (getFootCount() == 0) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFootPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllFootView() {
        if (getFootCount() == 0) return;

        mFooterLayout.removeAllViews();
        int position = getFootPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /***********************************       空布局API      **************************************/

    public int getNullCount() {

        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) return 0;

        if (mModelList.size() != 0) return 0;
        return 1;
    }

    public void setNullView(Context context, int layoutResId) {
        View view = LayoutInflater.from(context).inflate(layoutResId, null, false);
        setNullView(view);
    }

    public void setNullView(View emptyView) {
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);

        if (!insert) return;
        if (getNullCount() == 1) {
            int position = 0;
            if (getHeadCount() != 0) {
                position++;
            }
            notifyItemInserted(position);
        }
    }

    public View getNullView() {
        return mEmptyLayout;
    }

    public void removeNullView() {

        if (null == mEmptyLayout) return;
        mEmptyLayout.removeAllViews();
        mEmptyLayout.setVisibility(View.GONE);
    }

    /***********************************       数据API      **************************************/

    public void clearAddData(@Nullable List<T> data) {

        mModelList.clear();
        mLastPosition = -1;
        mModelList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull T data) {
        mModelList.add(data);
        notifyItemInserted(mModelList.size() + getHeadCount());
        notifyDataSetChanged();
    }

    public void addData(@NonNull Collection<? extends T> newData) {
        mModelList.addAll(newData);
        notifyItemRangeInserted(mModelList.size() - newData.size() + getHeadCount(), newData.size());
        notifyDataSetChanged();
    }

    public void remove(@IntRange(from = 0) int position) {
        mModelList.remove(position);
        int internalPosition = position + getHeadCount();
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition, mModelList.size() - internalPosition);
    }

    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        mModelList.set(index, data);
        notifyItemChanged(index + getHeadCount());
    }

    @NonNull
    public List<T> getData() {
        return mModelList;
    }

    /**********************************       抽象方法API     **************************************/

    protected abstract void onNext(K holder, T model, int position);
}