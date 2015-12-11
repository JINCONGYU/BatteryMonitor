package com.sanmu.lee.batterymonitor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by zhangfx on 2015/12/4.
 */
public class BatteryReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LLLAlarm","BatteryReceiver!");
        String batteryChangedAction = intent.getAction();

        DataBaseHandler dataBaseHandler = new DataBaseHandler(context);

        if (Intent.ACTION_BATTERY_CHANGED.equals(batteryChangedAction)){
            Log.d("LLLAlarm","IF");
            int voltage = intent.getIntExtra("voltage",0); //获得当前电压
            int current = intent.getIntExtra("level",0);//获得当前电量
            int total = intent.getIntExtra("scale",0);//获得总电量
            int percent = current*100/total;

            int currentId = this.getDateBaseCount(dataBaseHandler) + 1;
            String currentBattery = "Battery:" + String.valueOf(percent) + "%";
            String currentVoltage = "Vlotage:"+ String.valueOf(voltage / 1000.00) + "v";
            String currentDate = this.currentDate();
            this.saveData(dataBaseHandler, currentId, currentBattery, currentVoltage, currentDate);

            String display = currentBattery + "\n" + currentVoltage;

            Toast.makeText(context,display,Toast.LENGTH_LONG).show();

            context.unregisterReceiver(this);
        }
    }

    private void saveData(DataBaseHandler dataBaseHandler,int id,String battery,String voltage, String date){
            SQLiteDatabase database = dataBaseHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id",id);
            values.put("battery",battery);
            values.put("voltage", voltage);
            values.put("date", date);
            database.insert("BatteryInfo", null, values);

//            Log.d("DATABASE_TEST", "saveData");
            database.close();
        }

    private String currentDate(){
//        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat ("MMM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private int getDateBaseCount(DataBaseHandler dataBaseHandler){
        return dataBaseHandler.getCount();
    }

}


