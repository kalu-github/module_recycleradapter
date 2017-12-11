package lib.kalu.adapter

import android.graphics.Canvas
import android.support.annotation.LayoutRes
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.listener.OnDragChangeListener
import lib.kalu.adapter.listener.OnSwipeChangeListener
import java.util.*

/**
 * description: 侧滑, 拖拽
 * created by kalu on 2017/5/26 14:52
 */
abstract class BaseCommonSwipeDragAdapter<T, K : RecyclerHolder>
/** */
(data: MutableList<T>, @LayoutRes layoutResId: Int) : BaseCommonAdapter<T, K>(data, layoutResId) {
    protected var mToggleViewId = NO_TOGGLE_VIEW
    protected var mItemTouchHelper: ItemTouchHelper? = null
    var isItemDraggable = false
        protected set
    var isItemSwipeEnable = false
        protected set
    protected var mOnItemDragListener: OnDragChangeListener? = null
    protected var mOnItemSwipeListener: OnSwipeChangeListener? = null
    protected var mDragOnLongPress = true

    protected var mOnToggleViewTouchListener: View.OnTouchListener? = null
    protected var mOnToggleViewLongClickListener: View.OnLongClickListener? = null

    /** */

    override fun onBindViewHolder(holder: K, positions: Int) {
        super.onBindViewHolder(holder, positions)
        val viewType = holder.itemViewType

        if (mItemTouchHelper != null && isItemDraggable && viewType != RecyclerHolder.LOAD_VIEW && viewType != RecyclerHolder.HEAD_VIEW
                && viewType != RecyclerHolder.NULL_VIEW && viewType != RecyclerHolder.FOOT_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                val toggleView = holder.getView<View>(mToggleViewId)
                if (toggleView != null) {
                    toggleView.setTag(RecyclerHolder.HOLDER_ID_TAG, holder)
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener)
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener)
                    }
                }
            } else {
                holder.itemView.setTag(RecyclerHolder.HOLDER_ID_TAG, holder)
                holder.itemView.setOnLongClickListener(mOnToggleViewLongClickListener)
            }
        }
    }

    fun setToggleViewId(toggleViewId: Int) {
        mToggleViewId = toggleViewId
    }

    fun setToggleDragOnLongPress(longPress: Boolean) {
        mDragOnLongPress = longPress
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null
            mOnToggleViewLongClickListener = View.OnLongClickListener { v ->
                if (mItemTouchHelper != null && isItemDraggable) {
                    mItemTouchHelper!!.startDrag(v.getTag(RecyclerHolder.HOLDER_ID_TAG) as RecyclerView.ViewHolder)
                }
                true
            }
        } else {
            mOnToggleViewTouchListener = View.OnTouchListener { v, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && !mDragOnLongPress) {
                    if (mItemTouchHelper != null && isItemDraggable) {
                        mItemTouchHelper!!.startDrag(v.getTag(RecyclerHolder.HOLDER_ID_TAG) as RecyclerView.ViewHolder)
                    }
                    true
                } else {
                    false
                }
            }
            mOnToggleViewLongClickListener = null
        }
    }

    @JvmOverloads
    fun enableDragItem(itemTouchHelper: ItemTouchHelper, toggleViewId: Int = NO_TOGGLE_VIEW, dragOnLongPress: Boolean = true) {
        isItemDraggable = true
        mItemTouchHelper = itemTouchHelper
        setToggleViewId(toggleViewId)
        setToggleDragOnLongPress(dragOnLongPress)
    }

    fun disableDragItem() {
        isItemDraggable = false
        mItemTouchHelper = null
    }

    fun enableSwipeItem() {
        isItemSwipeEnable = true
    }

    fun disableSwipeItem() {
        isItemSwipeEnable = false
    }

    fun setOnItemDragListener(onItemDragListener: OnDragChangeListener) {
        mOnItemDragListener = onItemDragListener
    }

    fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder): Int {
        return viewHolder.adapterPosition - headCount
    }

    fun onItemDragStart(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemDragListener != null && isItemDraggable) {
            mOnItemDragListener!!.onDragStart(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun onItemDragMove(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val from = getViewHolderPosition(source)
        val to = getViewHolderPosition(target)

        if (from < to) {
            for (i in from until to) {
                Collections.swap(data!!, i, i + 1)
            }
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(data!!, i, i - 1)
            }
        }
        notifyItemMoved(source.adapterPosition, target.adapterPosition)

        if (mOnItemDragListener != null && isItemDraggable) {
            mOnItemDragListener!!.onDragMove(source, target, from, to)
        }
    }

    fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemDragListener != null && isItemDraggable) {
            mOnItemDragListener!!.onDragEnd(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun setOnItemSwipeListener(listener: OnSwipeChangeListener) {
        mOnItemSwipeListener = listener
    }

    fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemSwipeListener != null && isItemSwipeEnable) {
            mOnItemSwipeListener!!.onSwipeStart(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun onItemSwipeEnd(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemSwipeListener != null && isItemSwipeEnable) {

            val position = getViewHolderPosition(viewHolder)
            mOnItemSwipeListener!!.onSwipeEnd(viewHolder, position == -1, position)
        }
    }

    fun onSwipeRemove(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemSwipeListener != null && isItemSwipeEnable) {
            mOnItemSwipeListener!!.onSwipeRemove(viewHolder, getViewHolderPosition(viewHolder))
        }

        val pos = getViewHolderPosition(viewHolder)

        data!!.removeAt(pos)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun onItemSwiping(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
        if (mOnItemSwipeListener != null && isItemSwipeEnable) {
            mOnItemSwipeListener!!.onSwipeMove(canvas, viewHolder, dX, dY, isCurrentlyActive, dX > 0)
        }
    }

    companion object {

        private val NO_TOGGLE_VIEW = 0
    }
}