package lib.kalu.adapter;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.SectionModel;

/**
 * description: 分组
 * created by kalu on 2017/5/26 14:54
 */
public abstract class BaseCommonSectionAdapter<T extends SectionModel> extends BaseCommonAdapter<T> {

    @Override
    protected int getItemModelType(int position) {
        return getData().get(position).isSection() ? RecyclerHolder.SECTION_VIEW : 0;
    }

    @Override
    protected RecyclerHolder createModelHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerHolder.SECTION_VIEW) {
            final View inflate = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(initSectionResId(), parent, false);
            return createSimpleHolder(inflate);
        } else {
            return super.createModelHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RecyclerHolder.SECTION_VIEW:
                setModelStyle(holder, false);
                final int headCount = getHeadCount();
                int realPosition = holder.getLayoutPosition() - headCount;
                onSection(holder, onData().get(realPosition), realPosition);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    protected boolean isModelType(int type) {
        return super.isModelType(type) && (type != RecyclerHolder.SECTION_VIEW);
    }

    /**********************************************************************************************/

    protected abstract @LayoutRes
    int initSectionResId();

    protected abstract void onSection(RecyclerHolder holder, T model, int position);
}
