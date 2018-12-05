package com.juxun.business.street.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iboxpay.print.PrintManager;
import com.iboxpay.print.model.CharacterParams;
import com.iboxpay.print.model.PrintItemJobInfo;
import com.iboxpay.print.model.PrintJobInfo;
import com.iflytek.cloud.SpeechSynthesizer;
import com.juxun.business.street.activity.MyOrderDetailsActivity;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.yl.ming.efengshe.R;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.yl.ming.efengshe.R.id.contentView;

public class MyMQTTService extends Service implements MqttCallback {
    public static boolean isStart = false;
    private static final String MQTT_BROKER = "139.129.194.191"; // Broker URL
    // or
    // IP
    // Address
    private static final int MQTT_PORT = 1883; // Broker Port

    public static final int MQTT_QOS_0 = 0; // QOS Level 0 ( Delivery Once no
    // confirmation )
    public static final int MQTT_QOS_1 = 1; // QOS Level 1 ( Delevery at least
    // Once with confirmation )
    public static final int MQTT_QOS_2 = 2; // QOS Level 2 ( Delivery only once
    // with confirmation with handshake
    // )

    private static final int MQTT_KEEP_ALIVE = 1; // KeepAlive Interval in
    // MS

    private static final String MQTT_URL_FORMAT = "tcp://%s:%d"; // URL Format
    // normally
    // don't
    // change

    private static final boolean MQTT_CLEAN_SESSION = false; // Start a clean
    // session?
    private static final String ACTION = "KEEP_CONNECT";
    private AlarmManager mAlarmManager; // Alarm manager to perform repeating

    private static final String DEVICE_ID_FORMAT = "andr_%s";
    private MqttClient mClient; // Mqtt Client
    private MqttConnectOptions mOpts; // Connection Options
    private String mDeviceId;
    private MqttDefaultFilePersistence mDataStore; // Defaults to FileStore
    private PrintManager mPrintManager;
    private Handler mConnHandler; // Seperate Handler thread for networking
    private MemoryPersistence mMemStore; // On Fail reverts to MemoryStore
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    @Override
    public void onCreate() {
        isStart = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(mConnectivityReceiver, filter);
        // mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0); // 开始时间
        // mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
        // System.currentTimeMillis(), MQTT_KEEP_ALIVE, pi);
        mDeviceId = String.format(DEVICE_ID_FORMAT, Secure.getString(getContentResolver(), Secure.ANDROID_ID));
        mOpts = new MqttConnectOptions();
        mOpts.setCleanSession(MQTT_CLEAN_SESSION);
        mOpts.setUserName("eeline");
        mOpts.setPassword("eeline@)!$".toCharArray());
        // 设置超时时间
        // mOpts.setConnectionTimeout(10000);

        // 设置会话心跳时间
        // mOpts.setKeepAliveInterval(20000);
        HandlerThread thread = new HandlerThread("HANDLER");
        thread.start();
        mConnHandler = new Handler(thread.getLooper());
        try {
            mDataStore = new MqttDefaultFilePersistence(getCacheDir().getAbsolutePath());
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
            mDataStore = null;
            mMemStore = new MemoryPersistence();
        }
        super.onCreate();

    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        connect();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Connects to the broker with the appropriate datastore
     */
    private synchronized void connect() {
        String url = String.format(Locale.US, MQTT_URL_FORMAT, MQTT_BROKER, MQTT_PORT);
        try {
            mClient = new MqttClient(url, mDeviceId, mDataStore);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mClient = new MqttClient(url, mDeviceId, mMemStore);
            } catch (MqttException e1) {
                e1.printStackTrace();
            }

        }
        mConnHandler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    // Thread.sleep(2000);
                    mClient.connect(mOpts);
                    mClient.subscribe(mDeviceId, MQTT_QOS_2);
                    mClient.setCallback(MyMQTTService.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /***
     *
     * mqttcallback
     **/
    @Override
    public void connectionLost(Throwable arg0) {
        mClient = null;
        sendBroadcast(new Intent(ACTION));
    }

    @Override
    public void deliveryComplete(MqttDeliveryToken arg0) {

    }

    @Override
    public void messageArrived(MqttTopic arg0, MqttMessage message) {
        Gson gson = new Gson();
        OrderModel model;
        try {
            model = gson.fromJson(new String(message.getPayload()), OrderModel.class);
            if (model != null) {
                // mPrintManager.printLocaleJob(createPrintReceiptTask(model),
                // null);
                createNotification(message, model);
            } else {
                Log.i("aa", "数据不对");
            }
        } catch (JsonSyntaxException e) {
            Log.i("aa", "数据不对");
            e.printStackTrace();
        } catch (MqttException e) {
            Log.i("aa", "数据不对");
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createNotification(MqttMessage message, OrderModel oModel) {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

//        Notification notification = new Notification(R.drawable.ic_launcher, "您有新的订单", System.currentTimeMillis());

        PendingIntent paddingIntent = null;
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("orderModel", oModel);
        intent.putExtras(mBundle);
        intent.setClass(this, MyOrderDetailsActivity.class);
        paddingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//		notification.setLatestEventInfo(this, getString(R.string.app_name), oModel.getCommunity_name(), paddingIntent);
//        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dingdong);

        Uri parse = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dingdong);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("您有新的订单")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSound(parse)
                .setContentIntent(paddingIntent);
        Notification notification = builder.build();

        notificationManager.notify("Test", 123, notification);
    }

    /**
     * Start MQTT Client
     *
     * @return void
     */
    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, MyMQTTService.class);
        i.setAction("START");
        ctx.startService(i);
    }

    /**
     * Receiver that listens for connectivity chanes via ConnectivityManager
     */
    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mClient == null || !mClient.isConnected()) {
                // mPrintManager.printLocaleJob(create("已经断开连接 正在尝试重新连接"),
                // null);
                mClient = null;
                connect();

            }
        }
    };

    public void onDestroy() {
        Log.i("aa", "测试");
        super.onDestroy();
    }

    private PrintJobInfo create(String msg) {
        PrintJobInfo receiptTask = new PrintJobInfo();
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(msg + "  \n", new CharacterParams(2, 2)));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo(dateFormat.format(date) + "\n", new CharacterParams(2, 2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptTask;
    }
}
