package com.example.irrigation2;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.LinearGauge;
import com.anychart.core.lineargauge.pointers.Tank;

public class MyTank {
    private AnyChartView anyChartView;
    private LinearGauge gauge;

    public MyTank(AnyChartView anyChartView){
        this.anyChartView = anyChartView;
    }

    public void createChart(){

        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        gauge = AnyChart.tank();
        gauge.data(new SingleValueDataSet(new Integer[] { 55 }));
        Tank tank = gauge.tank(0);
        tank.width("10%");
        tank.offset("10%");

        LinearGauge axis = gauge.axis("");
        axis.globalOffset("-1%");
        axis.scale().maximum(100);
        axis.scale().minimum(0);


        anyChartView.setChart(gauge);

    }

    public void changeValue(int value){
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        gauge.data(new SingleValueDataSet(new Integer[] { value }));
    }
}
