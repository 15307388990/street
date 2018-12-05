package com.example.imagedemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * 自定义dialog
 * 
 */
public class SavaPhotoDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Button btn_owner;
	private Button btn_renter;
	private Button btn_cancel;

	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void back(int id);
	}

	private OnCustomDialogListener customDialogListener;
	EditText etName;

	public SavaPhotoDialog(Context context, String name,
			OnCustomDialogListener customDialogListener) {
		super(context);
		this.customDialogListener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_photo_dialog);
		btn_owner = (Button) findViewById(R.id.btn_ok);
		btn_owner.setOnClickListener(this);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		customDialogListener.back(v.getId());
		this.cancel();
	}

}
