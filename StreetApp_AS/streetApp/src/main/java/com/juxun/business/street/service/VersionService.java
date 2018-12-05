package com.juxun.business.street.service;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allenliu.versionchecklib.core.AVersionService;

public class VersionService extends AVersionService {

    private int localVersion;

    @Override
    public void onResponses(AVersionService aVersionService, String s) {
        loadVersion();
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject rejson = jsonObject.getJSONObject("result");
        int serviceVersion = rejson.getInteger("mall_version");
        String downloadAddress = rejson.getString("mall_url");
        String mall_name = rejson.getString("mall_name");
        // 标志是否强制更新，0为不强制更新，1为强制更新
        final int mall_tag = rejson.getInteger("mall_tag");
        if (localVersion < serviceVersion) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isForceUpdate", mall_tag == 1 ? true : false);
            showVersionDialog(downloadAddress, "版本更新", "检查到新版本: " + mall_name, bundle);

        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void loadVersion() {
        PackageManager pm = this.getPackageManager();// context为当前Activity上下
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        localVersion = pi.versionCode;
    }
}
