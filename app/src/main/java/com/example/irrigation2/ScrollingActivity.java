package com.example.irrigation2;

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

import com.anychart.AnyChartView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

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
import java.util.Map;
import java.util.Objects;

//public Map<String, ZoneGui> zones;

public class ScrollingActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Handler customHandler;
    private Runnable updateTimerThread;

    private ArrayList<String> zonesId;
    private Map<String, ZoneGui> zones;

    private String urlServer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Objects.requireNonNull(zones.get(zoneName)).changeMainLabel(zoneName);
            }catch (NullPointerException e){
                Log.e("zones objects problem", Objects.requireNonNull(e.getMessage()));
                finish();
            }

        }

        urlServer = "http://192.168.1.33:8080/api/clientData/controller1";

        customHandler = new Handler();
        customHandler.postDelayed(updateTimerThread, 1000);

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
            Log.d("iniciateComServer","parece que eu passei aqui");
            AlertDialog.Builder builder = new AlertDialog.
                    Builder(ScrollingActivity.this);
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

    protected void onStop(){
        customHandler.removeCallbacks(updateTimerThread);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        MenuItem brushSizeItem = menu.findItem(R.id.brushSizeItem);
        brushSizeItem.setActionView(R.layout.radio_group_item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        RadioButton automatic = (RadioButton) findViewById(R.id.automatic);
        RadioButton manual = (RadioButton) findViewById(R.id.manual);
        //automatic.setChecked(false);
        manual.setChecked(true);
    }

    public void onClickOpenSensor(MenuItem menu){
        Intent intent = new Intent(ScrollingActivity.this, SensorsActivity.class);
        startActivity(intent);
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

    class MyAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingActivity.this, "downloading", "please wait");
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
                        Builder(ScrollingActivity.this);
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

//                TextView textView = (TextView) findViewById(R.id.textView);
                RadioButton automatic = (RadioButton) findViewById(R.id.automatic);
                RadioButton manual = (RadioButton) findViewById(R.id.manual);
                boolean automaticController = jsonObject.getBoolean("automatic");
                if(automaticController)
                    automatic.setChecked(automaticController);
                else
                    manual.setChecked(!automaticController);


                JSONObject zonesJson  = jsonObject.getJSONObject("activeZones");
                zonesJson = zonesJson.getJSONObject("zones");

                JSONObject activeTimesJson  = jsonObject.getJSONObject("activeTimes").getJSONObject("timeZones");
//                textView.setText( activeTimesJson.getJSONObject("Canteiros Laterais").getString("nextTime") );

                try{
                    for(String id : zonesId){

                        Objects.requireNonNull(zones.get(id)).updateActive(zonesJson.getBoolean(id));

                        Objects.requireNonNull(zones.get(id)).updateZone(activeTimesJson.getJSONObject(id));
                        // textView.setText(activeTimesJson.getJSONArray(id).getString(0));


                    }
                }catch (NullPointerException e){
                    Log.e("zones objects problem", Objects.requireNonNull(e.getMessage()));
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
