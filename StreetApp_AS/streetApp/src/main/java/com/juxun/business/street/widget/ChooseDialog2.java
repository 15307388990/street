package com.juxun.business.street.widget;

import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ChooseDialog2 extends PopupWindow implements OnClickListener {

	protected Context mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	// 查看详情
	private LinearLayout ll_checkdetails;
	private Button btn_checkdetails;
	// 修改信息
	private LinearLayout ll_amend_message;
	private Button btn_amend_message;
	// // 修改库存
	// private LinearLayout ll_amend_shelflife;
	// private Button btn_amend_shelflife;
	// 上架商品
	private LinearLayout ll_shelves;
	private Button btn_shelves;
	// // 下架商品
	// private LinearLayout ll_soldout;
	// private Button btn_soldout;
	// 删除商品
	private LinearLayout ll_delete;
	private Button btn_delete;
	// 取消
	private TextView tv_colse;
	private onConfirmListener listener = null;
	private TextView tv_dim;

	public ChooseDialog2(Context context, onConfirmListener listener) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.choose_dialog2, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setUpViews();
		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
		this.listener = listener;

	}

	private void setUpViews() {
		ll_checkdetails = (LinearLayout) mMenuView.findViewById(R.id.ll_checkdetails);
		ll_amend_message = (LinearLayout) mMenuView.findViewById(R.id.ll_amend_message);
		// ll_amend_shelflife = (LinearLayout)
		// mMenuView.findViewById(R.id.ll_amend_shelflife);
		ll_shelves = (LinearLayout) mMenuView.findViewById(R.id.ll_shelves);
		// ll_soldout = (LinearLayout) mMenuView.findViewById(R.id.ll_soldout);
		ll_delete = (LinearLayout) mMenuView.findViewById(R.id.ll_delete);
		btn_checkdetails = (Button) mMenuView.findViewById(R.id.btn_checkdetails);
		btn_amend_message = (Button) mMenuView.findViewById(R.id.btn_amend_message);
		// btn_amend_shelflife = (Button)
		// mMenuView.findViewById(R.id.btn_amend_shelflife);
		btn_shelves = (Button) mMenuView.findViewById(R.id.btn_shelves);
		// btn_soldout = (Button) mMenuView.findViewById(R.id.btn_soldout);
		btn_delete = (Button) mMenuView.findViewById(R.id.btn_delete);
		tv_colse = (TextView) mMenuView.findViewById(R.id.tv_colse);
		ll_checkdetails.setOnClickListener(this);
		ll_amend_message.setOnClickListener(this);
		// ll_amend_shelflife.setOnClickListener(this);
		ll_shelves.setOnClickListener(this);
		// ll_soldout.setOnClickListener(this);
		ll_delete.setOnClickListener(this);
		btn_checkdetails.setOnClickListener(this);
		btn_amend_message.setOnClickListener(this);
		// btn_amend_shelflife.setOnClickListener(this);
		btn_shelves.setOnClickListener(this);
		// btn_soldout.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		tv_colse.setOnClickListener(this);
		tv_dim = (TextView) mMenuView.findViewById(R.id.tv_dim);
		tv_dim.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_checkdetails: {
			listener.onConfirm(R.id.ll_checkdetails);
			this.dismiss();
			break;
		}
		case R.id.ll_amend_message: {
			listener.onConfirm(R.id.ll_amend_message);
			this.dismiss();
			break;
		}
		case R.id.ll_amend_shelflife: {
			listener.onConfirm(R.id.ll_amend_shelflife);
			this.dismiss();
			break;
		}
		case R.id.ll_shelves: {
			listener.onConfirm(R.id.ll_shelves);
			this.dismiss();
			break;
		}
		case R.id.ll_soldout: {
			listener.onConfirm(R.id.ll_soldout);
			this.dismiss();
			break;
		}
		case R.id.ll_delete: {
			listener.onConfirm(R.id.ll_delete);
			this.dismiss();
			break;
		}
		case R.id.btn_checkdetails: {
			listener.onConfirm(R.id.ll_checkdetails);
			this.dismiss();
			break;
		}
		case R.id.btn_amend_message: {
			listener.onConfirm(R.id.ll_amend_message);
			this.dismiss();
			break;
		}
		case R.id.btn_amend_shelflife: {
			listener.onConfirm(R.id.ll_amend_shelflife);
			this.dismiss();
			break;
		}
		case R.id.btn_shelves: {
			listener.onConfirm(R.id.ll_shelves);
			this.dismiss();
			break;
		}
		case R.id.btn_soldout: {
			listener.onConfirm(R.id.ll_soldout);
			this.dismiss();
			break;
		}
		case R.id.btn_delete: {
			listener.onConfirm(R.id.ll_delete);
			this.dismiss();
			break;
		}
		case R.id.tv_colse: {
			this.dismiss();
			break;
		}
		case R.id.tv_dim: {
			this.dismiss();
			break;
		}
		default:
			break;
		}

	}

	public interface onConfirmListener {
		public void onConfirm(int id);
	}
}
