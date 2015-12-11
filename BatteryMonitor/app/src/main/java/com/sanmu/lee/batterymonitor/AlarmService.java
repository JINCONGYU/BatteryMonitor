package com.sanmu.lee.batterymonitor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zhangfx on 2015/12/10.
 */
public class AlarmService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LLLAlarm","AlarmService-OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("LLLAlarm","AlarmService-OnStartCommand");

        this.registerBatteryReceiver(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void registerBatteryReceiver(Context context) {
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        IntentFilter battertIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(batteryReceiver, battertIntentFilter);
    }
}
