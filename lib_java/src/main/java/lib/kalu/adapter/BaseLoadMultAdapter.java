package lib.kalu.adapter;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.MultModel;

/**
 * description: 分类型, 加载更多
 * created by kalu on 2017/5/26 15:02
 */
public abstract class BaseLoadMultAdapter<T extends MultModel> extends BaseLoadAdapter<T> {

    private final SparseArray<Integer> mResIdList = new SparseArray<>();

    public BaseLoadMultAdapter(List<T> data, @LayoutRes int loadResId) {
        super(data, loadResId);
        onMult();
    }

    @Override
    protected int getItemModelType(int position) {
        Object item = getData().get(position);
        return item instanceof MultModel ? ((MultModel) item).getItemType() : MultModel.TYPE_1;
    }

    @Override
    protected RecyclerHolder createModelHolder(ViewGroup parent, int viewType) {
        final int layoutId = getLayoutId(viewType);
        final View inflate = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(layoutId, parent, false);
        return createSimpleHolder(inflate);
    }

    private int getLayoutId(int viewType) {
        return mResIdList.get(viewType);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        mResIdList.put(type, layoutResId);
    }

    /**
     * 添加分类型布局
     */
    protected abstract void onMult();
}