package com.juxun.business.street.widget;

import com.juxun.business.street.widget.wheel.widget.OnWheelChangedListener;
import com.juxun.business.street.widget.wheel.widget.WheelView;
import com.juxun.business.street.widget.wheel.widget.adapter.ArrayWheelAdapter;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 
 * @author Administrator 省市区�?�择�? 罗富�? 2016-9-28
 *
 */
public class WheelCityDialog extends WheelViewBaseDialog implements OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;
	private Button mBtnCancel;
	private onConfirmListener listener = null;
	private View mMenuView;
	private ViewFlipper viewfipper;

	public WheelCityDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WheelCityDialog(Context context, onConfirmListener listener) {
		// TODO Auto-generated constructor stub
		super(context);
		this.listener = listener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.city_dialog, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setUpViews();
		setUpListener();
		setUpData();

		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();

	}

	public interface onConfirmListener {
		public void onConfirm(String area, String areaName, String id);
	}

	private void setUpViews() {
		mViewProvince = (WheelView) mMenuView.findViewById(R.id.id_province);
		mViewCity = (WheelView) mMenuView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) mMenuView.findViewById(R.id.id_district);
		mBtnConfirm = (Button) mMenuView.findViewById(R.id.btn_confirm);
		mBtnCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
	}

	private void setUpData() {
		initDatas();
		// initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);

		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mIdDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信�?
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
		mCurrentZipCode = mIdDatasMap.get(mCurrentDistrictName);
	}

	/**
	 * 根据当前的省，更新市WheelView的信�?
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm: {
			showSelectedResult();
			this.dismiss();
			break;
		}
		case R.id.btn_cancel: {
			this.dismiss();
			break;
		}
		default:
			break;
		}
	}

	private void showSelectedResult() {
		// Toast.makeText(
		// getContext(),
		// "当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
		// + mCurrentDistrictName + "," + mCurrentZipCode,
		// Toast.LENGTH_SHORT).show();
		String area = mCurrentProviceName + mCurrentCityName + mCurrentDistrictName;
		String id = mCurrentZipCode;
		String areaName = mCurrentDistrictName;
		listener.onConfirm(area, areaName, id);
	}

}
