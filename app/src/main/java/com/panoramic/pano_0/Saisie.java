package com.panoramic.pano_0;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;


public class Saisie extends Activity implements View.OnClickListener {


    Button button_up;
    Button button_down;
    Button button_left;
    Button button_right;
    TextView text;

    private static final String DEVICE_ADDRESS =  "20:13:11:14:04:23";
    private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie);
        button_up = (Button) findViewById(R.id.button_up);
        button_down = (Button) findViewById(R.id.button_down);
        button_left = (Button) findViewById(R.id.button_left);
        button_right = (Button) findViewById(R.id.button_right);
        text = (TextView) findViewById(R.id.textView);

       button_up.setOnClickListener(this);
       button_down.setOnClickListener(this);
        button_left.setOnClickListener(this);
        button_right.setOnClickListener(this);
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
        switch (id){
            case R.id.button_down : text.setText("down");
                break;
            case R.id.button_up : text.setText(("up"));
                Amarino.sendDataToArduino(this.getApplicationContext(),DEVICE_ADDRESS,'A','V');
                break;
            case R.id.button_left : text.setText("left");
                Amarino.sendDataToArduino(this.getApplicationContext(),DEVICE_ADDRESS,'E','\0');
                break;
            case R.id.button_right : text.setText(("right"));
                Amarino.sendDataToArduino(this.getApplicationContext(),DEVICE_ADDRESS,'A','R');
                break;
            default: break;
        }

    }



    /**
     * ArduinoReceiver is responsible for catching broadcasted Amarino
     * events.
     *
     * It extracts data from the intent and updates the graph accordingly.
     */
    public class ArduinoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = null;

            // the device address from which the data was sent, we don't need it here but to demonstrate how you retrieve it
            final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

            // the type of data which is added to the intent
            final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);

            // we only expect String data though, but it is better to check if really string was sent
            // later Amarino will support differnt data types, so far data comes always as string and
            // you have to parse the data to the type you have sent from Arduino, like it is shown below
            if (dataType == AmarinoIntent.STRING_EXTRA){
                data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);

                if (data != null){
                   // mValueTV.setText(data);
                    try {
                        // since we know that our string value is an int number we can parse it to an integer
                        final int sensorReading = Integer.parseInt(data);
                    //    mGraph.addDataPoint(sensorReading);
                    }
                    catch (NumberFormatException e) { /* oh data was not an integer */ }
                }
            }
        }
    }
}
