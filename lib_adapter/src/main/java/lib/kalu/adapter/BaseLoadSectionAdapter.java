package lib.kalu.adapter;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.SectionModel;

/**
 * description: 分组, 加载更多
 * created by kalu on 2017/5/26 14:54
 */
public abstract class BaseLoadSectionAdapter<T extends SectionModel> extends BaseLoadAdapter<T> {

    @Override
    protected int getItemModelType(int position) {
        return onData().get(position).isSection() ? RecyclerHolder.SECTION_VIEW : 0;
    }

    @Override
    protected RecyclerHolder createHolder(@NonNull ViewGroup parent, int resource, int viewType) {
        if (viewType == RecyclerHolder.SECTION_VIEW) {
            resource = onSection();
        }
        return super.createHolder(parent, resource, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RecyclerHolder.SECTION_VIEW:
                onSection(position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    protected abstract void onSection(int position);

    /**********************************       抽象方法API     **************************************/

    protected abstract @LayoutRes
    int onSection();
}