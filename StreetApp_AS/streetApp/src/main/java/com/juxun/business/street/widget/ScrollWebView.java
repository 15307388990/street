package com.juxun.business.street.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by zhanglei on 15/7/18.
 */
public class ScrollWebView extends WebView {
	public OnScrollChangeListener listener;

	public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollWebView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);

		float webcontent = getContentHeight() * getScale();// webview的高�?
		float webnow = getHeight() + getScrollY();// 当前webview的高�?
		Log.i("TAG1", "webview.webcontent====>>" + webcontent);
		Log.i("TAG1", "webview.webnow====>>" + webnow);
		Log.i("TAG1", "l" + l + "  t" + t + "  oldl" + oldl + "  oldt" + oldt);
		if (Math.abs(webcontent - webnow) < 1) {
			// 已经处于底端
			// Log.i("TAG1", "已经处于底端");
			listener.onPageEnd(l, t, oldl, oldt);
		} else if (t == 0) {
			// Log.i("TAG1", "已经处于顶端");
			listener.onPageTop(l, t, oldl, oldt);
		} else {
			listener.onScrollChanged(l, t, oldl, oldt);
		}

	}

	public void setOnScrollChangeListener(OnScrollChangeListener listener) {

		this.listener = listener;

	}

	public interface OnScrollChangeListener {
		public void onPageEnd(int l, int t, int oldl, int oldt);

		public void onPageTop(int l, int t, int oldl, int oldt);

		public void onScrollChanged(int l, int t, int oldl, int oldt);

	}

}
