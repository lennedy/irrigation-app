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
import java.util.ArrayList;
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

    public ZoneGui(TextView zoneName, ImageView activeImage, TextView textBerore, TextView textAfter, TextView activeTimeText, AnyChartView chart){
 /*       this.zoneName = zoneName;
        this.activeImage = activeImage;
        this.textBefore = textBerore;
        this.textAfter = textAfter;
        this.activeTimeText = activeTimeText;
        this.moistureSensors = chart;
        this.layoutA = layoutA;
        this.layoutB = layoutB;


        int count = this.layoutB.getChildCount();
        ArrayList<TextView> v = new ArrayList<>();
        for(int i=0; i<count; i++) {
            v.add( (TextView) layoutB.getChildAt(i) );
            //do something with your child element
        }

        v.get(0).setText("Passei aqui");
*/
    }

    public ZoneGui(LinearLayout layout1, LinearLayout layoutA, LinearLayout layoutB, LinearLayout layout3, ImageView imageView, AnyChartView chart){

        this.layoutA = layoutA;
        this.layoutB = layoutB;
        this.activeImage    = imageView;
        this.moistureSensors= chart;

        int qtdLayouts1 = layout1.getChildCount();
        ArrayList<View> l1 = new ArrayList<>();
        for(int i=0; i<qtdLayouts1; i++) {
            l1.add( layoutB.getChildAt(i) );
        }
        this.zoneName = (TextView) l1.get(0);

        int qtdLayoutA = layoutA.getChildCount();
        ArrayList<View> la = new ArrayList<>();
        for(int i=0; i<qtdLayoutA; i++){
           la.add(layoutA.getChildAt(i));
        }
        this.textBefore = (TextView) la.get(1);

        int qtdLayoutsB = layoutB.getChildCount();
        ArrayList<View> lb = new ArrayList<>();
        for(int i=0; i<qtdLayoutsB; i++) {
            lb.add( layoutB.getChildAt(i) );
        }
        this.textAfter = (TextView) lb.get(1);

        int qtdLayout3 = layout3.getChildCount();
        ArrayList<View> l3 = new ArrayList<>();
        for(int i=0; i<qtdLayout3; i++){
            l3.add(layout3.getChildAt(i));
        }
        this.activeTimeText = (TextView) l3.get(1);

        //AnyChartView anyChartView = findViewById(R.id.any_chart_view1);
        Charts1 c1 = new Charts1(moistureSensors);
        c1.createChart();
        c1.changeChartValue(75, 50, 75);


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
        DateFormat hour     = new SimpleDateFormat("HH");
        DateFormat minute   = new SimpleDateFormat("mm");

        if(!this.active) {
            layoutA.setVisibility(View.VISIBLE);
            layoutB.setVisibility(View.VISIBLE);

            Date currentTime = Calendar.getInstance().getTime();
            Date nextTime = Calendar.getInstance().getTime();

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

            textAfter.setText("" + difference);
        }else{
            layoutA.setVisibility(View.INVISIBLE);
            layoutB.setVisibility(View.INVISIBLE);
            textAfter.setText("-");
        }


    }

}
