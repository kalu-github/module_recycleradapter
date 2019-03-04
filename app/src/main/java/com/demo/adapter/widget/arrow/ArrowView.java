package com.demo.adapter.widget.arrow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.demo.adapter.R;
import com.demo.adapter.util.DeviceUtil;
import com.demo.adapter.util.LogUtil;

/**
 * description: 箭头
 * created by kalu on 2017/4/10 17:25
 */
public class ArrowView extends View {

    private final String TAG = ArrowView.class.getSimpleName();

    private int arrowColor; // 箭头颜色
    private int arrowWidth; // 箭头宽度
    private int arrowOrientation; // 箭头方向

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArrowView);

        try {
            arrowColor = array.getColor(R.styleable.ArrowView_av_color, Color.BLACK);
            arrowWidth = array.getDimensionPixelSize(R.styleable.ArrowView_av_width, DeviceUtil.dp2px(1));
            arrowOrientation = array.getInt(R.styleable.ArrowView_av_orientation, 1);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(arrowColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(arrowWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        float centerX = measuredWidth / 2f;
        float offsetX = measuredWidth * 0.3f;
        float offsetY = measuredHeight * 0.3f;

        // 1.画直线
        canvas.drawLine(centerX, 0, centerX, measuredHeight, paint);

        // 2.画箭头
        Path path = new Path();
        switch (arrowOrientation) {
            case 1:
                path.moveTo(centerX - offsetX, measuredHeight - offsetY);
                path.lineTo(centerX, measuredHeight);
                path.lineTo(centerX + offsetX, measuredHeight - offsetY);
                break;
            case 2:
                path.moveTo(centerX - offsetX, offsetY);
                path.lineTo(centerX, 0);
                path.lineTo(centerX + offsetX, offsetY);
                break;
        }
        canvas.drawPath(path, paint);
        canvas.save();
        canvas.restore();
    }
}
