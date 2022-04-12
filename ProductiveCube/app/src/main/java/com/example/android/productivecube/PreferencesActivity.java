package com.example.android.productivecube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class PreferencesActivity extends AppCompatActivity implements PrefAdapter.ItemClickListener{

    private RecyclerView mRecycleView;
    private PrefAdapter mPrefAdapter;
    private SharedPreferences sharedPreferences;
    private HashMap<String, String> colorPreferences;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mRecycleView = (RecyclerView) findViewById(R.id.rv_pref);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mPrefAdapter = new PrefAdapter(this, this);
        mRecycleView.setAdapter(mPrefAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecycleView.addItemDecoration(decoration);
        this.sharedPreferences = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        this.colorPreferences = new HashMap<>();
//        resetColorsInShared(); <!-- This will restart the memory -->
        initializeColors();
        Log.d("Something", "MADE IT TO PREF ACTIVITY");

    }

    @Override
    public void onItemClickListener(Task task) {
        Intent changePrefIntent = new Intent(PreferencesActivity.this, ChangePrefActivity.class);
        changePrefIntent.putExtra("color", task.getColor());
        startActivity(changePrefIntent);
    }

    public void retrieveTasks(){
        //todo: get list of tasks from moshe
        ArrayList<Task> tasks = new ArrayList<>();
        for(int i=0; i<6; i++) {
            Color color = Color.values()[i];
            String key = color.toString().toLowerCase();
            String colorPref = this.sharedPreferences.getString("com.example.app.color."+key, "");
            String[] arr = colorPref.split(";");
            tasks.add(new Task(arr[1],getColorByString(arr[0]),getTimeByString(arr[2])));
        }
        mPrefAdapter.setTasksEntries(tasks);
    }

    private Color getColorByString(String colorString) {
        for (Color color : Color.values()) {
            if (colorString.toLowerCase().equals(color.toString().toLowerCase()))
                return color;
        }
        return Color.RED;
    }

    private Time getTimeByString(String timeString) {
        String[] arr = timeString.split(":");
        Time test = new Time(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),Integer.parseInt(arr[2]));
        return test;
    }

    private void resetColorsInShared() {
        for (Color color : Color.values()) {
            String colorString = color.toString().toLowerCase();
            String specificColor = this.sharedPreferences.getString("com.example.app.color." + colorString, "");
            if (specificColor.equals("")) {
                Time time = new Time(0, 0, 0);
                String defaultColorValue = colorString + ";'';" + time.toString();
                this.sharedPreferences.edit().putString("com.example.app.color." + colorString, defaultColorValue).apply();
                this.colorPreferences.put(colorString, defaultColorValue);
            } else {
                Time time = new Time(0, 0, 0);
                String defaultColorValue = colorString + ";'';" + time.toString();
                this.sharedPreferences.edit().putString("com.example.app.color." + colorString, defaultColorValue).apply();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void initializeColors() {
        for (Color color : Color.values()) {
            String colorString = color.toString().toLowerCase();
            String specificColor = this.sharedPreferences.getString("com.example.app.color."+colorString, "");
            if (specificColor.equals("")) {
                Time time = new Time(0,0,0);
                String defaultColorValue = colorString+";'';"+time.toString();
                this.sharedPreferences.edit().putString("com.example.app.color."+colorString, defaultColorValue).apply();
                this.colorPreferences.put(colorString,defaultColorValue);
            }
            else {
                this.colorPreferences.put(colorString,specificColor);
            }
        }
    }
}