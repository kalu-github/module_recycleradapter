package com.demo.adapter.adapter;

import android.view.View;

import com.demo.adapter.R;
import com.demo.adapter.entity.Level0Item;
import com.demo.adapter.entity.Level1Item;
import com.demo.adapter.entity.Person;

import java.util.List;

import lib.kalu.adapter.BaseCommonMultAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;
import lib.kalu.adapter.model.MultModel;

public class TransAdapter extends BaseCommonMultAdapter<MultModel> {

    private static final String TAG = TransAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_PERSON = 2;

    public TransAdapter(List<MultModel> data) {
        super(data);
    }

    @Override
    protected void onMult() {
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
        addItemType(TYPE_PERSON, R.layout.item_expandable_lv2);
    }

    @Override
    protected void onNext(final RecyclerHolder holder, final MultModel item, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                switch (holder.getLayoutPosition() %
                        3) {
                    case 0:
                        holder.setImageResource(R.id.iv_head, R.mipmap.ic_launcher);
                        break;
                    case 1:
                        holder.setImageResource(R.id.iv_head, R.mipmap.ic_launcher);
                        break;
                    case 2:
                        holder.setImageResource(R.id.iv_head, R.mipmap.ic_launcher);
                        break;
                }
                final Level0Item lv0 = (Level0Item) item;
                holder.setText(R.id.title, lv0.title)
                        .setText(R.id.sub_title, lv0.subTitle)
                        .setImageResource(R.id.iv, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item) item;
                holder.setText(R.id.title, lv1.title)
                        .setText(R.id.sub_title, lv1.subTitle)
                        .setImageResource(R.id.iv, lv1.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv1.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_PERSON:
                final Person person = (Person) item;
                holder.setText(R.id.tv, person.name + " <=> " + position + "\n二级 <=> " + getParentPosition(person));
                break;
        }
    }
}
