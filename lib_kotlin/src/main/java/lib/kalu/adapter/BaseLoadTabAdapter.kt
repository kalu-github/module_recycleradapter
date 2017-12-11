package lib.kalu.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.model.TabModel

/**
 * description: 网格多个合并, 加载更多
 * created by kalu on 2017/5/26 14:54
 */
abstract class BaseLoadTabAdapter<T : TabModel<*>, K : RecyclerHolder>(data: MutableList<T>?, @LayoutRes itemResId: Int, @LayoutRes loadResId: Int, @field:LayoutRes
private val sectionResId: Int) : BaseLoadAdapter<T, K>(data, itemResId, loadResId) {

    override fun getItemModelType(position: Int): Int {
        return if (data!![position].isTab) RecyclerHolder.SECTION_VIEW else 0
    }

    override fun createModelHolder(parent: ViewGroup, viewType: Int): K {
        if (viewType == RecyclerHolder.SECTION_VIEW) {
            val inflate = LayoutInflater.from(parent.context.applicationContext).inflate(sectionResId, parent, false)
            return createSimpleHolder(inflate)
        }
        return super.createModelHolder(parent, viewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun isModelType(type: Int): Boolean {
        return super.isModelType(type) && type != RecyclerHolder.SECTION_VIEW
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        when (holder.itemViewType) {
            RecyclerHolder.SECTION_VIEW -> {
                setModelStyle(holder, false)
                onSection(position)
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }

    protected abstract fun onSection(position: Int)
}
