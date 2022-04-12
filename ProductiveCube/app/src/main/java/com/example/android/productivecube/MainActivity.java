package com.example.android.productivecube;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.time.Duration;
import java.time.Instant;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "Main";
    private static final String ARDUINO_NAME = "ProductiveCube ";

    private TextView tvTaskTitle;
    private TextView tvClock;
    private Bluetooth bt;
    private SharedPreferences sharedPreferences;
    private Color curColor;
    private View view;
    private Instant startTime;
    private CountDownTimer countDownTimer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTaskTitle = (TextView) findViewById(R.id.tv_task_title);
        tvTaskTitle.setText("Task Cube");
        tvClock = (TextView) findViewById(R.id.tv_stop_watch);
        tvClock.setText("00:00:00");
        curColor = Color.BLUE;
        startTime = Instant.now();
        view = (View) findViewById(R.id.main_view) ;

        sharedPreferences = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);

        initPref();
        bt = new Bluetooth(this, mHandler);
        connectService();
    }

    public void initPref(){
        if (sharedPreferences.getAll().isEmpty()) {
            Log.d(TAG, "sould not init tis !!!!!!!!!!!!!!!!!!!!!!!!!!1");
            String timerKey = "com.example.app.timer.";
            String prefKey = "com.example.app.color.";
            for (int i = 0; i < 6; i++) {
                String color = Color.values()[i].toString().toLowerCase();
                sharedPreferences.edit().putString(timerKey + color, "30").apply();
                sharedPreferences.edit().putString(prefKey + color, color + ";" + "task" + i + ";00:30:00").apply();
            }
        }
    }

    public void updateTaskStatus(Instant end){
        Duration timePassed = Duration.between(startTime, end);
        String colorString = curColor.toString().toLowerCase();
        String timerKey = "com.example.app.timer.";
        String prevColorTime = this.sharedPreferences.getString(timerKey+colorString, "20");
        int remaining = (int) (Integer.valueOf(prevColorTime) - timePassed.toMinutes());
        sharedPreferences.edit().putString(timerKey+colorString, String.valueOf(remaining)).apply();
    }

    private int getRemainingTime(){
        String colorString = curColor.toString().toLowerCase();
        String timerKey = "com.example.app.timer.";
        String remainingTime = this.sharedPreferences.getString(timerKey+colorString, "30");
        return 60 * 1000 * Integer.valueOf(remainingTime);
    }

    public void onRed(View view){
        bt.sendMessage("1");
    }

    private String formatTime(long millis){
        int s = (int) millis / 1000;
        int sec = s % 60;
        int min = (s / 60)%60;
        int hours = (s/60)/60;

        String strSec=(sec<10)?"0"+Integer.toString(sec):Integer.toString(sec);
        String strmin=(min<10)?"0"+Integer.toString(min):Integer.toString(min);
        String strHours=(hours<10)?"0"+Integer.toString(hours):Integer.toString(hours);

        return strHours + ":" + strmin + ":" + strSec;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onFlipCube(String colorId){
        Log.d(TAG, "in onFLIP@@@@@@@@@@@@@@@@");
        int color = Integer.parseInt(colorId);
        Instant end = Instant.now();
        updateTaskStatus(end);
        startTime = end;
        curColor = Color.values()[color];
        view.setBackgroundResource(PrefAdapter.colors.get(curColor));
        String taskTitle = sharedPreferences.getString("com.example.app.color." + curColor.toString().toLowerCase(), "default");
        taskTitle = taskTitle.split(";")[1];
        tvTaskTitle.setText(taskTitle);

        int remainingTime = getRemainingTime();
        if (countDownTimer != null)
            countDownTimer.cancel();
        countDownTimer = new CountDownTimer(remainingTime, 1000) {

            public void onTick(long millisUntilFinished) {
                tvClock.setText(formatTime(millisUntilFinished));

            }

            public void onFinish() {
                onRed(view);

            }

        };
        countDownTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    public void onPrefChange(MenuItem menuItem){
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    public void connectService(){
        try {
            Log.d("main", "Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice(ARDUINO_NAME);
                Log.d(TAG, "Btservice started - listening");
                Log.d("main","Connected");
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                Log.d("main", "Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            Log.d("main", "Unable to connect " +e);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }


    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    String faceString = new String((byte[]) msg.obj, 0, 1);
                    Log.d(TAG, "MESSAGE_READ " + faceString);
                    if (isInteger(faceString)) {
                        onFlipCube(faceString);
                    }
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME " + msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST " + msg);
                    break;
            }
        }
    };
}