package com.juxun.business.street.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.juxun.business.street.config.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yl.ming.efengshe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wood121 on 2018/1/9.
 */

public class GoodsInfoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int displayWidth;
    private List<String> mList;

    private DisplayImageOptions appOptions;

    public GoodsInfoPagerAdapter(Context context, int displayWidth) {
        this.mContext = context;
        this.displayWidth = displayWidth;
    }

    public void update(ArrayList<String> list) {
        if (this.mList != null)
            this.mList.clear();
        if (list != null)
            this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // imageView.setImageResource(resIds.get(position % resIds.size()));
        if (mList.size() > 0 && mList.get(position % mList.size()) != null) {
            appOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.banner_is_loading)
                    .showImageOnLoading(null).showImageOnFail(R.drawable.banner_is_loading)
                    .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(displayWidth, displayWidth * 3 / 5));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(Constants.imageUrl+mList.get(0),
                    imageView, appOptions);
            container.addView(imageView);
//            Constants.imageUrl + mList.get(position % mList.size()),
            return imageView;
        } else {
            return null;
        }
    }

}
