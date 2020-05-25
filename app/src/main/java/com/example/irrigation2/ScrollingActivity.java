package com.example.irrigation2;

import android.os.Bundle;

import com.anychart.AnyChartView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//public Map<String, ZoneGui> zones;

public class ScrollingActivity extends AppCompatActivity {
    private ArrayList<String> zonesId;
    private Map<String, ZoneGui> zones;


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
                zones.get(zoneName).changeMainLabel(zoneName);
            }catch (NullPointerException e){
                Log.e("zones objects problem",e.getMessage());
                finish();
            }

        }


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

    }
}
