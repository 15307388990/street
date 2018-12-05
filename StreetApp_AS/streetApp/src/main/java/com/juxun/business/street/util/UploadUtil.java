package com.juxun.business.street.util;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.Zone;

public class UploadUtil {
    static Configuration config = new Configuration.Builder().chunkSize(256 * 1024) // 分片上传时，每片的大小。
            // 默认256K
            .putThreshhold(512 * 1024) // 启用分片上传阀值。默认512K
            .connectTimeout(10) // 链接超时。默认10秒
            .responseTimeout(60) // 服务器响应超时。默认60秒
            .recorder(null) // recorder分片上传时，已上传片记录器。默认null
            .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
            .build();
    private static UploadManager uploadManager = new UploadManager(config);

    public static UploadManager getUploadManager() {
        return uploadManager;
    }

}
