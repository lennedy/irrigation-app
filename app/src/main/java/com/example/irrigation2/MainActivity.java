package com.example.irrigation2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.anychart.AnyChartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    private int cont=0;
    //private Charts1 c1, c2;
    public Map<String, ZoneGui> zones;
    ArrayList<String> zonesId;
    boolean changed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zonesId = new ArrayList<>();
        zonesId.add("Canteiros Laterais");
        zonesId.add("Gramado Frontal");
        zonesId.add("Canteiros Frotais");

        zones = new HashMap<>();

        zones.put(zonesId.get(0), new ZoneGui(
                (LinearLayout) findViewById(R.id.layoutMainA1),
                (LinearLayout) findViewById(R.id.layout_A1),
                (LinearLayout) findViewById(R.id.layout_B1),
                (LinearLayout) findViewById(R.id.layoutMainC1),
                (ImageView) findViewById(R.id.gardenActive1),
                (AnyChartView) findViewById(R.id.any_chart_view1)
        ) );

        zones.put(zonesId.get(1), new ZoneGui(
                (LinearLayout) findViewById(R.id.layoutMainA2),
                (LinearLayout) findViewById(R.id.layout_A2),
                (LinearLayout) findViewById(R.id.layout_B2),
                (LinearLayout) findViewById(R.id.layoutMainC2),
                (ImageView) findViewById(R.id.gardenActive2),
                (AnyChartView) findViewById(R.id.any_chart_view2)
        ) );

        zones.put(zonesId.get(2), new ZoneGui(
                (LinearLayout) findViewById(R.id.layoutMainA3),
                (LinearLayout) findViewById(R.id.layout_A3),
                (LinearLayout) findViewById(R.id.layout_B3),
                (LinearLayout) findViewById(R.id.layoutMainC3),
                (ImageView) findViewById(R.id.gardenActive3),
                (AnyChartView) findViewById(R.id.any_chart_view3)
        ) );

        for(String zoneName : zonesId){
            try {
                zones.get(zoneName).changeMainLabel(zoneName);
            }catch (NullPointerException e){
                Log.e("zones objects problem",e.getMessage());
                finish();
            }

        }

    }

    public void clickActualize(View view){
        //iniciateComunicationWithServer();
    }

    public void clickChangeChart(View view){
//        c1.changeChartValue(10, 70, 35);
    }

    public void clickOnChart(View view){
        Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
        startActivity(intent);

        Log.d("clickOnChart","I don't know what is going on");
    }

    public void clickAutomatic (View view){
        Switch s = (Switch) view;
        s.onCheckIsTextEditor();

        this.changed=true;
        Log.d("Teste", "Estamos passando por aqui");
        //iniciateComunicationWithServer();
    }




}
