package fiu.learningwithar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SizeF;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by MariaP on 11/17/2016.
 */

public class FloatingText extends View implements SensorEventListener, LocationListener {

    private Location lastLocation = null;
    public LocationManager locationManager;
    public ArrayList<FIULocations.MyLocation> fiuLocationses;

    Sensor sensorRot;
    SensorManager sensorManager;
    float horizonalAngle;
    float verticalAngle;

    //latitude, longitude
    //green library: 25.757338, -80.373936
    //my location: 25.756824, -80.372788

    Location myLoc;
    Location greenLib;

    float[] curBearingToMW;
    CameraManager manager;

    Paint contentPaint;
    float cameraRotation[] = new float[9];

    // orientation vector
    float orientation[] = new float[3];

    public FloatingText(Context context, CameraManager manager) {
        super(context);
        contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensorRot = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this,sensorRot,SensorManager.SENSOR_DELAY_NORMAL);

        myLoc = new Location("My Location");
        myLoc.setLatitude(25.756775);
        myLoc.setLongitude(-80.372738);
        myLoc.setAltitude(30);

        greenLib = new Location("Green Library");
        greenLib.setLatitude(25.756511);
        greenLib.setLongitude(-80.372748);
        greenLib.setAltitude(240);

        FIULocations locations = new FIULocations();
        fiuLocationses = locations.getLocations();

        //curBearingToMW = new float[1];
        curBearingToMW = new float[fiuLocationses.size()];

        //curBearingToMW[0] = myLoc.bearingTo(greenLib);

        this.manager = manager;

        calculateFOV(manager);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String best = locationManager.getBestProvider(criteria, true);
        Log.v("FloatingText", "Best provider: " + best);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return ;
        }
        locationManager.requestLocationUpdates(best, 50, 0, this);

    }

    public FloatingText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        contentPaint.setTextAlign(Paint.Align.RIGHT);
        contentPaint.setTextSize(30);

        contentPaint.setColor(Color.MAGENTA);

        if(lastLocation == null)
            canvas.drawText("Calculating your Location", canvas.getWidth() / 2, canvas.getHeight() / 2 ,contentPaint);
        else
        {
            canvas.drawText("Your Location: ["+lastLocation.getLatitude()+", "+lastLocation.getLongitude()+"]", canvas.getWidth(), canvas.getHeight() ,contentPaint);
            for(int i = 0; i < fiuLocationses.size(); i++) {

                FIULocations.MyLocation fiuLoc = fiuLocationses.get(i);

                contentPaint.setColor(fiuLoc.getCl());
                //loop
                canvas.rotate((float) (0.0f - Math.toDegrees(orientation[2])));

                float dx = (float) ((canvas.getWidth() / horizonalAngle) * (Math.toDegrees(orientation[0]) - curBearingToMW[i]));
                float dy = (float) ((canvas.getHeight() / verticalAngle) * (Math.toDegrees(orientation[1])));

                canvas.translate(0.0f, 0.0f - dy);
                canvas.drawLine(0f - canvas.getHeight(), canvas.getHeight() / 2, canvas.getWidth() + canvas.getHeight(), canvas.getHeight() / 2, contentPaint);
                canvas.translate(0.0f - dx, 0.0f);

                canvas.drawText(fiuLoc.getName(), canvas.getWidth() / 2, canvas.getHeight() / 2, contentPaint);
                //Log.i("FloatingText", "orientation: "+ Arrays.toString(orientation));
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            float rotation[] = new float[9];

            SensorManager.getRotationMatrixFromVector(rotation , event.values);
            SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X, SensorManager.AXIS_Z, cameraRotation);
            SensorManager.getOrientation(cameraRotation, orientation);
        }

        this.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void calculateFOV(CameraManager cManager) {
        try {
            for (final String cameraId : cManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                    float[] maxFocus = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                    SizeF size = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
                    float w = size.getWidth();
                    float h = size.getHeight();
                    horizonalAngle = (float) (2*Math.atan(w/(maxFocus[0]*2)));
                    verticalAngle = (float) (2*Math.atan(h/(maxFocus[0]*2)));
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        horizonalAngle =  (float) Math.toDegrees( horizonalAngle);
        verticalAngle =  (float) Math.toDegrees( verticalAngle);

    }

    @Override
    public void onLocationChanged(Location location) {
        //SensorManager.getOrientation(cameraRotation, orientation);
        lastLocation = location;

        for(int i = 0; i < fiuLocationses.size(); i++){
            curBearingToMW[i] = lastLocation.bearingTo(fiuLocationses.get(i).getLoc());
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
