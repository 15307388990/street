package com.juxun.business.street.adapter;

import java.util.List;

import com.juxun.business.street.activity.WebviewActivity;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MessageBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author luoming 消息中心
 */
public class MessageAdapter extends BaseAdapter implements OnClickListener {
	private List<MessageBean> list = null;
	private Context mContext;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private TextPaint dString;
	private onClickBack oClickBack;

	public MessageAdapter(Context mContext, List<MessageBean> list,
			onClickBack clickBack) {
		this.mContext = mContext;
		this.list = list;
		this.oClickBack = clickBack;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<MessageBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public MessageBean getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.message_list_item, null);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.tv_context = (TextView) view
					.findViewById(R.id.tv_context);
			viewHolder.tv_text = (TextView) view.findViewById(R.id.tv_text);// 纯文本
			viewHolder.tv_link = (TextView) view.findViewById(R.id.tv_link);
			viewHolder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
			viewHolder.tv_timer = (TextView) view.findViewById(R.id.tv_timer);
			

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.iv_img.setTag(position);
		viewHolder.tv_title.setTag(position);
		viewHolder.tv_context.setTag(position);
		MessageBean msBean = list.get(position);
		// viewHolder.tv_text.setText();
		viewHolder.tv_context.setText(msBean.getMessage_abstract());
		viewHolder.tv_title.setText(msBean.getMessage_name());
		viewHolder.tv_link.setText(msBean.getMessage_url());
		viewHolder.tv_timer
				.setText(Tools.getDateformat2(msBean.getCreateDate()));
		imageLoader.displayImage(Constants.imageUrl + msBean.getMessage_icon(),
				viewHolder.iv_img, options);
		// 1.纯文本2超链接3富文本"
		switch (msBean.getContext_type()) {
		case 1:
			viewHolder.tv_title.setVisibility(View.GONE);
			viewHolder.tv_link.setVisibility(View.GONE);
			viewHolder.iv_img.setVisibility(View.GONE);
			viewHolder.tv_context.setVisibility(View.GONE);
			viewHolder.tv_text.setVisibility(View.VISIBLE);
			break;
		case 2:
			viewHolder.tv_title.setVisibility(View.VISIBLE);
			viewHolder.tv_link.setVisibility(View.GONE);
			viewHolder.iv_img.setVisibility(View.VISIBLE);
			viewHolder.tv_context.setVisibility(View.VISIBLE);
			viewHolder.tv_text.setVisibility(View.GONE);
			break;
		case 3:
			viewHolder.tv_title.setVisibility(View.VISIBLE);
			viewHolder.tv_link.setVisibility(View.GONE);
			viewHolder.iv_img.setVisibility(View.VISIBLE);
			viewHolder.tv_context.setVisibility(View.VISIBLE);
			viewHolder.tv_text.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		// Spanned str = Html.fromHtml(msBean.getMessage_context());
		viewHolder.tv_text
				.setText(getClickableHtml(msBean.getMessage_context()));
		viewHolder.tv_text.setMovementMethod(LinkMovementMethod.getInstance());
		viewHolder.iv_img.setOnClickListener(this);
		viewHolder.tv_context.setOnClickListener(this);
		viewHolder.tv_title.setOnClickListener(this);
		return view;

	}

	private CharSequence getClickableHtml(String html) {
		Spanned spannedHtml = Html.fromHtml(html);
		SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(
				spannedHtml);
		URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(),
				URLSpan.class);
		for (final URLSpan span : urls) {
			setLinkClickable(clickableHtmlBuilder, span);
		}
		return clickableHtmlBuilder;
	}

	private void setLinkClickable(
			final SpannableStringBuilder clickableHtmlBuilder,
			final URLSpan urlSpan) {
		int start = clickableHtmlBuilder.getSpanStart(urlSpan);
		int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
		int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
		ClickableSpan clickableSpan = new ClickableSpan() {
			public void onClick(View view) {
				// Do something with URL here.
				Intent intent = new Intent(mContext, WebviewActivity.class);
				Agreement7 agreement7 = new Agreement7();
				agreement7.setLink_url(urlSpan.getURL());
				agreement7.setTitle("详情");
				intent.putExtra("agreement7", agreement7);
				mContext.startActivity(intent);
			}
		};
		clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
	}

	public final static class ViewHolder {
		TextView tv_title, tv_context;
		TextView tv_text;
		TextView tv_link;
		TextView tv_timer;
		ImageView iv_img;

	}

	public interface onClickBack {
		void onClick(int id);

	}

	@Override
	public void onClick(View v) {
		int da = (Integer) v.getTag();
		oClickBack.onClick(list.get((Integer) v.getTag()).getId());
	}

}