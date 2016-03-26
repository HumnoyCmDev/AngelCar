package com.beta.cls.angelcar.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * เอาไว้รับข้อมูลจาก Server ที่ส่งเข้ามาผ่าน GCM
 */
public class GcmDownstreamService extends GcmListenerService{
    private static final String TAG = "DcmDownstreamService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.e(TAG, "Message Incoming");

    }
}
