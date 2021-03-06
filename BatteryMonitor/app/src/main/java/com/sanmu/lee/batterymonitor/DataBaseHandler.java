package com.sanmu.lee.batterymonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfx on 2015/12/8.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "dataBase";
    private static  final  String TABLE_NAME = "BatteryInfo";
    private static final int DATABASE_VERSION = 1;

    private static final  String KEY_ID = "id";
    private static final  String KEY_DATE = "date";
    private static final  String KEY_VOLTAGE = "voltage";
    private static final  String KEY_BATTERY = "battery";

    public DataBaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE_TEST", "OnCReat");
        db.execSQL(this.creatTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private String creatTable(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("+ KEY_ID + " INTEGER PRIMARY KEY,"+
                KEY_BATTERY +" TEXT," +
                KEY_VOLTAGE + " TEXT," +
                KEY_DATE + " TEXT" +")";
        return CREATE_TABLE;
    }


    public int getCount(){
        SQLiteDatabase database = this.getReadableDatabase();
        String count = "SELECT * FROM " + "BatteryInfo";
        Cursor cursor = database.rawQuery(count,null);
//        cursor.close();   //同一线程城内不能close两次dataBase
        return cursor.getCount();
    }


    public List<ItemInfo> getAllItems(){
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        String selectQuery = "SELECT * FROM " + this.TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do{
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setId(Integer.parseInt(cursor.getString(0)));
                itemInfo.setBattery(cursor.getString(1));
                itemInfo.setVoltage(cursor.getString(2));
                itemInfo.setDate(cursor.getString(3));

                itemInfoList.add(itemInfo);
            }while (cursor.moveToNext());
        }
        return itemInfoList;
    }

    public void saveData(SQLiteDatabase database,int id,String battery,String voltage,String currentDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("battery", battery);
        contentValues.put("voltage", voltage);
        contentValues.put("date", currentDate);
        database.insert("BatteryInfo", null, contentValues);
        database.close();
    }

    public void deleteTable(SQLiteDatabase db,String tableName){
        db.execSQL("DELETE FROM " + tableName);
        Log.d("DATABASE_TEST", "DElete");
        db.close();
    }

    public String currentDate(){
//        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat ("MMM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
