package com.sanmu.lee.batterymonitor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by zhangfx on 2015/12/3.
 */
public class PhoneState extends PhoneStateListener {
    private String PHONE_STATE = "PHONESTATE";
    private static final int FIVE_MINUTES = 5 * 60 * 1000;
//    private Timer timer = null;
//    private TimerTask task = null;
//    private  BatteryReceiver batteryReceiver = null;
    private Context context = null;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public PhoneState(Context context){
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state){
            case TelephonyManager.CALL_STATE_IDLE:

                this.idleMission();
                break;
            case TelephonyManager.CALL_STATE_RINGING:

                this.ringingMission();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                this.offhookMission();
                break;

            default:
                break;
        }
    }

    public void idleMission(){
//        this.cancelTimer();
        this.idleRemark(this.context);

        this.stopAlarmMission();

        Log.d(PHONE_STATE, "IdleState!");
    }

    public void ringingMission(){
        Log.d(PHONE_STATE, "RingingState!");
    }

    public void offhookMission(){
//        this.startTask();

        this.startAlarmMission(this.context);
//        this.registerBatteryReceiver();

        Log.d(PHONE_STATE, "offhookState!");
    }

//    private void initialWithTask(){
//        task = new TimerTask() {
//
//            Handler handler = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    if(msg.what ==1) {
//                        registerBatteryReceiver();
//                    }
//                }
//            };
//
//            @Override
//            public void run() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Log.d(PHONE_STATE, "GO!,GO!,GO");
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);
//                    }
//                }).start();
//            }
//        };
//    }

//    public void setTimer(){
//        this.timer = new Timer();
//    }

//    public void startTask(){
//        setTimer();
//        this.initialWithTask();
//        timer.schedule(task,1000,FIVE_MINUTES);
//    }

//    private void cancelTimer(){
//        if (timer!= null){
//            timer.cancel();
//        }
//    }

//    private void registerBatteryReceiver(){
//        batteryReceiver = new BatteryReceiver();
//        IntentFilter battertIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        context.registerReceiver(batteryReceiver,battertIntentFilter);
//    }

//    private void unregisterBatteryReceiver() {
//        if (batteryReceiver != null) {
//            context.unregisterReceiver(batteryReceiver);
//        }
//    }

    private void startAlarmMission(Context cotext){
        alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmService.class);
//        intent.setAction(Intent.ACTION_BATTERY_CHANGED);
        pendingIntent = PendingIntent.getService(context,0,intent,0);
        long triggerAtMillis = SystemClock.elapsedRealtime();
        triggerAtMillis += 60*1000;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis,FIVE_MINUTES,pendingIntent);

        Log.d("LLLAlarm","StartAlarmMission");
    }

    private void stopAlarmMission(){
        if (alarmManager!=null){
            alarmManager.cancel(pendingIntent);
            alarmManager = null;
            autoDial(this.context);
        }
    }

    private void autoDial(Context context) {
        Uri uri = Uri.parse("tel:" + 668184);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
            Log.d(PHONE_STATE, "Auto-Call");
        } catch (ActivityNotFoundException e) {
            Log.d("ActivityException",e.getLocalizedMessage());
        }
    }


    private void idleRemark(Context context){
        DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
        SQLiteDatabase database = dataBaseHandler.getWritableDatabase();

        int id = dataBaseHandler.getCount() + 1;
        String date = dataBaseHandler.currentDate();

        dataBaseHandler.saveData(database,id,"Idle","State",date);
    }



}

