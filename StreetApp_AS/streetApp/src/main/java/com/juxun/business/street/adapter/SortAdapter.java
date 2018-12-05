package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.juxun.business.street.widget.wheel.CityModel;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * @author luoming 城市适配�?
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<CityModel> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<CityModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化�?,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<CityModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final CityModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.city_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // Boolean hasShow = false;
        // String sortLetters = mContent.getSortLetters();
        // for(int i = 0;i<position;i++)
        // {
        // if (sortLetters == list.get(i).getSortLetters() )
        // hasShow = true;
        // }
        //
        // if (!hasShow) {
        // viewHolder.tvLetter.setVisibility(View.VISIBLE);
        // viewHolder.tvLetter.setText(mContent.getSortLetters());
        // } else {
        // viewHolder.tvLetter.setVisibility(View.GONE);
        // }

        // 根据position获取分类的首字母的char ascii�?
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位�? ，则认为是第�?次出�?
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSort());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(list.get(position).getCity_name());

        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii�?
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSort().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSort();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母�?#代替�?
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}