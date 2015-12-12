package com.sanmu.lee.batterymonitor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

/**
 * Created by zhangfx on 2015/12/9.
 */
public class PowerOffReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
        SQLiteDatabase database = dataBaseHandler.getWritableDatabase();

        int currentId = dataBaseHandler.getCount()+1;
        String currentDate = dataBaseHandler.currentDate();

        dataBaseHandler.saveData(database,currentId,"Shut","Down",currentDate);
        Toast.makeText(context,"ShutDown",Toast.LENGTH_SHORT).show();
    }

    private void saveData(SQLiteDatabase database,int id,String currentDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("date",currentDate);
        database.insert("BatteryInfo",null,contentValues);
        database.close();
    }
}
