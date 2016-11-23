package fiu.learningwithar;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class RotationVectorActivity extends AppCompatActivity implements SensorEventListener {
    //private RelativeLayout mainLayout;
    private LineChart mChart;

    Sensor sensorRot;
    SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rot_vector);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorRot = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sensorManager.registerListener(this,sensorRot,SensorManager.SENSOR_DELAY_NORMAL);

        //Create chart
        mChart = (LineChart) findViewById(R.id.chart);

        //Edit line chart
        mChart.setDescription(""); // Text at bottom right corner of the chart
        mChart.setNoDataTextDescription("No data available at the moment");

        //Enable value highlighting
        mChart.setHighlightPerTapEnabled(true);

        //Enable touch gestures
        mChart.setTouchEnabled(true);

        //Enable Drag and Scaling
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        //Enable pinch zoom to avoid scaling x and y separately
        mChart.setPinchZoom(true);

        //Background color
        mChart.setBackgroundColor(Color.GRAY);

        //----------DATA------------
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        //Legend object
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.DKGRAY);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setDrawAxisLine(true);
        xl.setAxisLineColor(Color.DKGRAY);
        xl.setAvoidFirstLastClipping(true); //?

        YAxis yl = mChart.getAxisLeft();
        yl.setTextColor(Color.WHITE);
        //yl.setAxisMaxValue(120.0f);
        yl.setDrawGridLines(true);
        yl.setGridColor(Color.DKGRAY);
        yl.setDrawAxisLine(true);
        yl.setAxisLineColor(Color.DKGRAY);
        yl.setEnabled(true);

        mChart.getAxisRight().setEnabled(false);

    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //simulate real time
//        //Wrap the action in runnable and then in thread
//        //so it can be a new thread and inherit other good stuffs
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // add 100 entries
//                for (int i=0;i<100;i++) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            float[] arr = {0f,0f,0f,0f};
//                            addEntry(arr); // chart is notified of updates
//                            //it's run on separated thread and to add it back to UIThread,
//                            //it has to be wrapped in a Runnable object and parsed to runOnUiThread()
//                        }
//                    });
//
//                    //pause between adds
//                    try {
//                        Thread.sleep(600);
//                    } catch (InterruptedException e) {
//                        //
//                    }
//                }
//            }
//        }).start();
//    }

    //-----Add entry-----------
    private void addEntry(float[] rotation_Vector){
        String[] setName = {"x","y","z","w"};
        int[] setColor = {Color.RED, Color.GREEN, Color.BLUE, Color.BLACK};
        LineData data = mChart.getData();

        if (data != null) {

            for(int i=0; i < rotation_Vector.length; i++){
                ILineDataSet set = data.getDataSetByIndex(i);

                if(set == null){
                    set = createSet(setName[i], setColor[i]);
                    data.addDataSet(set);
                }


                //data.addEntry(new Entry((float)(Math.random() * 20) + 20f, set.getEntryCount()), i);
                data.addEntry(new Entry(rotation_Vector[i], set.getEntryCount()), i);
            }
            data.addXValue("");
/*
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            //add new random value
            data.addXValue("");
            data.addEntry(new Entry((float)(Math.random() * 20) + 20f, set.getEntryCount()), 0);
*/

            //let the chart know its data changed
            mChart.notifyDataSetChanged();

            //limit number of visible entries
            mChart.setVisibleXRange(20,20);

            //scroll to last entry
            mChart.moveViewToX(data.getXValCount() - 7);
        }
    }

    //-------Create set-------
    private LineDataSet createSet(String name, int color) {

        LineDataSet set = new LineDataSet(null, name);
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        set.setColor(color);
        set.setCircleColor(color);

        set.setLineWidth(3f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(224, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        return set;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            //Toast.makeText(this, "update");
            float[] vals = new float[4];
            vals[0] = event.values[0];
            vals[1] = event.values[1];
            vals[2] = event.values[2];
            vals[3] = (event.values[3] == -1) ?  0 : event.values[3];
            addEntry(vals);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
