package fr.mmi.rodet.project_android_studio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity
{
    // Sensor light
    TextView textLIGHT_available, textLIGHT_reading;

    // flash light
    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mTorchOnOffButton;
    private Boolean isTorchOn;
    private MediaPlayer mp;
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
        isTorchOn = false;
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable)
        {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // closing the application
                            finish();
                            System.exit(0);
                        }
                    });
            alert.show();
            return;
        }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

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
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();








        // Sensor light
        textLIGHT_available
                = (TextView) findViewById(R.id.LIGHT_available);
        textLIGHT_reading
                = (TextView) findViewById(R.id.LIGHT_reading);
        SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);




        if (LightSensor != null)
        {

            textLIGHT_available.setText("Sensor.TYPE_LIGHT NOT Available");
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            textLIGHT_available.setText("Sensor.TYPE_LIGHT NOT Available");
        }


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

        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {

                mTorchOnOffButton.setImageResource(R.drawable.ic_launcher_background);








            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // flash light
    public void turnOffFlashLight()
    {

        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {

                mTorchOnOffButton.setImageResource(R.mipmap.ic_launcher);
                mCameraManager.setTorchMode(mCameraId, false);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}




