package com.beta.cls.angelcar.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * สำหรับเช็คว่า Token หมดอายุหรือยัง เพื่อที่จะได้ขอ Token จาก GCM ใหม่
 */
public class GcmTokenRefreshService extends InstanceIDListenerService{
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this,GcmRegisterService.class);
        startService(intent);
    }
}
