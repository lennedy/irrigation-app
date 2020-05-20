package com.example.irrigation2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;

import org.json.JSONException;
import org.json.JSONObject;


public class ZoneGui extends AppCompatActivity {
    private TextView zoneName;
    private ImageView activeImage;
    private TextView textBefore;
    private TextView textAfter;
    private AnyChartView moistureSensors;
    private TextView activeTimeText;

    public ZoneGui(TextView zoneName, ImageView activeImage, TextView textBerore, TextView textAfter, TextView activeTimeText, AnyChartView chart){
        this.zoneName = zoneName;
        this.activeImage = activeImage;
        this.textBefore = textBerore;
        this.textAfter = textAfter;
        this.activeTimeText = activeTimeText;
        this.moistureSensors = chart;
    }

    public ZoneGui(View zoneName, View activeImage, View textBerore, View textAfter, View activeTimeText, View chart) {
        this.zoneName = (TextView) zoneName;
        this.activeImage = (ImageView) activeImage;
        this.textBefore = (TextView) textBerore;
        this.textAfter = (TextView) textAfter;
        this.activeTimeText = (TextView) activeTimeText;
        this.moistureSensors = (AnyChartView) chart;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateActive(boolean active){
        if(active) {
            activeImage.setImageResource(R.drawable.circverde);
        }
        else{
            activeImage.setImageResource(R.drawable.circvermelho);
        }
    }

    public void updateZone(String jsonText){


/*
        if(zoneActive) {
            activeImage.setImageResource(R.drawable.circverde);
        }
        else{
            activeImage.setImageResource(R.drawable.circvermelho);
        }
*/

    }

}
