package com.example.ipv4subnetcal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GeneralInfo extends AppCompatActivity {
    TextView reasons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);
        reasons= findViewById(R.id.tvReasons);
        reasons.setText(R.string.reasons);
    }
}