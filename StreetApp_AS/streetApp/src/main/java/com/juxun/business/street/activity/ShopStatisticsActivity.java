package com.juxun.business.street.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.ShopStaAdapter;
import com.juxun.business.street.bean.ShopStaBean;
import com.juxun.business.street.bean.ShopStaBean.TopBean;
import com.juxun.business.street.bean.ShopStaBean.TopChannel;
import com.juxun.business.street.bean.ShopStaModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChartLine;
import com.juxun.business.street.widget.ChartPie;
import com.juxun.business.street.widget.ChartPie.Element;
import com.juxun.business.street.widget.InsertListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopStatisticsActivity extends BaseActivity {

    @ViewInject(R.id.tv_dayincome)
    private TextView tv_dayincome;// 今日收入
    @ViewInject(R.id.scrollview)
    private ScrollView scrollview;

    @ViewInject(R.id.tv_income_fen)
    private TextView tv_income_fen;// 分佣收入
    @ViewInject(R.id.tv_income_pos)
    private TextView tv_income_pos;// pos、台卡收入
    @ViewInject(R.id.tv_income_order)
    private TextView tv_income_order;// 订单收入
    @ViewInject(R.id.circle_view)
    private ChartPie circle_view;// 饼状图

    @ViewInject(R.id.tv_orders_seven)
    private TextView tv_orders_seven;// 近7日接单数
    @ViewInject(R.id.tv_orders_day)
    private TextView tv_orders_day;// 今日接单数
    @ViewInject(R.id.tv_orders_total)
    private TextView tv_orders_total;// 接单总数
    @ViewInject(R.id.chart)
    private ChartLine chart;// 折线图

    @ViewInject(R.id.rbt_today)
    private RadioButton rbt_today;// 今日销量
    @ViewInject(R.id.rbt_total)
    private RadioButton rbt_total;// 累计销量
    @ViewInject(R.id.rbt_category)
    private RadioButton rbt_category;// 分类销量
    @ViewInject(R.id.lv_listview)
    private InsertListView lv_listview;// 列表
    @ViewInject(R.id.tv_none)
    private TextView tv_none;// 无数据

    private ArrayList<Element> datasList = new ArrayList<Element>(); // 饼图颜色及比例列表
    private ArrayList<Object> listDatas = new ArrayList<Object>(); // 列表数据
    private List<ShopStaModel> mFormatList;
    private List<TopBean> mToday_top5_list;
    private List<ShopStaBean.TopBean2> mTotal_top5_list;
    private List<TopChannel> mChannel_top5_list;
    private int orderSeven = 0;
    private ShopStaAdapter mShopStaAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_statistics);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("营业分析");
        obtainDatas();
    }

    /* 获取数据 */
    public void obtainDatas() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.shopStatistics, this, this,
                map));
        loading();
    }

    private void initViewDatas(ShopStaBean shopStaBean) {
        DecimalFormat df = new DecimalFormat("0.00");
        // 今日收入
        tv_dayincome
                .setText(""
                        + df.format(Tools.getFenYuan(shopStaBean
                        .getToday_total_price())));
        // 环形图部分
        double today_commissions_price = Tools.getFenYuan(shopStaBean
                .getToday_commissions_price());
        double today_pos_price = Tools.getFenYuan(shopStaBean
                .getToday_pos_price());
        double today_order_price = Tools.getFenYuan(shopStaBean
                .getToday_order_price());
        double totolIncome = today_commissions_price + today_pos_price
                + today_order_price;
        tv_income_fen.setText(df.format(today_commissions_price) + " 元");
        tv_income_pos.setText(df.format(today_pos_price) + " 元");
        tv_income_order.setText(df.format(today_order_price) + " 元");

        if (totolIncome == 0) {
            circle_view.setData(datasList, "", "");
        } else {
            datasList.add(new Element(getResources().getColor(
                    R.color.point_cash_yellow),
                    (float) (today_commissions_price * 360 / totolIncome)));
            datasList.add(new Element(getResources().getColor(
                    R.color.point_cash_green),
                    (float) (today_pos_price * 360 / totolIncome)));
            datasList.add(new Element(getResources().getColor(
                    R.color.point_cash_blue),
                    (float) (today_order_price * 360 / totolIncome)));
            circle_view.setData(datasList, "", "");
        }

        // 折线图部分
        tv_orders_day.setText("今日接单数：" + shopStaBean.getToday_order_count());
        tv_orders_total.setText("接单总数：" + shopStaBean.getTotal_order_count());

        List<ShopStaModel> order_count_list = shopStaBean.getOrder_count_list();
        // 处理下集合，同时计算好近7日的总量
        if (order_count_list.size() != 0) {
            mFormatList = orderCount(order_count_list);
        }
        tv_orders_seven.setText("近7日接单数：" + orderSeven); // 设置7日总数量
        chart.setDrawPoints(true).setFillArea(true).setPlayAnim(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chart.setDatas(mFormatList);
            }
        }, 200);

        mToday_top5_list = shopStaBean.getToday_top5_list();
        mTotal_top5_list = shopStaBean.getTotal_top5_list();
        mChannel_top5_list = shopStaBean.getChannel_top5_list();

        lv_listview.setSelector(R.color.transparent);
        lv_listview.setDividerHeight(0);
        lv_listview.setDivider(null);
        mShopStaAdapter = new ShopStaAdapter(this);
        listDatas.clear();
        listDatas.addAll(mToday_top5_list);
        if (listDatas.size() == 0) {
            lv_listview.setVisibility(View.GONE);
            tv_none.setVisibility(View.VISIBLE);
        } else {
            lv_listview.setVisibility(View.VISIBLE);
            tv_none.setVisibility(View.GONE);
            mShopStaAdapter.updateAdapter(listDatas);
        }
        lv_listview.setAdapter(mShopStaAdapter);
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.rbt_today, R.id.rbt_total, R.id.rbt_category})
    public void clickMethod(View v) {
        listDatas.clear();

        switch (v.getId()) {
            case R.id.rbt_today:
                listDatas.addAll(mToday_top5_list);
                if (listDatas.size() == 0) {
                    lv_listview.setVisibility(View.GONE);
                    tv_none.setVisibility(View.VISIBLE);
                } else {
                    lv_listview.setVisibility(View.VISIBLE);
                    tv_none.setVisibility(View.GONE);
                    mShopStaAdapter.updateAdapter(listDatas);
                }
                scrollToBottom();
                break;
            case R.id.rbt_total:
                listDatas.addAll(mTotal_top5_list);
                if (listDatas.size() == 0) {
                    lv_listview.setVisibility(View.GONE);
                    tv_none.setVisibility(View.VISIBLE);
                } else {
                    lv_listview.setVisibility(View.VISIBLE);
                    tv_none.setVisibility(View.GONE);
                    mShopStaAdapter.updateAdapter(listDatas);
                }
                scrollToBottom();
                break;
            case R.id.rbt_category:
                listDatas.addAll(mChannel_top5_list);
                if (listDatas.size() == 0) {
                    lv_listview.setVisibility(View.GONE);
                    tv_none.setVisibility(View.VISIBLE);
                } else {
                    lv_listview.setVisibility(View.VISIBLE);
                    tv_none.setVisibility(View.GONE);
                    mShopStaAdapter.updateAdapter(listDatas);
                }
                scrollToBottom();
                break;
            default:
                break;
        }
    }

    private void scrollToBottom() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private List<ShopStaModel> orderCount(List<ShopStaModel> order_count_list) {
        List<ShopStaModel> list = new ArrayList<ShopStaModel>();
        for (int i = 0; i < order_count_list.size(); i++) {
            // 计算出总的数量
            ShopStaModel shopStaModelfrom = order_count_list.get(i);
            orderSeven = orderSeven + (int) shopStaModelfrom.count;
            // 修改时间格式
            String[] dataSplits = shopStaModelfrom.date.split("-");
            shopStaModelfrom.date = dataSplits[1] + "/" + dataSplits[2];

            list.add(shopStaModelfrom);
        }
        return list;

        // ShopStaModel model1 = new ShopStaModel();
        // model1.count = 44;
        // model1.date = "05/24";
        // list.add(model1);
        //
        // ShopStaModel model2 = new ShopStaModel();
        // model2.count = 23;
        // model2.date = "05/25";
        // list.add(model2);
        //
        // ShopStaModel model3 = new ShopStaModel();
        // model3.count = 24;
        // model3.date = "05/26";
        // list.add(model3);
        //
        // ShopStaModel model4 = new ShopStaModel();
        // model4.count = 45;
        // model4.date = "05/27";
        // list.add(model4);
        //
        // ShopStaModel model5 = new ShopStaModel();
        // model5.count = 80;
        // model5.date = "05/28";
        // list.add(model5);
        //
        // ShopStaModel model6 = new ShopStaModel();
        // model6.count = 205;
        // model6.date = "05/29";
        // list.add(model6);
        //
        // ShopStaModel model7 = new ShopStaModel();
        // model7.count = 60;
        // model7.date = "05/30";
        // list.add(model7);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) { // 返回成功
                String resultJson = json.getString("result");
                ShopStaBean shopStaBean = JSON.parseObject(resultJson,
                        ShopStaBean.class);
                if (shopStaBean != null)
                    initViewDatas(shopStaBean);

                mShopStaAdapter.updateAdapter(listDatas);
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

}
