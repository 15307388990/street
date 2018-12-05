package com.juxun.business.street.widget;

import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/10/31.
 */

public class PasswordView extends EditText {
	private Paint mLinePaint; // 线的画笔
	private int mPasswordTextLength; // 输入密码的长度
	private int mWidth;
	private int mHeight;

	private static final int PASSWORD_LENGTH = 6;// 密码的长度
	private static final int PASSWORD_RADIUS = 15;

	public PasswordView(Context context) {
		this(context, null);
	}

	public PasswordView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		initView();
	}

	private void initView() {
		mLinePaint = new Paint();
		mLinePaint.setColor(getResources().getColor(R.color.white));
		mLinePaint.setStrokeWidth(4);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();

		drawLine(canvas);
	}

	/**
	 * 绘制分割线
	 * 
	 * @param canvas
	 */
	private void drawLine(Canvas canvas) {
		for (int i = 1; i < PASSWORD_LENGTH; i++) {
			float x = mWidth * i / PASSWORD_LENGTH;
			canvas.drawLine(x, 12, x, mHeight - 12, mLinePaint);
		}
	}

	// @Override
	// protected void onTextChanged(CharSequence text, int start, int
	// lengthBefore, int lengthAfter) {
	// super.onTextChanged(text, start, lengthBefore, lengthAfter);
	//
	// mPasswordTextLength = text.toString().length();
	//
	// if (mPasswordTextLength == PASSWORD_LENGTH) {
	// Toast.makeText(getContext(), "您设置的密码为: " + text,
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// invalidate();
	// }

	public void reset() {
		setText("");
		invalidate();
	}
}
