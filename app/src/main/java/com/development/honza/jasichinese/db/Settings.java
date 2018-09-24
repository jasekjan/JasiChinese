package com.development.honza.jasichinese.db;

import java.util.Set;

public class Settings {
    private float speed;
    private String path;

    public Settings() {};

    public Settings(float speed, String path) {
        this.speed = speed;
        this.path = path;
    }

    public float getSpeed() {
        return speed;
    }

    public String getPath() {
        return path;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPath(String path) { this.path = path;}
}
