package lib.kalu.adapter

import android.support.annotation.LayoutRes
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.model.MultModel

/**
 * description: 分类型
 * created by kalu on 2017/5/26 15:02
 */
abstract class BaseCommonMultAdapter<T : MultModel, K : RecyclerHolder>(data: MutableList<T>) : BaseCommonAdapter<T, K>(data) {

    private val mResIdList = SparseArray<Int>()

    init {
        onMult()
    }

    override fun getItemModelType(position: Int): Int {
        val item = data!![position]
        return if (item is MultModel) (item as MultModel).itemType else MultModel.TYPE_1
    }

    override fun createModelHolder(parent: ViewGroup, viewType: Int): K {

        val layoutId = getLayoutId(viewType)
        val itemView = LayoutInflater.from(parent.context.applicationContext).inflate(layoutId, parent, false)
        return createSimpleHolder(itemView)
    }

    private fun getLayoutId(viewType: Int): Int {
        return mResIdList.get(viewType)
    }

    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        mResIdList.put(type, layoutResId)
    }

    /**
     * 添加分类型布局
     */
    protected abstract fun onMult()
}