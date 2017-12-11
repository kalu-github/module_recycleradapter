package lib.kalu.adapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutParams
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import lib.kalu.adapter.animation.*
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.model.TransModel
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType


/**
 * description: 没有加载更多
 * created by kalu on 2017/5/26 14:22
 */
abstract class BaseCommonAdapter<T, K : RecyclerHolder>
/**
 * 普通, 对外暴露API
 */
(// 数据集合
        private val mModelList: MutableList<T>?, // 布局ID
        @param:LayoutRes protected var mLayoutResId: Int) : RecyclerView.Adapter<K>() {

    // 是否仅仅第一次加载显示动画
    private var isOpenAnimFirstOnly = true
    // 显示动画
    private var isOpenAnim = false
    // 动画显示时间
    private var mAnimTime = 300

    protected var mLastPosition = -1

    private var mSelectAnimation: BaseAnimation = AlphaInAnimation()
    private val mInterpolator = LinearInterpolator()

    var headLayout: LinearLayout? = null
        protected set
    var footLayout: LinearLayout? = null
        protected set
    protected var mEmptyLayout: FrameLayout? = null

    /***********************************       头部API        */

    private val headPosition: Int
        get() = if (headCount == 1) -1 else 0

    val headCount: Int
        get() = if (headLayout == null || headLayout!!.childCount == 0) 0 else 1

    /***********************************       尾部API        */

    private val footPosition: Int
        get() {

            val footCount = footCount
            if (footCount != 1) return -1

            val headCount = headCount
            return if (headCount == 1) {
                headCount + mModelList!!.size
            } else mModelList!!.size
        }

    val footCount: Int
        get() = if (footLayout == null || footLayout!!.childCount == 0) 0 else 1

    /***********************************       空布局API       */

    val nullCount: Int
        get() {

            if (mEmptyLayout == null || mEmptyLayout!!.childCount == 0) return 0

            return if (mModelList!!.size != 0) 0 else 1
        }

    var nullView: View?
        get() = mEmptyLayout
        set(emptyView) {
            var insert = false
            if (mEmptyLayout == null) {
                mEmptyLayout = FrameLayout(emptyView!!.getContext())
                val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                val lp = emptyView!!.getLayoutParams()
                if (lp != null) {
                    layoutParams.width = lp.width
                    layoutParams.height = lp.height
                }
                mEmptyLayout!!.layoutParams = layoutParams
                insert = true
            }
            mEmptyLayout!!.removeAllViews()
            mEmptyLayout!!.addView(emptyView)

            if (!insert) return
            if (nullCount == 1) {
                var position = 0
                if (headCount != 0) {
                    position++
                }
                notifyItemInserted(position)
            }
        }

    val data: MutableList<T>?
        get() = mModelList

    /***********************************     构造器API        */

    /**
     * 分类型, 不对外暴露API
     */
    internal constructor(data: MutableList<T>?) : this(data, 0) {}

    /***********************************       方法API        */

    open fun onMerge(position: Int): Int {
        return 1
    }

    fun getModel(@IntRange(from = 0) position: Int): T? {
        return if (position < mModelList!!.size) mModelList[position] else null
    }

    protected open fun getItemModelType(position: Int): Int {
        return super.getItemViewType(position)
    }

    protected open fun createModelHolder(parent: ViewGroup, viewType: Int): K {
        val itemView = LayoutInflater.from(parent.context.applicationContext).inflate(mLayoutResId, parent, false)
        return createSimpleHolder(itemView)
    }

    protected fun createSimpleHolder(view: View?): K {
        var clazz: Class<*>? = javaClass
        var z: Class<*>? = null
        while (null == z && null != clazz) {
            z = createKClass(clazz)
            clazz = clazz.superclass
        }
        val k = createKModel(z, view)
        return k ?: RecyclerHolder(view!!) as K
    }

    private fun createKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    if (RecyclerHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                }
            }
        }
        return null
    }

    private fun createKModel(z: Class<*>?, view: View?): K? {
        try {
            val constructor: Constructor<*>
            val buffer = Modifier.toString(z!!.modifiers)
            val className = z.name
            if (className.contains("$") && !buffer.contains("static")) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                return constructor.newInstance(this, view) as K
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                return constructor.newInstance(view) as K
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }

        return null
    }

    protected fun setModelStyle(holder: RecyclerView.ViewHolder?, isModel: Boolean) {

        if (isModel) {
            if (!isOpenAnim) return
            if (!isOpenAnimFirstOnly || holder!!.layoutPosition > mLastPosition) {
                for (anim in mSelectAnimation.getAnimators(holder!!.itemView)) {
                    anim.setDuration(mAnimTime.toLong()).start()
                    anim.interpolator = mInterpolator
                }
                mLastPosition = holder.layoutPosition
            }
        } else {

            if (null == holder || null == holder.itemView) return

            val layoutParams = holder.itemView.layoutParams ?: return

            val isStaggeredGridLayoutManager = layoutParams is StaggeredGridLayoutManager.LayoutParams
            if (!isStaggeredGridLayoutManager) return

            val params = layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    protected open fun isModelType(type: Int): Boolean {
        return type != RecyclerHolder.HEAD_VIEW && type != RecyclerHolder.FOOT_VIEW && type != RecyclerHolder.NULL_VIEW
    }

    /***********************************       重写API        */

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mModelList!!.size + nullCount + headCount + footCount
    }

    override fun getItemViewType(position: Int): Int {
        if (nullCount == 1) {
            val header = headCount != 0
            when (position) {
                0 -> return if (header) RecyclerHolder.HEAD_VIEW else RecyclerHolder.NULL_VIEW
                1 -> return if (header) RecyclerHolder.NULL_VIEW else RecyclerHolder.FOOT_VIEW
                2 -> return RecyclerHolder.FOOT_VIEW
                else -> return RecyclerHolder.NULL_VIEW
            }
        }
        val numHeaders = headCount
        if (position < numHeaders) {
            return RecyclerHolder.HEAD_VIEW
        } else {
            var adjPosition = position - numHeaders
            val adapterCount = mModelList!!.size
            if (adjPosition < adapterCount) {
                return getItemModelType(adjPosition)
            } else {
                adjPosition = adjPosition - adapterCount
                val numFooters = footCount
                return if (adjPosition < numFooters) RecyclerHolder.FOOT_VIEW else RecyclerHolder.LOAD_VIEW
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        val holder: K
        when (viewType) {
            RecyclerHolder.NULL_VIEW -> holder = createSimpleHolder(mEmptyLayout)
            RecyclerHolder.HEAD_VIEW -> holder = createSimpleHolder(headLayout)
            RecyclerHolder.FOOT_VIEW -> holder = createSimpleHolder(footLayout)
            else -> holder = createModelHolder(parent, viewType)
        }
        return holder
    }

    override fun onViewAttachedToWindow(holder: K?) {
        super.onViewAttachedToWindow(holder)

        val type = holder!!.itemViewType
        val isModel = isModelType(type)
        setModelStyle(holder, isModel)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)

        val manager = recyclerView!!.layoutManager as? GridLayoutManager ?: return

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                val type = getItemViewType(position)
                val modelType = isModelType(type)
                return if (modelType) onMerge(position - headCount) else manager.spanCount
            }
        }
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        when (holder.itemViewType) {
            RecyclerHolder.HEAD_VIEW, RecyclerHolder.NULL_VIEW, RecyclerHolder.FOOT_VIEW -> {
            }
            else -> onNext(holder, mModelList!![holder.layoutPosition - headCount], position)
        }
    }

    /***********************************       动画API        */

    @IntDef(BaseAnimation.ALPHAIN.toLong(), BaseAnimation.SCALEIN.toLong(), BaseAnimation.SLIDEIN_BOTTOM.toLong(), BaseAnimation.SLIDEIN_LEFT.toLong(), BaseAnimation.SLIDEIN_RIGHT.toLong())
    @Retention(RetentionPolicy.SOURCE)
    annotation class AnimationType

    fun openLoadAnimation(@AnimationType animationType: Int, animTime: Int, isOpenAnimFirstOnly: Boolean) {
        this.isOpenAnim = true
        this.isOpenAnimFirstOnly = isOpenAnimFirstOnly
        this.mAnimTime = animTime

        when (animationType) {
            BaseAnimation.ALPHAIN -> mSelectAnimation = AlphaInAnimation()
            BaseAnimation.SCALEIN -> mSelectAnimation = ScaleInAnimation()
            BaseAnimation.SLIDEIN_BOTTOM -> mSelectAnimation = SlideInBottomAnimation()
            BaseAnimation.SLIDEIN_LEFT -> mSelectAnimation = SlideInLeftAnimation()
            BaseAnimation.SLIDEIN_RIGHT -> mSelectAnimation = SlideInRightAnimation()
            else -> {
            }
        }
    }

    /***********************************       展开API        */

    @JvmOverloads
    fun expand(@IntRange(from = 0) position: Int, animate: Boolean = true) {
        var position = position
        position -= headCount

        val model = getModel(position)
        if (null == model || model !is TransModel<*>) return

        val trans = model as TransModel<T>?
        if (trans!!.isExpanded) return

        val tempList = trans.getModelList()
        if (null == tempList || tempList.size == 0) return

        // 需要展开
        trans.isExpanded = true
        val tempSize = tempList.size
        val tempBegin = position + 1
        mModelList!!.addAll(tempBegin, tempList)

        if (animate) {
            notifyItemRangeInserted(tempBegin, tempSize)
        } else {
            notifyDataSetChanged()
        }
    }

    fun expandAll() {
        for (i in mModelList!!.size - 1 downTo 0 + headCount) {
            expand(i, true)
        }
    }

    /***********************************       折叠API        */

    @JvmOverloads
    fun collapse(@IntRange(from = 0) position: Int, animate: Boolean = true) {
        var position = position
        position -= headCount

        val model = getModel(position)
        if (null == model || model !is TransModel<*>) return

        val trans = model as TransModel<*>?
        if (!trans!!.isExpanded) return

        val tempList = trans.getModelList()
        if (null == tempList || tempList.size == 0) return

        // 需要折叠
        trans.isExpanded = false
        val tempSize = tempList.size
        val tempBegin = position + 1
        for (i in 0 until tempSize) {
            mModelList!!.removeAt(tempBegin)
        }

        if (animate) {
            notifyItemRangeRemoved(tempBegin, tempSize)
        } else {
            notifyDataSetChanged()
        }
    }

    fun collapseAll() {
        for (i in mModelList!!.size - 1 downTo 0 + headCount) {
            collapse(i, true)
        }
    }

    /***********************************       索引API        */

    fun getParentPosition(item: T): Int {

        if (null == item || null == mModelList || mModelList.isEmpty()) return -1

        val position = mModelList.indexOf(item)
        if (position == -1) return -1

        val level = if (item is TransModel<*>) (item as TransModel<*>).level else Integer.MAX_VALUE

        if (level == 0) return position
        if (level == -1) return -1

        for (i in position downTo 0) {
            val temp = mModelList[i] as? TransModel<*> ?: continue
            if (temp.level >= 0 && temp.level < level) return i
        }
        return -1
    }

    fun getViewPosition(recyclerView: RecyclerView?, position: Int, @IdRes viewId: Int): View? {

        if (recyclerView == null) return null

        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as RecyclerHolder ?: return null

        return viewHolder.getView(viewId)
    }

    @JvmOverloads
    fun addHeadView(header: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        var index = index
        if (headLayout == null) {
            headLayout = LinearLayout(header.context)
            if (orientation == LinearLayout.VERTICAL) {
                headLayout!!.orientation = LinearLayout.VERTICAL
                headLayout!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                headLayout!!.orientation = LinearLayout.HORIZONTAL
                headLayout!!.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        val childCount = headLayout!!.childCount
        if (index < 0 || index > childCount) {
            index = childCount
        }
        headLayout!!.addView(header, index)
        if (headLayout!!.childCount == 1) {
            val position = headPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    @JvmOverloads
    fun setHeadView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        if (headLayout == null || headLayout!!.childCount <= index) {
            return addHeadView(header, index, orientation)
        } else {
            headLayout!!.removeViewAt(index)
            headLayout!!.addView(header, index)
            return index
        }
    }

    fun removeHeadView(header: View) {
        if (headCount == 0) return

        headLayout!!.removeView(header)
        if (headLayout!!.childCount == 0) {
            val position = headPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllHeadView() {
        if (headCount == 0) return

        headLayout!!.removeAllViews()
        val position = headPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    @JvmOverloads
    fun addFootView(footer: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        var index = index
        if (footLayout == null) {
            footLayout = LinearLayout(footer.context)
            if (orientation == LinearLayout.VERTICAL) {
                footLayout!!.orientation = LinearLayout.VERTICAL
                footLayout!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                footLayout!!.orientation = LinearLayout.HORIZONTAL
                footLayout!!.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        val childCount = footLayout!!.childCount
        if (index < 0 || index > childCount) {
            index = childCount
        }
        footLayout!!.addView(footer, index)
        if (footLayout!!.childCount == 1) {
            val position = footPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return index
    }

    @JvmOverloads
    fun setFootView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        if (footLayout == null || footLayout!!.childCount <= index) {
            return addFootView(header, index, orientation)
        } else {
            footLayout!!.removeViewAt(index)
            footLayout!!.addView(header, index)
            return index
        }
    }

    fun removeFootView(footer: View) {
        if (footCount == 0) return

        footLayout!!.removeView(footer)
        if (footLayout!!.childCount == 0) {
            val position = footPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllFootView() {
        if (footCount == 0) return

        footLayout!!.removeAllViews()
        val position = footPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    fun setNullView(context: Context, layoutResId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutResId, null, false)
        nullView = view
    }

    fun removeNullView() {

        if (null == mEmptyLayout) return
        mEmptyLayout!!.removeAllViews()
        mEmptyLayout!!.visibility = View.GONE
    }

    /***********************************       数据API       */

    open fun clearAddData(data: MutableList<T>?) {

        mModelList!!.clear()
        mLastPosition = -1
        mModelList.addAll(data!!)
        notifyDataSetChanged()
    }

    fun addData(data: T) {
        mModelList!!.add(data)
        notifyItemInserted(mModelList.size + headCount)
        notifyDataSetChanged()
    }

    fun addData(newData: Collection<T>) {
        mModelList!!.addAll(newData)
        notifyItemRangeInserted(mModelList.size - newData.size + headCount, newData.size)
        notifyDataSetChanged()
    }

    fun remove(@IntRange(from = 0) position: Int) {
        mModelList!!.removeAt(position)
        val internalPosition = position + headCount
        notifyItemRemoved(internalPosition)
        notifyItemRangeChanged(internalPosition, mModelList.size - internalPosition)
    }

    fun setData(@IntRange(from = 0) index: Int, data: T) {
        mModelList!![index] = data
        notifyItemChanged(index + headCount)
    }

    /**********************************       抽象方法API      */

    protected abstract fun onNext(holder: K, model: T, position: Int)

    companion object {
        protected val TAG = BaseCommonAdapter::class.java.simpleName
    }
}