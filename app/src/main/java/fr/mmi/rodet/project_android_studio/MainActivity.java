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
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
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
    private Float i;



    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        try
        {
            mCameraId = mCameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }


        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {
                                    if(i<= 160)
                                    {
                                        mCameraManager.setTorchMode(mCameraId, true);
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
                }
            }
        };

        t.start();
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

    // flash light
    public void turnOnFlashLight()
    {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                mTorchOnOffButton.setImageResource(R.drawable.ic_launcher_background);
            }
    }

    // flash light
    public void turnOffFlashLight()
    {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                mTorchOnOffButton.setImageResource(R.mipmap.ic_launcher);
            }
    }


}




