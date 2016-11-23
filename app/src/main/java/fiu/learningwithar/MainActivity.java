package fiu.learningwithar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Send button */
    public void onCameraClick(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void onGPSClick(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }

    public void onAccGyroClick(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AcclGyroActivity.class);
        startActivity(intent);
    }

    public void onRotationVectorClick(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, RotationVectorActivity.class);
        startActivity(intent);
    }
}
