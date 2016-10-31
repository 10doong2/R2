package com.doongis.r2.GPS;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.doongis.r2.R;

public class FsActivity extends AppCompatActivity {

    String cycle_start_time;
    String cycle_end_time;
    String cycle_total_time;
    String cycle_distance;
    String cycle_speed;

    TextView tv_start_time;
    TextView tv_end_time;
    TextView tv_total_time;
    TextView tv_distance;
    TextView tv_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stats);

        Intent intent = getIntent();
        cycle_start_time = intent.getStringExtra("cycle_start_time");
        cycle_end_time = intent.getStringExtra("cycle_end_time");
        cycle_total_time = intent.getStringExtra("cycle_total_time");
        cycle_distance = intent.getStringExtra("cycle_distance");
        cycle_speed = intent.getStringExtra("cycle_speed");

        tv_start_time = (TextView)findViewById(R.id.tv_start_time);
        tv_end_time = (TextView)findViewById(R.id.tv_end_time);
        tv_total_time = (TextView)findViewById(R.id.tv_total_time);
        tv_distance = (TextView)findViewById(R.id.tv_distance);
        tv_speed = (TextView)findViewById(R.id.tv_speed);

        tv_start_time.setText(cycle_start_time);
        tv_end_time.setText(cycle_end_time);
        tv_total_time.setText(cycle_total_time);
        tv_distance.setText(cycle_distance+" km");
        tv_speed.setText(cycle_speed + " km/h");

    }

}
