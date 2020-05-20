package com.example.irrigation2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private String urlServer;
    ProgressDialog progressDialog;
    private Handler customHandler;
    private Runnable updateTimerThread;
    private int cont=0;
    private Charts1 c1, c2;
    public Map<String, ZoneGui> zones;
    ArrayList<String> zonesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = (ImageView) findViewById(R.id.gardenActive1);
        imgView.setImageResource(R.drawable.circvermelho);

        zonesId = new ArrayList<>();
        zonesId.add("Canteiros Laterais");
        zonesId.add("Gramado Frontal");

        zones = new HashMap<>();

        zones.put(zonesId.get(0) , new ZoneGui(
                findViewById(R.id.gardenName1),
                findViewById(R.id.gardenActive1),
                findViewById(R.id.timerViewGarden1),
                findViewById(R.id.text1Before2),
                findViewById(R.id.text1After2),
                findViewById(R.id.any_chart_view1)
        ) );

        zones.put(zonesId.get(1), new ZoneGui(
                (TextView)  findViewById(R.id.gardenName2),
                (ImageView) findViewById(R.id.gardenActive2),
                (TextView) findViewById(R.id.timerViewGarden2),
                (TextView) findViewById(R.id.text2Before2),
                (TextView) findViewById(R.id.text2After2),
                (AnyChartView) findViewById(R.id.any_chart_view2)
        ) );


/*        zones.put("zone3", new ZoneGui(
                R.id.gardenName3,
                R.id.gardenActive3,
                R.id.timerViewGarden3,
                R.id.text,
                R.id.text1After,
                R.id.any_chart_view1
        ) );
*/
//        TextView t = (TextView) findViewById(R.id.gardenName1);
//        t.setText("    Gramado da Frente");

//        TextView t2 = (TextView) findViewById(R.id.gardenName2);
//        t2.setText("    Canteiros da Frente");

//        TextView t3 = (TextView) findViewById(R.id.gardenName3);
//        t3.setText("    Canteiros do Lado");

        textView = (TextView) findViewById(R.id.textView3);
        urlServer = "http://192.168.1.33:8080/api/activeZones";


        AnyChartView anyChartView = findViewById(R.id.any_chart_view1);
        c1 = new Charts1(anyChartView);
        c1.createChart();
        c1.changeChartValue(0, 0, 0);


        AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);
        c2 = new Charts1(anyChartView2);
        c2.createChart();
        c2.changeChartValue(33, 48, 0);

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

                TextView t = (TextView) findViewById(R.id.text1Before2);
                t.setText(""+cont);
                cont++;
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
        c1.changeChartValue(10, 70, 35);
    }

    public void clickOnChart(View view){
        Intent intent = new Intent(MainActivity.this, SensorsActivity.class);
        startActivity(intent);

        Log.d("clickOnChart","I don't know what is going on");
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

                textView.setText( jsonObject.getBoolean("automatic") ? "Sim": "Não");

                JSONObject zonesJson  = jsonObject.getJSONObject("zones");

                for(String id : zonesId){
                    zones.get(id).updateActive(zonesJson.getBoolean(id));

                }


/*                boolean b = zonesJson.getBoolean("Gramado Frontal");
                z.updateActive(true);
                ImageView activeImage = (ImageView) findViewById(R.id.gardenActive1);

                if(b) {
                    activeImage.setImageResource(R.drawable.circverde);
                }
                else{
                    activeImage.setImageResource(R.drawable.circvermelho);
                }
*/
//                zones.get("Canteiros Laterais").updateActive(zonesJson.getBoolean("Canteiros Laterais"));
//                zones.get("Gramado Frontal").updateActive(zonesJson.getBoolean("Gramado Frontal"));

//                zonesJson  = jsonObject.getJSONObject("timeZones");
//                zones.get("Canteiros Laterais").updateActive(zonesJson.getBoolean("Canteiros Laterais"));
//                zones.get("Gramado Frontal").updateActive(zonesJson.getBoolean("Gramado Frontal"));



            } catch (JSONException e) {
                e.printStackTrace();
            }


//            for(ZoneGui zone: zones){
//                zone.updateZone(s);
//            }


/*
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONObject zones  = jsonObject.getJSONObject("zones");

                if(zones.length()>0) {
                    JSONArray jsonArrayValves = zones.names();

                    String name = jsonArrayValves.getString(0);
                    //Log.i("", name);
                    Boolean value = zones.getBoolean(name);
                    ImageView garden1Activate = (ImageView) findViewById(R.id.gardenActive1);
                    TextView textView = (TextView) findViewById(R.id.textView);

                    //textView.setText(value ? "True" : "False");
                    textView.setText(name);

                    if (value) {
                        garden1Activate.setImageResource(R.drawable.circverde);
                    } else {
                        garden1Activate.setImageResource(R.drawable.circvermelho);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
*/
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
