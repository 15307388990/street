package com.juxun.business.street.interfaces;

import com.juxun.business.street.widget.WheelView;

public interface OnWheelChangedListener {
	void onChanged(WheelView wheel, int oldValue, int newValue);
}
