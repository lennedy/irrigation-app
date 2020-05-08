package com.example.irrigation2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private String urlServer;
    ProgressDialog progressDialog;
    private Handler customHandler;
    private Runnable updateTimerThread;
    private int cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = (ImageView) findViewById(R.id.gardenActive1);
        imgView.setImageResource(R.drawable.circvermelho);

        TextView t = (TextView) findViewById(R.id.gardenName1);
        t.setText("    Gramado da Frente");

        TextView t2 = (TextView) findViewById(R.id.gardenName2);
        t2.setText("    Canteiros da Frente");

        TextView t3 = (TextView) findViewById(R.id.gardenName3);
        t3.setText("    Canteiros do Lado");

        textView = (TextView) findViewById(R.id.textView3);
        urlServer = "http://192.168.1.33:8080/api/activeZones";

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

                iniciateComunicationWithServer();

                TextView t = (TextView) findViewById(R.id.textBefore2);
                t.setText(""+cont);
                cont++;
                //enter "sendRequest" method here
                customHandler.postDelayed(this,1000);//you can put 60000(1 minut)
            }
        };
    }

    private void iniciateComunicationWithServer(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            MyAsync myAsync = new MyAsync();
            myAsync.execute();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.
                    Builder(MainActivity.this);
            builder.setTitle("Alert!");
            builder.setMessage("Please check your network connection");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            builder.create().show();

        }
    }

    public void clickActualize(View view){
        //ImageView imgView = (ImageView) findViewById(R.id.gardenActive1);
        //imgView.setImageResource(R.drawable.circverde);
        iniciateComunicationWithServer();
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
                AlertDialog.Builder builder = new AlertDialog.
                        Builder(MainActivity.this);
                builder.setTitle("Alert!");
                builder.setMessage(s);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                builder.create().show();

            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONObject zones  = jsonObject.getJSONObject("zones");
                if(zones.length()>0) {
                    JSONArray jsonArrayValves = zones.names();

                    String name = jsonArrayValves.getString(0);
                    Boolean value = zones.getBoolean(name);
                    ImageView garden1Activate = (ImageView) findViewById(R.id.gardenActive1);
                    TextView textView = (TextView) findViewById(R.id.textView);

                    textView.setText(value ? "True" : "False");

                    if (value) {
                        garden1Activate.setImageResource(R.drawable.circverde);
                    } else {
                        garden1Activate.setImageResource(R.drawable.circvermelho);
                    }
/*
                    textView.setText("");
                    for (int i = 0; i < jsonArrayValves.length(); i++) {
                        String name = jsonArrayValves.getString(i);
                        Boolean value = zones.getBoolean(name);

                        textView.append(name + "\n");
                        textView.append(value ? "True" : "False" + "\n");
                    }*/
                }
                /*
                JSONObject valves  = jsonObject.getJSONObject("valves");
                if(valves.length()>0) {
                    JSONArray jsonArrayValves = valves.names();
                    textView.setText("");
                    for (int i = 0; i < jsonArrayValves.length(); i++) {
                        String name = jsonArrayValves.getString(i);
                        Boolean value = valves.getBoolean(name);

                        textView.append(name + "\n");
                        textView.append(value ? "True" : "False" + "\n");
                    }
                }
                JSONObject pumps  = jsonObject.getJSONObject("pumps");
                if(pumps.length()>0){
                    JSONArray jsonArrayPumps = pumps.names();
                    for (int i = 0; i < jsonArrayPumps.length(); i++) {
                        String name = jsonArrayPumps.getString(i);
                        Boolean value = pumps.getBoolean(name);

                        textView.append(name + "\n");
                        textView.append( value?"True":"False" + "\n\n");
                    }
                }
                */


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
