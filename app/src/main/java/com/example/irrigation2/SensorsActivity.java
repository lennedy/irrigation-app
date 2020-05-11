package com.example.irrigation2;

import android.content.Intent;
import android.os.Bundle;

import com.anychart.AnyChartView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class SensorsActivity extends AppCompatActivity {

    private MyTank myTank;
    private MySpeedometer mySpeedometer1;
    private MySpeedometer mySpeedometer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        AnyChartView tankChartView = findViewById(R.id.tank_chart_view);
        myTank = new MyTank(tankChartView);
        myTank.createChart();

        AnyChartView tankChartView2 = findViewById(R.id.any_chart_view1);
        MyTank myTank2 = new MyTank(tankChartView2);
        myTank2.createChart();


        AnyChartView vmeterChartView1 = findViewById(R.id.vmeter_chart_view1);
        mySpeedometer1 = new MySpeedometer(vmeterChartView1);
        mySpeedometer1.createChart();

        AnyChartView vmeterChartView2 = findViewById(R.id.vmeter_chart_view2);
        mySpeedometer2 = new MySpeedometer(vmeterChartView2);
        mySpeedometer2.createChart();
    }

    public void clickComeBack(View view){
        Intent intent = new Intent(SensorsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
