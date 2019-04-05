package com.qishiyun.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClockView time_view = findViewById(R.id.time_view);
        time_view.setTime(9,35,43);
        time_view.start();
    }
}
