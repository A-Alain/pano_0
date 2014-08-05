package com.panoramic.pano_0;

import android.content.Context;

import at.abraxas.amarino.Amarino;

/**
 * Created by tanguy on 05/08/14.
 */
public class LedManager {

    private Context context;

    public LedManager(Context context){
        this.context = context;
    }

    public void blink_red_and_green(){
        sendData('A','V');
    }

    public void switch_off(){
        sendData('E','\0');
    }

    public void quickly_blink_red(){
        sendData('A','R');
    }

    public void slowly_blink_red(){

    }


    private void sendData(char flag, char message){
        Amarino.sendDataToArduino(context,Saisie.DEVICE_ADDRESS,flag,message);
    }

    private void sendData(char flag, String message){
        Amarino.sendDataToArduino(context,Saisie.DEVICE_ADDRESS,flag,message);
    }
}
