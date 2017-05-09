package com.wzf.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.wzf.customview.R;

/**
 * Created by wzf on 2017/5/9.
 * <p>
 * 进度条，模仿守望屁股登录
 */

public class HiveProgressView extends View {

    private static final int MAX_PROGRESS_VALUE = 1400;
    private static final int PROGRESS_TIME = 2000;
    private static final int MAX_ALPHA = 70;

    private Paint paint = new Paint();
    private int hexHeight;
    private int hexWidth;
    private int hexPadding = 0;
    private float actualProgress = 0;
    private int maxAlpha = MAX_ALPHA;
    private int animationTime = PROGRESS_TIME;
    private int color;

    private AnimatorSet indeterminateAnimator;

    public HiveProgressView(Context context) {
        super(context);
    }

    public HiveProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiveProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributes(attrs, defStyle);
        initPaint();
    }

    private void initAttributes(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.HiveProgressView,
                defStyle, 0);
        animationTime = a.getInteger(R.styleable.HiveProgressView_hive_animDuration, PROGRESS_TIME);
        maxAlpha = a.getInteger(R.styleable.HiveProgressView_hive_maxAlpha, MAX_ALPHA);
        if (a.hasValue(R.styleable.HiveProgressView_hive_color)) {
            color = a.getColor(R.styleable.HiveProgressView_hive_color, Color.BLACK);
        }
        Log.d("HiveProgressView", "animationTime: " + animationTime);
        Log.d("HiveProgressView", "maxAlpha: " + maxAlpha);
        Log.d("HiveProgressView", "color: " + color);

        a.recycle();
    }

    private void initPaint() {
        paint.setAlpha(0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        int currentVisibility = getVisibility();
        super.setVisibility(visibility);
        if (visibility != currentVisibility) {
            if (visibility == View.VISIBLE) {
                resetAnimator();
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation();
            }
        }
    }

    private void startAnimation() {
        resetAnimator();
    }

    private void stopAnimation() {
        actualProgress = 0;
        if (indeterminateAnimator != null) {
            indeterminateAnimator.cancel();
            indeterminateAnimator = null;
        }
    }

    private void resetAnimator() {
        if (indeterminateAnimator != null && indeterminateAnimator.isRunning()) {
            indeterminateAnimator.cancel();
        }
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(0, MAX_PROGRESS_VALUE);
        progressAnimator.setDuration(animationTime);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                actualProgress = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        indeterminateAnimator = new AnimatorSet();
        indeterminateAnimator.play(progressAnimator);
        indeterminateAnimator.addListener(new AnimatorListenerAdapter() {
            boolean wasCancelled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!wasCancelled) {
                    resetAnimator();
                }
            }
        });
        indeterminateAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = viewWidth;
        hexWidth = viewWidth / 3;
        hexHeight = viewHeight * 2 / 5;
        hexPadding = viewHeight / 23;
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path hexPath = hiveRect(hexWidth / 2, hexPadding, hexWidth * 3 / 2, hexHeight + hexPadding);
        paint.setAlpha(getAlpha(1, actualProgress));
        canvas.drawPath(hexPath, paint);
        hexPath = hiveRect(hexWidth * 3 / 2, hexPadding, hexWidth * 5 / 2, hexHeight + hexPadding);
        paint.setAlpha(getAlpha(2, actualProgress));
        canvas.drawPath(hexPath, paint);
        hexPath = hiveRect(0, hexHeight * 3 / 4 + hexPadding, hexWidth,
                hexHeight * 7 / 4 + hexPadding);
        paint.setAlpha(getAlpha(6, actualProgress));
        canvas.drawPath(hexPath, paint);
        hexPath = hiveRect(hexWidth, hexHeight * 3 / 4 + hexPadding, hexWidth * 2,
                hexHeight * 7 / 4 + hexPadding);
        paint.setAlpha(getAlpha(7, actualProgress));
        canvas.drawPath(hexPath, paint);
        hexPath = hiveRect(hexWidth * 2, hexHeight * 3 / 4 + hexPadding, hexWidth * 3,
                hexHeight * 7 / 4 + hexPadding);
        paint.setAlpha(getAlpha(3, actualProgress));
        canvas.drawPath(hexPath, paint);
        hexPath = hiveRect(hexWidth / 2, hexHeight * 6 / 4 + hexPadding, hexWidth * 3 / 2,
                hexHeight * 10 / 4 + hexPadding);
        paint.setAlpha(getAlpha(5, actualProgress));
        canvas.drawPath(hexPath, paint);
        hexPath = hiveRect(hexWidth * 3 / 2, hexHeight * 6 / 4 + hexPadding, hexWidth * 5 / 2,
                hexHeight * 10 / 4 + hexPadding);
        paint.setAlpha(getAlpha(4, actualProgress));
        canvas.drawPath(hexPath, paint);
    }

    private int getAlpha(int num, float progress) {
        float alpha;
        if (progress > num * 100) {
            alpha = maxAlpha;
        } else {
            int min = (num - 1) * 100;
            alpha = (progress - min) > 0 ? progress - min : 0;
            alpha = alpha * maxAlpha / 100;
        }
        if (progress > 700) {
            float fadeProgress = progress - 700;
            if (fadeProgress > num * 100) {
                alpha = 0;
            } else {
                int min = (num - 1) * 100;
                alpha = (fadeProgress - min) > 0 ? fadeProgress - min : 0;
                alpha = maxAlpha - alpha * maxAlpha / 100;
            }
        }
        return (int) alpha;
    }

    private Path hiveRect(int left, int top, int right, int bottom) {
        Path path = new Path();
        int height = Math.abs(bottom - top);
        int width = Math.abs(right - left);
        int r = width > height ? height : width;
        r = r / 2;
        int x = (right - left) / 2 + left;
        int y = top;
        int edge = (int) (r * Math.sqrt(3) / 2);
        path.moveTo(x, y);
        x = x + edge;
        y = y + r / 2;
        path.lineTo(x, y);
        y = y + r;
        path.lineTo(x, y);
        x = x - edge;
        y = y + r / 2;
        path.lineTo(x, y);
        x = x - edge;
        y = y - r / 2;
        path.lineTo(x, y);
        y = y - r;
        path.lineTo(x, y);
        path.close();
        return path;
    }
}
