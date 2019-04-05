package com.qishiyun.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xll
 * @date 2019/4/5 0005
 */
public class ClockView extends View {
    private Context mContext;
    private Paint mPaint;
    /**
     * 秒针的度数
     */
    private float mSecondDegree;
    /**
     * 分针的度数
     */
    private float mMinDegree;
    /**
     * 时针的度数
     */
    private float mHourDegree;
    /**
     * 当前是不是晚上
     */
    private boolean mIsNight;
    /**
     * 总秒数
     */
    private float mTotalSecond;
    /**
     * 圆的边框颜色
     */
    private int borderColor;
    /**
     * 圆的边框粗细
     */
    private float borderWidth;
    /**
     * 圆心颜色
     */
    private int centerPointColor;
    /**
     * 圆心大小
     */
    private float centerPointSize;
    /**
     * 圆的背景
     */
    private int circleBackground;
    /**
     * 时针颜色
     */
    private int minScaleColor;
    /**
     * 分针颜色
     */
    private int midScaleColor;
    /**
     * 秒针颜色
     */
    private int maxScaleColor;
    /**
     * 时针长度
     */
    private float minScaleLength;
    /**
     * 分针长度
     */
    private float midScaleLength;
    /**
     * 秒针长度
     */
    private float maxScaleLength;
    /**
     * 数字颜色
     */
    private int textColor;
    /**
     * 数字大小
     */
    private float textSize;
    /**
     * 秒针颜色
     */
    private int secondPointerColor;
    /**
     * 分针颜色
     */
    private int minPointerColor;
    /**
     * 时针颜色
     */
    private int hourPointerColor;
    /**
     * 秒针长度
     */
    private float secondPointerLength;
    /**
     * 分针长度
     */
    private float minPointerLength;
    /**
     * 时针长度
     */
    private float hourPointerLength;
    /**
     * 秒针粗细
     */
    private float secondPointerSize;
    /**
     * 分针粗细
     */
    private float minPointerSize;
    /**
     * 时针粗细
     */
    private float hourPointerSize;
    /**
     * 圆心大小
     */
    private float centerPointRadius;
    /**
     * 圆心类型
     */
    private String centerPointType;

    private boolean isSecondGoSmooth;
    private int sleepTime;
    /**
     * 是否显示数字
     */
    private boolean isDrawText;

    private Timer mTimer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {//具体的定时任务逻辑
            if (mSecondDegree == 360) {
                mSecondDegree = 0;
            }
            if (mMinDegree == 360) {
                mMinDegree = 0;
            }
            if (mHourDegree == 360) {
                mHourDegree = 0;
                mIsNight = !mIsNight;
            }
            mSecondDegree = mSecondDegree + 0.3f * sleepTime / 50;
            mMinDegree = mMinDegree + 0.005f * sleepTime / 50;
            mHourDegree = mHourDegree + 1.0f * sleepTime / 50 / 2400.0f;
            postInvalidate();
        }
    };

    public ClockView(Context context) {
        super(context);
        this.mContext = context;
        initPainter();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
        initPainter();
    }

    /**
     * 初始化画笔
     */
    private void initPainter() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
    }

    /**
     * 初始化各参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        borderColor = ta.getColor(R.styleable.ClockView_borderColor, Color.BLACK);
        circleBackground = ta.getColor(R.styleable.ClockView_circleBackground, Color.WHITE);
        borderWidth = ta.getDimension(R.styleable.ClockView_borderWidth,
                SizeUtils.dp2px(context, 1));
        minScaleColor = ta.getColor(R.styleable.ClockView_minScaleColor, Color.BLACK);
        midScaleColor = ta.getColor(R.styleable.ClockView_midScaleColor, Color.BLACK);
        maxScaleColor = ta.getColor(R.styleable.ClockView_maxScaleColor, Color.BLACK);
        minScaleLength = ta.getDimension(R.styleable.ClockView_minScaleLength,
                SizeUtils.dp2px(context, 7));
        midScaleLength = ta.getDimension(R.styleable.ClockView_midScaleLength,
                SizeUtils.dp2px(context, 12));
        maxScaleLength = ta.getDimension(R.styleable.ClockView_maxScaleLength,
                SizeUtils.dp2px(context, 14));
        textColor = ta.getColor(R.styleable.ClockView_textColor, Color.BLACK);
        textSize = ta.getDimension(R.styleable.ClockView_textSize,
                SizeUtils.dp2px(context, 15));
        isDrawText = ta.getBoolean(R.styleable.ClockView_isDrawText, true);
        secondPointerColor = ta.getColor(R.styleable.ClockView_secondPointerColor, Color.RED);
        minPointerColor = ta.getColor(R.styleable.ClockView_minPointerColor, Color.BLACK);
        hourPointerColor = ta.getColor(R.styleable.ClockView_hourPointerColor, Color.BLACK);
        secondPointerLength = ta.getDimension(R.styleable.ClockView_secondPointerLength,
                SizeUtils.dp2px(context, getWidth() / 3 * 2 / 3));
        minPointerLength = ta.getDimension(R.styleable.ClockView_minPointerLength,
                SizeUtils.dp2px(context, getWidth() / 3 / 2));
        hourPointerLength = ta.getDimension(R.styleable.ClockView_hourPointerLength,
                SizeUtils.dp2px(context, getWidth() / 3 / 3));
        secondPointerSize = ta.getDimension(R.styleable.ClockView_secondPointerSize,
                SizeUtils.dp2px(context, 1));
        minPointerSize = ta.getDimension(R.styleable.ClockView_minPointerSize,
                SizeUtils.dp2px(context, 3));
        hourPointerSize = ta.getDimension(R.styleable.ClockView_hourPointerSize,
                SizeUtils.dp2px(context, 5));
        centerPointColor = ta.getColor(R.styleable.ClockView_centerPointColor, Color.BLACK);
        centerPointSize = ta.getDimension(R.styleable.ClockView_centerPointSize,
                SizeUtils.dp2px(context, 5));
        centerPointRadius = ta.getDimension(R.styleable.ClockView_centerPointRadius,
                SizeUtils.dp2px(context, 1));
        centerPointType = ta.getString(R.styleable.ClockView_centerPointType);
        isSecondGoSmooth = ta.getBoolean(R.styleable.ClockView_isSecondGoSmooth, false);
        if (isSecondGoSmooth) {
            sleepTime = 50;
        } else {
            sleepTime = 1000;
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 测量控件尺寸
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = SizeUtils.dp2px(mContext, 300);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量控件尺寸
     *
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = SizeUtils.dp2px(mContext, 300);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外圆边界
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, mPaint);
        //圆背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleBackground);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3 - borderWidth / 2, mPaint);

        //圆心
        mPaint.setColor(centerPointColor);
        mPaint.setStrokeWidth(centerPointSize);
        if ("rect".equals(centerPointType)) {
            canvas.drawPoint(getWidth() / 2, getHeight() / 2, mPaint);
        } else if ("circle".equals(centerPointType)) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, centerPointRadius, mPaint);
        }

        //刻度
        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        for (int i = 0; i < 360; i++) {
            if (i % 30 == 0) {
                //长刻度
                mPaint.setColor(maxScaleColor);
                canvas.drawLine(getWidth() / 2, getHeight() / 2 - (getWidth() / 3 - borderWidth / 2),
                        getWidth() / 2, getHeight() / 2 - (getWidth() / 3 - borderWidth / 2) + maxScaleLength, mPaint);
            } else if (i % 6 == 0) {
                //中刻度
                mPaint.setColor(midScaleColor);
                canvas.drawLine(getWidth() / 2, getHeight() / 2 - (getWidth() / 3 - borderWidth / 2),
                        getWidth() / 2, getHeight() / 2 - (getWidth() / 3 - borderWidth / 2) + midScaleLength, mPaint);
            } else {
                //短刻度
                mPaint.setColor(minScaleColor);
                canvas.drawLine(getWidth() / 2, getHeight() / 2 - (getWidth() / 3 - borderWidth / 2),
                        getWidth() / 2, getHeight() / 2 - (getWidth() / 3 - borderWidth / 2) + minScaleLength, mPaint);
            }
            canvas.rotate(1, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();

        //数字
        if (isDrawText) {
            canvas.save();
            mPaint.setColor(textColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(1);
            mPaint.setTextSize(textSize);
            canvas.translate(getWidth() / 2, getHeight() / 2);
            for (int i = 0; i < 12; i++) {
                if (i == 0) {
                    drawNum(canvas, i * 30, 12 + "", mPaint);
                } else {
                    drawNum(canvas, i * 30, i + "", mPaint);
                }
            }
            canvas.restore();
        }

        //时针
        canvas.save();
        mPaint.setColor(hourPointerColor);
        mPaint.setStrokeWidth(hourPointerSize);
        canvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - hourPointerLength, mPaint);
        canvas.restore();

        //分针
        canvas.save();
        mPaint.setColor(minPointerColor);
        mPaint.setStrokeWidth(minPointerSize);
        canvas.rotate(mMinDegree, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - minPointerLength, mPaint);
        canvas.restore();

        //秒针
        canvas.save();
        mPaint.setColor(secondPointerColor);
        mPaint.setStrokeWidth(secondPointerSize);
        canvas.rotate(mSecondDegree, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - secondPointerLength, mPaint);
        canvas.restore();
    }

    /**
     * 数字
     *
     * @param canvas
     * @param degree
     * @param text
     * @param paint
     */
    private void drawNum(Canvas canvas, int degree, String text, Paint paint) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        float textHeight = textBound.height();
        canvas.rotate(degree);
        canvas.translate(0, borderWidth / 2 + maxScaleLength + textHeight * 3f / 4 - getWidth() / 3);
        canvas.rotate(-degree);
        canvas.drawText(text, -textBound.width() / 2, textBound.height() / 2, paint);
        canvas.rotate(degree);
        canvas.translate(0, getWidth() / 3 - (borderWidth / 2 + maxScaleLength + textHeight * 3f / 4));
        canvas.rotate(-degree);
    }

    /**
     * @param hour   时
     * @param min    分
     * @param second 秒
     */
    public void setTime(int hour, int min, int second) {
        mMinDegree = second * 0.1f + min * 6f;
        if (hour >= 24 || hour < 0 || min >= 60 || min < 0 || second >= 60 || second < 0) {
            Toast.makeText(getContext(), "时间不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hour >= 12) {
            mIsNight = true;
            //            mHourDegree = min * 0.5f + (hour - 12) * 30f;
            mHourDegree = (hour + min * 1.0f / 60f + second * 1.0f / 3600f - 12) * 30f;
        } else {
            mIsNight = false;
            //            mHourDegree = min * 0.5f + hour * 30f;
            mHourDegree = (hour + min * 1.0f / 60f + second * 1.0f / 3600f) * 30f;
        }
        mMinDegree = (min + second * 1.0f / 60f) * 6f;
        mSecondDegree = second * 6f;
        invalidate();//重绘
    }

    /**
     * 开启定时器
     */
    public void start() {
        mTimer.schedule(task, 0, sleepTime);
    }

    /**
     * 获取时间对应的总秒数
     *
     * @return
     */
    public float getTimeTotalSecond() {
        if (mIsNight) {
            mTotalSecond = mHourDegree * 120 + 12 * 3600;
            return mTotalSecond;
        } else {
            mTotalSecond = mHourDegree * 120;
            return mTotalSecond;
        }
    }

    /**
     * 设置总时间（最大值为 24 * 3600）
     *
     * @param mTotalSecond
     */
    public void setTotalSecond(float mTotalSecond) {
        if (mTotalSecond >= (24 * 3600)) {
            this.mTotalSecond = mTotalSecond - 24 * 3600;
        } else {
            this.mTotalSecond = mTotalSecond;
        }
        int hour = (int) (mTotalSecond / 3600);
        int min = (int) ((mTotalSecond - hour * 3600) * 1.0 / 60);
        int second = (int) (mTotalSecond - hour * 3600 - min * 60);
        setTime(hour, min, second);
    }

    /**
     * 获取hour
     *
     * @return
     */
    public int getHour() {
        return (int) (getTimeTotalSecond() / 3600);
    }

    /**
     * 获取Min
     *
     * @return
     */
    public int getMin() {
        return (int) ((getTimeTotalSecond() - getHour() * 3600) / 60);
    }

    public int getSecond() {
        return (int) (getTimeTotalSecond() - getHour() * 3600 - getMin() * 60);
    }

    /**
     * 尺寸转换工具类
     */
    private static class SizeUtils {
        public static int dp2px(Context context, float dp) {
            final float density = context.getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5);
        }

        public static int px2dp(Context context, float px) {
            final float density = context.getResources().getDisplayMetrics().density;
            return (int) (px / density + 0.5);
        }
    }
}
