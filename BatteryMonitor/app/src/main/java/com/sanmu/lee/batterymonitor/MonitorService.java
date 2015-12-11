package com.sanmu.lee.batterymonitor;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangfx on 2015/12/3.
 */
public class MonitorService extends Service {

    private String SERVICE_STATE = "SERVICESTATE";
    private int ONGOING_NOTIFICATION_ID = 1000;
    private MyBinder myBinder = null;
    private PhoneState phoneState = null;
    private TelephonyManager telephonyManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(SERVICE_STATE, "OnCreate");

        this.startForward("Monitor", "Dial", R.mipmap.ic_launcher);

        Toast.makeText(this,"ServiceOnCreate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(SERVICE_STATE, "onStartCommand!");

        this.getPhoneState();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    //Binder
    class  MyBinder extends Binder {

        public void startMission(){
//            Log.d(SERVICE_STATE,"StartMission");
        }
    }

    //前台运行
    private void startForward(String title,String text,int smallIcon){
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(smallIcon);
        startForeground(ONGOING_NOTIFICATION_ID, notificationBuilder.build());
    }

    private void getPhoneState(){
        telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        phoneState = new PhoneState(this);
        telephonyManager.listen(phoneState, PhoneStateListener.LISTEN_CALL_STATE);
    }


}
