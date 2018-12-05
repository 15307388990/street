package com.juxun.business.street.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.yl.ming.efengshe.R;

public class LoadDialog extends Dialog {
	private TextView tv;
	private String title;
	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public LoadDialog(Context context, String title) {
		super(context, R.style.loadingDialogStyle);
		this.title = title;
	}

	private LoadDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		setCanceledOnTouchOutside(false);
		tv = (TextView) this.findViewById(R.id.tv);
		tv.setText(title);
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		this.getWindow().setAttributes(lp);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

}
