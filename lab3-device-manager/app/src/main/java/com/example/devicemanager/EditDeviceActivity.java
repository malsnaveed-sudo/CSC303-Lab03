package com.example.devicemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditDeviceActivity extends AppCompatActivity {

    private static final String TAG = "EditDeviceActivityLC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_edit_device);

        EditText nameInput = findViewById(R.id.etDeviceName);
        EditText batteryInput = findViewById(R.id.etBattery);

        String receivedName = getIntent().getStringExtra(MainActivity.EXTRA_DEVICE_NAME);
        int receivedBattery = getIntent().getIntExtra(MainActivity.EXTRA_BATTERY, 0);

        nameInput.setText(receivedName == null ? "" : receivedName);
        batteryInput.setText(String.valueOf(receivedBattery));

        findViewById(R.id.btnSave).setOnClickListener(view -> {
            String updatedName = nameInput.getText().toString().trim();
            String batteryText = batteryInput.getText().toString().trim();

            if (updatedName.isEmpty()) {
                nameInput.setError("Device name is required");
                nameInput.requestFocus();
                return;
            }

            int updatedBattery;
            try {
                updatedBattery = Integer.parseInt(batteryText);
            } catch (NumberFormatException ex) {
                batteryInput.setError("Enter a whole number from 0 to 100");
                batteryInput.requestFocus();
                return;
            }

            if (updatedBattery < 0 || updatedBattery > 100) {
                batteryInput.setError("Battery must be from 0 to 100");
                batteryInput.requestFocus();
                return;
            }

            Intent resultData = new Intent();
            resultData.putExtra(MainActivity.EXTRA_DEVICE_NAME, updatedName);
            resultData.putExtra(MainActivity.EXTRA_BATTERY, updatedBattery);
            setResult(RESULT_OK, resultData);
            finish();
        });

        findViewById(R.id.btnCancel).setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
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
}