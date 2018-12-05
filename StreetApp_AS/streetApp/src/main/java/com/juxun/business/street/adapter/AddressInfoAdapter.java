package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxun.business.street.bean.AddressInfoModel;
import com.yl.ming.efengshe.R;

import java.util.List;

public class AddressInfoAdapter extends BaseAdapter implements OnItemClickListener, OnClickListener {
    private List<AddressInfoModel> list = null;
    private Context mContext;
    EditCallBack editCallBack = null;
    DeleteCallBack deleteCallBack = null;
    SetDefaultAddressCallBack defAddressCallBack = null;

    public AddressInfoAdapter(Context mContext, List<AddressInfoModel> list, EditCallBack eCallback,
                              DeleteCallBack dCallback, SetDefaultAddressCallBack sCallback) {
        this.mContext = mContext;
        this.list = list;
        this.editCallBack = eCallback;
        this.deleteCallBack = dCallback;
        this.defAddressCallBack = sCallback;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<AddressInfoModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public AddressInfoModel getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final AddressInfoModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.address_info_item, null);
            viewHolder.tv_receiver_name = (TextView) view.findViewById(R.id.receiver_name);
            viewHolder.tv_receiver_phone = (TextView) view.findViewById(R.id.receiver_phone);
            viewHolder.tv_receiver_address = (TextView) view.findViewById(R.id.receiver_address);
            viewHolder.ll_edit_address = (LinearLayout) view.findViewById(R.id.ll_edit_address);
            viewHolder.cb_default_address = (CheckBox) view.findViewById(R.id.cb_default_address);
            viewHolder.ll_layout = (LinearLayout) view.findViewById(R.id.ll_layout);
            viewHolder.ll_edit_address.setOnClickListener(this);
            viewHolder.ll_delete_address = (LinearLayout) view.findViewById(R.id.ll_delete_address);
            viewHolder.ll_delete_address.setOnClickListener(this);
            viewHolder.cb_default_address.setOnClickListener(this);
            viewHolder.ll_layout.setOnClickListener(this);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_receiver_name.setText(mContent.getUser_name());
        viewHolder.tv_receiver_phone.setText(mContent.getTel());
        viewHolder.tv_receiver_address.setText(mContent.getArea_name() + mContent.getAddress());
        viewHolder.ll_edit_address.setTag(position);
        viewHolder.ll_delete_address.setTag(position);
        viewHolder.cb_default_address.setTag(position);
        viewHolder.ll_layout.setTag(position);
        viewHolder.cb_default_address.setChecked(mContent.getDefault_address_id() == 1);
        view.setOnClickListener(this);
        return view;

    }

    public final static class ViewHolder {
        TextView tv_receiver_name;
        TextView tv_receiver_phone;
        TextView tv_receiver_address;
        LinearLayout ll_edit_address;
        LinearLayout ll_delete_address, ll_layout;
        CheckBox cb_default_address;
    }

    public Boolean checkIfNeedSetDefaultAddress() {
        if (list.size() == 1 && list.get(0).getDefault_address_id() != 1) {// 当只有一个地址的时候
            return true;
        } else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public interface EditCallBack {
        void onEdit(AddressInfoModel model);
    }

    public interface DeleteCallBack {
        void onDelete(AddressInfoModel model);

    }

    public interface SetDefaultAddressCallBack {
        void onSetDefAddress(AddressInfoModel model);
    }

    @Override
    public void onClick(View v) {
        final int index = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.ll_edit_address:
                editCallBack.onEdit(list.get(index));
                break;
            case R.id.ll_delete_address:
                deleteCallBack.onDelete(list.get(index));
                break;
            case R.id.cb_default_address:
                defAddressCallBack.onSetDefAddress(list.get(index));
                break;
            case R.id.ll_layout:
                defAddressCallBack.onSetDefAddress(list.get(index));
                break;
        }
    }

}