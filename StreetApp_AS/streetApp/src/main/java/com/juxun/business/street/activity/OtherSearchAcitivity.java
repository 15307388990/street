/**
 *
 */
package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.CommodityInfoBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChooseDialog;
import com.juxun.business.street.widget.ChooseDialog2;
import com.juxun.business.street.widget.ChooseDialog3;
import com.juxun.business.street.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 类名称：GoodsSearchAcitivity 类描述： 其它商品搜索 首页 创建人：罗富贵 创建时间：2016年9月21日
 */
public class OtherSearchAcitivity extends BaseActivity
        implements ChooseDialog.onConfirmListener, ChooseDialog2.onConfirmListener, ChooseDialog3.onConfirmListener {

    @ViewInject(R.id.ed_clear)
    private ClearEditText ed_clear;// 输入框
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;// 无数据
    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.tv_search)
    private TextView tv_search;// 搜索
    @ViewInject(R.id.lv_list)
    private ListView lv_list;// 列表
    private InthesaleAdapter2 mInthsealeAdapter;
    private List<CommodityInfoBean> mCommdity;
    private CommodityInfoBean mCommodityInfoBean;// 被选中的项
    private int id;// 被选中的项

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othe_search);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        ed_clear.setFocusable(true);
        mCommdity = new ArrayList<>();
        mInthsealeAdapter = new InthesaleAdapter2(this, mCommdity);
        lv_list.setAdapter(mInthsealeAdapter);
        lv_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                id = arg2;
                mCommodityInfoBean = mCommdity.get(arg2);
                int type = 1;
                // 商品状态1.审核中 2.审核通过 3.审核失败 4.撤销",
                switch (mCommodityInfoBean.getCommodity_state()) {
                    case 1:
                        type = 3;
                        break;
                    case 2:
                        // 销售中
                        if (mCommodityInfoBean.getSale_state() == 2) {
                            type = 1;
                        } else {
                            // 未上架
                            // 2:修改 上架 删除
                            type = 2;
                        }
                        break;
                    case 3:
                        // 3:修改 删除
                        type = 3;
                        break;
                    case 4:
                        // 3:修改 删除
                        type = 3;
                        break;
                }
                /***
                 * 三种弹框 1:修改 下架 删除 2:修改 上架 删除 3:修改 删除
                 */
                if (type == 1) {
                    // 1:修改 下架 删除
                    ChooseDialog chooseDialog = new ChooseDialog(getApplicationContext(), OtherSearchAcitivity.this);
                    chooseDialog.showAtLocation(tv_search, Gravity.BOTTOM, 0, 0);
                } else if (type == 2) {
                    // 2:修改 上架 删除
                    ChooseDialog2 chooseDialog2 = new ChooseDialog2(getApplicationContext(), OtherSearchAcitivity.this);
                    chooseDialog2.showAtLocation(tv_search, Gravity.BOTTOM, 0, 0);
                } else if (type == 3) {
                    // 3:修改 删除
                    ChooseDialog3 chooseDialog3 = new ChooseDialog3(getApplicationContext(), OtherSearchAcitivity.this);
                    chooseDialog3.showAtLocation(tv_search, Gravity.BOTTOM, 0, 0);
                }
            }
        });
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.tv_search})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.tv_search) {
            if (!TextUtils.isEmpty(ed_clear.getText())) {
                getInventories(ed_clear.getText().toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(ed_clear.getText().toString())) {
            getInventories(ed_clear.getText().toString());
        }
    }

    // 下架商品
    private void downSelf() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.downSelf, this, this, map));
    }

    // 删除商品
    private void delInventory() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.delInventory, this, this, map));
    }

    // 上架商品
    private void upShelf() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.upShelf, this, this, map));
    }

    /***
     *
     * @param channelIds
     *            频道id集合 示例 7,71,72,73,74,76
     * @param orderType
     *            排序字段 1.上架时间 2.商品销量 3.商品价格 默认上架时间排序
     * @param order_type
     *            排序方式 1.升序 2.降序 默认升序
     * @param filterRules
     *            过滤规则 1.即将过期 2.库存不足 默认不过滤
     */
    private void getInventories(String keyword) {
        Map<String, String> map = new HashMap<>();

        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNumber", "1");
        map.put("pageSize", "10");
        map.put("pcid", "0");// 父频道id
        // map.put("ccid", ccid + "");// 子频道id
        map.put("order_by", "0");// 排序方式
        map.put("order_type", "0");// 排序类型 ：当order_by大于0时有效，0为正序 1倒序
        map.put("commodity_name", keyword);
        map.put("commodity_state", "0");// 商品当前状态0、查询所有1、出售中的商品2、审核通过但未上架的商品3、库存不足的商品4、待审核的商品5、审核未通过的商品6、强制下架的商品
        mQueue.add(ParamTools.packParam(Constants.getInventories, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.getInventories)) {
                    String liString = json.getString("result");
                    mCommdity = JSON.parseArray(liString, CommodityInfoBean.class);
                    if (mCommdity.size() > 0) {
                        lv_list.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                    } else {
                        Tools.showToast(OtherSearchAcitivity.this, "未搜索到商品");
                        lv_list.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                    }
                    mInthsealeAdapter.updateListView(mCommdity);
                } else if (url.contains(Constants.downSelf)) {
                    Tools.showToast(getApplicationContext(), "下架成功");
                    mCommdity.get(id).setSale_state(3);
                    mCommdity.get(id).setCommodity_state(2);
                    mInthsealeAdapter.updateListView(mCommdity);
                } else if (url.contains(Constants.delInventory)) {
                    Tools.showToast(getApplicationContext(), "删除商品成功");
                    mCommdity.remove(id);
                    mInthsealeAdapter.updateListView(mCommdity);
                } else if (url.contains(Constants.upShelf)) {
                    Tools.showToast(getApplicationContext(), "上架商品成功");
                    // pageNumber = 1;
                    // getInventories();
                    mCommdity.get(id).setSale_state(2);
                    mCommdity.get(id).setCommodity_state(2);
                    mInthsealeAdapter.updateListView(mCommdity);
                }
            } else {
                Tools.showToast(this, msg);
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @Override
    public void onConfirm(int id) {
        Intent intent = new Intent();
        switch (id) {
            // 查看详情
            case R.id.ll_checkdetails:
                intent.setClass(getApplicationContext(), CheckdetailsAcitivity.class);
                startActivity(intent);
                break;
            // 商品上架
            case R.id.ll_shelves:
                AlertDialog.Builder builder = new Builder(OtherSearchAcitivity.this);
                builder.setMessage("确认要上架这件商品吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("上架", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upShelf();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder.create().show();
                break;
            // 修改信息
            case R.id.ll_amend_message:
                intent.setClass(OtherSearchAcitivity.this, ModifyGoodsAcitivity.class);
                intent.putExtra("commodity_id", mCommodityInfoBean.getId());
                startActivity(intent);
                break;
            // 修改库存保质期
            case R.id.ll_amend_shelflife:
                intent.setClass(getApplicationContext(), AmendShekflifeAcitivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
                break;
            // 商品下架
            case R.id.ll_soldout:
                AlertDialog.Builder builder3 = new Builder(OtherSearchAcitivity.this);
                builder3.setMessage("确认要下架这件商品吗？");
                builder3.setTitle("提示");
                builder3.setPositiveButton("下架", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downSelf();
                    }
                });
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder3.create().show();
                break;
            // 删除商品
            case R.id.ll_delete:
                AlertDialog.Builder builder2 = new Builder(OtherSearchAcitivity.this);
                builder2.setMessage("确认删除这件商品吗？");
                builder2.setTitle("提示");
                builder2.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delInventory();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder2.create().show();
                break;
            default:
                break;
        }
    }
}
