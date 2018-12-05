/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juxun.business.street.bean.ImageWithDelete;
import com.juxun.business.street.bean.ImageWithDelete.ICallBack;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageCompress;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.UploadUtil;
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
 * @version 添加账户
 */
public class AddAccountActivity extends BaseActivity implements TextWatcher {

    @ViewInject(R.id.ll_banl_cb)
    private LinearLayout ll_banl_cb;
    @ViewInject(R.id.iv_banl)
    private ImageView iv_banl;

    @ViewInject(R.id.ll_banl)
    private LinearLayout ll_banl;// 银行卡
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
    private Button btn_next;// 完成

    @ViewInject(R.id.iw_positive)
    private ImageWithDelete iw_positive;// 银行卡正面
    @ViewInject(R.id.iv_positive)
    private ImageView iv_positive;// 银行卡正面
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;// 银行卡正面

    private String account_bank_branch_code;
    private String positive_icon = null;
    private String upload_token;
    private Intent intent;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account2);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("添加账户");
        getUploadToken();
        initView();
    }

    private void initView() {
        btn_next.setEnabled(false);
        et_banl_name.addTextChangedListener(this);
        et_banl_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // 添加账户
    private void addAgencyAccountAndApply() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("account_name", et_banl_name.getText().toString());// 持卡人真实姓名
        map.put("account_type", "1");// 0为支付宝 1为银行卡
        map.put("bank_phone", et_bank_phone.getText().toString());// 预留手机号
        map.put("account_bank_branch", tv_banl_line.getText().toString());// 支行名称
        map.put("account_bank_branch_code", account_bank_branch_code);// 支行，行号
        map.put("account_bank", tv_banl_adds.getText().toString());// 所属银行
        map.put("account_card", et_banl_number.getText().toString().replace(" ", ""));// 银行卡号
        map.put("bank_card_icon", positive_icon);// 银行卡正面照
        mQueue.add(ParamTools.packParam(Constants.addAgencyAccountAndApply, this, this, map));
        loading();
    }

    // 提交审核
    private void applyDecometer() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.applyDecometer, this, this, map));
        loading();
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
    @OnClick({R.id.btn_next, R.id.ll_banl_cb, R.id.tv_banl_line, R.id.btn_positive})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (positive_icon == null) {
                    Tools.showToast(getApplicationContext(), "请上传银行卡正面照");
                    return;
                }
                // 完成
                addAgencyAccountAndApply();
                break;
            case R.id.tv_banl_line:
                if (tv_banl_adds.getText().toString().length() > 1) {
                    Intent intent = new Intent(AddAccountActivity.this, SelectCityActivity.class);
                    intent.putExtra("belongs_bank", tv_banl_adds.getText().toString());// 所属银行
                    startActivityForResult(intent, 1);
                } else {
                    Tools.showToast(getApplicationContext(), "请先输入正确的银行卡号");
                }
                break;
            case R.id.btn_positive:
                checkoutWritePermissions();
                checkoutCameraPermissions();
                intent = new Intent(this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//                startActivityForResult(intent, 2);// 身份证正面
                break;
        }
    }

    @Override
    protected void gotThePermission() {
        if (count > 1) {
            startActivityForResult(intent, 2);
        }
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        count = count + 1;
        if (count > 1) {
            startActivityForResult(intent, 2);
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
            startActivityForResult(intent, 2);
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                tv_banl_line.setText(data.getStringExtra("cname"));
                account_bank_branch_code = data.getStringExtra("account_bank_branch_code");
            } else if (requestCode == 2) {
                List<String> resultList = data.getExtras().getStringArrayList("select_result");
                Bitmap bitmap = ImageUtil.decodeImage(resultList.get(0));
                upLoadPhoto(resultList.get(0), requestCode);
                iw_positive.setVisibility(View.VISIBLE);
                iw_positive.setBackgroundDrawable(bitmap);
                iv_positive.setVisibility(View.GONE);
            }
        }
    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<String, String>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
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
                            Tools.showToast(AddAccountActivity.this, "上传成功");
                            String icon_id = null;
                            try {
                                icon_id = arg2.getString("key");
                                positive_icon = icon_id;
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoading();
                            Tools.showToast(AddAccountActivity.this, "上传失败");
                        }
                    }

                }, null);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.getBankName)) {
                    tv_banl_adds.setText(json.getString("bank_name"));
                } else if (url.contains(Constants.addAgencyAccountAndApply)) {
                    applyDecometer();
                } else if (url.contains(Constants.applyDecometer)) {
                    setResult(3);
                    Intent intent = new Intent(AddAccountActivity.this, RegisteredSuccessfullyActivity.class);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                } else if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("token");
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(AddAccountActivity.this, LoginActivity.class, false);
                Tools.showToast(AddAccountActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            Tools.showToast(getApplicationContext(), "解析数据错误");
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

    private boolean initBtn() {
        if (tv_banl_line.getText().length() > 0 && et_banl_name.getText().length() > 0
                && et_banl_number.getText().length() > 0 && tv_banl_adds.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;

        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }

    }

}
