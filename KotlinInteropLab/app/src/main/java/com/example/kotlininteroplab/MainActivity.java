package com.example.kotlininteroplab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView reportView = findViewById(R.id.tvReport);
        Button analyzeButton = findViewById(R.id.btnAnalyze);

        analyzeButton.setOnClickListener(view -> {
            String report = DeviceAnalyzer.loadAndBuildReport();
            reportView.setText(report);
        });
    }
}