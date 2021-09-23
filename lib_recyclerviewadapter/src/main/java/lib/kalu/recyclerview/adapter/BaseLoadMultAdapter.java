package lib.kalu.recyclerview.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import lib.kalu.recyclerview.viewholder.RecyclerHolder;
import lib.kalu.recyclerview.model.MultModel;

/**
 * description: 分类型, 加载更多
 * created by kalu on 2017/5/26 15:02
 */
public abstract class BaseLoadMultAdapter<T extends MultModel> extends BaseLoadAdapter<T> {

    private final SparseArray<Integer> mResIdList = new SparseArray<>();

    public BaseLoadMultAdapter() {
        onMult();
    }

    @Override
    protected int getItemModelType(int position) {
        MultModel item = onData().get(position);
        return item.getMultType();
    }

    @Override
    protected RecyclerHolder createHolder(@NonNull ViewGroup parent, int resource, int viewType) {
        resource = getMultId(viewType);
        return super.createHolder(parent, resource, viewType);
    }

    private int getMultId(int viewType) {
        return mResIdList.get(viewType);
    }

    protected void addMult(int type, @LayoutRes int layoutResId) {
        mResIdList.put(type, layoutResId);
    }

    @Override
    protected int onView() {
        return 0;
    }

    /**
     * 添加分类型布局
     */
    protected abstract void onMult();
}