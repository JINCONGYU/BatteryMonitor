package com.sanmu.lee.batterymonitor;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    Button startButton;
    Button deleteButton;
    ListView listView;
    private static  final  String TABLE_NAME = "BatteryInfo";
    private MonitorService.MyBinder myBinder = null;
    private MonitorService monitorService = null;
    DataBaseHandler dataBaseHandler;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MonitorService.MyBinder)service;
//            myBinder.startMission();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        startButton = (Button)findViewById(R.id.button);
        deleteButton = (Button)findViewById(R.id.button2);
        listView = (ListView)findViewById(R.id.listView);
        startButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        monitorService = new MonitorService();

        dataBaseHandler = new DataBaseHandler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        String[] array = this.getDataBase();
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.button):
                startService();
//                this.binderService();
                break;
            case(R.id.button2):
                this.deleteTable();
                String[] array = this.getDataBase();
                listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array));
                break;
        }
    }

    //启动服务
    private void startService(){
        if (monitorService != null){
            this.startService(new Intent(this,monitorService.getClass()));
        }
    }
    //停止服务
    private void stopService(){
        if (monitorService != null){
            this.stopService(new Intent(this, monitorService.getClass()));
        }
    }

//    private void binderService() {
//        if (monitorService != null) {
//            Intent intent = new Intent(this,monitorService.getClass());
//            bindService(intent,serviceConnection,BIND_AUTO_CREATE);
//        }
//    }

    private String[]  getDataBase(){
        List itemInfoList = dataBaseHandler.getAllItems();
        String[] listArray = new String[itemInfoList.size()];
        for (int i = 0,len = itemInfoList.size();i< len;i++ ){
            ItemInfo item = (ItemInfo)itemInfoList.get(i);
            String string = item.getId() + ". " + item.getBattery() + " " +item.getVoltage() + " " + item.getDate();
//            Log.d("GETDATA",string);
            listArray[i]=string;
        }
        return listArray;
    }

    private void deleteTable(){
        SQLiteDatabase database = dataBaseHandler.getWritableDatabase();
        dataBaseHandler.deleteTable(database,TABLE_NAME);
    }

}
