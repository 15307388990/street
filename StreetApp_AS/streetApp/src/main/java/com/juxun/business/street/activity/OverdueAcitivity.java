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
import com.juxun.business.street.util.SwipeBackController;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChooseDialog4;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 
 * 类名称：OverdueAcitivity 类描述：将要过期的商品 创建人：罗富贵 创建时间：2016年10月11日
 * 
 * @version
 * 
 */
public class OverdueAcitivity extends BaseActivity implements ChooseDialog4.onConfirmListener {
	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;// 无数据
	@ViewInject(R.id.button_back)
	private ImageView button_back;// 返回
	@ViewInject(R.id.button_function)
	private ImageView button_function;// 查找
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPulllistview;// 查找
	private int pageNumber = 1;
	private ListView mListview;
	private InthesaleAdapter mInthsealeAdapter;
	private List<CommodityInfoBean> mCommdity;
	private CommodityInfoBean mCommodityInfoBean;// 被选中的项
	private View tishiView;
	public SwipeBackController swipeBackController;// 右滑关闭
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_overdue);
		ViewUtils.inject(this);
		swipeBackController = new SwipeBackController(this);
		initView();

	}

	private void initView() {
		initPull();
		mCommdity = new ArrayList<CommodityInfoBean>();
		mInthsealeAdapter = new InthesaleAdapter(this, mCommdity);
		mListview.setAdapter(mInthsealeAdapter);
		mListview.setDivider(null);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 >= 1) {
					ChooseDialog4 chooseDialog = new ChooseDialog4(getApplicationContext(), OverdueAcitivity.this);
					chooseDialog.showAtLocation(button_function, Gravity.BOTTOM, 0, 0);
					mCommodityInfoBean = mCommdity.get(arg2 - 1);
				}
			}
		});
		tishiView = LayoutInflater.from(this).inflate(R.layout.ll_tishi2, null);
		mListview.addHeaderView(tishiView);

	}

	private void initPull() {
		mPulllistview.setPullLoadEnabled(false);
		mPulllistview.setScrollLoadEnabled(true);
		mListview = mPulllistview.getRefreshableView();
		// mlistview.setSelector(R.color.gray);
		// mlistview.setDividerHeight(20);
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
		// TODO Auto-generated method stub
		super.onResume();
		pageNumber = 1;
		getInventories();
	}

	/** 单击事件 */
	@OnClick({ R.id.button_back, R.id.button_function })
	public void clickMethod(View v) {
		if (v.getId() == R.id.button_back) {
			this.finish();
		} else if (v.getId() == R.id.button_function) {
			Tools.jump(this, GoodsSearchAcitivity.class, false);
		}
	}

	// 下架商品
	private void downSelf() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("commodity_id", mCommodityInfoBean.getCommodity_id() + "");
		mQueue.add(ParamTools.packParam(Constants.downSelf, this, this, map));
	}

	// 删除商品
	private void delInventory() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
		// commodityId int 商品id
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("commodity_id", mCommodityInfoBean.getCommodity_id() + "");
		mQueue.add(ParamTools.packParam(Constants.delInventory, this, this, map));
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
	private void getInventories() {
		Map<String, String> map = new HashMap<String, String>();
		// channelIds String 频道id集合 示例 7,71,72,73,74,76
		// commodity_name String 要搜索的商品名（搜索才传）
		// commodity_type int 0非海淘 1海淘 传0即可
		// community_id int 社区id
		// pageNumber int 页码
		// pageSize int 每页大小
		// orderBy int 排序字段 1.上架时间 2.商品销量 3.商品价格 默认上架时间排序
		// order_type int 排序方式 1.升序 2.降序 默认升序
		// saleState int 上架状态 1.下架 2.上架 默认上架
		// filterRules int 过滤规则 1.即将过期 2.库存不足 默认不过滤
		map.put("channelIds", "");
		map.put("commodity_type", 0 + "");
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("pageNumber", pageNumber + "");
		map.put("pageSize", 10 + "");
		map.put("order_by", 3 + "");
		map.put("filterRules", 1 + "");
		mQueue.add(ParamTools.packParam(Constants.getInventories, this, this, map));
		loading();

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
					String liString = json.getString("list");
					List<CommodityInfoBean> list = JSON.parseArray(liString, CommodityInfoBean.class);
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
					mInthsealeAdapter.updateListView(mCommdity, 0);
					mPulllistview.onPullDownRefreshComplete();
					mPulllistview.onPullUpRefreshComplete();
				} else if (url.contains(Constants.upShelf)) {
					Tools.showToast(getApplicationContext(), "上架成功");
					pageNumber = 1;
					getInventories();
				} else if (url.contains(Constants.delInventory)) {
					Tools.showToast(getApplicationContext(), "删除商品成功");
					pageNumber = 1;
					getInventories();
				} else if (url.contains(Constants.downSelf)) {
					Tools.showToast(getApplicationContext(), "下架成功");
					pageNumber = 1;
					getInventories();
				}

			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	@Override
	public void onConfirm(int id) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mCommodityInfoBean", mCommodityInfoBean);
		intent.putExtras(bundle);
		switch (id) {
		// 查看详情
		case R.id.ll_checkdetails:
			intent.setClass(OverdueAcitivity.this, CheckdetailsAcitivity.class);
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		// 修改信息
		case R.id.ll_amend_message:
			intent.setClass(OverdueAcitivity.this, AmendShekflifeAcitivity.class);
			startActivity(intent);
			break;
		// 修改库存保质期
		case R.id.ll_amend_shelflife:
			intent.setClass(OverdueAcitivity.this, AmendShekflifeAcitivity.class);
			startActivity(intent);
			break;
		// 商品下架
		case R.id.ll_soldout:
			AlertDialog.Builder builder = new Builder(OverdueAcitivity.this);
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
			delInventory();
			break;
		default:
			break;
		}

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (swipeBackController.processEvent(event)) {
			return true;
		} else {
			return onTouchEvent(event);
		}
		
	}
}
