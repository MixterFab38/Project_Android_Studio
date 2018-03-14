package fr.mmi.rodet.project_android_studio;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Kevin on 14/03/2018.
 */

public class Page_Parametre extends MapActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void Page21 (View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }


    protected boolean isRouteDisplayed() {
        return false;
    }

}
