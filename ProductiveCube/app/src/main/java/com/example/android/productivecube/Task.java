package com.example.android.productivecube;

import java.sql.Time;

public class Task {
    private String name;
    private Color color;
    private Time time;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Task(String name, Color color, Time time) {
        this.name = name;
        this.color = color;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
