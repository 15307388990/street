package com.juxun.business.street.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView {
	

	    public CustomListView(Context paramContext) {
	        super(paramContext);
	    }

	    public CustomListView(Context paramContext, AttributeSet paramAttributeSet) {
	        super(paramContext, paramAttributeSet);
	    }

	    public CustomListView(Context paramContext, AttributeSet paramAttributeSet,
	            int paramInt) {
	        super(paramContext, paramAttributeSet, paramInt);
	    }
	    
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    }

	

}
