package com.juxun.business.street.util;

import java.util.List;

import com.juxun.business.street.bean.ChannelBean;
import com.juxun.business.street.widget.wheel.widget.OnWheelChangedListener;
import com.juxun.business.street.widget.wheel.widget.WheelView;
import com.juxun.business.street.widget.wheel.widget.adapter.AbstractWheelTextAdapter;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

public class WheelViewDialog extends PopupWindow implements android.view.View.OnClickListener, OnWheelChangedListener {

    /**
     * 当前一级分类的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前二级分类的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前一级频道ID
     */
    protected int first_level_channel_id;
    /**
     * 当前二级频道ID
     */
    protected int second_level_channel_id;

    private List<ChannelBean> grouplist;
    private List<ChannelBean> chidlist;
    private WheelView mViewGroup;
    private WheelView mViewChid;
    private Button mBtnConfirm;
    private Button mBtnCancel;
    private onConfirmListener listener = null;
    protected Context mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;

    /**
     * 解析省市区的XML数据
     */

    public WheelViewDialog(Context context, List<ChannelBean> grouplist, List<ChannelBean> chidlist,
                           onConfirmListener listener) {
        super(context);
        mContext = context;
        this.grouplist = grouplist;
        this.chidlist = chidlist;
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.category, null);
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

    private void setUpViews() {
        mViewGroup = (WheelView) mMenuView.findViewById(R.id.id_group);
        mViewChid = (WheelView) mMenuView.findViewById(R.id.id_chid);
        mBtnConfirm = (Button) mMenuView.findViewById(R.id.submit);
        mBtnCancel = (Button) mMenuView.findViewById(R.id.cancel);
    }

    private void setUpListener() {
        // 添加change事件
        mViewGroup.addChangingListener(this);
        // 添加change事件
        mViewChid.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    private void setUpData() {
        // initProvinceDatas();
        mViewGroup.setViewAdapter(new ArrayWheelAdapter(mContext, grouplist));
        // 设置可见条目数量
        mViewGroup.setVisibleItems(7);
        mViewChid.setVisibleItems(7);
        updateCities();
    }

    /**
     * 根据当前的一级分类，更新二级WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewGroup.getCurrentItem();
        mCurrentProviceName = grouplist.get(pCurrent).getChannel_name();
        chidlist = grouplist.get(pCurrent).getChildChannelList();
        if (chidlist.size() < 2) {
            mCurrentCityName = "";
            second_level_channel_id = grouplist.get(pCurrent).getId();
        }
        mViewChid.setViewAdapter(new ArrayWheelAdapter(mContext, chidlist));
        mViewChid.setCurrentItem(0);
    }

    // protected String[] strProvince;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {
                if (!mCurrentProviceName.equals("全部")) {
                    showSelectedResult();
                    this.dismiss();
                } else {
                    Tools.showToast(mContext, "不能选择全部分类");
                }

                break;
            }
            case R.id.cancel: {
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
        int pCurrent = mViewGroup.getCurrentItem();
        int ids = grouplist.get(pCurrent).getId();
        String areaName = null;
        if (mCurrentCityName != null) {
            areaName = mCurrentProviceName + mCurrentCityName;
        } else {
            areaName = mCurrentProviceName;
        }


        listener.onConfirm(areaName, ids, second_level_channel_id);
    }

    public interface onConfirmListener {
        /**
         * @param areaName 频道名称
         *                 一级频道
         *                 二级频道
         */
        public void onConfirm(String areaName, int first_level_channel_id, int second_level_channel_id);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewGroup) {
            updateCities();
        } else if (wheel == mViewChid) {
            mCurrentCityName = ">>" + chidlist.get(newValue).getChannel_name();
            second_level_channel_id = chidlist.get(newValue).getId();
        }

    }

    public class ArrayWheelAdapter extends AbstractWheelTextAdapter {

        // items
        private List<ChannelBean> list;

        /**
         * Constructor
         *
         * @param context the current context
         *                the items
         */
        public ArrayWheelAdapter(Context context, List<ChannelBean> list) {
            super(context);

            // setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
            this.list = list;
        }

        @Override
        public CharSequence getItemText(int index) {
            if (index >= 0 && index < list.size()) {
                String item = list.get(index).getChannel_name();
                if (item instanceof CharSequence) {
                    return (CharSequence) item;
                }
                return item.toString();
            }
            return null;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }
    }

}
