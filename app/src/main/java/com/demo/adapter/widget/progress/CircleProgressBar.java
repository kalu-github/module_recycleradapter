package com.demo.adapter.widget.progress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.demo.adapter.R;

/**
 * description: 圆形进度条
 * https://github.com/Vultur/CircleProgressBar
 */
public class CircleProgressBar extends View {

    // Properties
    private float progress = 0;
    private float strokeWidth = getResources().getDimension(R.dimen.d4);
    private float backgroundStrokeWidth = getResources().getDimension(R.dimen.d25);
    private int color = Color.WHITE;
    private int backgroundColor = Color.GRAY;

    // Object used to draw
    private int startAngle = -90;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;

    /**********************************************************************************************/

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    //region Constructor & Init Method
    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);
        //Reading values from the XML layout
        try {
            // Value
            progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress_value, progress);
            // StrokeWidth
            strokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progress_width, strokeWidth);
            backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_background_width, backgroundStrokeWidth);
            // Color
            color = typedArray.getInt(R.styleable.CircleProgressBar_progress_color, color);
            backgroundColor = typedArray.getInt(R.styleable.CircleProgressBar_background_color, backgroundColor);
        } finally {
            typedArray.recycle();
        }

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeCap(Paint.Cap.ROUND);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    /**********************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / 100;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        invalidate();
    }

    public float getProgressBarWidth() {
        return strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1000);
    }

    public void setProgressWithAnimation(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
