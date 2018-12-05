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
import com.juxun.business.street.widget.ChooseDialog2;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

/**
 * 
 * 类名称：OtherStateAcitivity 类描述：其它状态 创建人：罗富贵 创建时间：2016年10月11日
 * 
 * @version
 * 
 */
public class OtherStateAcitivity extends BaseActivity implements ChooseDialog2.onConfirmListener {
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
	@ViewInject(R.id.toaudit)
	private RadioButton toaudit;// 待审核
	@ViewInject(R.id.notthrough)
	private RadioButton notthrough;// 未通过
	@ViewInject(R.id.withdrawn)
	private RadioButton withdrawn;// 已撤销
	private int commodityState = 1;// 商品状态 1.审核中 3.审核失败 4.撤销
	public SwipeBackController swipeBackController;// 右滑关闭

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_other_state);
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
				ChooseDialog2 chooseDialog = new ChooseDialog2(getApplicationContext(), OtherStateAcitivity.this);
				chooseDialog.showAtLocation(button_function, Gravity.BOTTOM, 0, 0);
				mCommodityInfoBean = mCommdity.get(arg2);
			}
		});
		toaudit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commodityState = 1;
				pageNumber = 1;
				getInventories();
				mListview.smoothScrollToPosition(0);
			}

		});
		notthrough.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commodityState = 3;
				pageNumber = 1;
				getInventories();
				mListview.smoothScrollToPosition(0);
			}
		});
		withdrawn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commodityState = 4;
				pageNumber = 1;
				getInventories();
				mListview.smoothScrollToPosition(0);
			}
		});

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
	private void upShelf() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
		// map.put("inventoryId", inventoryId + "");
		map.put("commodity_id", mCommodityInfoBean.getCommodity_id() + "");
		map.put("auth_token", partnerBean.getAuth_token());

		mQueue.add(ParamTools.packParam(Constants.upShelf, this, this, map));
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

	private void getInventories() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// commodityState int 商品状态 1.审核中 3.审核失败 4.撤销
		map.put("commodityState", commodityState + "");
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("pageNumber", pageNumber + "");
		map.put("pageSize", 10 + "");
		mQueue.add(ParamTools.packParam(Constants.getCommodities, this, this, map));
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
				if (url.contains(Constants.getCommodities)) {
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
					mInthsealeAdapter.updateListView(mCommdity, commodityState);
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
			intent.setClass(OtherStateAcitivity.this, CheckdetailsAcitivity.class);
			if (commodityState == 1) {
				intent.putExtra("type", 3);
			} else if (commodityState == 3) {
				intent.putExtra("type", 5);
			} else {
				intent.putExtra("type", 4);
			}
			startActivity(intent);
			break;
		// 修改信息
		case R.id.ll_amend_message:
			intent.setClass(getApplicationContext(), ModifyGoodsAcitivity.class);
			startActivity(intent);
			break;
		// 修改库存保质期
		case R.id.ll_amend_shelflife:
			intent.setClass(OtherStateAcitivity.this, AmendShekflifeAcitivity.class);
			startActivity(intent);
			break;
		// 商品上架
		case R.id.ll_shelves:
			AlertDialog.Builder builder = new Builder(OtherStateAcitivity.this);
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
		// 删除商品
		case R.id.ll_delete:
			AlertDialog.Builder builder2 = new Builder(OtherStateAcitivity.this);
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
