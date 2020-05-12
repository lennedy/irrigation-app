package com.example.irrigation2;

import android.util.Log;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;

import java.text.DecimalFormat;


public class Charts1 {

    private CircularGauge circularGauge;
    private AnyChartView anyChartView;
    private float value=0;

    public Charts1(AnyChartView anyChartView){
        this.anyChartView = anyChartView;
    }

    public void createChart(){


        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        circularGauge = AnyChart.circular();
        circularGauge.data(new SingleValueDataSet(new String[] { "23", "90", "67", "93", "56", "100"}));
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 0d, 0d);
        circularGauge.startAngle(0d);
        circularGauge.sweepAngle(270d);

        Circular xAxis = circularGauge.axis(0)
                .radius(100d)
                .width(1d)
                .fill((Fill) null);
        xAxis.scale()
                .minimum(0d)
                .maximum(100d);
        xAxis.ticks("{ interval: 1 }")
                .minorTicks("{ interval: 1 }");
        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);

        Bar bar0 = circularGauge.bar(0d);
        bar0.dataIndex(0d);
        bar0.radius(100d);
        bar0.width(17d);
        bar0.fill(new SolidFill("#64b5f6", 1d));
        bar0.stroke(null);
        bar0.zIndex(5d);

        Bar bar100 = circularGauge.bar(100d);
        bar100.dataIndex(5d);
        bar100.radius(100d);
        bar100.width(17d);
        bar100.fill(new SolidFill("#F5F4F4", 1d));
        bar100.stroke("1 #e5e4e4");
        bar100.zIndex(4d);

        Bar bar1 = circularGauge.bar(1d);
        bar1.dataIndex(1d);
        bar1.radius(80d);
        bar1.width(17d);
        bar1.fill(new SolidFill("#1976d2", 1d));
        bar1.stroke(null);
        bar1.zIndex(5d);

        Bar bar101 = circularGauge.bar(101d);
        bar101.dataIndex(5d);
        bar101.radius(80d);
        bar101.width(17d);
        bar101.fill(new SolidFill("#F5F4F4", 1d));
        bar101.stroke("1 #e5e4e4");
        bar101.zIndex(4d);

        Bar bar2 = circularGauge.bar(2d);
        bar2.dataIndex(2d);
        bar2.radius(60d);
        bar2.width(17d);
        bar2.fill(new SolidFill("#ef6c00", 1d));
        bar2.stroke(null);
        bar2.zIndex(5d);

        Bar bar102 = circularGauge.bar(102d);
        bar102.dataIndex(5d);
        bar102.radius(60d);
        bar102.width(17d);
        bar102.fill(new SolidFill("#F5F4F4", 1d));
        bar102.stroke("1 #e5e4e4");
        bar102.zIndex(4d);

        anyChartView.setChart(circularGauge);

    }

    private void changeLabel(String s){
        //gauge label
        circularGauge.label(0)
                .text(s)
                .anchor("center") //set the position of the label
                .adjustFontSize(true,true)
                .hAlign("center")
                .offsetY("10%")
                .offsetX("60%")
                .width("40%")
                .height("25%")
                .zIndex(10);

    }
    private void printValue(){
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        String numberAsString = decimalFormat.format(this.value);
        changeLabel(numberAsString+"%");
    }

    public void changeChartValue(int value0, int value1, int value2){

        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        circularGauge.data(new SingleValueDataSet(new Integer[] {value0, value1, value2, 100, 100, 100}));

        this.value = (float) ((value0+value1+value2)/3.0);

        printValue();
    }
}
