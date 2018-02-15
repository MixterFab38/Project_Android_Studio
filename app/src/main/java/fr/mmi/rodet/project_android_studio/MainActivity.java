package fr.mmi.rodet.project_android_studio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity
{
    // Sensor light
    TextView textLIGHT_reading;

    // flash light
    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mTorchOnOffButton;
    private ImageButton mTorchOnOffButton2;
    private Float i;
    private Boolean isTorchOn;



    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // flash light
        Log.d("FlashLightActivity", "onCreate()");
        mTorchOnOffButton = (ImageButton) findViewById(R.id.button_on_off);
        mTorchOnOffButton2 = (ImageButton) findViewById(R.id.imageButton);
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



        mTorchOnOffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    if (isTorchOn)
                    {
                        mTorchOnOffButton.setImageResource(R.drawable.ic_launcher_background);
                        Update();
                        isTorchOn = false;
                    }
                    else
                    {
                        mTorchOnOffButton.setImageResource(R.drawable.ic_launcher_foreground);
                        isTorchOn = true;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }



    // Sensor light
    private final SensorEventListener LightSensorListener = new SensorEventListener()
    {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {


        }

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

}




