package lib.kalu.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.SectionModel;

/**
 * description: 分组, 加载更多
 * created by kalu on 2017/5/26 14:54
 */
public abstract class BaseLoadSectionAdapter<T extends SectionModel> extends BaseLoadAdapter<T> {

    private @LayoutRes
    int sectionResId;

    public BaseLoadSectionAdapter(@Nullable List<T> data, @LayoutRes int itemResId, @LayoutRes int loadResId, int sectionResId) {
        super(data, itemResId, loadResId);
        this.sectionResId = sectionResId;
    }

    @Override
    protected int getItemModelType(int position) {
        return getData().get(position).isSection() ? RecyclerHolder.SECTION_VIEW : 0;
    }

    @Override
    protected RecyclerHolder createModelHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerHolder.SECTION_VIEW) {
            final View inflate = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(sectionResId, parent, false);
            return createSimpleHolder(inflate);
        }
        return super.createModelHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    protected boolean isModelType(int type) {
        return super.isModelType(type) && (type != RecyclerHolder.SECTION_VIEW);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RecyclerHolder.SECTION_VIEW:
                setModelStyle(holder, false);
                onSection(position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    protected abstract void onSection(int position);
}