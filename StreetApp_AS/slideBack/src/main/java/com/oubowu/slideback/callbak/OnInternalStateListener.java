package com.oubowu.slideback.callbak;

import com.oubowu.slideback.widget.SlideBackLayout;

/**
 * Created by Oubowu on 2016/9/22 0022 18:22.
 */
public interface OnInternalStateListener {

	void onSlide(float percent);

	void onOpen();

	void onClose(Boolean finishActivity);

	void onCheckPreActivity(SlideBackLayout slideBackLayout);

}
