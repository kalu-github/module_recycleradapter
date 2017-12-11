package lib.kalu.adapter.holder

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.text.util.Linkify
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.CheckedTextView
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView

class RecyclerHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun setAdapter(viewId: Int, adapter: Adapter): RecyclerHolder {
        val view = getView<AdapterView<Adapter>>(viewId)
        view.setAdapter(adapter)
        return this
    }

    /** */

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener): RecyclerHolder {

        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
        return this
    }

    fun setOnClickListener(listener: View.OnClickListener, vararg viewId: Int): RecyclerHolder {

        if (null == viewId) return this

        for (id in viewId) {
            val view = getView<View>(id)
            view.setOnClickListener(listener)
        }
        return this
    }

    fun setOnLongClickListener(viewId: Int, listener: View.OnLongClickListener): RecyclerHolder {
        val view = getView<View>(viewId)
        view.setOnLongClickListener(listener)
        return this
    }

    fun setOnLongClickListener(listener: View.OnLongClickListener, vararg viewId: Int): RecyclerHolder {

        if (null == viewId) return this

        for (id in viewId) {
            val view = getView<View>(id)
            view.setOnLongClickListener(listener)
        }

        return this
    }

    fun setOnItemSelectedClickListener(viewId: Int, listener: AdapterView.OnItemSelectedListener): RecyclerHolder {
        val view = getView<AdapterView<*>>(viewId)
        view.onItemSelectedListener = listener
        return this
    }

    fun setOnCheckedChangeListener(viewId: Int, listener: CompoundButton.OnCheckedChangeListener): RecyclerHolder {
        val view = getView<CompoundButton>(viewId)
        view.setOnCheckedChangeListener(listener)
        return this
    }

    /** */

    fun <T : View> getView(viewId: Int): T {
        return itemView.findViewById<View>(viewId) as T
    }

    fun setText(viewId: Int, value: CharSequence): RecyclerHolder {
        val view = getView<TextView>(viewId)
        view.text = value
        return this
    }

    fun setText(viewId: Int, @StringRes strId: Int): RecyclerHolder {
        val view = getView<TextView>(viewId)
        view.setText(strId)
        return this
    }

    fun setImageResource(viewId: Int, @DrawableRes imageResId: Int): RecyclerHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(imageResId)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): RecyclerHolder {
        val view = getView<View>(viewId)
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, @DrawableRes backgroundRes: Int): RecyclerHolder {
        val view = getView<View>(viewId)
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): RecyclerHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(textColor)
        return this
    }

    fun setTextScaleX(viewId: Int, size: Float): RecyclerHolder {
        val view = getView<TextView>(viewId)
        view.textScaleX = size
        return this
    }

    fun seBackgroundColor(viewId: Int, bgColor: Int): RecyclerHolder {
        val view = getView<View>(viewId)
        view.setBackgroundColor(bgColor)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable): RecyclerHolder {
        val view = getView<ImageView>(viewId)
        view.setImageDrawable(drawable)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap): RecyclerHolder {
        val view = getView<ImageView>(viewId)
        view.setImageBitmap(bitmap)
        return this
    }

    fun setAlpha(viewId: Int, value: Float): RecyclerHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId).alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId).startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): RecyclerHolder {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): RecyclerHolder {
        val view = getView<TextView>(viewId)
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(viewId: Int, typeface: Typeface): RecyclerHolder {
        val view = getView<TextView>(viewId)
        view.typeface = typeface
        view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        return this
    }

    fun setTypeface(typeface: Typeface, vararg viewIds: Int): RecyclerHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): RecyclerHolder {
        val view = getView<ProgressBar>(viewId)
        view.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): RecyclerHolder {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): RecyclerHolder {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): RecyclerHolder {
        val view = getView<RatingBar>(viewId)
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): RecyclerHolder {
        val view = getView<RatingBar>(viewId)
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any): RecyclerHolder {
        val view = getView<View>(viewId)
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any): RecyclerHolder {
        val view = getView<View>(viewId)
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): RecyclerHolder {
        val view = getView<View>(viewId)
        // View unable cast to Checkable
        if (view is CompoundButton) {
            view.isChecked = checked
        } else if (view is CheckedTextView) {
            view.isChecked = checked
        }
        return this
    }

    companion object {

        val HOLDER_ID_TAG = RecyclerHolder::class.java.hashCode()

        const val NULL_VIEW = -1 //空布局
        const val HEAD_VIEW = -2 // 头布局
        const val FOOT_VIEW = -3 // 脚布局
        const val LOAD_VIEW = -4 // 加载布局
        const val SECTION_VIEW = -5 // 分组布局
    }
}