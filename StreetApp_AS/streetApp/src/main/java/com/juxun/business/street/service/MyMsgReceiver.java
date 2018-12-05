package com.juxun.business.street.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.iboxpay.print.PrintManager;
import com.iboxpay.print.model.CharacterParams;
import com.iboxpay.print.model.PrintItemJobInfo;
import com.iboxpay.print.model.PrintJobInfo;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.juxun.business.street.activity.MyOrderDetailsActivity;
import com.juxun.business.street.activity.PromptActivity;
import com.juxun.business.street.activity.WebviewActivity;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.bean.StoreBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.HttpUtil;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anetwork.channel.Header;

import static com.juxun.business.street.bean.ParseModel.storeBean;

public class MyMsgReceiver extends MessageReceiver {
    private PrintManager mPrintManager;
    private Context mContext;
    private Gson gson = new Gson();
    private SavePreferencesData mSavePreferencesData;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private PartnerBean partnerBeana;

    @Override
    public void onHandleCall(Context arg0, Intent arg1) {
        // super.onHandleCall(arg0, arg1);
        String json = arg1.getStringExtra("body");
        JSONObject jsonObject = null;
        this.mContext = arg0;
        mPrintManager = (PrintManager) arg0.getSystemService("iboxpay_print");
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        SpeechSynthesizer.getSynthesizer();
        mSavePreferencesData = new SavePreferencesData(arg0);
        partnerBeana = JSON.parseObject(mSavePreferencesData.getStringData("json"), PartnerBean.class);
        try {
            // "id": "string,外部id号",
            // "order_id": "string,订单id 订单通知时必传",
            // "message_type": "string,消息类型 0为订单通知，1为推送通知，2，为刷卡支付成功推送",
            // "member_id": "string,用户id 下单的用户id 订单通知时必传"
            jsonObject = new JSONObject(json);
            // 标题
            String message_title = jsonObject.getString("title");
            String ext = jsonObject.getString("ext");
            jsonObject = new JSONObject(ext);
            String id = jsonObject.getString("id");
            String orderid = jsonObject.getString("order_id");
            String member_id = jsonObject.getString("member_id");
            int message_type = jsonObject.getInt("message_type");
            // 摘要
            String message_summary = jsonObject.getString("summary");
            if (message_type == 0) {
                initUrl(orderid, member_id);
            } else if (message_type == 1) {
                createNotificationMessge(id, message_title);
            } else if (message_type == 2) {
                // 移动数据分析，收集开始合成事件
                FlowerCollector.onEvent(mContext, "tts_play");

                // 设置参数
                setParam();
                int code = mTts.startSpeaking(message_summary, mTtsListener);
                System.out.println("code=" + code);
                // /**
                // * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
                // * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
                // */
                // String path =
                // Environment.getExternalStorageDirectory()+"/tts.pcm";
                // int code = mTts.synthesizeToUri(
                //     text, path, mTtsListener);

                // if (code != ErrorCode.SUCCESS) {
                // if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // // 未安装则跳转到提示安装页面
                // mInstaller.install();
                // } else {
                // showTip("语音合成失败,错误码: " + code);
                // }
                // }
            } else if (message_type == 3) {
                // 需要修改支付密码
                mSavePreferencesData.putBooleanData("isMima", true);
                createNotificationMessge(id, message_title);
                Intent intent = new Intent(mContext, PromptActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

        } catch (JSONException e) {
            Tools.showToast(mContext, "数据格式不对");
            e.printStackTrace();
        }

    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Tools.showToast(mContext, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            // showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            // showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            // showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            // mPercentForBuffering = percent;
            // showTip(String.format(getString(R.string.tts_toast_format),
            // mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            // mPercentForPlaying = percent;
            // showTip(String.format(getString(R.string.tts_toast_format),
            // mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                // showTip("播放完成");
            } else if (error != null) {
                Tools.showToast(mContext, error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            // if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            // String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            // Log.d(TAG, "session id =" + sid);
            // }
        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            // 设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            // 设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            // 设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    private Response.Listener listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response, String url) {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
            int status = jsonObject.getIntValue("status");
            String msg = jsonObject.getString("msg");
            if (status == 0) {
                try {
                    String json = jsonObject.getString("result");
                    OrderModel model = gson.fromJson(json, OrderModel.class);
                    if (model != null) {
                        if (partnerBeana.getStore_id() == model.getAgency_id()) {
                            createNotification(model);
                            if (mSavePreferencesData.getBooleanData("isSn")) {
                                mPrintManager.printLocaleJob(createPrintReceiptTask(model), null);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Tools.showToast(mContext, "解析错误");
                }
            } else

            {
                Tools.showToast(mContext, msg);
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Tools.showToast(mContext, error.toString());
        }
    };

    // 根据 订单ID获取订单信息
    private void initUrl(final String orderid, String member_id) {
        //创建请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        //创建请求
        String url = Constants.mainUrl + Constants.getOrderInfo;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
                map.put("order_num", orderid);// 管理员设备id
                return map;
            }
        };

        //加入请求队列
        requestQueue.add(stringRequest);

        RequestParams params = new RequestParams();
        // auth_token String 授权码
        // member_id Integer 用户id
        // orderId Integer 订单id
        params.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        params.put("orderId", orderid);// 管理员设备id

//        HttpUtil.get(Constants.mainUrl + Constants.getOrderInfo, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
//                                  org.json.JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                if (statusCode == 200) {
//                    try {
//                        if (response.getInt("ret") == 0) {
//                            String json = response.getString("order");
//                            OrderModel model = gson.fromJson(json, OrderModel.class);
//                            if (model != null) {
//                                if (storeBean.getAdmin_agency().equals(model.getCommunity_id() + "")) {
//                                    createNotification(model);
//                                    if (mSavePreferencesData.getBooleanData("isSn")) {
//                                        mPrintManager.printLocaleJob(createPrintReceiptTask(model), null);
//                                    }
//                                }
//                            }
//                        } else {
//                            Toast.makeText(mContext.getApplicationContext(), response.getString("msg"), Toast.LENGTH_LONG).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(mContext.getApplicationContext(), "网络故障", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    /*
     *
     * 创建打印任务
     **/
    private PrintJobInfo createPrintReceiptTask(OrderModel odModel) {
        PrintJobInfo receiptTask = new PrintJobInfo();
        try {
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("   商品订单      \n", new CharacterParams(2, 2)));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  客户名称:" + odModel.getConsignee_name() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  联系电话:" + odModel.getConsignee_phone() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  地址:" + odModel.getConsignee_address() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  商品清单:" + "\n\n", new CharacterParams()));
            List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
            try {
                msgmodels = new ParseModel().getMsgmodel(odModel.getSpec());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < msgmodels.size(); i++) {
                receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  " + msgmodels.get(i).getCommodityName() + "("
                        + msgmodels.get(i).getSpecNames() + ")" + " x " + msgmodels.get(i).getGoodsCount() + "\n\n",
                        new CharacterParams()));
            }
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  订单单号:" + odModel.getOrder_id() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  创建时间:" + odModel.getCreate_date() + "\n\n", new CharacterParams()));

            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  支付时间:" + odModel.getPay_time() + "\n\n", new CharacterParams()));
            if (odModel.getPay_type() == 1) {
                receiptTask.addPrintItemJobTask(
                        new PrintItemJobInfo("         支付方式: 微信支付" + "\n\n", new CharacterParams()));
            } else {
                receiptTask.addPrintItemJobTask(
                        new PrintItemJobInfo("         支付方式: 支付宝支付" + "\n\n", new CharacterParams()));
            }
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  RMB:" + odModel.getTotal_price() + "元\n\n\n\n\n\n",
                    new CharacterParams(2, 2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptTask;
    }

    private void createNotification(OrderModel oModel) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.drawable.ic_launcher, "您有新的订单", System.currentTimeMillis());

        PendingIntent paddingIntent = null;
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("orderModel", oModel);
        intent.putExtras(mBundle);
        intent.setClass(mContext, MyOrderDetailsActivity.class);
        paddingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(mContext, mContext.getString(R.string.app_name),"收到" + oModel.getConsigneeName() + "的新订单", paddingIntent);
//        notification.sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.dingdong);

        Uri parse = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.dingdong);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("您有新的订单")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSound(parse)
                .setContentIntent(paddingIntent);
        Notification notification = builder.build();
        notificationManager.notify("Test", 123, notification);
    }

    private void createNotificationMessge(String id, String message_title) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

//        Notification notification = new Notification(R.drawable.ic_launcher, "您有新的消息", System.currentTimeMillis());

        PendingIntent paddingIntent = null;
        String jsonString = mSavePreferencesData.getStringData("json");
        StoreBean storeBean = JSON.parseObject(jsonString, StoreBean.class);
        String urlString = Constants.mainUrl + Constants.messageInfo + "agency_id=" + storeBean.getAdmin_agency()
                + "&auth_token=" + storeBean.getAuth_token() + "&message_id=" + id;
        Intent intent = new Intent(mContext, WebviewActivity.class);
        Agreement7 agreement7 = new Agreement7();
        agreement7.setLink_url(urlString);
        agreement7.setTitle("消息详情");
        intent.putExtra("agreement7", agreement7);
        paddingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

//        notification.defaults = Notification.DEFAULT_SOUND;
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(mContext, mContext.getString(R.string.app_name), message_title, paddingIntent);
//        notification.sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.dingdong);

        Uri parse = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.dingdong);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("您有新的订单")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSound(parse)
                .setContentIntent(paddingIntent);
        Notification notification = builder.build();
        notificationManager.notify("Test", 123, notification);
    }
}