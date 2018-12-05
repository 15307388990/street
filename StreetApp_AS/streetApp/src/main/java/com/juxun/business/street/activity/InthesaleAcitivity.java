/**
 *
 */
package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.SuperChannelAdapter;
import com.juxun.business.street.bean.ChannelBean;
import com.juxun.business.street.bean.CommodityInfoBean;
import com.juxun.business.street.bean.SkuBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChooseDialog;
import com.juxun.business.street.widget.ChooseDialog2;
import com.juxun.business.street.widget.ChooseDialog3;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称：InthesaleAcitivity 类描述：商品管理 首页 创建人：罗富贵 创建时间：2017/9／7
 */
public class InthesaleAcitivity extends BaseActivity
        implements ChooseDialog.onConfirmListener, ChooseDialog2.onConfirmListener, ChooseDialog3.onConfirmListener {
    /**
     * 分类
     */
    @ViewInject(R.id.ll_class)
    private LinearLayout ll_class;
    @ViewInject(R.id.tv_class)
    private TextView tv_class;
    @ViewInject(R.id.iv_class)
    private ImageView iv_class;
    /**
     * 刷选方式
     */
    @ViewInject(R.id.ll_shuaixuan)
    private LinearLayout ll_shuaixuan;
    @ViewInject(R.id.tv_shuaixuan)
    private TextView tv_shuaixuan;
    @ViewInject(R.id.iv_shuaixuan)
    private ImageView iv_shuaixuan;
    /**
     * 排序方式
     */
    @ViewInject(R.id.ll_paixu)
    private LinearLayout ll_paixu;
    @ViewInject(R.id.tv_paixu)
    private TextView tv_paixu;
    @ViewInject(R.id.iv_paixu)
    private ImageView iv_paixu;

    @ViewInject(R.id.ll_prompt)
    private LinearLayout ll_prompt;// 提示
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;// 无数据
    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.button_search)
    private ImageView button_search;// 查找
    @ViewInject(R.id.button_add)
    private ImageView button_add;// 添加
    @ViewInject(R.id.lv_list)
    private PullToRefreshListView mPulllistview;// 查找
    private PopupWindow popupWindow;
    private SuperChannelAdapter groupAdapter, chidAdapter;
    private ListView lv_group, lv_chid;
    private List<ChannelBean> grouplist, chidlist;
    private int pageNumber = 1;
    private ListView mListview;
    private InthesaleAdapter2 mInthsealeAdapter;
    private List<CommodityInfoBean> mCommdity;

    private String commodity_name = "";
    private int pcid = -1, ccid = -1;// 一级频道 二级频道
    private int commodity_state = 0;
    private int orderBy = 0;
    private String[] order_type_arr = {"desc", "asc"};

    /**
     * 5个坐标记录
     */
    private int calssgroup = 0, calsschid = 0, shuixuangroup = 0, paixugroup = 0, paixuchid = 0;
    private CommodityInfoBean mCommodityInfoBean;// 被选中的项
    private int id; // 被选中的项
    private int whichPop;// 判断打开的是哪个弹框
    private int shelves_status = 0; //上架状态
    private int order_type = 0;  //默认
    private Intent intentAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inthesale);

        ViewUtils.inject(this);
        popupWindow = new PopupWindow(this);

        initView();
    }

    private void initView() {
        //获取商户是否审核通过的操作
        if (partnerBean.getApproval_status() == 1) {
            ll_prompt.setVisibility(View.GONE);
        } else {
            ll_prompt.setVisibility(View.VISIBLE);
        }

        //获取列表控件
        initPull();
        //数据显示
        mCommdity = new ArrayList<>();
        mInthsealeAdapter = new InthesaleAdapter2(this, mCommdity);
        mListview.setAdapter(mInthsealeAdapter);
        //点击事件
        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mCommodityInfoBean = mCommdity.get(arg2);
                id = arg2;
                int type = 1;
                switch (mCommodityInfoBean.getCommodity_state()) {  //商品状态 1.审核中 2.审核通过 3.审核失败 4.撤销",
                    case 1:
                        type = 3;
                        break;
                    case 2:
                        // 销售中
                        if (mCommodityInfoBean.getShelves_status() == 3) {  //上架状态 1.未通过审核  2、下架中 3、上架 4、强制下架",
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
                    ChooseDialog chooseDialog = new ChooseDialog(getApplicationContext(), InthesaleAcitivity.this);
                    chooseDialog.showAtLocation(tv_class, Gravity.BOTTOM, 0, 0);
                } else if (type == 2) {
                    // 2:修改 上架 删除
                    ChooseDialog2 chooseDialog2 = new ChooseDialog2(getApplicationContext(), InthesaleAcitivity.this);
                    chooseDialog2.showAtLocation(tv_class, Gravity.BOTTOM, 0, 0);
                } else if (type == 3) {
                    // 3:修改 删除
                    ChooseDialog3 chooseDialog3 = new ChooseDialog3(getApplicationContext(), InthesaleAcitivity.this);
                    chooseDialog3.showAtLocation(tv_class, Gravity.BOTTOM, 0, 0);
                }
            }
        });
    }

    private void initPull() {
        mPulllistview.setPullLoadEnabled(false);
        mPulllistview.setScrollLoadEnabled(true);
        mListview = mPulllistview.getRefreshableView();
        mListview.setDivider(new ColorDrawable(getResources().getColor(R.color.gray)));
        mListview.setDividerHeight(1);
        mPulllistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                getInventories();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                getInventories();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNumber = 1;
        getInventories();
    }

    /**
     * 改变ui O没有打开任何的弹框
     *
     * @author 1 打开了第一个
     */
    private void initIndex() {
        if (whichPop == 0) {
            iv_class.setBackgroundResource(R.drawable.icon_arrow_down);
            iv_paixu.setBackgroundResource(R.drawable.icon_arrow_down);
            iv_shuaixuan.setBackgroundResource(R.drawable.icon_arrow_down);
        } else if (whichPop == 1) {
            iv_class.setBackgroundResource(R.drawable.icon_arrow_up);
            iv_paixu.setBackgroundResource(R.drawable.icon_arrow_down);
            iv_shuaixuan.setBackgroundResource(R.drawable.icon_arrow_down);
        } else if (whichPop == 2) {
            iv_class.setBackgroundResource(R.drawable.icon_arrow_down);
            iv_paixu.setBackgroundResource(R.drawable.icon_arrow_up);
            iv_shuaixuan.setBackgroundResource(R.drawable.icon_arrow_down);
        } else if (whichPop == 3) {
            iv_class.setBackgroundResource(R.drawable.icon_arrow_down);
            iv_paixu.setBackgroundResource(R.drawable.icon_arrow_down);
            iv_shuaixuan.setBackgroundResource(R.drawable.icon_arrow_up);
        }
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.ll_class, R.id.ll_shuaixuan, R.id.ll_paixu, R.id.button_back, R.id.button_add, R.id.button_search})
    public void clickMethod(View v) {
        if (v.getId() == R.id.ll_class) {
            if (whichPop == 1) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    whichPop = 0;
                } else {
                    loading();
                    whichPop = 1;
                    getOpenChannels();
                }
            } else {
                popupWindow.dismiss();
                loading();
                getOpenChannels();
                whichPop = 1;
            }
            initIndex();
        } else if (v.getId() == R.id.ll_shuaixuan) {
            if (whichPop == 3) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    whichPop = 0;
                } else {
                    popupWindow.dismiss();
                    whichPop = 0;
                }
            } else {
                popupWindow.dismiss();
                getSkuNumberWithCommodityState();
                whichPop = 3;
            }
            initIndex();
        } else if (v.getId() == R.id.ll_paixu) {
            if (whichPop == 2 && popupWindow.isShowing()) {
                popupWindow.dismiss();
                whichPop = 0;
            } else {
                popupWindow.dismiss();
                String[] strings = {"默认全部", "商品价格", "商品销量", "添加时间"};
                String[] strings2 = {"从高到低", "从低到高"};
                String[] strings3 = {"从远到近", "从近到远"};
                grouplist = new ArrayList<>();
                for (int i = 0; i < strings.length; i++) {
                    ChannelBean channel = new ChannelBean();
                    channel.setChannel_name(strings[i]);
                    List<ChannelBean> sublist = new ArrayList<>();
                    for (int j = 0; j < strings2.length; j++) {
                        ChannelBean channel2 = new ChannelBean();
                        channel2.setChannel_name(strings2[j]);
                        sublist.add(channel2);
                    }
                    if (i == 0) {
                        List<ChannelBean> channels = new ArrayList<>();
                        channel.setChildChannelList(channels);
                    } else if (i == 3) {
                        List<ChannelBean> channels = new ArrayList<>();
                        for (int j = 0; j < strings3.length; j++) {
                            ChannelBean channel3 = new ChannelBean();
                            channel3.setChannel_name(strings3[j]);
                            channels.add(channel3);
                        }
                        channel.setChildChannelList(channels);
                    } else {
                        channel.setChildChannelList(sublist);
                    }
                    grouplist.add(channel);
                }
                chidlist = new ArrayList<>();
                initDialog(tv_paixu, iv_paixu);
                whichPop = 2;
            }
            initIndex();
        } else if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.button_search) {
            Intent intent = new Intent(getApplicationContext(), OtherSearchAcitivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        } else if (v.getId() == R.id.button_add) {
            intentAdd = new Intent(getApplicationContext(), MipcaActivityCapture.class);
            intentAdd.putExtra("type", 1);
            checkoutCameraPermissions();
        }
    }

    @Override
    protected void gotThePermission() {
        startActivity(intentAdd);
    }


    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        //扫描只有在被同意的情况下才能跳转
        startActivity(intentAdd);
    }

    @PermissionDenied(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraFailed() {
        Toast.makeText(this, "请打开拍照权限以扫码添加商品", Toast.LENGTH_SHORT).show();
    }


    private void getOpenChannels() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getOpenChannels, this, this, map));
    }

    // 下架商品
    private void downSelf() {
        Map<String, String> map = new HashMap<>();
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.downSelf, this, this, map));
    }

    // 删除商品
    private void delInventory() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.delInventory, this, this, map));
    }

    /***
     * 获取商品列表
     */
    private void getInventories() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        map.put("commodity_name", commodity_name);
        if (pcid != -1) {
            map.put("first_level_channel_id", pcid + "");// 父频道id
        }
        if (ccid != -1) {
            map.put("second_level_channel_id", ccid + "");// 子频道id
        }
        //commodity_state//0所有商品数量 1:出售中的商品数量 2:审核通过但未上架  3：库存不足 4：待审核 5:审核未通过 6：强制下架的
        switch (commodity_state) {
            case 0:
                map.put("commodity_state", "0");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "0");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "0");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
            case 1:
                map.put("commodity_state", "2");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "3");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "0");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
            case 2:
                map.put("commodity_state", "2");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "2");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "0");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
            case 3:
                map.put("commodity_state", "2");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "0");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "1");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
            case 4:
                map.put("commodity_state", "1");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "0");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "0");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
            case 5:
                map.put("commodity_state", "3");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "0");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "0");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
            case 6:
                map.put("commodity_state", "4");//  1.审核中 2.审核通过 3.审核失败 4.撤销 0全部
                map.put("shelves_status", "0");//获取上架状态1.未通过审核2、下架中3、上架4、强制下架 0全部
                map.put("inventory_status", "0");//库存状态 0：全部 1库存不足（库存小于等于10为库存不足）
                break;
        }

        map.put("order_by", orderBy + "");// 排序方式 * 0、默认排序 1、价格排序 2、销量排序 3、添加时间排序
        map.put("order_type", order_type_arr[order_type]);// 排序类型 desc 降序 asc 升序 默认升序
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 10 + "");
        mQueue.add(ParamTools.packParam(Constants.getInventories, this, this, map));
    }

    /***
     * 获取各个状态下sku数量
     */
    private void getSkuNumberWithCommodityState() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("first_level_channel_id", pcid + "");// 父频道id
        map.put("second_level_channel_id", ccid + "");// 子频道id
        mQueue.add(ParamTools.packParam(Constants.getSkuNumberWithCommodityState, this, this, map));
    }

    public void setHeight(ListView listView, ListView listView2) {
        int listViewHeight = 0;
        int adaptCount = groupAdapter.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = groupAdapter.getView(i, null, listView);
            temp.measure(0, 0);
            listViewHeight += temp.getMeasuredHeight();
        }
        LayoutParams layoutParams = lv_group.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT / 2;
        layoutParams.height = listViewHeight;
        listView.setLayoutParams(layoutParams);
        listView2.setLayoutParams(layoutParams);
    }

    private void initDialog(final TextView textView, final ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.icon_arrow_up);
        View superview = LayoutInflater.from(this).inflate(R.layout.super_pop_win, null);
        popupWindow.setContentView(superview);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //初始化数据
        groupAdapter = new SuperChannelAdapter(this, grouplist, 0);
        chidAdapter = new SuperChannelAdapter(this, chidlist, 1);
        popupWindow.setBackgroundDrawable(null);
        lv_group = (ListView) superview.findViewById(R.id.lv_group);
        lv_chid = (ListView) superview.findViewById(R.id.lv_chid);
        lv_group.setAdapter(groupAdapter);
        lv_chid.setAdapter(chidAdapter);
        //类别区分
        if (textView == tv_class) {
            LayoutParams layoutParams = lv_group.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT / 2;
            layoutParams.height = 900;
            lv_group.setLayoutParams(layoutParams);
            lv_chid.setLayoutParams(layoutParams);
            groupAdapter.setSelectItem(calssgroup);
            chidlist = grouplist.get(calssgroup).getChildChannelList();
            chidAdapter.updateListView(chidlist, 1);
            chidAdapter.setSelectItem(calsschid);
            lv_chid.setVisibility(View.VISIBLE);
        } else if (textView == tv_shuaixuan) {
            setHeight(lv_group, lv_chid);
            lv_chid.setVisibility(View.GONE);
            groupAdapter.setSelectItem(shuixuangroup);
        } else if (textView == tv_paixu) {
            setHeight(lv_group, lv_chid);
            groupAdapter.setSelectItem(paixugroup);
            chidlist = grouplist.get(paixugroup).getChildChannelList();
            chidAdapter.updateListView(chidlist, 1);
            chidAdapter.setSelectItem(paixuchid);
            lv_chid.setVisibility(View.VISIBLE);
        }
        //点击事件处理
        lv_group.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (textView == tv_class) {
                    if (arg2 == 0) {
                        pcid = -1;
                    } else {
                        pcid = grouplist.get(arg2).getId();
                    }

                    calssgroup = arg2;
                } else if (textView == tv_paixu) {
                    paixugroup = arg2;
                } else if (textView == tv_shuaixuan) {
                    shuixuangroup = arg2;
                }
                groupAdapter.setSelectItem(arg2);
                chidlist = grouplist.get(arg2).getChildChannelList();
                if (chidlist.size() > 1) {
                    chidAdapter.updateListView(chidlist, 1);
                    chidAdapter.setSelectItem(0);
                    if (textView == tv_paixu) {
                        //  0、默认排序 1、价格排序 2、销量排序 3、添加时间排序
                        orderBy = arg2;
                    }
                } else {
                    /**
                     * 三种情况 1：如果选择的是分类 改变channelID 2:如果选择的是刷选方式 3:排序方式
                     */
                    textView.setText(grouplist.get(arg2).getChannel_name());
                    if (textView == tv_class) {
                        pcid = grouplist.get(arg2).getId();
                        // if
                        // (grouplist.get(arg2).getChannel_name().equals("全部"))
                        // {
                        // channelId = "";
                        // }
                    } else if (textView == tv_shuaixuan) {
                        commodity_state = arg2;
                        switch (arg2) {
                            case 0:
                                textView.setText("商品状态");
                                break;
                            case 1:
                                textView.setText("出售中");
                                break;
                            case 2:
                                textView.setText("未上架");
                                break;
                            case 3:
                                textView.setText("库存不足");
                                break;
                            case 4:
                                textView.setText("待审核");
                                break;
                            case 5:
                                textView.setText("审核未通过");
                                break;
                            case 6:
                                textView.setText("已撤销");
                                break;
                            default:
                                break;
                        }
                    } else if (textView == tv_paixu) {
                        orderBy = arg2;
                    }
                    pageNumber = 1;
                    getInventories();
                    popupWindow.dismiss();
                    whichPop = 0;
                    initIndex();
                }

            }
        });
        lv_chid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                imageView.setBackgroundResource(R.drawable.icon_arrow_down);
                textView.setText(chidlist.get(arg2).getChannel_name().equals("全部")
                        ? grouplist.get(calssgroup).getChannel_name() : chidlist.get(arg2).getChannel_name());
                /**
                 * 三种情况 1：如果选择的是分类 改变channeiID 2:如果选择的是刷选方式
                 */
                if (textView == tv_class) {
                    ccid = chidlist.get(arg2).getId();
                    calsschid = arg2;
                } else if (textView == tv_paixu) {
                    if (arg2 == 0) {
                        order_type = 0; //0降序、1升序，对应字符串数组
                    } else {
                        order_type = 1;
                    }
                    paixuchid = arg2;
                }
                getInventories();
                popupWindow.dismiss();
                whichPop = 0;
                initIndex();
            }
        });
        TextView tv_close = (TextView) superview.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                whichPop = 0;
                initIndex();
            }
        });
        rightPopupShow();
    }

    private void rightPopupShow() {
        // popupWindow展示位置设置，7.0系统有区别的
        if (Build.VERSION.SDK_INT >= 24) {
            int[] location = new int[2];
            // 记录parent在屏幕中的位置
            ll_class.getLocationOnScreen(location);
            int offsetY = location[1];
            if (Build.VERSION.SDK_INT == 25) {
                // 重新设置 PopupWindow 的高度
                popupWindow.setHeight(mDisplayheight - offsetY - ll_class.getHeight());
            }
            popupWindow.showAtLocation(ll_class, Gravity.NO_GRAVITY, 0, offsetY + ll_class.getHeight());
        } else {
            popupWindow.showAsDropDown(ll_class, 0, 0);
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.getOpenChannels)) {
                    grouplist = new ArrayList<ChannelBean>();
                    String liString = json.getString("result");
                    List<ChannelBean> group = JSON.parseArray(liString, ChannelBean.class);
                    ChannelBean channelBean = new ChannelBean();
                    channelBean.setChannel_name("全部");
                    List<ChannelBean> group2 = new ArrayList<ChannelBean>();
                    channelBean.setChildChannelList(group2);
                    grouplist.add(channelBean);
                    grouplist.addAll(group);
                    chidlist = group.get(0).getChildChannelList();
                    initDialog(tv_class, iv_class);
                } else if (url.contains(Constants.getInventories)) {
                    List<CommodityInfoBean> list = JSON.parseArray(json.getString("result"), CommodityInfoBean.class);
                    if (list.size() < 10) { //超过预期，不可操作
                        mPulllistview.setHasMoreData(false);
                    }
                    if (pageNumber > 1) {
                        mCommdity.addAll(list);
                    } else {
                        mCommdity = list;
                    }
                    if (mCommdity.size() > 0) {
                        mPulllistview.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                    } else {
                        mPulllistview.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                    }
                    mInthsealeAdapter.updateListView(mCommdity);
                    mPulllistview.onPullDownRefreshComplete();
                    mPulllistview.onPullUpRefreshComplete();
                } else if (url.contains(Constants.downSelf)) {
                    Tools.showToast(getApplicationContext(), "下架成功");
                    mCommdity.get(id).setShelves_status(2);
                    mCommdity.get(id).setCommodity_state(2);
                    mInthsealeAdapter.updateListView(mCommdity);
                } else if (url.contains(Constants.delInventory)) {
                    Tools.showToast(getApplicationContext(), "删除商品成功");
                    mCommdity.remove(mCommodityInfoBean);
                    mInthsealeAdapter.updateListView(mCommdity);
                } else if (url.contains(Constants.upShelf)) {
                    Tools.showToast(getApplicationContext(), "上架商品成功");
                    mCommdity.get(id).setShelves_status(3);
                    mCommdity.get(id).setCommodity_state(2);
                    mInthsealeAdapter.updateListView(mCommdity);
                } else if (url.contains(Constants.getSkuNumberWithCommodityState)) {
                    SkuBean skuBean = JSON.parseObject(json.optString("result"), SkuBean.class);
                    String[] strings = {"全部状态的商品(" + skuBean.getTotal_count() + ")",
                            "出售中的商品(" + skuBean.getSaleing_count() + ")",
                            "未上架的商品(" + skuBean.getNot_saleing_count() + ")",
                            "库存不足的商品(" + skuBean.getInventory_shortage_count() + ")",
                            "待审核的商品(" + skuBean.getPending_review_count() + ")",
                            "审核未通过的商品(" + skuBean.getNot_pass_count() + ")",
                            "已撤销的商品(" + skuBean.getForced_off_the_shelf_count() + ")"};
                    grouplist = new ArrayList<>();
                    for (int i = 0; i < strings.length; i++) {
                        ChannelBean channel = new ChannelBean();
                        List<ChannelBean> channels = new ArrayList<>();
                        channel.setChildChannelList(channels);
                        channel.setChannel_name(strings[i]);
                        grouplist.add(channel);
                    }
                    chidlist = new ArrayList<>();
                    initDialog(tv_shuaixuan, iv_shuaixuan);
                }
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    //商品上架
    private void upShelf() {
        Map<String, String> map = new HashMap<>();
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.upShelf, this, this, map));
        loading();
    }

    @Override
    public void onConfirm(int id) {
        Intent intent = new Intent();
        switch (id) {
            // 查看详情
            case R.id.ll_checkdetails:
                intent.setClass(InthesaleAcitivity.this, CheckdetailsAcitivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            // 修改信息
            case R.id.ll_amend_message:
                intent.setClass(InthesaleAcitivity.this, ModifyGoodsAcitivity.class);
                intent.putExtra("commodity_id", mCommodityInfoBean.getId());
                startActivity(intent);
                break;
            // 修改库存保质期
            case R.id.ll_amend_shelflife:
                intent.setClass(InthesaleAcitivity.this, AmendShekflifeAcitivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
                break;
            // 商品下架
            case R.id.ll_soldout:
                AlertDialog.Builder builder = new Builder(InthesaleAcitivity.this, AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("确认要下架这件商品吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("下架", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downSelf();
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
            // 删除商品
            case R.id.ll_delete:
                AlertDialog.Builder builder2 = new Builder(InthesaleAcitivity.this, AlertDialog.THEME_HOLO_LIGHT);
                builder2.setMessage("确认要删除这件商品吗？");
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
            // 上架商品
            case R.id.ll_shelves:
                AlertDialog.Builder builder3 = new Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder3.setMessage("此商品是否需要上架？");
                builder3.setTitle("商品上架");
                builder3.setPositiveButton("上架", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upShelf();
                    }
                });

                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder3.create().show();
                break;
            default:
                break;
        }
    }
}
