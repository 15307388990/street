package com.juxun.business.street.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.juxun.business.street.UILApplication;
import com.juxun.business.street.activity.NotificationUpdateActivity;
import com.juxun.business.street.activity.NotificationUpdateActivity.ICallbackResult;
import com.yl.ming.efengshe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {
    private static final int NOTIFY_ID = 0;
    private int progress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    // 返回的安装包url
    public static String apkUrl;
    // private String apkUrl = MyApp.downloadApkUrl;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/jiebian/";

    private static String saveFileName = savePath + "StreetApp.apk";
    private ICallbackResult callback;
    private DownloadBinder binder;
    private UILApplication app;
    private boolean serviceIsDestroy = false;
    private Context mContext = this;
    private Notification mNotification;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    app.setDownload(false);
                    // 下载完毕、取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    installApk();
                    break;
                case 2:
                    app.setDownload(false);
                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case 1:
                    int rate = msg.arg1;
                    app.setDownload(true);
                    if (rate < 100) {   //进度的更新?
                        RemoteViews contentview = mNotification.contentView;
                        contentview.setTextViewText(R.id.tv_progress, rate + "%");
                        contentview.setProgressBar(R.id.progressbar, 100, rate, false);
                    } else {
                        System.out.println("下载完毕!!!!!!!!!!!");

                        // 下载完毕后变换通知形式
                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotification.contentView = null;

                        Intent intent = new Intent(mContext, NotificationUpdateActivity.class);
                        // 告知已完成
                        intent.putExtra("completed", "yes");
                        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

//                        mNotification.setLatestEventInfo(mContext, "下载完成", "文件已下载完毕", contentIntent);

                        builder.setContentIntent(contentIntent)
                                .setContent(null);
                        mNotification = builder.build();

                        serviceIsDestroy = true;
                        stopSelf();// 停掉自身服务
                    }
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
            }
        }
    };
    private NotificationCompat.Builder builder;

    //
    // @Override
    // public int onStartCommand(Intent intent, int flags, int startId) {
    // return START_STICKY;
    // }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("url", "onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("downloadservice onUnbind");
        return super.onUnbind(intent);
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        System.out.println("downloadservice onRebind");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
//        startForeground(true);// 这个不确定是否有作用、设置为前台服
        app = (UILApplication) getApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("downloadservice ondestroy");
        // 假如被销毁了，无论如何都默认取消了。
        app.setDownload(false);
    }

    public class DownloadBinder extends Binder {

        public void start() {
            if (downLoadThread == null || !downLoadThread.isAlive()) {
                progress = 0;
                setUpNotification();
                new Thread() {
                    public void run() {
                        startDownload();
                    }
                }.start();
            }
        }

        public void cancel() {
            canceled = true;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void cancelNotification() {
            mHandler.sendEmptyMessage(2);
        }

        public void addCallback(ICallbackResult callback) {
            DownloadService.this.callback = callback;
        }
    }

    //下载
    private void startDownload() {
        canceled = false;
        downloadApk();
    }

    /**
     * 创建通知
     */
    private void setUpNotification() {
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();

        // 指定个性化视图
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
        contentView.setTextViewText(R.id.name, "e蜂社商家版.apk 正在下载...");
        //指定pendingIntent
        Intent intent = new Intent(this, NotificationUpdateActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(tickerText)
                .setAutoCancel(true)
                .setWhen(when)
                .setContent(contentView)
                .setContentIntent(contentIntent);
        mNotification = builder.build();
        mNotificationManager.notify(NOTIFY_ID, mNotification);

//        mNotification = new Notification(icon, tickerText, when);
//         放置在"正在运行"栏目中
//        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
//         指定内容意图
//        mNotification.contentIntent = contentIntent;
//        mNotification.contentView = contentView;
//        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    //
    /**
     * 下载apk
     *
     * @param url
     */
    private Thread downLoadThread;

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        callback.OnBackResult("finish");

    }

    private int lastRate = 0;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = progress;
                    if (progress >= lastRate + 1) {
                        mHandler.sendMessage(msg);
                        lastRate = progress;
                        if (callback != null)
                            callback.OnBackResult(progress);
                    }
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(0);
                        // 下载完了，cancelled也要设置
                        canceled = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!canceled);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
