package fr.mmi.rodet.project_android_studio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity
{

    TextView tv1;
    TextView tv2;
    TextView Latitude;
    TextView Longitude;

    // Sensor light
    TextView textLIGHT_reading;

    // flash light
    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mTorchOnOffButton;
    private ImageButton mTorchOnOffButton2;
    private Float i;
    private Boolean isTorchOn;

    // Proximyty
    /*SensorManager mySensorManager;
    Sensor myProximitySensor;
    TextView ProximitySensor;
    TextView ProximityMax;
    TextView ProximityReading;*/







    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        Latitude = (TextView)findViewById(R.id.Latitude);
        Longitude = (TextView)findViewById(R.id.Longitude);

        Intent intent = getIntent();
        if (intent != null){
            String str = "";
            String str2 = "";
            String str3 = "";
            String str4 = "";
            if (intent.hasExtra("edittext")){ // vérifie qu'une valeur est associée à la clé “edittext”
                str = intent.getStringExtra("edittext"); // on récupère la valeur associée à la clé
            }

            if (intent.hasExtra("edittext2")){ // vérifie qu'une valeur est associée à la clé “edittext”
                str2 = intent.getStringExtra("edittext2"); // on récupère la valeur associée à la clé
            }

            if (intent.hasExtra("Latitude")){ // vérifie qu'une valeur est associée à la clé “edittext”
                str3 = intent.getStringExtra("Latitude"); // on récupère la valeur associée à la clé
            }

            if (intent.hasExtra("Longitude")){ // vérifie qu'une valeur est associée à la clé “edittext”
                str4 = intent.getStringExtra("Longitude"); // on récupère la valeur associée à la clé
            }

            TextView textView = (TextView) findViewById(R.id.tv1);
            TextView textView2 = (TextView) findViewById(R.id.tv2);
            TextView textView3 = (TextView) findViewById(R.id.Latitude);
            TextView textView4 = (TextView) findViewById(R.id.Longitude);
            textView.setText(str);
            textView2.setText(str2);
            textView3.setText(str3);
            textView4.setText(str4);
        }


        // Proximity
       /* ProximitySensor = (TextView)findViewById(R.id.proximitySensor);
        ProximityMax = (TextView)findViewById(R.id.proximityMax);
        ProximityReading = (TextView)findViewById(R.id.proximityReading);

        mySensorManager = (SensorManager)getSystemService(
                Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);

        if (myProximitySensor == null){
            ProximitySensor.setText("No Proximity Sensor!");
        }else{
            ProximitySensor.setText(myProximitySensor.getName());
            ProximityMax.setText("Maximum Range: "
                    + String.valueOf(myProximitySensor.getMaximumRange()));
            mySensorManager.registerListener(proximitySensorEventListener,
                    myProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }*/


        // flash light
        Log.d("FlashLightActivity", "onCreate()");
        mTorchOnOffButton = (ImageButton) findViewById(R.id.button_on_off);
        Boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Sensor light
        textLIGHT_reading = (TextView) findViewById(R.id.LIGHT_reading);
        SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mySensorManager.registerListener(LightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        isTorchOn = false;

        try {
            mCameraId = mCameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels) {
            mTorchOnOffButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    try
                    {

                        if (isTorchOn && Latitude.getText() != tv1.getText() && Longitude.getText() != tv2.getText())
                        {
                            mTorchOnOffButton.setImageResource(R.drawable.ic_launcher_background);
                            Update();
                            isTorchOn = false;
                        } else
                            {
                            mTorchOnOffButton.setImageResource(R.drawable.ic_launcher_foreground);
                            isTorchOn = true;
                        }

                    }
                    catch (Exception e)

                    {
                        e .printStackTrace();
                    }
                }


            });
        }
        else
        {
            textLIGHT_reading.setText("255");
        }
    }



    // Sensor light
    private final SensorEventListener LightSensorListener = new SensorEventListener()
    {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {


        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                textLIGHT_reading.setText("LIGHT: " + event.values[0]);
                i = event.values[0];
            }
        }
    };

    public void Update()
    {
        Thread t = new Thread()
        {

            @Override
            public void run()
            {
                try
                {
                    while (!isInterrupted())
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run()
                            {
                                try
                                {
                                    if(isTorchOn == true)
                                    {
                                        if(i<= 160)
                                        {
                                            mCameraManager.setTorchMode(mCameraId, true);
                                        }
                                        else
                                        {
                                            mCameraManager.setTorchMode(mCameraId, false);
                                        }
                                    }
                                    else
                                    {
                                        mCameraManager.setTorchMode(mCameraId, false);
                                    }

                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }

    /*SensorEventListener proximitySensorEventListener
            = new SensorEventListener()
    {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            // TODO Auto-generated method stub

            if(event.sensor.getType()==Sensor.TYPE_PROXIMITY)
            {
                ProximityReading.setText("Proximity Sensor Reading:"
                        + String.valueOf(event.values[0]));
            }
        }
     };*/

    public void Param (View view)
    {
        String str = tv1.getText().toString();
        String str2 = tv2.getText().toString();
        Intent intent = new Intent(this, Page_Parametre.class);
        intent.putExtra("edittext3", str);
        intent.putExtra("edittext4", str2);
        startActivity(intent);
    }

    public boolean CheckLatitude()
    {
        if(Latitude.getText() == tv1.getText())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean CheckLongitude()
    {
        if(Longitude.getText() == tv1.getText())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}