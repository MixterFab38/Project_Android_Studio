package fr.mmi.rodet.project_android_studio;



import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    TextView textLIGHT_available, textLIGHT_reading;
    private Button button;
    private TextView blanc;
    private boolean isFlashOn = false;
    private Camera camera;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        button = (Button) findViewById(R.id.flash);
        blanc = (TextView) findViewById(R.id.barreEtat);

        Context context = this;
        PackageManager pm = context.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getApplicationContext(),"Votre téléphone ne possède pas de flash!", Toast.LENGTH_SHORT).show();
            return;
        }
        camera = Camera.open();
        final Camera.Parameters p = camera.getParameters();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (isFlashOn) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    isFlashOn = false;
                    //on change la couleur de la zone en gris signifiant que le flash est éteint
                    blanc.setBackgroundColor(Color.rgb(51, 51, 51));
                } else {
                    //on change la couleur de la zone en blanc signifiant que le flash est allumé
                    blanc.setBackgroundColor(Color.WHITE);
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    isFlashOn = true;
                }
            }
        });


        textLIGHT_available
                = (TextView)findViewById(R.id.LIGHT_available);
        textLIGHT_reading
                = (TextView)findViewById(R.id.LIGHT_reading);

        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(LightSensor != null){
            textLIGHT_available.setText("Sensor.TYPE_LIGHT Available");
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }else{
            textLIGHT_available.setText("Sensor.TYPE_LIGHT NOT Available");
        }
    }

    private final SensorEventListener LightSensorListener = new SensorEventListener()
    {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                textLIGHT_reading.setText("LIGHT: " + event.values[0]);
            }
        }

    };




}
