package fiu.learningwithar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class AcclGyroActivity extends AppCompatActivity implements SensorEventListener{

    float x,y,z;
    Entry e;
    Sensor sensorA, sensorG;
    SensorManager sensorManagerA;
    private LinearLayout accLayout;
    private LinearLayout gyrLayout;
    private LineChart mChart;
    private LineChart mChart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManagerA=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorA=sensorManagerA.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorG=sensorManagerA.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManagerA.registerListener(this,sensorA,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManagerA.registerListener(this,sensorG,SensorManager.SENSOR_DELAY_NORMAL);

        setContentView(R.layout.activity_accl_gyro);


        accLayout=(LinearLayout) findViewById(R.id.acc_layout);
        gyrLayout=(LinearLayout) findViewById(R.id.gyr_layout);


        mChart= new LineChart(this);
        accLayout.addView(mChart);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("no data for the moment");

        // mChart.setHighlightPerTapEnabled(true);
        mChart.setMinimumHeight(500);
        mChart.setMinimumWidth(1000);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        //enable pinch zoom to avoid scaling
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        Legend l= mChart.getLegend();

        XAxis x1=mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1=mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(120f);
        y1.setDrawGridLines(true);
        YAxis y12=mChart.getAxisRight();
        y12.setEnabled(false);


        //gyroscope chart

        mChart2= new LineChart(this);
        gyrLayout.addView(mChart2);
        mChart2.setDescription("");
        mChart2.setNoDataTextDescription("no data for the moment");

        // mChart.setHighlightPerTapEnabled(true);
        mChart2.setMinimumHeight(500);
        mChart2.setMinimumWidth(1000);
        mChart2.setTouchEnabled(true);
        mChart2.setDragEnabled(true);
        mChart2.setScaleEnabled(true);
        mChart2.setDrawGridBackground(false);

        //enable pinch zoom to avoid scaling
        mChart2.setPinchZoom(true);
        mChart2.setBackgroundColor(Color.LTGRAY);

        data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart2.setData(data);
        l = mChart2.getLegend();

        x1 = mChart2.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        y1=mChart2.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(120f);
        y1.setDrawGridLines(true);

        y12=mChart2.getAxisRight();
        y12.setEnabled(false);

    }

    //    @Override
//    protected void onResume(){
//        super.onResume();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //for (int i=0;i<100;i++){
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            addEntry();
//                            Log.i("gggg","oooo");
//
//                        }
//                    });
//                    try {
//                        Thread.sleep(600);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//        //    }
//        }).start();
//    }
    private void addEntry(float x,float y,float z,  LineChart mChart ){
        LineData data = mChart.getData();

        if(data != null){
            LineDataSet set= (LineDataSet) data.getDataSetByIndex(0);
            LineDataSet set1= (LineDataSet) data.getDataSetByIndex(1);
            LineDataSet set2= (LineDataSet) data.getDataSetByIndex(2);

            if (set==null){
                set=createSet(1);
                data.addDataSet(set);
            }

            if (set1==null){
                set1=createSet(2);
                data.addDataSet(set1);
            }

            if (set2==null){
                set2 =createSet(3);
                data.addDataSet(set2);
            }
            data.addXValue("");
            // data.addEntry(new Entry((float)(Math.random() *75) + 50f,set.getEntryCount()),0);
            data.addEntry(new Entry(x,set.getEntryCount()),0);
            data.addEntry(new Entry(y,set1.getEntryCount()),1);
            data.addEntry(new Entry(z,set2.getEntryCount()),2);
            mChart.notifyDataSetChanged();
            //Log.i("maryyyyyyyyyyyyy","mm");
            mChart.setVisibleXRange(20,20);

            mChart.moveViewToX(data.getXValCount()-7);
        }
    }
    private LineDataSet createSet(int color){
        LineDataSet set=new LineDataSet(null,"SPL Db");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        switch (color){
            case 1:
                set.setColor(Color.GREEN);
                break;
            case 2:
                set.setColor(Color.BLACK);
                break;
            case 3:
                set.setColor(Color.YELLOW);
                break;



        }
        // set.setColor(ColorTemplate.getHoloBlue());
        // set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(224,117,177));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);

        return set;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            addEntry(x,y,z, mChart);
        }
        else
        {
            if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                addEntry(x,y,z, mChart2);
            }

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
