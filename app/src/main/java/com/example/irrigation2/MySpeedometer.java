package com.example.myapplication4;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;

public class MySpeedometer {
    private AnyChartView anyChartView;
    private CircularGauge gauge;

    public MySpeedometer(AnyChartView anyChartView){
        this.anyChartView =  anyChartView;
    }

    public void createChart(){
                // create data set on our data
        //var dataSet = anychart.data.set([100,400]);

        // set the gauge type
        //gauge = Anychart.gauges.circular();

        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        gauge = AnyChart.circular();

        // gauge settings
        //gauge.data(dataSet);
        gauge.data(new SingleValueDataSet(new Integer[] { 55, 400 }));
        gauge.padding("10%");
        gauge.startAngle(270);
        gauge.sweepAngle(180);

        // axis settings
        Circular axis = gauge.axis(0)
                .radius(95)
                .width(1);

        // scale settings
        axis.scale()
                .minimum(0)
                .maximum(120)
                .ticks("{interval: 10}")
                .minorTicks("{interval: 1}");

        // ticks settings
        axis.ticks()
                .type("trapezium")
                .length(8);

        // minor ticks settings
        axis.minorTicks()
                .enabled(true)
                .length("1");

    // second axis settings
        Circular axis_1 = gauge.axis(1)
                .radius(50)
                .width(3);

        // second scale settings
        axis_1.scale()
                .minimum(0)
                .maximum(600)
                .ticks("{interval: 100}")
    .minorTicks("{interval: 20}");

        // second ticks settings
        axis_1.ticks()
                .type("trapezium")
                .length(8);

        // second minor ticks settings
        axis_1.minorTicks()
                .enabled(true)
                .length("3");

    // needle
        gauge.needle(0)
                .enabled(true)
                .startRadius("-5%")
                .endRadius("80%")
                .middleRadius(0)
                .startWidth("0.1%")
                .endWidth("0.1%")
                .middleWidth("5%");

    // marker
        gauge.marker(0)
                .axisIndex(1)
                .dataIndex(1)
                .size(7)
                .type("triangle-down")
                .position("outside")
                .radius(50);

        // bar
        gauge.bar(0)
                .axisIndex(1)
                .position("inside")
                .dataIndex(1)
                .width(3)
                .radius(50)
                .zIndex(10);

        // cap
        gauge.cap()
                .radius("6%")
                .enabled(true);

        // draw the chart
    //            gauge.container("container").draw();
        anyChartView.setChart(gauge);

    }

    public void changeValue(int value){
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        gauge.data(new SingleValueDataSet(new Integer[] { value, 400 }));
    }
}
