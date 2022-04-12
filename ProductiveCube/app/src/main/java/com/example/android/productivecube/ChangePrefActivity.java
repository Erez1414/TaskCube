package com.example.android.productivecube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;

public class ChangePrefActivity extends AppCompatActivity {

    private EditText taskText;
    private EditText timeText;
    private TextView colorLabel;
    private String colorKey;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pref);
        taskText = (EditText)findViewById(R.id.editText);
        colorLabel = (TextView)findViewById(R.id.colorLabel);
        timeText = (EditText)findViewById(R.id.timerEdit);
        Color result = (Color) getIntent().getSerializableExtra("color");
        colorKey = result.toString().toLowerCase();
        this.sharedPreferences = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);

        String colorPref = this.sharedPreferences.getString("com.example.app.color."+result.toString().toLowerCase(), "");
        colorLabel.setText(colorPref.split(";")[0]);
        taskText.setText(colorPref.split(";")[1]);
        Time time = getTimeByString(colorPref.split(";")[2]);
        int minutes = timeToMinutes(time);
        timeText.setText(String.valueOf(minutes));
    }

    public void assignTask(View v) {
        String textToSave = colorKey+";"+taskText.getText()+";"+new Time(0,Integer.parseInt(timeText.getText().toString()),0);
        this.sharedPreferences.edit().putString("com.example.app.color."+colorKey, textToSave).apply();
        this.sharedPreferences.edit().putString("com.example.app.timer."+colorKey, timeText.getText().toString()).apply();
        finish();
    }

    private int timeToMinutes(Time time) {
        String[] arr = time.toString().split(":");
        int minutes = Integer.parseInt(arr[0]) * 60 + Integer.parseInt(arr[1]);
        return minutes;
    }

    private Time getTimeByString(String timeString) {
        String[] arr = timeString.split(":");
        Time test = new Time(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),Integer.parseInt(arr[2]));
        return test;
    }
}