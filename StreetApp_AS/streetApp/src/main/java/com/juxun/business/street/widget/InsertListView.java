package com.juxun.business.street.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class InsertListView extends ListView {

    public InsertListView(Context context) {
        super(context);
    }

    public InsertListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    public InsertListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpece = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpece);
    }
}
