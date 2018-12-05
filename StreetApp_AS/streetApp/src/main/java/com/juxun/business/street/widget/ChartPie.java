package com.juxun.business.street.widget;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.yl.ming.efengshe.R;

public class ChartPie extends View {
    private float startAngle = 270.0f;// 累积角度
    private ArrayList<Element> datasList = new ArrayList<Element>(); // 外部数据列表
    private Paint arcPaint; // 圆弧画笔
    private Paint textPaint; // 文字画笔

    private PointF circle; // 圆心

    private String propertyText = "";// 总资产
    private String proyText = "";//
    private boolean isAnim = false;

    public static class Element {
        public int Color;
        public float Weight;

        public Element(int c, float w) {
            Color = c;
            Weight = w;
        }
    }

    public ChartPie(Context context) {
        super(context);
        init();
    }

    public ChartPie(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartPie(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 外面画的圆弧
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        // 中间的文字
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(28);
    }

    /**
     * 是否播放动画
     */
    private boolean playAnim = true;

    public void setData(ArrayList<Element> dataList, String property, String proyText) {
        this.datasList.clear();
        datasList.addAll(dataList); // 需要绘制的所有内容

        this.propertyText = property;
        this.proyText = proyText;
        initListener();
        initAnimator();

        if (playAnim) {
            valueAnimator.start();
        }
        postInvalidate();
    }

    /**
     * 动画相关
     */
    private ValueAnimator valueAnimator;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private float mAnimatorValue; // 动画的属性数值

    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        };
    }

    /**
     * 动画是否结束
     */
    private boolean isOver = false;

    private void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1500);
        valueAnimator.addUpdateListener(mUpdateListener);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOver = true;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setStartAngle(float startAngleValue) {
        this.startAngle = startAngleValue;
        postInvalidate();
    }

    public void setAnim(boolean isAnim) {
        this.isAnim = isAnim;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        startAngle = 0;
        // 确定中心点
        circle = new PointF(getWidth() / 2f, getHeight() / 2f);
        // 确定圆半径 取高和宽小的一部分作为直径
        float min = getHeight() > getWidth() ? getWidth() : getHeight();
        // 减20作为拉开间距
        float arcRadio = min / 2f - 10;
        // 圆的外接矩形
        RectF rect = new RectF(circle.x - arcRadio, circle.y - arcRadio,
                circle.x + arcRadio, circle.y + arcRadio);
        // 开始画外部圆弧
        if (datasList != null && datasList.size() > 0) {
            for (int i = 0; i < datasList.size(); i++) {
                // 颜色、角度
                arcPaint.setColor(datasList.get(i).Color); // 这段圆弧的颜色设置
                float angle = datasList.get(i).Weight; // 按照360度去分的
                // 最后一个
//				if (i == datasList.size() - 1) {
//					angle = (int) (360 - startAngle); // 最后一个需要扫过的面积
//				}

//				if (angle != 0) {
//					angle = angle - 0.5f; // 每个扫过的，中间留白，少画0.5f个间隙
//				}

                // 画一个弧度、角度开始添加
                // canvas.drawArc(oval, angle, sweepAngle, useCenter, paint)
                // 圆弧大小和形状，开始的角度（0是水平右侧），扫过的角度，是否经过圆心（不经过画成半月形），画笔
                canvas.drawArc(rect, startAngle, angle * mAnimatorValue, true,
                        arcPaint);
                startAngle += datasList.get(i).Weight; // 开始的角度在变化

                // 画y
//				if (i < datasList.size() - 1) { // 最后一个位置与第一个，中间留白0.5f
//					arcPaint.setColor(Color.WHITE);
//					canvas.drawArc(rect, startAngle, 0.5f, true, arcPaint);
//					startAngle += 0.5;
//				}
            }
        } else {
            arcPaint.setColor(Color.rgb(0xe0, 0xdf, 0xdf));
            canvas.drawArc(rect, 0.0f, 360.0f, true, arcPaint);
        }

        // 画中心圆，白色覆盖扇行图
        arcPaint.setColor(Color.WHITE);
        canvas.drawCircle(circle.x, circle.y, arcRadio / 1.4f, arcPaint);

        textPaint.setColor(getContext().getResources().getColor(R.color.actionbar_title_color));
        textPaint.setTextSize(30);
        canvas.drawText(proyText, circle.x,
                circle.y -
                        20,
                textPaint);
        textPaint.setColor(getContext().getResources().getColor(R.color.actionbar_title_color));
        textPaint.setTextSize(30);
        canvas.drawText(propertyText, circle.x,
                circle.y +
                        20,
                textPaint);

    }
}
