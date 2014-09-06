package com.panoramic.pano_0;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import at.abraxas.amarino.Amarino;

/**
 * Created by Alain on 06/09/2014.
 */
public class PilotageMoteurs {

    private Context context;
    public enum Pan {Droite, Gauche}


    public PilotageMoteurs(Context context){
        this.context = context;
    }

    public void PanOn(Pan direction){
        switch (direction){
            case Droite: sendData('P', "D");
                break;
            case Gauche: sendData('P',"G");
                break;
        }
    }

    public void PanOff(){
        sendData('P',"O");
    }


    private void sendData(char flag, char message){
        Amarino.sendDataToArduino(context, Saisie.DEVICE_ADDRESS, flag, message);
    }

    private void sendData(char flag, String message){
        Log.d("PANO", message);
        Amarino.sendDataToArduino(context,Saisie.DEVICE_ADDRESS,flag,message);
    }
}
