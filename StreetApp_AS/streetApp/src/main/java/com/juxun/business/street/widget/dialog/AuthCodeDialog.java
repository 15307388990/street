package com.juxun.business.street.widget.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

/**
 * @author Administrator AuthCodeDialog 登录界面 验证码弹框
 */
public class AuthCodeDialog extends PopupWindow implements OnClickListener {

    private int mType = 0;  //100使用安全手机进行验证
    protected Context mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private onConfirmListener listener = null;
    private String phone;
    private ImageView iv_code;// 验证码图片
    private TextView tv_retry;// 换一张
    private Button btn_colse, btn_ok;// 取消 确定
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private String urlimg;
    private EditText et_code;

    //验证手机验证码
    public AuthCodeDialog(Context context, onConfirmListener listener, String phone, int type) {
        super(context);
        mContext = context;
        this.phone = phone;
        this.listener = listener;
        this.mType = type;
        initViews(context);
    }

    public AuthCodeDialog(Context context, onConfirmListener listener,
                          String phone) {
        super(context);
        mContext = context;
        this.phone = phone;
        this.listener = listener;
        initViews(context);
    }

    private void initViews(Context context) {
        // 初始化样式
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.auth_code_dialog, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);

        this.setContentView(viewfipper);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.update();
        setUpViews();
    }

    private void setUpViews() {
        iv_code = (ImageView) mMenuView.findViewById(R.id.iv_code);
        tv_retry = (TextView) mMenuView.findViewById(R.id.tv_retry);
        btn_colse = (Button) mMenuView.findViewById(R.id.btn_colse);
        btn_ok = (Button) mMenuView.findViewById(R.id.btn_ok);
        et_code = (EditText) mMenuView.findViewById(R.id.et_code);
        tv_retry.setOnClickListener(this);
        btn_colse.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        if (mType == 100) {
            urlimg = Constants.mainUrl + Constants.safeImageValidStream +
                    "?auth_token=" + phone + "&time=" + System.currentTimeMillis();
        } else {
            urlimg = Constants.mainUrl + Constants.imageValidStream + "?phone="
                    + phone + "&time=" + System.currentTimeMillis();
        }
        imageLoader.displayImage(urlimg, iv_code, options);
    }

    // 刷新 验证码
    public void Refresh() {
        if (mType == 100) {
            urlimg = Constants.mainUrl + Constants.safeImageValidStream
                    + "?auth_token=" + phone + "&time=" + System.currentTimeMillis();
        } else {
            urlimg = Constants.mainUrl + Constants.imageValidStream + "?phone="
                    + phone + "&time=" + System.currentTimeMillis();
        }
        imageLoader.displayImage(urlimg, iv_code, options);
        et_code.setText("");
    }

    public Bitmap getBitmapFromByte(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_retry: {
                Refresh();
                break;
            }
            case R.id.btn_colse: {
                dismiss();
                break;
            }
            case R.id.btn_ok: {
                if (!TextUtils.isEmpty(et_code.getText())) {
                    listener.onConfirm(R.id.btn_ok, et_code.getText().toString());
                } else {
                    Tools.showToast(mContext, "验证码不能为空");
                }
                break;
            }
            default:
                break;
        }

    }

    public interface onConfirmListener {
        void onConfirm(int id, String verifyCode);
    }
}
