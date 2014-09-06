package com.panoramic.pano_0;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;


public class Saisie extends Activity implements View.OnClickListener, View.OnTouchListener {


    ImageButton button_up;
    ImageButton button_down;
    ImageButton button_left;
    ImageButton button_right;
    TextView text;
    PilotageMoteurs moteurs;

    public static final String DEVICE_ADDRESS = "20:13:11:14:04:23";
    private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();

    private LedManager ledManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie);
        button_up = (ImageButton) findViewById(R.id.button_up);
        button_down = (ImageButton) findViewById(R.id.button_down);
        button_left = (ImageButton) findViewById(R.id.button_left);
        button_right = (ImageButton) findViewById(R.id.button_right);
        text = (TextView) findViewById(R.id.textView);

        button_left.setOnTouchListener(this);
        button_up.setOnClickListener(this);
        button_down.setOnClickListener(this);

        button_right.setOnClickListener(this);

        ledManager = new LedManager(this.getApplicationContext());
        moteurs = new PilotageMoteurs(this.getApplicationContext());
        // moteurs.PanOn(PilotageMoteurs.Pan.Droite);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.saisie, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // in order to receive broadcasted intents we need to register our receiver

        registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));

        // this is how you tell Amarino to connect to a specific BT device from within your own code
        Amarino.connect(this, DEVICE_ADDRESS);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // if you connect in onStart() you must not forget to disconnect when your app is closed
        Amarino.disconnect(this, DEVICE_ADDRESS);

        // do never forget to unregister a registered receiver
        unregisterReceiver(arduinoReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.button_down:
                text.setText("down");
                break;
            case R.id.button_up:
                text.setText(("up"));
                ledManager.blink_red_and_green();
                break;
            case R.id.button_left:
                text.setText("left");
                ledManager.switch_off();
                break;
            case R.id.button_right:
                text.setText(("right"));
                ledManager.quickly_blink_red();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.button_left:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    moteurs.PanOn(PilotageMoteurs.Pan.Gauche);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    moteurs.PanOff();
                }
                break;
        }
        return false;
    }


}
