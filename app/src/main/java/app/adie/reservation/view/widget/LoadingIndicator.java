package app.adie.reservation.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import app.adie.reservation.R;


public class LoadingIndicator extends View {
    public static final int BallPulse = 0;
    public static final int DEFAULT_SIZE = 45;
    private boolean mHasAnimation;
    int mIndicatorColor;
    BaseIndicatorController mIndicatorController;
    int mIndicatorId;
    Paint mPaint;

    public @interface Indicator {
    }

    public LoadingIndicator(Context context) {
        super(context);
        init(null, BallPulse);
    }

    public LoadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, BallPulse);
    }

    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(21)
    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingIndicator);
        this.mIndicatorId = a.getInt(BallPulse, BallPulse);
        this.mIndicatorColor = a.getColor(1, getResources().getColor(R.color.colorPrimary));
        a.recycle();
        this.mPaint = new Paint();
        this.mPaint.setColor(this.mIndicatorColor);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
        applyIndicator();
    }

    private void applyIndicator() {
        switch (this.mIndicatorId) {
            case BallPulse /*0*/:
                this.mIndicatorController = new BallPulseIndicator();
                break;
        }
        this.mIndicatorController.setTarget(this);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec), measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec));
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode ==MeasureSpec.EXACTLY) {
            return specSize;
        }
        if (specMode == Integer.MIN_VALUE) {
            return Math.min(defaultSize, specSize);
        }
        return defaultSize;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!this.mHasAnimation) {
            this.mHasAnimation = true;
            applyAnimation();
        }
    }

    void drawIndicator(Canvas canvas) {
        this.mIndicatorController.draw(canvas, this.mPaint);
    }

    void applyAnimation() {
        this.mIndicatorController.createAnimation();
    }

    private int dp2px(int dpValue) {
        return ((int) getContext().getResources().getDisplayMetrics().density) * dpValue;
    }
}
