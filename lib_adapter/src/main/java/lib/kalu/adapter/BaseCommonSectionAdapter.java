package lib.kalu.adapter;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.SectionModel;

/**
 * description: 分组
 * created by kalu on 2017/5/26 14:54
 */
public abstract class BaseCommonSectionAdapter<T extends SectionModel> extends BaseCommonAdapter<T> {

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
    protected boolean isItem(int itemType) {
        return super.isItem(itemType) && itemType != RecyclerHolder.SECTION_VIEW;
    }

    /**********************************       抽象方法API     **************************************/

    protected abstract @LayoutRes
    int onSection();
}
