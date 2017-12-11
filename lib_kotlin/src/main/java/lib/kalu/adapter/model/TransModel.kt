package lib.kalu.adapter.model

import java.util.ArrayList

/**
 * description: 多级菜单
 * created by kalu on 2017/5/26 15:11
 */
abstract class TransModel<T> {

    // 默认合并
    var isExpanded = false
    // 数据集合
    private var modelList: MutableList<T>? = null

    abstract val level: Int

    fun getModelList(): MutableList<T>? {
        return modelList
    }

    fun setModelList(modelList: MutableList<T>) {
        this.modelList = modelList
    }

    /** */

    fun hasModel(): Boolean {
        return modelList != null && modelList!!.size > 0
    }

    fun getModel(position: Int): T? {
        return if (hasModel() && position < modelList!!.size) {
            modelList!![position]
        } else {
            null
        }
    }

    fun getModelPosition(subItem: T): Int {
        return if (modelList != null) modelList!!.indexOf(subItem) else -1
    }

    fun addModel(subItem: T) {
        if (modelList == null) {
            modelList = ArrayList()
        }
        modelList!!.add(subItem)
    }

    fun addModel(position: Int, subItem: T) {
        if (modelList != null && position >= 0 && position < modelList!!.size) {
            modelList!!.add(position, subItem)
        } else {
            addModel(subItem)
        }
    }

    operator fun contains(subItem: T): Boolean {
        return modelList != null && modelList!!.contains(subItem)
    }

    fun removeModel(subItem: T): Boolean {
        return modelList != null && modelList!!.remove(subItem)
    }

    fun removeModel(position: Int): Boolean {
        if (modelList != null && position >= 0 && position < modelList!!.size) {
            modelList!!.removeAt(position)
            return true
        }
        return false
    }
}
