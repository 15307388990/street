package com.juxun.business.street.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.MallNewTemplateBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ClearEditText;
import com.juxun.business.street.zxing.camera.CameraManager;
import com.juxun.business.street.zxing.decoding.CaptureActivityHandler;
import com.juxun.business.street.zxing.decoding.InactivityTimer;
import com.juxun.business.street.zxing.decoding.RGBLuminanceSource;
import com.juxun.business.street.zxing.view.ViewfinderView;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 * 商品添加：扫描界面
 */
public class MipcaActivityCapture extends BaseActivity implements Callback, View.OnClickListener {

    private static final int REQUEST_CODE_TAKE_PHOTE = 0;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    private RelativeLayout rl_san2;
    private RadioGroup rl_san;
    private int type;
    // 条码查询
    private LinearLayout ll_barcode;
    private Button btn_query;
    private RelativeLayout ll_san;
    // 按钮1 按钮2
    private ImageButton iv_one, iv_two;
    private TextView tv_one, tv_two;
    private TextView textview_title;
    private ClearEditText ed_clear;
    private String code;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        // ViewUtil.addTopView(getApplicationContext(), this,
        // R.string.scan_card);

        initCamera();
        initView();
    }

    private void initCamera() {
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        ImageButton mImageButton = (ImageButton) findViewById(R.id.button_function);
        mImageButton.setOnClickListener(this);
        textview_title = (TextView) findViewById(R.id.textview_title);

        rl_san = (RadioGroup) findViewById(R.id.rl_san);
        rl_san2 = (RelativeLayout) findViewById(R.id.rl_san2);
        TextView tv_code = (TextView) findViewById(R.id.tv_code);
        TextView tv_crcodes = (TextView) findViewById(R.id.tv_crcodes);
        ImageButton btn_img2 = (ImageButton) findViewById(R.id.btn_one);
        ImageButton btn_img3 = (ImageButton) findViewById(R.id.btn_two);
        tv_code.setOnClickListener(this);
        tv_crcodes.setOnClickListener(this);
        btn_img2.setOnClickListener(this);
        btn_img3.setOnClickListener(this);
        mWidthPixels = getResources().getDisplayMetrics().widthPixels;
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {    //扫码添加商品
            rl_san.setVisibility(View.GONE);
            rl_san2.setVisibility(View.VISIBLE);
            textview_title.setText("扫码添加商品");
        } else if (type == 5) { //扫码付款
            rl_san.setVisibility(View.GONE);
            rl_san2.setVisibility(View.GONE);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mWidthPixels = getResources().getDisplayMetrics().widthPixels;
                if (type == 1) {
                    viewfinderView.drawViewfinder(CameraManager.get(), 240, 240, mWidthPixels, mWidthPixels / 3);
                } else if (type == 5) {
                    viewfinderView.drawViewfinder(CameraManager.get(), 240, 240, (int) (mWidthPixels / 1.5),
                            (int) (mWidthPixels / 1.5));
                } else {
                    viewfinderView.drawViewfinder(CameraManager.get(), 240, 240, mWidthPixels, mWidthPixels / 3);
                }
            }
        }, 800);
    }

    private void initView() {
        ImageView mButtonBack = (ImageView) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(this);

        ll_barcode = (LinearLayout) findViewById(R.id.ll_barcode);
        ll_san = (RelativeLayout) findViewById(R.id.ll_san);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
        btn_query.setClickable(false);
        iv_one = (ImageButton) findViewById(R.id.btn_one);
        iv_two = (ImageButton) findViewById(R.id.btn_two);
        tv_one = (TextView) findViewById(R.id.tv_one);
        tv_two = (TextView) findViewById(R.id.tv_two);
        iv_one.setOnClickListener(this);
        iv_two.setOnClickListener(this);
        ed_clear = (ClearEditText) findViewById(R.id.ed_clear);
        ed_clear.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 8) {
                    btn_query.setBackground(getResources().getDrawable(R.drawable.button_bg1));
                    btn_query.setTextColor(getResources().getColor(R.color.white));
                    btn_query.setClickable(true);
                } else {
                    btn_query.setBackground(getResources().getDrawable(R.drawable.button_bg));
                    btn_query.setTextColor(getResources().getColor(R.color.gray));
                    btn_query.setClickable(false);
                }
            }
        });
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.tv_code:
                viewfinderView.drawViewfinder(CameraManager.get(), 240, 240, mWidthPixels, mWidthPixels / 3);
                break;
            case R.id.tv_crcodes:
                viewfinderView.drawViewfinder(CameraManager.get(), 240, 240, (int) (mWidthPixels / 1.5),
                        (int) (mWidthPixels / 1.5));
                break;
            case R.id.btn_one:
                if (ll_san.getVisibility() == View.VISIBLE) {
                    ll_san.setVisibility(View.GONE);
                    ll_barcode.setVisibility(View.VISIBLE);
                    tv_one.setText("扫码添加商品");
                    iv_one.setBackground(getResources().getDrawable(R.drawable.codescan_icon_scan));
                    rl_san2.setBackground(getResources().getDrawable(R.drawable.title_back));
                    textview_title.setText("手动输入条码");
                } else {
                    ll_san.setVisibility(View.VISIBLE);
                    ll_barcode.setVisibility(View.GONE);
                    tv_one.setText("手动输入条码");
                    textview_title.setText("扫码添加商品");
                    iv_one.setBackground(getResources().getDrawable(R.drawable.codescan_icon_input));
                    rl_san2.setBackground(getResources().getDrawable(R.drawable.bg_black));
                }
                break;
            case R.id.btn_two:
                Intent intent = new Intent(this, AddGoodsAcitivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_query:
                initUrl(ed_clear.getText().toString());
                break;
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    onResultHandler((String) msg.obj, scanBitmap);
                    break;
                case PARSE_BARCODE_FAIL:
                    Toast.makeText(MipcaActivityCapture.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    mProgress = new ProgressDialog(MipcaActivityCapture.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result.getText();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = "Scan failed!";
                                mHandler.sendMessage(m);
                            }
                        }
                    }).start();

                    break;

            }
        }
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        onResultHandler(resultString, barcode);
    }

    /**
     * 跳转到上一个页面
     *
     * @param resultString
     * @param bitmap
     */
    private void onResultHandler(String resultString, Bitmap bitmap) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(MipcaActivityCapture.this, "扫描失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type == 5) {    //扫码到的支付码返回给支付接口
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            // bundle.putParcelable("bitmap", bitmap);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
            MipcaActivityCapture.this.finish();
        } else {
            if (rl_san.getCheckedRadioButtonId() == R.id.tv_code) {
                // 把返回的条码去服务器请求数据
                loading();
                initUrl(resultString);
            } else {
                Intent intent = new Intent(MipcaActivityCapture.this, WebviewActivity.class);
                Agreement7 agreement7 = new Agreement7();
                agreement7.setTitle("核验订单");
                agreement7.setLink_url(Constants.mainUrl + "/mall/orderInfo.html?order_sn_code="
                        + resultString.replace(" ", "")  + "&auth_token="
                        + partnerBean.getAuth_token());
                intent.putExtra("agreement7", agreement7);
                startActivity(intent);
                finish();
            }
        }
    }

    private void initUrl(String resultString) {
        code = resultString;
        Map<String, String> map = new HashMap<>();
        map.put("code", resultString);
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getTemplates, this, this, map));
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        // viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private int mWidthPixels;

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                // 如果模版库有该条码模版 直接跳修改商品信息页面
                MallNewTemplateBean mallNewTemplateBean = JSON.parseObject(json.optString("result"), MallNewTemplateBean.class);
                Intent intent = new Intent(this, ModifyGoodsAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mallNewTemplateBean", mallNewTemplateBean);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else if (status == -1) {
                initDialog();
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("未查询到相关信息\n您可以手动添加此商品");
        builder.setTitle("提示");
        builder.setPositiveButton("手动添加", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 如果模版库有该条码模版 直接跳修改商品信息页面
                Intent intent = new Intent(MipcaActivityCapture.this, AddGoodsAcitivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (handler != null) {
                    handler.quitSynchronously();
                    handler = null;
                }
                CameraManager.get().closeDriver();
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();

                if (hasSurface) {
                    initCamera(surfaceHolder);
                } else {
                    surfaceHolder.addCallback(MipcaActivityCapture.this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
                decodeFormats = null;
                characterSet = null;

                playBeep = true;
                AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                    playBeep = false;
                }
                initBeepSound();
                vibrate = true;
            }
        });
        builder.create().show();
    }
}