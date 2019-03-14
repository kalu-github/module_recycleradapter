package lib.kalu.adapter.holder;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * description: 不可以被继承
 * created by kalu on 2017/12/12 8:43
 */
public final class RecyclerHolder extends RecyclerView.ViewHolder {

    public static final int NULL_VIEW = -1; //空布局
    public static final int HEAD_VIEW = -2; // 头布局
    public static final int FOOT_VIEW = -3; // 脚布局
    public static final int LOAD_VIEW = -4; // 加载布局
    public static final int SECTION_VIEW = -5; // 分组布局

    WeakReference<ViewGroup> mNestedRecyclerView;

    public RecyclerHolder(final ViewGroup parent, final View view) {
        super(view);
        ((RecyclerView) view.getParent()).setHasFixedSize(true);
        mNestedRecyclerView = new WeakReference<>(parent);
    }

    public ViewGroup getParent() {
        return mNestedRecyclerView.get();
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return ((RecyclerView) mNestedRecyclerView.get()).getLayoutManager();
    }

    public RecyclerHolder setAdapter(int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**********************************************************************************************/

    public RecyclerHolder setOnClickListener(int viewId, View.OnClickListener listener) {

        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RecyclerHolder setOnClickListener(View.OnClickListener listener, int... viewId) {

        if (null == viewId) return this;

        for (int id : viewId) {
            View view = getView(id);
            view.setOnClickListener(listener);
        }
        return this;
    }

    public RecyclerHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public RecyclerHolder setOnLongClickListener(View.OnLongClickListener listener, int... viewId) {

        if (null == viewId) return this;

        for (int id : viewId) {
            View view = getView(id);
            view.setOnLongClickListener(listener);
        }

        return this;
    }

    public RecyclerHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    public RecyclerHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    public RecyclerHolder setOnFocusChangeListener(View.OnFocusChangeListener listener, int... viewId) {
        if (null == viewId) return this;

        for (int id : viewId) {
            View view = getView(id);
            view.setOnFocusChangeListener(listener);
        }
        return this;
    }

    public RecyclerHolder setOnFocusChangeListener(int id, View.OnFocusChangeListener listener) {
        View view = getView(id);
        view.setOnFocusChangeListener(listener);
        return this;
    }

    public RecyclerHolder setOnTextChangedListener(int id, TextWatcher listener) {

        EditText view = getView(id);
        view.addTextChangedListener(listener);
        return this;
    }

    /**********************************************************************************************/

    public <T extends View> T getView(int viewId) {
        return (T) itemView.findViewById(viewId);
    }

    public RecyclerHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        if (null == view)
            return this;
        view.setText(value);
        return this;
    }

    public RecyclerHolder setText(int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        if (null == view) return this;
        view.setText(strId);
        return this;
    }

    public RecyclerHolder setImageResource(int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        if (null == view) return this;
        view.setImageResource(imageResId);
        return this;
    }

    public RecyclerHolder setBackgroundRes(int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        if (null == view) return this;
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public RecyclerHolder setTextScaleX(int viewId, float size) {
        TextView view = getView(viewId);
        if (null == view) return this;
        view.setTextScaleX(size);
        return this;
    }

    public RecyclerHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        if (null == view) return this;
        view.setImageDrawable(drawable);
        return this;
    }

    public RecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        if (null == view) return this;
        view.setImageBitmap(bitmap);
        return this;
    }

    public RecyclerHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public RecyclerHolder setVisible(int viewId, int visibility) {
        View view = getView(viewId);
        if (null == view) return this;
        view.setVisibility(visibility);
        return this;
    }

    public RecyclerHolder linkify(int viewId) {
        TextView view = getView(viewId);
        if (null == view) return this;
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public RecyclerHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        if (null == view) return this;
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public RecyclerHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            if (null == view) continue;
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public RecyclerHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        if (null == view) return this;
        view.setProgress(progress);
        return this;
    }

    public RecyclerHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        if (null == view) return this;
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public RecyclerHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        if (null == view) return this;
        view.setRating(rating);
        return this;
    }

    public RecyclerHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        if (null == view) return this;
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public RecyclerHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        if (null == view) return this;
        view.setTag(tag);
        return this;
    }

    public RecyclerHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        if (null == view) return this;
        view.setTag(key, tag);
        return this;
    }

    public RecyclerHolder setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        if (null == view) return this;
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }
}