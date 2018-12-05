package com.juxun.business.street.widget.dialog;

import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 
 * @author TimerDialog 时间弹框
 *
 */
public class TimerDialog extends PopupWindow implements OnClickListener {

	protected Context mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private onConfirmListener listener = null;

	private Button btn_one, btn_two;
	private TextView tv_content;
	private View v_1;
	private String contentString;

	public TimerDialog(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.timer_dialog, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();

		setUpViews();

	}

	public void setonConfirmListener(onConfirmListener listener) {
		this.listener = listener;
	}

	private void setUpViews() {
		tv_content = (TextView) mMenuView.findViewById(R.id.tv_content);
		btn_one = (Button) mMenuView.findViewById(R.id.btn_one);
		btn_two = (Button) mMenuView.findViewById(R.id.btn_two);
		v_1 = (View) mMenuView.findViewById(R.id.v_1);
		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);
		tv_content.setText(contentString);
	}

	/**
	 * 
	 * @param visibility
	 *            设置按钮二是否显示 默认隐藏
	 */
	public void setVisibility(int visibility) {
		btn_two.setVisibility(visibility);
		v_1.setVisibility(visibility);
	}

	/**
	 * 
	 * @param oneString
	 *            按钮1的文本
	 * @param twoString
	 *            按钮2的文本
	 */
	public void setText(String oneString, String twoString) {
		btn_one.setText(oneString);
		btn_two.setText(twoString);
	}

	/**
	 * 
	 * @param content
	 *            提示内容
	 */
	public void setContent(String content) {
		tv_content.setText(content);

	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (listener!=null) {
			listener.onConfirm(v.getId());
		}

	}

	public interface onConfirmListener {
		public void onConfirm(int id);
	}
}
