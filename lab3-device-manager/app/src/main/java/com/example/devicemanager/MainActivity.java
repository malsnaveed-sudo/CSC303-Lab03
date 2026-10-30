package com.example.devicemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLC";

    public static final String EXTRA_DEVICE_NAME = "com.example.devicemanager.DEVICE_NAME";
    public static final String EXTRA_BATTERY = "com.example.devicemanager.BATTERY";

    private String deviceName = "Sensor-A";
    private int battery = 15;

    private final ActivityResultLauncher<Intent> editDeviceLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {
                            Intent data = result.getData();
                            String returnedName = data.getStringExtra(EXTRA_DEVICE_NAME);
                            int returnedBattery = data.getIntExtra(EXTRA_BATTERY, battery);

                            if (returnedName != null) {
                                deviceName = returnedName;
                            }
                            battery = returnedBattery;
                            updateSummary();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        updateSummary();

        Button editButton = findViewById(R.id.btnEditDevice);
        editButton.setOnClickListener(view -> {
            Intent editIntent = new Intent(MainActivity.this, EditDeviceActivity.class);
            editIntent.putExtra(EXTRA_DEVICE_NAME, deviceName);
            editIntent.putExtra(EXTRA_BATTERY, battery);

            Log.d("MainActivity", "Sending EXTRA_DEVICE_NAME: " + deviceName);
            Log.d("MainActivity", "Sending EXTRA_BATTERY: " + battery);

            editDeviceLauncher.launch(editIntent);
        });

        Button docsButton = findViewById(R.id.btnOpenDocumentation);
        docsButton.setOnClickListener(view -> {
            Uri page = Uri.parse("https://developer.android.com");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, page);
            try {
                startActivity(webIntent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "No application can open this link", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void updateSummary() {
        TextView nameView = findViewById(R.id.tvDeviceName);
        TextView batteryView = findViewById(R.id.tvBattery);
        nameView.setText(getString(R.string.device_name_value, deviceName));
        batteryView.setText(getString(R.string.battery_value, battery));
    }
}