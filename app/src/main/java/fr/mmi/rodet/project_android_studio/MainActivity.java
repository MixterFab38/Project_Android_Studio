package fr.mmi.rodet.project_android_studio;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Variables éléments graphique
    private Button button;
    private TextView blanc;

    //Variables état du flash et flash
    private boolean isFlashOn = false;
    private Camera camera;
    //A tester après (ci-dessous) :
    //private android.graphics.Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        button = (Button) findViewById(R.id.buttonFlash);
        blanc = (TextView) findViewById(R.id.blanc);

        //Vérif si smartphone possède un flash :
        Context context = this;
        PackageManager pm = context.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getApplicationContext(), "Ce téléphone ne possède pas de flash !", Toast.LENGTH_SHORT).show();
            return;
        }

        camera = Camera.open();
        final Camera.Parameters p = camera.getParameters();

        //clic sur le bouton
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isFlashOn) {
                    Camera.stopPreview();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    isFlashOn = false;
                    //changement de la couleur de la zone en gris - flash éteint
                    blanc.setBackgroundColor(Color.rgb(51,51,51));
                } else {
                    Camera.startPreview();
                    blanc.setBackgroundColor(Color.WHITE);
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    isFlashOn = true;
                }

            }
        });

    }
}
