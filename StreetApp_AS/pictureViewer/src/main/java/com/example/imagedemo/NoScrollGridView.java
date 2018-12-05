package com.example.imagedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

/**
 * 自定义的“九宫格”——用在显示帖子详情的图片集合 
 * 解决的问题：GridView显示不全，只显示了一行的图片，比较奇怪，尝试重写GridView来解决
 * 
 * @author lichao
 * @since 2014-10-16 16:41
 * 
 */
public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//		int widthSpec = 0;
//		for(int i = 0;i<getChildCount();i++)
//		{
//			View child = getChildAt(i);
//			Log.d("yj", "getMeasuredWidth"+child.getMeasuredWidth());
//			widthSpec += MeasureSpec.makeMeasureSpec(80, MeasureSpec.EXACTLY);			
//		}
//		Log.d("yj", "widthSpec"+widthSpec);
//		Log.d("yj", "widthMeasureSpec"+widthMeasureSpec);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
