package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.ImageWithDelete;
import com.juxun.business.street.bean.ImageWithDelete.ICallBack;
import com.juxun.business.street.bean.PartnerAgencyInfo;
import com.juxun.business.street.bean.PartnerDecom;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageCompress;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.TimerDateUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.UploadUtil;
import com.juxun.business.street.widget.dialog.SelectBirthdayPop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yl.ming.efengshe.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @author 台卡申请 填写资料
 */

public class DeccaDataActivity extends BaseActivity implements TextWatcher, OnClickListener {
    @ViewInject(R.id.et_name)
    private EditText et_name;// 姓名
    @ViewInject(R.id.et_cord)
    private EditText et_cord;// 身份证号
    @ViewInject(R.id.et_email)
    private EditText et_email;// 邮箱
    @ViewInject(R.id.tv_star_timer)
    private TextView tv_star_timer;// 开始时间
    @ViewInject(R.id.tv_end_timer)
    private TextView tv_end_timer;// 结束时间
    @ViewInject(R.id.et_store_name)
    private EditText et_store_name;// 店铺名称

    @ViewInject(R.id.iv_positive)
    private ImageView iv_positive;// 身份证正面照片
    @ViewInject(R.id.iw_positive)
    private ImageWithDelete iw_positive;// 身份证正面照片 iw
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;// 身份证正面

    @ViewInject(R.id.iv_reverse)
    private ImageView iv_reverse;// 身份证反面照片
    @ViewInject(R.id.iw_reverse)
    private ImageWithDelete iw_reverse;// 身份证反面照片 iw
    @ViewInject(R.id.btn_reverse)
    private Button btn_reverse;// 身份证反面

    @ViewInject(R.id.iv_store)
    private ImageView iv_store;// 店面内部照片
    @ViewInject(R.id.iw_store)
    private ImageWithDelete iw_store;// 店面内部照片 iw

    @ViewInject(R.id.tv_banl_adds)
    private TextView tv_banl_adds;// 银行卡 所属银行
    @ViewInject(R.id.et_banl_number)
    private EditText et_banl_number;// 银行卡 卡号
    @ViewInject(R.id.tv_banl_line)
    private TextView tv_banl_line;// 银行卡 开户行名称
    @ViewInject(R.id.et_banl_name)
    private EditText et_banl_name;// 银行卡 真实姓名
    @ViewInject(R.id.et_bank_phone)
    private EditText et_bank_phone;// 银行预留手机号

    @ViewInject(R.id.btn_next)
    private Button btn_next;// 下一步

    private String shop_icon = null;// shop_icon
    private String positive_icon = null;// 身份证正面照片
    private String reverse_icon = null;// 身份证反面照片
    private String upload_token;
    private PartnerAgencyInfo mPartnerAgencyInfo;
    private PartnerDecom mPartnerDecom;
    private SelectBirthdayPop birth;
    private String account_bank_branch_code;
    TimerDateUtil timerDate = new TimerDateUtil();
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // iw_handheld.setVisibility(View.VISIBLE);
                    // iw_handheld.setBackgroundDrawable((Bitmap) msg.obj);
                    // iv_handheld.setVisibility(View.GONE);
                    break;
                case 2:
                    iw_store.setVisibility(View.VISIBLE);
                    iw_store.setBackgroundDrawable((Bitmap) msg.obj);
                    iv_store.setVisibility(View.GONE);
                    break;
                case 3:
                    iw_positive.setVisibility(View.VISIBLE);
                    iw_positive.setBackgroundDrawable((Bitmap) msg.obj);
                    iv_positive.setVisibility(View.GONE);
                    break;
                case 4:
                    iw_reverse.setVisibility(View.VISIBLE);
                    iw_reverse.setBackgroundDrawable((Bitmap) msg.obj);
                    iv_reverse.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private Intent intent;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decca_data);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        btn_next.setEnabled(initBtn());
        initTitle();
        title.setText("填写身份资料");
        getUploadToken();
        initView();
        getDecometerApplyInfo();
    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<String, String>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
    }

    private void initView() {
        et_store_name.addTextChangedListener(this);
        et_cord.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        et_name.addTextChangedListener(this);
        et_banl_name.addTextChangedListener(this);
        iw_store.setonClick(new ICallBack() {

            @Override
            public void onClickDeleteButton(int position) {
                iw_store.setVisibility(View.GONE);
                iv_store.setVisibility(View.VISIBLE);
                shop_icon = null;

            }
        });
        iw_positive.setonClick(new ICallBack() {

            @Override
            public void onClickDeleteButton(int position) {
                iw_positive.setVisibility(View.GONE);
                iv_positive.setVisibility(View.VISIBLE);
                positive_icon = null;
            }
        });
        iw_reverse.setonClick(new ICallBack() {

            @Override
            public void onClickDeleteButton(int position) {
                iw_reverse.setVisibility(View.GONE);
                iv_reverse.setVisibility(View.VISIBLE);
                reverse_icon = null;
            }
        });
        et_banl_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_next.setEnabled(initBtn());
                String string = et_banl_number.getText().toString().replace(" ", "");
                if (string.length() >= 7) {
                    getBankName(string.subSequence(0, 6).toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        iv_store.setOnClickListener(this);
        btn_positive.setOnClickListener(this);
        btn_reverse.setOnClickListener(this);
    }

    // 根据卡号前6位 获取所属银行
    private void getBankName(String card_no) {

        Map<String, String> map = new HashMap<String, String>();
        // auth_token 登录令牌
        // agency_id 机构id
        // admin_id管理员id
        // randomNum 手机验证码
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("card_no", card_no);
        mQueue.add(ParamTools.packParam(Constants.getBankName, this, this, map));
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_next, R.id.tv_star_timer, R.id.tv_end_timer, R.id.tv_banl_line})
    public void clickMethod(View v) {
        if (v.getId() == R.id.tv_star_timer) {
            birth = new SelectBirthdayPop(DeccaDataActivity.this, timerDate.getNowDate(), tv_star_timer);
            birth.showAtLocation(tv_star_timer, Gravity.BOTTOM, 0, 0);
        } else if (v.getId() == R.id.tv_end_timer) {
            birth = new SelectBirthdayPop(DeccaDataActivity.this, timerDate.getNowDate(), tv_end_timer);
            birth.showAtLocation(tv_end_timer, Gravity.BOTTOM, 0, 0);

        } else if (v.getId() == R.id.btn_next) {
            if (shop_icon != null && positive_icon != null && reverse_icon != null) {
                if (!Tools.emailFormat(et_email.getText().toString())) {
                    Tools.showToast(DeccaDataActivity.this, "邮箱输入有误");
                    return;
                }
                editDecometerApplyInfo();
            } else {
                Tools.showToast(DeccaDataActivity.this, "图片没有上传");
            }

        } else if (v.getId() == R.id.tv_banl_line) {
            if (tv_banl_adds.getText().toString().length() > 1) {
                Intent intent = new Intent(DeccaDataActivity.this, SelectCityActivity.class);
                intent.putExtra("belongs_bank", tv_banl_adds.getText().toString());// 所属银行
                startActivityForResult(intent, 1);
            } else {
                Tools.showToast(getApplicationContext(), "请先输入正确的银行卡号");
            }
        }
    }

    // 上传图片得到字符串
    private void upLoadPhoto(String filePaths, final int id) {
        loading();
        UploadManager uploadManager = UploadUtil.getUploadManager();
        String key = null;
        key = Tools.getRandomFileName();
        // 这里压缩一下图片 不然上传过慢导致加载缓慢 by yejia
        uploadManager.put(ImageCompress.BitmapToBytes2(filePaths), key, upload_token, // filePaths.get(i)
                new UpCompletionHandler() {

                    @Override
                    public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {

                        // TODO Auto-generated method stub
                        if (arg1.statusCode == 200) {
                            dismissLoading();
                            Tools.showToast(DeccaDataActivity.this, "上传成功");
                            String icon_id = null;
                            try {
                                icon_id = arg2.getString("key");
                                switch (id) {
                                    case 1:
                                        break;
                                    case 2:
                                        positive_icon = icon_id;
                                        break;
                                    case 3:
                                        reverse_icon = icon_id;
                                        break;
                                    case 4:
                                        shop_icon = icon_id;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoading();
                            Tools.showToast(DeccaDataActivity.this, "上传失败");
                        }
                    }

                }, null);
    }

    // 填写台卡申请资料
    private void editDecometerApplyInfo() {
        // idNumber 身份证号码
        // idNumberPositiveAndNegativeIcon 身份证正反面
        // businessLicenceIcon 营业执照/门头照
        // agency_id 机构Id
        // partner_name 姓名
        // id_number_valid_start 身份证有效期开始
        // id_number_valid_end 身份证有效期结束
        // partner_email 邮箱
        // mall_name 店铺/字号名称
        // account_name 持卡人真实姓名
        // account_card 持卡人卡号/支付宝账号
        // account_bank 开户行 为银行卡时必填
        // account_bank_address 开户行地址
        // bank_phone 银行预留手机号
        // account_bank_branch 支行名称
        // account_bank_branch_code 支行，行号

        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("idNumber", et_cord.getText().toString());
        map.put("idNumberPositiveAndNegativeIcon", positive_icon + "," + reverse_icon);
        map.put("businessLicenceIcon", shop_icon);
        map.put("partner_name", et_name.getText().toString());
        map.put("id_number_valid_start", tv_star_timer.getText().toString());
        map.put("id_number_valid_end", tv_end_timer.getText().toString());
        map.put("partner_email", et_email.getText().toString());
        map.put("mall_name", et_store_name.getText().toString());

        map.put("account_name", et_banl_name.getText().toString());// 持卡人真实姓名
        map.put("bank_phone", et_bank_phone.getText().toString());// 预留手机号
        map.put("account_bank_branch", tv_banl_line.getText().toString());// 支行名称
        map.put("account_bank_branch_code", account_bank_branch_code);// 支行，行号
        map.put("account_bank", tv_banl_adds.getText().toString());// 所属银行
        map.put("account_card", et_banl_number.getText().toString().replace(" ", ""));// 银行卡号
        mQueue.add(ParamTools.packParam(Constants.editDecometerApplyInfo, this, this, map));
        loading();
    }

    // 获取申请资料
    private void getDecometerApplyInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getDecometerApplyInfo, this, this, map));
        loading();
    }

    private void initDate() {

        et_cord.setText(mPartnerAgencyInfo.getIdNumber());
        et_email.setText(mPartnerAgencyInfo.getPartner_email());
        et_name.setText(mPartnerAgencyInfo.getPartner_name());
        et_store_name.setText(mPartnerAgencyInfo.getMall_name());
        tv_star_timer.setText(mPartnerAgencyInfo.getId_number_valid_start());
        tv_end_timer.setText(mPartnerAgencyInfo.getId_number_valid_end());
        et_banl_name.setText(mPartnerDecom.getAccount_name());
        et_bank_phone.setText(mPartnerDecom.getBank_phone());
        tv_banl_line.setText(mPartnerDecom.getAccount_bank_branch());
        tv_banl_adds.setText(mPartnerDecom.getAccount_bank());
        et_banl_number.setText(mPartnerDecom.getAccount_card());
        account_bank_branch_code = mPartnerDecom.getAccount_bank_branch_code();
        if (mPartnerAgencyInfo.getBusinessLicenceIcon() != null) {
            // 店铺门头照
            shop_icon = mPartnerAgencyInfo.getBusinessLicenceIcon();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Bitmap bitmap = ImageUtil.getbitmap(Constants.imageUrl + shop_icon);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    msg.what = 2;
                    handler.sendMessage(msg);

                }
            }).start();
        }
        if (mPartnerAgencyInfo.getIdNumberPositiveAndNegativeIcon() != null) {
            String[] iStrings = mPartnerAgencyInfo.getIdNumberPositiveAndNegativeIcon().split(",");
            if (iStrings.length > 0) {
                // 身份证正面照
                positive_icon = iStrings[0];
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Bitmap bitmap = ImageUtil.getbitmap(Constants.imageUrl + positive_icon);
                        Message msg = new Message();
                        msg.obj = bitmap;
                        msg.what = 3;
                        handler.sendMessage(msg);

                    }
                }).start();

            }
            if (iStrings.length > 1) {
                // 身份证反面照
                reverse_icon = iStrings[1];
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Bitmap bitmap = ImageUtil.getbitmap(Constants.imageUrl + reverse_icon);
                        Message msg = new Message();
                        msg.obj = bitmap;
                        msg.what = 4;
                        handler.sendMessage(msg);

                    }
                }).start();
            }
        }
        btn_next.setEnabled(initBtn());
        // 异步加载图片

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.editDecometerApplyInfo)) {
                    applyDecometer();
                } else if (url.contains(Constants.getBankName)) {
                    tv_banl_adds.setText(json.getString("bank_name"));
                } else if (url.contains(Constants.applyDecometer)) {
                    setResult(3);
                    Intent intent = new Intent(DeccaDataActivity.this, RegisteredSuccessfullyActivity.class);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                } else if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("token");
                } else if (url.contains(Constants.getDecometerApplyInfo)) {

                    String jsonString = json.getString("partnerInfo");
                    String partnerDecom = json.getString("partnerDecom");
                    if (!jsonString.equals("")) {
                        mPartnerAgencyInfo = JSON.parseObject(jsonString, PartnerAgencyInfo.class);
                        if (!partnerDecom.equals("")) {
                            mPartnerDecom = JSON.parseObject(partnerDecom, PartnerDecom.class);
                        } else {
                            mPartnerDecom = new PartnerDecom();
                        }
                        initDate();
                    }

                }
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }

    }

    // 提交审核
    private void applyDecometer() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.applyDecometer, this, this, map));
        loading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                tv_banl_line.setText(data.getStringExtra("cname"));
                account_bank_branch_code = data.getStringExtra("account_bank_branch_code");
            } else {
                List<String> resultList = data.getExtras().getStringArrayList("select_result");
                Bitmap bitmap = ImageUtil.decodeImage(resultList.get(0));
                upLoadPhoto(resultList.get(0), requestCode);
                switch (requestCode) {
                    case 2:
                        iw_positive.setVisibility(View.VISIBLE);
                        iw_positive.setBackgroundDrawable(bitmap);
                        iv_positive.setVisibility(View.GONE);
                        break;
                    case 3:
                        iw_reverse.setVisibility(View.VISIBLE);
                        iw_reverse.setBackgroundDrawable(bitmap);
                        iv_reverse.setVisibility(View.GONE);
                        break;
                    case 4:
                        iw_store.setVisibility(View.VISIBLE);
                        iw_store.setBackgroundDrawable(bitmap);
                        iv_store.setVisibility(View.GONE);
                        break;
                }
            }

        }
    }

    private boolean initBtn() {
        if (et_store_name.getText().length() >= 1 && et_cord.getText().length() > 1 && et_email.getText().length() > 1
                && et_name.getText().length() > 1 && tv_star_timer.getText().length() > 1
                && tv_end_timer.getText().length() > 1 && tv_banl_line.getText().length() > 0
                && et_banl_name.getText().length() > 0 && et_banl_number.getText().length() > 0
                && tv_banl_adds.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_next.setEnabled(initBtn());
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        checkoutCameraPermissions();
        checkoutWritePermissions();
        intent = new Intent(DeccaDataActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        switch (v.getId()) {
            case R.id.btn_positive:
                startActivityForResult(intent, 2);// 身份证正面
                type = "btn_positive";
                break;
            case R.id.btn_reverse:
                startActivityForResult(intent, 3);// 身份证反面
                type = "btn_reverse";
                break;
            case R.id.iv_store:
                startActivityForResult(intent, 4);// 店面门头照
                type = "iv_store";
                break;
            default:
                break;
        }
    }

    @Override
    protected void gotThePermission() {
        if (count > 1) {
            if (type.equals("btn_positive")) {
                startActivityForResult(intent, 2);
            } else if (type.equals("btn_reverse")) {
                startActivityForResult(intent, 3);
            } else {
                startActivityForResult(intent, 4);
            }
        }
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        count = count + 1;
        if (count > 1) {
            if (type.equals("btn_positive")) {
                startActivityForResult(intent, 2);
            } else if (type.equals("btn_reverse")) {
                startActivityForResult(intent, 3);
            } else {
                startActivityForResult(intent, 4);
            }
        }
    }

    @PermissionDenied(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraFailed() {
        Toast.makeText(this, "请打开拍照权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionGrant(REQUEST_CODE_WRITE)
    public void requestWriteSuccess() {
        count = count + 1;
        if (count > 1) {
            if (type.equals("btn_positive")) {
                startActivityForResult(intent, 2);
            } else if (type.equals("btn_reverse")) {
                startActivityForResult(intent, 3);
            } else {
                startActivityForResult(intent, 4);
            }
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

}
