package com.iotclientbeta;

/**
 * Created by Administrator on 2016/12/6 0006.
 */

public class ViewData {
    String arduinoNum="null";
    String sensorNum="null";
    String temperature="null";
    String humidity="null";
    public ViewData(String arduinoNum, String sensorNum, String temperature, String humidity){
        this.arduinoNum=arduinoNum;
        this.sensorNum=sensorNum;
        this.temperature=temperature;
        this.humidity=humidity;
    }
    public String ToString(){
        return "arduinoNUM:"+arduinoNum+" sensorNum:"+sensorNum+" temperature:"+temperature+" humidity:"+humidity;
    }
}
