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
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChooseDialog;
import com.juxun.business.street.widget.ChooseDialog2;
import com.juxun.business.street.widget.ChooseDialog3;
import com.juxun.business.street.widget.ClearEditText;
import com.juxun.business.street.widget.PinnedSectionListView;
import com.juxun.business.street.widget.PinnedSectionListView.PinnedSectionListAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 类名称：GoodsSearchAcitivity 类描述：商品搜索 首页 创建人：罗富贵 创建时间：2016年9月21日
 * 
 * @version
 * 
 */
public class GoodsSearchAcitivity extends BaseActivity
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
	private PinnedSectionListView lv_list;// 列表
	List<CommodityInfoBean> salingList = new ArrayList<CommodityInfoBean>();// 上架商品
	List<CommodityInfoBean> noSaleList = new ArrayList<CommodityInfoBean>();// 下架商品
	List<CommodityInfoBean> checkingList = new ArrayList<CommodityInfoBean>();// 审核中商品
	List<CommodityInfoBean> failList = new ArrayList<CommodityInfoBean>();// 审核失败商品
	List<CommodityInfoBean> revokeList = new ArrayList<CommodityInfoBean>();// 撤销商品
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private CommodityInfoBean mcommodity;
	private int goodtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_search);
		ViewUtils.inject(this);
		initView();

	}

	private void initView() {
		ed_clear.setFocusable(true);
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				Item item = (Item) lv_list.getAdapter().getItem(position);
				mcommodity = item.commodityInfoBean;
				if (item != null) {
					goodtype = item.goodtype;
					switch (item.goodtype) {
					case 1:
						// 上架商品
						ChooseDialog chooseDialog = new ChooseDialog(getApplicationContext(),
								GoodsSearchAcitivity.this);
						chooseDialog.showAtLocation(tv_search, Gravity.BOTTOM, 0, 0);
						break;
					case 2:
						// 下架商品
						ChooseDialog3 chooseDialog3 = new ChooseDialog3(getApplicationContext(),
								GoodsSearchAcitivity.this);
						chooseDialog3.showAtLocation(tv_search, Gravity.BOTTOM, 0, 0);
						break;
					case 3:
					case 4:
					case 5:
						// 审核中商品
						ChooseDialog2 chooseDialog2 = new ChooseDialog2(getApplicationContext(),
								GoodsSearchAcitivity.this);
						chooseDialog2.showAtLocation(tv_search, Gravity.BOTTOM, 0, 0);
						break;
					default:
						break;
					}
				}

			}
		});
	}

	/** 单击事件 */
	@OnClick({ R.id.button_back, R.id.tv_search })
	public void clickMethod(View v) {
		if (v.getId() == R.id.button_back) {
			finish();
		} else if (v.getId() == R.id.tv_search) {
			// if (!TextUtils.isEmpty(ed_clear.getText())) {
			searchAll();
			// }

		}
	}

	// 下架商品
	private void downSelf() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
//		map.put("agencyId", storeBean.getAdmin_agency() + "");// 机构id
//		map.put("inventoryId", mcommodity.getInventory_id() + "");
		map.put("commodity_id", mcommodity.getCommodity_id() + "");
		mQueue.add(ParamTools.packParam(Constants.downSelf, this, this, map));
	}

	// 删除商品
	private void delInventory() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
		// commodityId int 商品id
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("commodity_id", mcommodity.getCommodity_id() + "");
		mQueue.add(ParamTools.packParam(Constants.delInventory, this, this, map));
	}

	// 上架商品
	private void upShelf() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
//		map.put("inventoryId", inventoryId + "");
		map.put("commodity_id", mcommodity.getCommodity_id()+"");
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.upShelf, this, this, map));
	}

	private void searchAll() {
		salingList = new ArrayList<CommodityInfoBean>();// 上架商品
		noSaleList = new ArrayList<CommodityInfoBean>();// 下架商品
		checkingList = new ArrayList<CommodityInfoBean>();// 审核中商品
		failList = new ArrayList<CommodityInfoBean>();// 审核失败商品
		revokeList = new ArrayList<CommodityInfoBean>();// 撤销商品
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("commodity_name", ed_clear.getText().toString());
		mQueue.add(ParamTools.packParam(Constants.searchAll, this, this, map));
		loading();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!TextUtils.isEmpty(ed_clear.getText().toString())) {
			searchAll();
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
				if (url.contains(Constants.searchAll)) {

					String salingString = json.optString("salingList");
					if (!salingString.equals("[]")) {
						salingList = JSON.parseArray(salingString, CommodityInfoBean.class);
					}

					String noSaleString = json.optString("noSaleList");
					if (!noSaleString.equals("[]")) {
						noSaleList = JSON.parseArray(noSaleString, CommodityInfoBean.class);
					}

					String checkingString = json.optString("checkingList");
					if (!checkingString.equals("[]")) {
						checkingList = JSON.parseArray(checkingString, CommodityInfoBean.class);
					}

					String failString = json.optString("failList");
					if (!failString.equals("[]")) {
						failList = JSON.parseArray(failString, CommodityInfoBean.class);
					}

					String revokeString = json.optString("revokeList");
					if (!revokeString.equals("[]")) {
						revokeList = JSON.parseArray(revokeString, CommodityInfoBean.class);
					}
					if (salingList.size() < 1 & noSaleList.size() < 1 & checkingList.size() < 1 & failList.size() < 1
							& revokeList.size() < 1) {
						lv_list.setVisibility(View.GONE);
						ll_wu.setVisibility(View.VISIBLE);
						Tools.showToast(getApplicationContext(), "没有找到符合要求的商品");
					} else {
						lv_list.setAdapter(new SearchAdatper(getApplicationContext()));
						lv_list.setVisibility(View.VISIBLE);
						ll_wu.setVisibility(View.GONE);
					}
				} else if (url.contains(Constants.downSelf)) {
					Tools.showToast(getApplicationContext(), "下架成功");
					searchAll();
				} else if (url.contains(Constants.delInventory)) {
					Tools.showToast(getApplicationContext(), "删除商品成功");
					searchAll();
				} else if (url.contains(Constants.upShelf)) {
					Tools.showToast(getApplicationContext(), "上架成功");
					searchAll();
				} else if (url.contains(Constants.delInventory)) {
					Tools.showToast(getApplicationContext(), "删除商品成功");
					searchAll();
				}

			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	class SearchAdatper extends ArrayAdapter<Item> implements PinnedSectionListAdapter {
		List<String> nameList = new ArrayList<String>();
		private Context context;

		public SearchAdatper(Context context) {
			super(context, 0);
			if (salingList.size() > 0) {
				nameList.add("上架商品");
			}
			if (noSaleList.size() > 0) {
				nameList.add("下架商品");
			}
			if (checkingList.size() > 0) {
				nameList.add("审核中商品");
			}
			if (failList.size() > 0) {
				nameList.add("审核失败商品");
			}
			if (revokeList.size() > 0) {
				nameList.add("撤销商品");
			}
			prepareSections(nameList.size());
			this.context = context;
			int sectionPosition = 0, listPosition = 0;
			int type = 0;
			for (char i = 0; i < nameList.size(); i++) {
				Item section = new Item(Item.SECTION, nameList.get(i), null);
				section.sectionPosition = sectionPosition;
				section.listPosition = listPosition++;
				onSectionAdded(section, sectionPosition);
				add(section);
				List<CommodityInfoBean> list = null;
				if (nameList.get(i).equals("上架商品")) {
					list = salingList;
					type = 1;
				} else if (nameList.get(i).equals("下架商品")) {
					list = noSaleList;
					type = 2;
				} else if (nameList.get(i).equals("审核中商品")) {
					list = checkingList;
					type = 3;
				} else if (nameList.get(i).equals("审核失败商品")) {
					list = failList;
					type = 5;
				} else if (nameList.get(i).equals("撤销商品")) {
					list = revokeList;
					type = 4;
				}
				if (list != null) {
					for (int j = 0; j < list.size(); j++) {
						Item item = new Item(Item.ITEM, null, list.get(j));
						item.sectionPosition = sectionPosition;
						item.listPosition = listPosition++;
						item.goodtype = type;
						add(item);
					}

					sectionPosition++;
				}

			}
		}

		protected void prepareSections(int sectionsNumber) {
		}

		protected void onSectionAdded(Item section, int sectionPosition) {
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			Item item = getItem(position);
			final CommodityInfoBean mallShoppingCartbean = item.commodityInfoBean;
			if (item.type == Item.SECTION) {
				convertView = LayoutInflater.from(context).inflate(R.layout.inthesale_text, null);
				TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
				textView.setText(item.text);

			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.inthesale_itm, null);
				viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
				viewHolder.iv_overdue = (ImageView) convertView.findViewById(R.id.iv_overdue);
				viewHolder.iv_expirated = (ImageView) convertView.findViewById(R.id.iv_expirated);
				viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
				viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tv_inventory = (TextView) convertView.findViewById(R.id.tv_inventory);
				viewHolder.tv_inventory_warning = (TextView) convertView.findViewById(R.id.tv_inventory_warning);
			}

			if (mallShoppingCartbean != null) {
				viewHolder.tv_name.setText(mallShoppingCartbean.getCommodity_name());
				viewHolder.tv_price.setText("¥" + mallShoppingCartbean.getCommodity_price_low());
				viewHolder.tv_inventory.setText(mallShoppingCartbean.getCommodity_inventory() + "");
				imageLoader.displayImage(Constants.imageUrl + mallShoppingCartbean.getCommodity_icon(),
						viewHolder.iv_img, options);
				/***
				 * 判断是否要过期了
				 * 
				 */
				if (mallShoppingCartbean.isExpiration()) {
					viewHolder.tv_time.setText("过期日期：" + Tools.getDateformat(mallShoppingCartbean.getExpirationTime()));
					viewHolder.tv_time.setVisibility(View.VISIBLE);
					viewHolder.iv_overdue.setVisibility(View.VISIBLE);
				} else {
					viewHolder.tv_time.setVisibility(View.INVISIBLE);
					viewHolder.iv_overdue.setVisibility(View.GONE);
				}

				/***
				 * 判断是否已过期了
				 * 
				 */
				if (mallShoppingCartbean.isExpirated()) {
					viewHolder.iv_expirated.setVisibility(View.VISIBLE);
				} else {
					viewHolder.iv_expirated.setVisibility(View.GONE);
				}

				/***
				 * 判断是否库存不足
				 * 
				 */
				if (mallShoppingCartbean.getCommodity_inventory() <=10) {
					viewHolder.tv_inventory.setTextColor(getResources().getColor(R.color.red));
					viewHolder.tv_inventory_warning.setVisibility(View.VISIBLE);
				} else {
					viewHolder.tv_inventory.setTextColor(getResources().getColor(R.color.black));
					viewHolder.tv_inventory_warning.setVisibility(View.GONE);
				}
			}

			return convertView;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position).type;
		}

		@Override
		public boolean isItemViewTypePinned(int viewType) {
			return viewType == Item.SECTION;
		}

	}

	static class Item {

		public static final int ITEM = 0;
		public static final int SECTION = 1;

		public final int type;
		public final String text;
		public CommodityInfoBean commodityInfoBean;
		public int goodtype;
		public int sectionPosition;
		public int listPosition;

		public Item(int type, String text, CommodityInfoBean commodityInfoBean) {
			this.type = type;
			this.text = text;
			this.commodityInfoBean = commodityInfoBean;
		}

		@Override
		public String toString() {
			return text;
		}

	}

	final static class ViewHolder {
		/**
		 * 商品名称 商品价格 时间 库存 库存预警
		 */
		TextView tv_name, tv_price, tv_time, tv_inventory, tv_inventory_warning;
		ImageView iv_img, iv_overdue, iv_expirated;
	}

	@Override
	public void onConfirm(int id) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mCommodityInfoBean", mcommodity);
		intent.putExtras(bundle);
		switch (id) {
		// 查看详情
		case R.id.ll_checkdetails:
			intent.setClass(getApplicationContext(), CheckdetailsAcitivity.class);
			intent.putExtra("type", goodtype);
			startActivity(intent);
			break;
		// 商品上架
		case R.id.ll_shelves:
			AlertDialog.Builder builder = new Builder(GoodsSearchAcitivity.this);
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

			break;
		// 修改库存保质期
		case R.id.ll_amend_shelflife:
			intent.setClass(getApplicationContext(), AmendShekflifeAcitivity.class);
			startActivity(intent);
			break;
		// 商品下架
		case R.id.ll_soldout:
			AlertDialog.Builder builder3 = new Builder(GoodsSearchAcitivity.this);
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
			AlertDialog.Builder builder2 = new Builder(GoodsSearchAcitivity.this);
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
