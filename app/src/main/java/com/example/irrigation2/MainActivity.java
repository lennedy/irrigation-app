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
    private String urlServer;
    ProgressDialog progressDialog;
    private Handler customHandler;
    private Runnable updateTimerThread;
    private int cont=0;
    //private Charts1 c1, c2;
    public Map<String, ZoneGui> zones;
    ArrayList<String> zonesId;
    boolean changed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = (ImageView) findViewById(R.id.gardenActive1);
        imgView.setImageResource(R.drawable.circvermelho);

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

      //  Switch a = findViewById( R.id.automaticSwitch );



        textView = (TextView) findViewById(R.id.textView3);
        urlServer = "http://192.168.1.33:8080/api/clientData/controller1";

        customHandler = new Handler();
        customHandler.postDelayed(updateTimerThread, 1000);


    }

    protected void onStop(){
        customHandler.removeCallbacks(updateTimerThread);
        super.onStop();
    }

    {
        updateTimerThread = new Runnable() {

            public void run() {
                boolean conection_realized=false;

                conection_realized = iniciateComunicationWithServer();

                //enter "sendRequest" method here
                if(conection_realized) {
                    customHandler.postDelayed(this, 1000);//you can put 60000(1 minut)
                }
            }
        };
    }

    private boolean iniciateComunicationWithServer(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            MyAsync myAsync = new MyAsync();
            myAsync.execute();
        }
        else {
            customHandler.removeCallbacks(updateTimerThread);
            Log.d("iniciateComunicationServer","parece que eu passei aqui");
            AlertDialog.Builder builder = new AlertDialog.
                    Builder(MainActivity.this);
            builder.setTitle("Alert!");
            builder.setMessage("Please check your network connection");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //finish();
                        }
                    });
            builder.create().show();

            return false;
        }
        return true;
    }

    public void clickActualize(View view){
        iniciateComunicationWithServer();
    }

    public void clickChangeChart(View view){
//        c1.changeChartValue(10, 70, 35);
    }

    public void clickOnChart(View view){
        Intent intent = new Intent(MainActivity.this, SensorsActivity.class);
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

    class MyAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "downloading", "please wait");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "err: ";//"";
            try {
                URL url = new URL(urlServer);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = inputStreamToString(in);
            } catch (Exception e) {
                result += e.getMessage();
                e.printStackTrace();
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            //Log.d("Teste", s);
            if(s.startsWith("err:")) {
                customHandler.removeCallbacks(updateTimerThread);
                AlertDialog.Builder builder = new AlertDialog.
                        Builder(MainActivity.this);
                builder.setTitle("Alert!");
                builder.setMessage(s);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //finish();
                            }
                        });
                builder.create().show();
            }

            try {
                JSONObject jsonObject = new JSONObject(s);

                TextView textView = (TextView) findViewById(R.id.textView);
                Switch switch1 = (Switch) findViewById(R.id.automaticSwitch);
                switch1.setChecked(jsonObject.getBoolean("automatic"));

                JSONObject zonesJson  = jsonObject.getJSONObject("activeZones");
                zonesJson = zonesJson.getJSONObject("zones");

                JSONObject activeTimesJson  = jsonObject.getJSONObject("activeTimes").getJSONObject("timeZones");
                textView.setText( activeTimesJson.getJSONObject("Canteiros Laterais").getString("nextTime") );

                try{
                    for(String id : zonesId){
                        Objects.requireNonNull(zones.get(id)).updateActive(zonesJson.getBoolean(id));
                        Objects.requireNonNull(zones.get(id)).updateZone(activeTimesJson.getJSONObject(id));
                       // textView.setText(activeTimesJson.getJSONArray(id).getString(0));
                    }
                }catch (NullPointerException e){
                    Log.e("zones objects problem",e.getMessage());
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }
}
