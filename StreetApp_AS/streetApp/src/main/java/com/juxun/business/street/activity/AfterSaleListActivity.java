package com.juxun.business.street.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.juxun.business.street.fragment.AfterSaleHistoryFragment;
import com.juxun.business.street.fragment.AfterSaleRequestFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import java.util.ArrayList;

/**
 * 退换／售后 activity
 *
 * @author wood121
 */
public class AfterSaleListActivity extends BaseActivity {
    @ViewInject(R.id.button_back)
    private ImageView buttonBack;

    @ViewInject(R.id.rg_btns)
    private RadioGroup rg_btns;
    @ViewInject(R.id.rbt_request)
    private RadioButton rbt_request;
    @ViewInject(R.id.rbt_req_history)
    private RadioButton rbt_history;

    @ViewInject(R.id.fl_container)
    private FrameLayout fl_container;

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_after_sale_list);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        fragments = new ArrayList<Fragment>();
        addFragments();
        rg_btns.check(R.id.rbt_request);
        slectFragment(0);
    }

    @OnClick({R.id.button_back, R.id.rbt_request, R.id.rbt_req_history})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.rbt_request:
                slectFragment(0);
                break;
            case R.id.rbt_req_history:
                slectFragment(1);
                break;
        }
    }

    private void addFragments() {
        fragments.add(new AfterSaleRequestFragment(AfterSaleListActivity.this));
        fragments.add(new AfterSaleHistoryFragment(AfterSaleListActivity.this));
    }

    public void requestSales() {
        ((AfterSaleRequestFragment) fragments.get(0)).requestSales();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            fragments.get(0).onActivityResult(requestCode, resultCode, data);
            if (fragments.get(1).isAdded()) {
                fragments.get(1).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void slectFragment(int position) {
        // 开启事务
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // 遍历集合
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i == position) {
                // 显示fragment
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.fl_container, fragment);
                }
            } else {
                // 隐藏fragment
                if (fragment.isAdded()) {
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        // Fragment fragment;
        // if (position == 0) {
        // fragment = new AfterSaleRequestFragment(AfterSaleListActivity.this);
        // } else {
        // fragment = new AfterSaleHistoryFragment(AfterSaleListActivity.this);
        // }
        // fragmentTransaction.add(R.id.fl_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResponse(String response, String url) {

    }
}
