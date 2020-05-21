package com.example.irrigation2;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;


public class ZoneGui extends AppCompatActivity {
    private boolean active=false;
    private TextView zoneName;
    private ImageView activeImage;
    private TextView textBefore;
    private TextView textAfter;
    private AnyChartView moistureSensors;
    private TextView activeTimeText;
    private LinearLayout layoutA;
    private LinearLayout layoutB;

    public ZoneGui(TextView zoneName, ImageView activeImage, TextView textBerore, TextView textAfter, TextView activeTimeText, AnyChartView chart, LinearLayout layoutA, LinearLayout layoutB){
        this.zoneName = zoneName;
        this.activeImage = activeImage;
        this.textBefore = textBerore;
        this.textAfter = textAfter;
        this.activeTimeText = activeTimeText;
        this.moistureSensors = chart;
        this.layoutA = layoutA;
        this.layoutB = layoutB;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateActive(boolean active){
        this.active = active;
        if(active) {
            activeImage.setImageResource(R.drawable.circverde);
        }
        else{
            activeImage.setImageResource(R.drawable.circvermelho);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateZone(JSONObject jsonText) throws JSONException {
        DateFormat hour     = new SimpleDateFormat("hh");
        DateFormat minute   = new SimpleDateFormat("mm");

        if(!this.active) {
            layoutA.setVisibility(View.VISIBLE);
            layoutB.setVisibility(View.VISIBLE);

            Date currentTime = Calendar.getInstance().getTime();
            Date nextTime = Calendar.getInstance().getTime();
            ;
            try {
                nextTime = hour.parse(jsonText.getString("nextTime"));
            } catch (ParseException e) {

            }

            int difference = Integer.parseInt(hour.format(nextTime)) - Integer.parseInt(hour.format(currentTime));
            if (difference == 0) {
                difference = Integer.parseInt(minute.format(nextTime)) - Integer.parseInt(minute.format(currentTime));
            } else if (difference < 0) {
                difference += 12;
            }

            activeTimeText.setText("" + difference);
        }else{
            layoutA.setVisibility(View.INVISIBLE);
            layoutB.setVisibility(View.INVISIBLE);
            activeTimeText.setText("-");
        }


    }

}
