package com.sanmu.lee.batterymonitor;

/**
 * Created by zhangfx on 2015/12/8.
 */
public class ItemInfo {
    private int id ;
    private String date;
    private  String voltage;
    private  String battery;

    public void setId(int id){
        this.id = id;
    }
    public int getId()
    {
        return this.id;
    }


    public void setDate(String date){
        this.date = date;
    }
    public String getDate()
    {
        return this.date;
    }


    public void setVoltage(String voltage){
        this.voltage = voltage;
    }
    public String getVoltage()
    {
        return this.voltage;
    }


    public void setBattery(String battery){
        this.battery = battery;
    }
    public String getBattery()
    {
        return this.battery;
    }

}
